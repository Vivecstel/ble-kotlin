package com.steleot.sample.ui.main

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steleot.sample.databinding.ItemBleScanResultBinding

class BleScanResultAdapter(
    private val viewModel: MainViewModel
) : ListAdapter<ScanResult, BleScanResultAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemBleScanResultBinding
                .inflate(LayoutInflater.from(parent.context), parent, false),
            viewModel
        )
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val bundle = payloads[0] as Bundle
            val rssi = bundle.getInt("rssi")
            holder.updateRssi(rssi)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    @SuppressLint("SetTextI18n")
    class ItemViewHolder(
        private val binding: ItemBleScanResultBinding,
        private val viewModel: MainViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            scanResult: ScanResult
        ) {
            val device = scanResult.device
            val rssi = scanResult.rssi
            binding.bleName.text =
                "Ble Device: ${if (device.name != null) device.name else "not available"}"
            binding.bleAddress.text = "Ble Address: ${device.address}"
            updateRssi(rssi)
            itemView.setOnClickListener {
                viewModel.handleDevice(scanResult.device)
            }
        }

        fun updateRssi(
            rssi: Int
        ) {
            binding.bleRssi.text = "Ble rssi: ${rssi}"
        }
    }
}

private object DiffCallback : DiffUtil.ItemCallback<ScanResult>() {

    override fun areItemsTheSame(
        oldItem: ScanResult,
        newItem: ScanResult
    ): Boolean {
        val oldDevice = oldItem.device
        val newDevice = newItem.device
        return oldDevice.address == newDevice.address
    }

    override fun areContentsTheSame(
        oldItem: ScanResult,
        newItem: ScanResult
    ): Boolean {
        return oldItem.rssi == newItem.rssi
    }

    override fun getChangePayload(
        oldItem: ScanResult,
        newItem: ScanResult
    ): Any {
        return Bundle().apply {
            putInt("rssi", newItem.rssi)
        }
    }
}
