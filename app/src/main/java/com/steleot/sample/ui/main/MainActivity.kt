package com.steleot.sample.ui.main

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.steleot.sample.databinding.ActivityMainBinding
import com.steleot.sample.ui.BLE_DEVICE
import com.steleot.sample.ui.IS_SAVED
import com.steleot.sample.ui.details.DetailsActivity
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    /* location permission */
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            handlePermissions()
        } else {
            Timber.d("Permission not granted")
        }
    }

    /* details result launcher */
    private val detailsResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.startScanning()
        }
    }

    /* bluetooth result launcher */
    private val bluetoothResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.startScanning()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handlePermissions()
        initRecyclerView()
        initObservers()
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
        val adapter = BleScanResultAdapter(viewModel)
        binding.recyclerView.adapter = adapter
    }

    private fun initObservers() {
        viewModel.results.observe(this, {
            (binding.recyclerView.adapter as BleScanResultAdapter).submitList(it)
        })
        viewModel.bluetoothEnabled.observe(this) {
            bluetoothResultLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
        viewModel.goToDetails.observe(this) { event ->
            event.getContentIfNotHandledOrReturnNull()?.let { item ->
                viewModel.stopScanning()
                detailsResultLauncher.launch(
                    Intent(this, DetailsActivity::class.java).apply {
                        putExtra(BLE_DEVICE, item.first.device)
                        putExtra(IS_SAVED, item.second)
                    })
            }
        }
    }
}
