package com.steleot.sample.ui.details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steleot.blekotlin.BleGattService
import com.steleot.sample.databinding.ItemBleGattServicesBinding

class BleGattServicesAdapter(
    private val viewModel: DetailsViewModel
) : ListAdapter<BleGattService, BleGattServicesAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemBleGattServicesBinding
                .inflate(LayoutInflater.from(parent.context), parent, false),
            viewModel
        )
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    @SuppressLint("SetTextI18n")
    class ItemViewHolder(
        private val binding: ItemBleGattServicesBinding,
        private val viewModel: DetailsViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            service: BleGattService
        ) {
            // todo
        }
    }
}

private object DiffCallback : DiffUtil.ItemCallback<BleGattService>() {

    override fun areItemsTheSame(
        oldItem: BleGattService,
        newItem: BleGattService
    ): Boolean = oldItem.uuid == newItem.uuid

    override fun areContentsTheSame(
        oldItem: BleGattService,
        newItem: BleGattService
    ): Boolean = oldItem.uuid == newItem.uuid
}
