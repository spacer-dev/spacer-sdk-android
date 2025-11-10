package com.spacer.sdk.services.cbLocker.scan

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.ParcelUuid
import com.spacer.sdk.data.IFailureCallback
import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.SPRError
import com.spacer.sdk.data.extensions.LoggerExtensions.logd
import com.spacer.sdk.models.cbLocker.CBLockerModel
import com.spacer.sdk.models.sprLocker.SPRLockerModel
import com.spacer.sdk.services.sprLocker.SPRLockerService
import com.spacer.sdk.values.cbLocker.CBLockerConst
import com.spacer.sdk.values.sprLocker.SPRLockerStatus
import java.util.*

open class CBLockerScanService {
    private lateinit var context: Context
    private lateinit var scanHandler: Handler
    private lateinit var scanCallback: CBLockerScanCallback

    private val bluetoothAdapter get() = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    private val bluetoothLeScanner get() = bluetoothAdapter.bluetoothLeScanner
    private val BluetoothAdapter.isDisabled get() = !isEnabled

    private var scanningCnt = 0
    protected var isScanning = false

    protected open fun startScan(context: Context, scanCallback: CBLockerScanCallback) {
        logd("startScan")

        this.context = context
        this.scanCallback = scanCallback
        this.scanHandler = Handler(Looper.getMainLooper())

        if (bluetoothAdapter.isDisabled) {
            return scanCallback.onFailure(SPRError.CBScanDisabled)
        }

        val scanFilters = buildScanFilters()
        val scanSettings = buildScanSettings()

        bluetoothLeScanner.startScan(scanFilters, scanSettings, scanCallback)

        isScanning = true
        scanningCnt = 0

        postDelayedRunnable()
    }

    protected open fun postDelayedRunnable() {
        val runnable = object : Runnable {
            override fun run() {
                scanningCnt++
                logd("scanningCnt: $scanningCnt")

                val isScanned = scanCallback.onDelayed()
                if (isScanned) {
                    return stopScan()
                }

                if (scanningCnt > MaxScanningCnt) {
                    stopScan()
                    scanCallback.onFailure(SPRError.CBScanDeviceNotFound)
                } else {
                    scanHandler.postDelayed(this, CBLockerConst.ScanMills)
                }
            }
        }

        scanHandler.postDelayed(runnable, CBLockerConst.ScanMills)
    }

    fun stopScan() {
        if (!isScanning) return

        isScanning = false
        scanningCnt = 0
        scanHandler.removeCallbacksAndMessages(null)
        bluetoothLeScanner.stopScan(scanCallback)
    }

    private fun buildScanFilters(): List<ScanFilter> {
        val scanFilters: MutableList<ScanFilter> = ArrayList()
        val builder = ScanFilter.Builder()
        builder.setServiceUuid(ParcelUuid(CBLockerConst.DeviceServiceUUID))
        scanFilters.add(builder.build())

        return scanFilters
    }

    private fun buildScanSettings(): ScanSettings? {
        val builder = ScanSettings.Builder()
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)

        return builder.build()
    }

    protected open inner class CBLockerScanCallback : ScanCallback(), IFailureCallback {

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            if (callbackType == ScanSettings.CALLBACK_TYPE_ALL_MATCHES) {
                val deviceName = result?.device?.name ?: return
                onDiscovered(result.parse(deviceName))
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            logd("onBatchScanResults: ${results.toString()}")
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            logd("onScanFailed: $errorCode")
        }

        open fun onDiscovered(cbLocker: CBLockerModel) {
            throw RuntimeException("not implementation!")
        }

        open fun onDelayed(): Boolean {
            throw RuntimeException("not implementation!")
        }
    }

    protected fun ScanResult.parse(spacerId: String) = CBLockerModel(spacerId, this.device.address)

    protected fun MutableList<CBLockerModel>.parse(
        token: String, callback: IResultCallback<List<SPRLockerModel>>
    ) {
        val spacerIds = this.map { it.spacerId }
        val cbLockerMap = this.associateBy { it.spacerId }
        val getLockersCallback = object : IResultCallback<List<SPRLockerModel>> {
            override fun onSuccess(result: List<SPRLockerModel>) {
                result.forEach { sprLocker ->
                    cbLockerMap[sprLocker.id]?.let { cbLocker ->
                        sprLocker.address = cbLocker.address
                        sprLocker.isScanned = spacerIds.contains(sprLocker.id)
                    }
                }
                callback.onSuccess(result)
            }

            override fun onFailure(error: SPRError) {
                callback.onFailure(error)
            }
        }
        SPRLockerService().getLockers(token, spacerIds, getLockersCallback)
    }

    protected fun CBLockerModel.parse(
        token: String, isScanned: Boolean, isGetLocker:Boolean, callback: IResultCallback<SPRLockerModel>
    ) {
        val spacerId = this.spacerId
        val address = this.address
        val getLockersCallback = object : IResultCallback<SPRLockerModel> {
            override fun onSuccess(result: SPRLockerModel) {
                result.address = address
                result.isScanned = isScanned
                callback.onSuccess(result)
            }

            override fun onFailure(error: SPRError) {
                callback.onFailure(error)
            }
        }
        if (!isGetLocker) {
            SPRLockerService().getLocker(token, spacerId, getLockersCallback)
        } else {
            callback.onSuccess(SPRLockerModel.placeholder(this.spacerId, this.address))
        }
    }

    companion object {
        private const val MaxScanningCnt = 3
    }
}