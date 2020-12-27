package com.steleot.sample.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skydoves.bundler.bundleNonNull
import com.steleot.blekotlin.BleScanResult
import com.steleot.sample.R
import com.steleot.sample.databinding.ActivityDetailsBinding
import com.steleot.sample.ui.IS_SAVED
import com.steleot.sample.ui.SCAN_RESULT

@SuppressLint("SetTextI18n")
class DetailsActivity : AppCompatActivity() {

    private val scanResult: BleScanResult by bundleNonNull(SCAN_RESULT)
    private val isSaved: Boolean by bundleNonNull(IS_SAVED)
    private val viewModel: DetailsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return if (modelClass === DetailsViewModel::class.java) {
                    @Suppress("UNCHECKED_CAST")
                    DetailsViewModel(scanResult, isSaved) as T
                } else error("")
            }
        }
    }
    private lateinit var binding: ActivityDetailsBinding
    private var _isSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initActionButton()
        initRecyclerView()
        initObservers()
    }

    private fun initActionButton() {
        binding.actionButton.setImageResource(R.drawable.ic_save)
        binding.actionButton.setOnClickListener {
            viewModel.handleActionButton(_isSaved)
        }
    }

    private fun initRecyclerView() {
        val adapter = BleGattServiceAdapter(viewModel)
        binding.recyclerView.adapter = adapter
    }

    private fun initObservers() {
        viewModel.connectionInfo.observe(this) { info ->
            supportActionBar?.title = info.title
            if (_isSaved != info.isSaved) {
                _isSaved = info.isSaved
                binding.actionButton.setImageResource(if (_isSaved) R.drawable.ic_delete else R.drawable.ic_save)
            }
            if (info.connectionStatus) {
                binding.connectionStatus.text = "Status: Connected"
                binding.connectionStatus.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.green)
                )
            } else {
                binding.connectionStatus.text = "Status: Not Connected"
                binding.connectionStatus.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.red)
                )
            }
            binding.bleManufacturerName.text = info.manufacturerName
            binding.bleManufacturerValue.text = info.manufacturerValue
            (binding.recyclerView.adapter as BleGattServiceAdapter).submitList(info.services)
        }
        viewModel.text.observe(this) { event ->
            event.getContentIfNotHandledOrReturnNull()?.let {
                if (it.isNotEmpty()) Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(
        item: MenuItem
    ): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        super.onBackPressed()
    }
}