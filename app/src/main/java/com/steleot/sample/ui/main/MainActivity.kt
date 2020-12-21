package com.steleot.sample.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.steleot.blekotlin.BleDevice
import com.steleot.sample.databinding.ActivityMainBinding
import com.steleot.sample.ui.BLE_DEVICE
import com.steleot.sample.ui.details.DetailsActivity
import timber.log.Timber

class MainActivity : AppCompatActivity(), BleScanResultAdapter.Callbacks {

    private val viewModel: MainViewModel by viewModels()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            handlePermissions()
        } else {
            Timber.d("Permission not granted")
        }
    }
    private val resultForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            //todo
        }
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handlePermissions()
        initRecyclerView()
    }

    private fun handlePermissions() {
        when {
            ContextCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            else -> {
                viewModel.startScanning()
            }
        }
    }

    private fun initRecyclerView() {
        val adapter = BleScanResultAdapter(this)
        binding.recyclerView.adapter = adapter
        viewModel.results.observe(this, {
            adapter.submitList(it)
        })
    }

    override fun handleDevice(
        bleDevice: BleDevice
    ) {
        viewModel.startScanning()
        resultForResult.launch(Intent(this, DetailsActivity::class.java).apply {
            putExtra(BLE_DEVICE, bleDevice)
        })
    }
}
