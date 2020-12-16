package com.steleot.blekotlin

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steleot.blekotlin.databinding.ItemBleScanResultBinding

class BleScanResultAdapter:
    ListAdapter<ScanResult, BleScanResultAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(ItemBleScanResultBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class ItemViewHolder(
        private val binding: ItemBleScanResultBinding
    ): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(scanResult: ScanResult) {
            val device = scanResult.device
            val rssi = scanResult.rssi
            binding.bleName.text =
                "Ble Device: ${if (device.name != null) device.name else "not available"}"
            binding.bleAddress.text = "Ble Address: ${device.address}"
            binding.bleRssi.text = "Ble rssi: ${rssi}"
        }
    }
}

object DiffCallback: DiffUtil.ItemCallback<ScanResult>() {

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
}
