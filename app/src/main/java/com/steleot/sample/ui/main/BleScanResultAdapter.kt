package com.steleot.sample.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steleot.blekotlin.utils.getManufacturerData
import com.steleot.blekotlin.utils.getManufacturerName
import com.steleot.sample.databinding.ItemBleScanResultBinding

class BleScanResultAdapter(
    private val viewModel: MainViewModel
) : ListAdapter<BleScanResultWithSelected, BleScanResultAdapter.ItemViewHolder>(DiffCallback) {

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
            item: BleScanResultWithSelected
        ) {
            val device = item.first.device
            val rssi = item.first.rssi
            binding.bleName.text =
                "Ble Device: ${if (device.name != null) device.name else "not available"}"
            binding.bleAddress.text = "Ble Address: ${device.address}"
            item.first.getManufacturerData()?.let {
                binding.bleManufacturerName.visibility = View.VISIBLE
                binding.bleManufacturerName.text =
                    "Ble Manufacturer: ${it.first.getManufacturerName()}"
            } ?: run {
                binding.bleManufacturerName.visibility = View.INVISIBLE
            }
            binding.isSaved.visibility = if (item.second) View.VISIBLE else View.GONE
            updateRssi(rssi)
            itemView.setOnClickListener {
                viewModel.handleDevice(item)
            }
        }

        fun updateRssi(
            rssi: Int
        ) {
            binding.bleRssi.text = "Ble rssi: ${rssi}"
        }
    }
}

private object DiffCallback : DiffUtil.ItemCallback<BleScanResultWithSelected>() {

    override fun areItemsTheSame(
        oldItem: BleScanResultWithSelected,
        newItem: BleScanResultWithSelected
    ): Boolean {
        val oldDevice = oldItem.first.device
        val newDevice = newItem.first.device
        return oldDevice.address == newDevice.address
    }

    override fun areContentsTheSame(
        oldItem: BleScanResultWithSelected,
        newItem: BleScanResultWithSelected
    ): Boolean {
        return oldItem.first.rssi == newItem.first.rssi
    }

    override fun getChangePayload(
        oldItem: BleScanResultWithSelected,
        newItem: BleScanResultWithSelected
    ): Any {
        return Bundle().apply {
            putInt("rssi", newItem.first.rssi)
        }
    }
}
