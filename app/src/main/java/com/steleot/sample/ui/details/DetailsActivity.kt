package com.steleot.sample.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skydoves.bundler.bundleNonNull
import com.steleot.blekotlin.BleDevice
import com.steleot.sample.databinding.ActivityDetailsBinding
import com.steleot.sample.ui.BLE_DEVICE

@SuppressLint("SetTextI18n")
class DetailsActivity : AppCompatActivity(), BleGattServicesAdapter.Callbacks {

    private val bleDevice: BleDevice by bundleNonNull(BLE_DEVICE)
    private val viewModel: DetailsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return if (modelClass === DetailsViewModel::class.java) {
                    @Suppress("UNCHECKED_CAST")
                    DetailsViewModel(bleDevice) as T
                } else error("")
            }
        }
    }
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObservers()
    }

    private fun initObservers() {
        viewModel.connectionStatus.observe(this) {
            binding.connectionStatus.text = "Status: $it"
        }
        val adapter = BleGattServicesAdapter(this)
        binding.recyclerView.adapter = adapter
        viewModel.services.observe(this) { adapter.submitList(it) }
    }

    override fun action() {
        // todo
    }
}