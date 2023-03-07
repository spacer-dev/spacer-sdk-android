package com.spacer.sdk.services.cbLocker.gatt

import android.bluetooth.*
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.spacer.sdk.data.ICallback
import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.SPRError
import com.spacer.sdk.data.extensions.LoggerExtensions.logd
import com.spacer.sdk.models.cbLocker.CBLockerModel
import com.spacer.sdk.values.cbLocker.*

open class CBLockerGattService {
    private lateinit var context: Context
    protected lateinit var cbLocker: CBLockerModel
    private lateinit var connectHandler: Handler
    private lateinit var gattCallback: CBLockerGattCallback
    private lateinit var actionType: CBLockerGattActionType
    private lateinit var gatt: BluetoothGatt

    protected val spacerId get() = cbLocker.spacerId
    private val bluetoothAdapter get() = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    private var connectRetryCnt = 0
    private var isRetry = false
    private var timeout = CBLockerConnectTimeouts { error ->
        gatt.disconnect()
        gatt.close()

        connectRetryCnt++
        if (connectRetryCnt > CBLockerConst.MaxRetryNum) {
            cbLocker.reset()
            gattCallback.onFailure(error)
        } else {
            isRetry = true
            logd("########## connectRetryCnt: $connectRetryCnt")
            connectRemoteDevice()
        }
    }


    open fun connect(context: Context, cbLocker: CBLockerModel, gattCallback: CBLockerGattCallback, actionType: CBLockerGattActionType) {
        logd("connect: ${cbLocker.spacerId} ")

        this.context = context
        this.cbLocker = cbLocker
        this.gattCallback = gattCallback
        this.connectHandler = Handler(Looper.getMainLooper())
        this.actionType = actionType

        connectRemoteDevice()
    }

    private fun connectRemoteDevice() {
        timeout.clearAll()
        val remoteDevice = bluetoothAdapter.getRemoteDevice(cbLocker.address)
        gatt = remoteDevice.connectGatt(
            context, false, gattCallback, BluetoothDevice.TRANSPORT_LE
        )
        timeout.during.set()
        timeout.start.set()
    }

    open inner class CBLockerGattCallback : BluetoothGattCallback(), ICallback {

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            timeout.start.clear()
            logd("onConnectionStateChange: $status, $newState")

            when {
                newState == BluetoothGatt.STATE_CONNECTED -> {
                    gatt.discoverServices()
                    timeout.discover.set()
                }
                status == GATT_ERROR_STATE -> {
                    connectRemoteDevice()
                }
                newState == BluetoothGatt.STATE_DISCONNECTED -> {
                    gatt.close()
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            timeout.discover.clear()
            logd("onServicesDiscovered: $status")
            if (status != BluetoothGatt.GATT_SUCCESS) {
                return gatt.fail(SPRError.CBServiceNotFound)
            }

            val service = gatt.services.firstOrNull { it.uuid == CBLockerConst.DeviceServiceUUID } ?: return gatt.fail(SPRError.CBServiceNotFound)

            val characteristic = service.characteristics.firstOrNull { it.uuid == CBLockerConst.DeviceCharacteristicUUID } ?: return gatt.fail(
                SPRError.CBCharacteristicNotFound
            )

            gatt.readCharacteristic(characteristic)
            if (cbLocker.status == CBLockerGattStatus.None) {
                timeout.readBeforeWrite.set()
            } else {
                timeout.readAfterWrite.set()
            }
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            logd("onMtuChanged: $mtu, $status")
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            logd("onCharacteristicRead: $status")
            if (cbLocker.status == CBLockerGattStatus.None) {
                timeout.readBeforeWrite.clear()
            } else {
                timeout.readAfterWrite.clear()
            }

            if (BluetoothGatt.GATT_SUCCESS != status) {
                return gatt.fail(SPRError.CBReadingCharacteristicFailed)
            }

            if (isRetry && alreadyWrittenToCharacteristic(characteristic.readData())) {
                gatt.finish(characteristic, cbLocker)
            } else {
                if (cbLocker.status == CBLockerGattStatus.None) {
                    gatt.getKeyAndWriteCharacteristic(characteristic, cbLocker)
                } else {
                    gatt.finish(characteristic, cbLocker)
                }
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            logd("onCharacteristicWrite: $status")
            timeout.write.clear()

            if (BluetoothGatt.GATT_SUCCESS != status) {
                return gatt.fail(SPRError.CBWritingCharacteristicFailed)
            }
            cbLocker.update(CBLockerGattStatus.Write)
            gatt.readCharacteristic(characteristic)
            timeout.readAfterWrite.set()
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            logd("onCharacteristicChanged")
        }

        open fun onKeyGet(characteristic: BluetoothGattCharacteristic, cbLocker: CBLockerModel, callback: IResultCallback<ByteArray>) {
            throw RuntimeException("not implementation!")
        }

        open fun onFinished(characteristic: BluetoothGattCharacteristic, cbLocker: CBLockerModel, callback: ICallback) {
            throw RuntimeException("not implementation!")
        }

        private fun BluetoothGatt.getKeyAndWriteCharacteristic(characteristic: BluetoothGattCharacteristic, cbLocker: CBLockerModel) {
            val callback = object : IResultCallback<ByteArray> {
                override fun onSuccess(result: ByteArray) {
                    characteristic.value = result
                    writeCharacteristic(characteristic)
                    timeout.write.set()
                }

                override fun onFailure(error: SPRError) = fail(error)
            }

            onKeyGet(characteristic, cbLocker, callback)
        }

        private fun BluetoothGatt.finish(characteristic: BluetoothGattCharacteristic, cbLocker: CBLockerModel) {
            val callback = object : ICallback {
                override fun onSuccess() = success()
                override fun onFailure(error: SPRError) = fail(error)
            }

            onFinished(characteristic, cbLocker, callback)
        }

        private fun BluetoothGatt.reset() {
            cbLocker.reset()
            disconnect()
            timeout.clearAll()
        }

        private fun BluetoothGatt.success() {
            onSuccess()
            reset()
        }

        private fun BluetoothGatt.fail(error: SPRError) {
            connectRetryCnt++
            if (connectRetryCnt > CBLockerConst.MaxRetryNum) {
                onFailure(error)
                reset()
            } else {
                isRetry = true
                disconnect()
                close()
                logd("########## connectRetryCnt: $connectRetryCnt")
                connectRemoteDevice()
            }
        }

        protected fun BluetoothGattCharacteristic.readData() = this.value.toString(Charsets.UTF_8)

        private fun alreadyWrittenToCharacteristic(readValue: String): Boolean {
            return when (actionType) {
                CBLockerGattActionType.Put -> CBLockerConst.UsingOrWriteReadData.contains(readValue)
                CBLockerGattActionType.Take -> !CBLockerConst.UsingReadData.contains(readValue)
                CBLockerGattActionType.OpenForMaintenance -> cbLocker.status == CBLockerGattStatus.Write
            }
        }
    }

    companion object {
        const val GATT_ERROR_STATE = 133
    }
}
