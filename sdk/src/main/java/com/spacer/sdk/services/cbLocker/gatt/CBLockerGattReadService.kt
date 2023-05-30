package com.spacer.sdk.services.cbLocker.gatt

import android.bluetooth.*
import android.content.Context
import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.SPRError
import com.spacer.sdk.data.extensions.LoggerExtensions.logd
import com.spacer.sdk.models.cbLocker.CBLockerModel
import com.spacer.sdk.values.cbLocker.*

open class CBLockerGattReadService {
    private lateinit var context: Context
    protected lateinit var cbLocker: CBLockerModel
    private lateinit var callback: IResultCallback<String>
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
        cbLocker: CBLockerModel,
        callback: IResultCallback<String>,
        isRetry: Boolean
    ) {
        logd("connect: ${cbLocker.spacerId} ")

        this.context = context
        this.cbLocker = cbLocker
        this.callback = callback
        this.isRetry = isRetry

        connectRemoteDevice()
    }

    @Synchronized
    private fun connectRemoteDevice() {
        val remoteDevice = bluetoothAdapter.getRemoteDevice(cbLocker.address)
        bluetoothGatt = remoteDevice.connectGatt(
            context, false, CBReadLockerGattCallback(), BluetoothDevice.TRANSPORT_LE
        )
        timeout.during.set()
        timeout.start.set()
    }

    private fun reset() {
        timeout.clearAll()
        bluetoothGatt?.disconnect()
    }

    @Synchronized
    private fun successIfNotCanceled(readData: String) {
        reset()
        if (!isCanceled) {
            isCanceled = true
            callback.onSuccess(readData)
        }
    }

    @Synchronized
    private fun failureIfNotCanceled(error: SPRError) {
        reset()
        if (!isCanceled) {
            isCanceled = true
            callback.onFailure(error)
        }
    }

    inner class CBReadLockerGattCallback : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            logd("onConnectionStateChange: $status, $newState")

            when {
                newState == BluetoothGatt.STATE_CONNECTED -> {
                    timeout.start.clear()
                    gatt.discoverServices()
                    timeout.discover.set()
                }
                status == GATT_ERROR_STATE -> {
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
                return failureIfNotCanceled(SPRError.CBServiceNotFound)
            }

            val service =
                gatt.services.firstOrNull { it.uuid == CBLockerConst.DeviceServiceUUID } ?: return failureIfNotCanceled(SPRError.CBServiceNotFound)

            val characteristic =
                service.characteristics.firstOrNull { it.uuid == CBLockerConst.DeviceCharacteristicUUID } ?: return failureIfNotCanceled(
                    SPRError.CBCharacteristicNotFound
                )

            gatt.readCharacteristic(characteristic)
            timeout.readBeforeWrite.set()
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            logd("onCharacteristicRead: $status")
            timeout.readBeforeWrite.clear()

            if (BluetoothGatt.GATT_SUCCESS != status) {
                return failureIfNotCanceled(SPRError.CBReadingCharacteristicFailed)
            }

            return successIfNotCanceled(characteristic.readData())
        }

        private fun BluetoothGattCharacteristic.readData() = this.value.toString(Charsets.UTF_8)
    }

    companion object {
        const val GATT_ERROR_STATE = 133
    }
}
