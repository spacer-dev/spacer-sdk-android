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
import com.spacer.sdk.values.cbLocker.CBLockerConst
import com.spacer.sdk.values.cbLocker.CBLockerGattStatus

open class CBLockerGattService {
    private lateinit var context: Context
    protected lateinit var cbLocker: CBLockerModel
    private lateinit var connectHandler: Handler
    private lateinit var gattCallback: CBLockerGattCallback
    protected var needsFirstRead: Boolean = false

    protected val spacerId get() = cbLocker.spacerId
    private val bluetoothAdapter get() = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    private var connectRetryCnt = 0
    private var characteristicBeforeReadSuccess = false
    private var characteristicAfterReadSuccess = false
    private var characteristicWriteSuccess = false

    open fun connect(context: Context, cbLocker: CBLockerModel, gattCallback: CBLockerGattCallback, needsFirstRead: Boolean) {
        logd("connect: ${cbLocker.spacerId} ")

        this.context = context
        this.cbLocker = cbLocker
        this.gattCallback = gattCallback
        this.connectHandler = Handler(Looper.getMainLooper())
        this.needsFirstRead = needsFirstRead

        connectRemoteDevice()
    }

    private fun connectRemoteDevice() {
        val remoteDevice = bluetoothAdapter.getRemoteDevice(cbLocker.address)
        remoteDevice.connectGatt(
            context,
            false,
            gattCallback,
            BluetoothDevice.TRANSPORT_LE
        )
    }

    protected open fun postDelayedRunnable(exec: () -> Unit, execMode: String) {
        val runnable = object : Runnable {
            override fun run() {
                if (execMode === "beforeRead") {
                    if (characteristicBeforeReadSuccess) return
                } else if (execMode === "afterRead") {
                    if (characteristicAfterReadSuccess) return
                } else if (execMode === "write"){
                    if (characteristicWriteSuccess) return
                }

                connectRetryCnt++
                logd("########## connectRetryCnt: $connectRetryCnt")
                if (connectRetryCnt > CBLockerConst.MaxRetryNum) {
                    gattCallback.onFailure(SPRError.CBConnectDuringTimeout)
                } else {
                    exec()
                    connectHandler.postDelayed(this, CBLockerConst.ConnectMills)
                }
            }
        }
        connectHandler.postDelayed(runnable, CBLockerConst.ConnectMills)
    }

    open inner class CBLockerGattCallback : BluetoothGattCallback(), ICallback {

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            logd("onConnectionStateChange: $status, $newState")

            when {
                newState == BluetoothGatt.STATE_CONNECTED -> {
                    gatt.discoverServices()
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
            if (status != BluetoothGatt.GATT_SUCCESS) {
                return gatt.fail(SPRError.CBServiceNotFound)
            }

            val service = gatt.services.firstOrNull { it.uuid == CBLockerConst.DeviceServiceUUID }
                ?: return gatt.fail(SPRError.CBServiceNotFound)

            val characteristic = service.characteristics.firstOrNull { it.uuid == CBLockerConst.DeviceCharacteristicUUID }
                ?: return gatt.fail(SPRError.CBCharacteristicNotFound)

            if (needsFirstRead) {
                gatt.readCharacteristic(characteristic)
                postDelayedRunnable ({ gatt.readCharacteristic(characteristic) }, "beforeRead")
            } else {
                gatt.getKeyAndWriteCharacteristic(characteristic, cbLocker)
            }
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            logd("onMtuChanged: $mtu, $status")
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            logd("onCharacteristicRead: $status")

            if (BluetoothGatt.GATT_SUCCESS != status) {
                return gatt.fail(SPRError.CBReadingCharacteristicFailed)
            }

            if (cbLocker.status == CBLockerGattStatus.None) {
                characteristicBeforeReadSuccess = true
                gatt.getKeyAndWriteCharacteristic(characteristic, cbLocker)
            } else {
                characteristicAfterReadSuccess = true
                gatt.finish(characteristic, cbLocker)
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            logd("onCharacteristicWrite: $status")

            if (BluetoothGatt.GATT_SUCCESS != status) {
                return gatt.fail(SPRError.CBWritingCharacteristicFailed)
            }
            characteristicWriteSuccess = true
            cbLocker.update(CBLockerGattStatus.Write)
            gatt.readCharacteristic(characteristic)
            postDelayedRunnable ({ gatt.readCharacteristic(characteristic) }, "afterRead")
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
                    postDelayedRunnable ({ writeCharacteristic(characteristic) }, "write")
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
        }

        private fun BluetoothGatt.success() {
            onSuccess()
            reset()
        }

        private fun BluetoothGatt.fail(error: SPRError) {
            onFailure(error)
            reset()
        }

        protected fun BluetoothGattCharacteristic.readData() = this.value.toString(Charsets.UTF_8)
    }

    companion object {
        const val GATT_ERROR_STATE = 133
    }
}
