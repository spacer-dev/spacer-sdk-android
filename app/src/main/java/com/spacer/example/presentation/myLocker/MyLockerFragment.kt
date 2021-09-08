package com.spacer.example.presentation.myLocker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.spacer.example.R
import com.spacer.example.databinding.FragmentMyLockerBinding

class MyLockerFragment : Fragment() {
    private lateinit var binding: FragmentMyLockerBinding
    private val viewModel by lazy { ViewModelProvider(this).get(MyLockerViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_locker, container, false)
        binding.viewModel = viewModel
        binding.listener = MyLockerListener(this)
        binding.lifecycleOwner = this

        return binding.root
    }
}