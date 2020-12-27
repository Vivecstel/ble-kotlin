package com.steleot.sample.ui.details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steleot.blekotlin.BleGattService
import com.steleot.blekotlin.utils.getCharacteristicName
import com.steleot.blekotlin.utils.getDescriptorName
import com.steleot.blekotlin.utils.getServiceName
import com.steleot.blekotlin.utils.isIndicatable
import com.steleot.blekotlin.utils.isNotifiable
import com.steleot.blekotlin.utils.isReadable
import com.steleot.blekotlin.utils.isWritable
import com.steleot.sample.databinding.ItemBleCharacteristicBinding
import com.steleot.sample.databinding.ItemBleDescriptorBinding
import com.steleot.sample.databinding.ItemBleGattServiceBinding

class BleGattServiceAdapter(
    private val viewModel: DetailsViewModel
) : ListAdapter<BleGattService, BleGattServiceAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemBleGattServiceBinding
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
        private val binding: ItemBleGattServiceBinding,
        private val viewModel: DetailsViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            service: BleGattService
        ) {
            binding.service.text = service.uuid.getServiceName()
            binding.inflateLayout.removeAllViews()
            val inflater = LayoutInflater.from(itemView.context)
            service.characteristics.forEachIndexed { index, characteristic ->
                val charBinding = ItemBleCharacteristicBinding
                    .inflate(inflater, binding.inflateLayout, true)
                charBinding.characteristic.text = characteristic.uuid.getCharacteristicName()
                charBinding.readable.visibility =
                    if (characteristic.isReadable()) View.VISIBLE else View.GONE
                charBinding.writable.visibility =
                    if (characteristic.isWritable()) View.VISIBLE else View.GONE
                charBinding.notifiable.visibility =
                    if (characteristic.isNotifiable() || characteristic.isIndicatable())
                        View.VISIBLE else View.GONE
                charBinding.readable.setOnClickListener {
                    viewModel.handleReadAction(characteristic)
                }
                charBinding.writable.setOnClickListener {
                    viewModel.handleWriteAction(characteristic)
                }
                charBinding.notifiable.setOnClickListener {
                    viewModel.handleNotifiableAction(characteristic)
                }
                charBinding.separator.root.visibility =
                    if (index == service.characteristics.size - 1) View.INVISIBLE else View.VISIBLE
                charBinding.inflateLayout.removeAllViews()
                characteristic.descriptors.forEach { descriptor ->
                    val descBinding = ItemBleDescriptorBinding
                        .inflate(inflater, charBinding.inflateLayout, true)
                    descBinding.descriptor.text = descriptor.uuid.getDescriptorName()
                }
            }
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
