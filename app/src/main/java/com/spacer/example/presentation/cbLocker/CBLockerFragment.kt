package com.spacer.example.presentation.cbLocker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.spacer.example.R
import com.spacer.example.databinding.FragmentCbLockerBinding

class CBLockerFragment : Fragment() {
    private lateinit var binding: FragmentCbLockerBinding
    private val viewModel by lazy { ViewModelProvider(this).get(CBLockerViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cb_locker, container, false)
        binding.viewModel = viewModel
        binding.listener = CBLockerListener(this)
        binding.lifecycleOwner = this

        return binding.root
    }
}