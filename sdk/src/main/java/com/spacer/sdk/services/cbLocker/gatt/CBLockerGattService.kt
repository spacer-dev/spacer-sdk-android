package com.spacer.sdk.services.cbLocker.gatt

import android.bluetooth.*
import android.content.Context
import com.spacer.sdk.data.ICallback
import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.SPRError
import com.spacer.sdk.data.api.APIHeader
import com.spacer.sdk.data.api.api
import com.spacer.sdk.data.api.reqData.logger.LoggerReqData
import com.spacer.sdk.data.extensions.LoggerExtensions.logd
import com.spacer.sdk.data.extensions.RetrofitCallExtensions.enqueue
import com.spacer.sdk.models.cbLocker.CBLockerModel
import com.spacer.sdk.values.cbLocker.*

open class CBLockerGattService {
    private lateinit var context: Context
    private lateinit var token: String
    protected lateinit var cbLocker: CBLockerModel
    private lateinit var gattCallback: CBLockerGattCallback
    private lateinit var actionType: CBLockerGattActionType
    private var bluetoothGatt: BluetoothGatt? = null
    private var isRetry: Boolean = false
    private var isCanceled = false

    protected val spacerId get() = cbLocker.spacerId
    private val bluetoothAdapter get() = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    private var timeout = CBLockerConnectTimeouts { error ->
        failureIfNotCanceled(error)
    }


    open fun connect(
        context: Context,
        token: String,
        cbLocker: CBLockerModel,
        gattCallback: CBLockerGattCallback,
        actionType: CBLockerGattActionType,
        isRetry: Boolean
    ) {
        logd("connect: ${cbLocker.spacerId} ")

        this.context = context
        this.token = token
        this.cbLocker = cbLocker
        this.gattCallback = gattCallback
        this.actionType = actionType
        this.isRetry = isRetry

        connectRemoteDevice()
    }

    @Synchronized
    private fun connectRemoteDevice() {
        val remoteDevice = bluetoothAdapter.getRemoteDevice(cbLocker.address)
        bluetoothGatt = remoteDevice.connectGatt(
            context, false, gattCallback, BluetoothDevice.TRANSPORT_LE
        )
        timeout.during.set()
        timeout.start.set()
    }

    private fun reset() {
        timeout.clearAll()
        bluetoothGatt?.disconnect()
    }

    @Synchronized
    private fun successIfNotCanceled() {
        cbLocker.reset()
        reset()
        if (!isCanceled) {
            isCanceled = true
            gattCallback.onSuccess()
        }
    }

    @Synchronized
    private fun failureIfNotCanceled(error: SPRError) {
        reset()
        if (!isCanceled) {
            isCanceled = true
            gattCallback.onFailure(error)
        }
    }

    open inner class CBLockerGattCallback : BluetoothGattCallback(), ICallback {

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            logd("onConnectionStateChange: $status, $newState")

            when {
                newState == BluetoothGatt.STATE_CONNECTED -> {
                    timeout.start.clear()
                    gatt.discoverServices()
                    timeout.discover.set()
                }
                status == GATT_ERROR_STATE -> {
                    sendErrorMessage("onConnectionStateChange", SPRError.CBServiceNotFound, status)
                    failureIfNotCanceled(SPRError.CBServiceNotFound)
                }
                newState == BluetoothGatt.STATE_DISCONNECTED -> {
                    bluetoothGatt?.close()
                    bluetoothGatt = null
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            timeout.discover.clear()
            if (status != BluetoothGatt.GATT_SUCCESS) {
                sendErrorMessage("onServicesDiscovered", SPRError.CBServiceNotFound, status)
                return failureIfNotCanceled(SPRError.CBServiceNotFound)
            }
            val service =
                gatt.services.firstOrNull { it.uuid == CBLockerConst.DeviceServiceUUID } ?: run {
                    val allUuids = gatt.services.joinToString(", ") { it.uuid.toString() }
                    sendErrorMessage(
                        "onServicesDiscovered",
                        SPRError.CBServiceNotFound,
                        status,
                        "Available service UUIDs: $allUuids"
                    )
                    return failureIfNotCanceled(SPRError.CBServiceNotFound)
                }

            val characteristic =
                service.characteristics.firstOrNull { it.uuid == CBLockerConst.DeviceCharacteristicUUID }
                    ?: run {
                        val allUuids =
                            service.characteristics.joinToString(", ") { it.uuid.toString() }
                        sendErrorMessage(
                            "onServicesDiscovered",
                            SPRError.CBCharacteristicNotFound,
                            status,
                            "Available characteristic UUIDs: $allUuids"
                        )
                        return failureIfNotCanceled(SPRError.CBCharacteristicNotFound)
                    }
            gatt.readCharacteristic(characteristic)
            timeout.readBeforeWrite.set()
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            logd("onMtuChanged: $mtu, $status")
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            logd("onCharacteristicRead: $status")
            timeout.readBeforeWrite.clear()

            if (BluetoothGatt.GATT_SUCCESS != status) {
                sendErrorMessage("onCharacteristicRead", SPRError.CBReadingCharacteristicFailed, status)
                return failureIfNotCanceled(SPRError.CBReadingCharacteristicFailed)
            }

            if (isRetry && alreadyWrittenToCharacteristic(characteristic.readData())) {
                finish()
            } else {
                gatt.getKeyAndWriteCharacteristic(characteristic, cbLocker)
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            logd("onCharacteristicWrite: $status")
            timeout.write.clear()

            if (BluetoothGatt.GATT_SUCCESS != status) {
                sendErrorMessage("onCharacteristicWrite", SPRError.CBWritingCharacteristicFailed, status)
                return failureIfNotCanceled(SPRError.CBWritingCharacteristicFailed)
            }
            cbLocker.update(CBLockerGattStatus.Write)
            finish()
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            logd("onCharacteristicChanged")
        }

        open fun onKeyGet(characteristic: BluetoothGattCharacteristic, cbLocker: CBLockerModel, callback: IResultCallback<ByteArray>) {
            throw RuntimeException("not implementation!")
        }

        open fun onFinished(callback: ICallback) {
            throw RuntimeException("not implementation!")
        }

        private fun BluetoothGatt.getKeyAndWriteCharacteristic(characteristic: BluetoothGattCharacteristic, cbLocker: CBLockerModel) {
            val callback = object : IResultCallback<ByteArray> {
                override fun onSuccess(result: ByteArray) {
                    characteristic.value = result
                    writeCharacteristic(characteristic)
                    timeout.write.set()
                }

                override fun onFailure(error: SPRError) = failureIfNotCanceled(error)
            }

            onKeyGet(characteristic, cbLocker, callback)
        }

        private fun finish() {
            val callback = object : ICallback {
                override fun onSuccess() = successIfNotCanceled()
                override fun onFailure(error: SPRError) = failureIfNotCanceled(error)
            }

            onFinished(callback)
        }

        protected fun BluetoothGattCharacteristic.readData() = this.value.toString(Charsets.UTF_8)

        private fun alreadyWrittenToCharacteristic(readValue: String): Boolean {
            return when (actionType) {
                CBLockerGattActionType.Put -> CBLockerConst.UsingOrWriteReadData.contains(readValue)
                CBLockerGattActionType.Take -> !CBLockerConst.UsingReadData.contains(readValue)
                CBLockerGattActionType.OpenForMaintenance -> cbLocker.status == CBLockerGattStatus.Write
            }
        }

        private fun sendErrorMessage(
            errorPoint: String,
            error: SPRError,
            status: Int,
            other: Any? = null
        ) {
            val message = StringBuilder("box: $spacerId, errorPoint: $errorPoint, status: $status, error: $error")
            if (other != null) {
                message.append(", $other")
            }
            val params = LoggerReqData(
                "bluetoothGattError", message
            )
            api.logger.sendWarn(APIHeader.createHeader(token), params).enqueue(object : ICallback {
                override fun onSuccess() {
                }

                override fun onFailure(error: SPRError) {
                }
            })
        }
    }

    companion object {
        const val GATT_ERROR_STATE = 133
    }
}
