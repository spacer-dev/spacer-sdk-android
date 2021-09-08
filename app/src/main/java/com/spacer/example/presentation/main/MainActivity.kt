package com.spacer.example.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.spacer.example.R
import com.spacer.example.data.SdkConst.SdkConfig
import com.spacer.example.databinding.ActivityMainBinding
import com.spacer.example.presentation.common.PermissionRequester
import com.spacer.example.presentation.common.progress.LoadingOption
import com.spacer.sdk.SPR

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataBinding()
        initNavController()

        PermissionRequester(this).run()

        configureSdk()
    }

    private fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initNavController() {
        navController = findNavController(R.id.container)
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun configureSdk() {
        SPR.configure(SdkConfig)
    }

    fun startLoading(option: LoadingOption) {
        val isOverlay = option == LoadingOption.Overlay
        val isNotTouchable = option == LoadingOption.Overlay || option == LoadingOption.ScreenLock

        viewModel.progress.enable(isOverlay)
        if (isNotTouchable) {
            window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    fun stopLoading() {
        viewModel.progress.disable()
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}