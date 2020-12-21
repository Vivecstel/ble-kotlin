package com.steleot.sample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.steleot.sample.databinding.ActivitySampleBinding
import timber.log.Timber

class SampleActivity : AppCompatActivity() {

    private val viewModel: SampleViewModel by viewModels()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            handlePermissions()
        } else {
            Timber.d("Permission not granted")
        }
    }
    private lateinit var binding: ActivitySampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySampleBinding.inflate(layoutInflater)
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
        val adapter = BleScanResultAdapter(viewModel)
        binding.recyclerView.adapter = adapter
        viewModel.results.observe(this, {
            adapter.submitList(it)
        })
    }
}
