package com.spacer.example.presentation.sprLocker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.spacer.example.R
import com.spacer.example.databinding.FragmentSprLockerBinding

class SPRLockerFragment : Fragment() {
    private lateinit var binding: FragmentSprLockerBinding
    private val viewModel by lazy { ViewModelProvider(this).get(SPRLockerViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_spr_locker, container, false)
        binding.viewModel = viewModel
        binding.listener = SPRLockerListener(this)
        binding.lifecycleOwner = this

        return binding.root
    }
}