package com.campuscoders.posterminalapp.presentation.cashier_and_daily_report.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.RecyclerItemDocumentBinding
import com.campuscoders.posterminalapp.domain.model.Orders

class DailyReportAdapter: RecyclerView.Adapter<DailyReportAdapter.MyViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Orders>() {
        override fun areItemsTheSame(oldItem: Orders, newItem: Orders): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Orders, newItem: Orders): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    private var orderList: List<Orders>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(val binding: RecyclerItemDocumentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Orders) {
            binding.textViewOrderTotal.text = "₺${item.orderTotal}"
            binding.textViewOrderStatus.text = when(item.orderStatus) {
                "Successful" -> {
                    binding.root.context.getString(R.string.adapter_successful)
                }
                "Basket Cancel" -> {
                    binding.root.context.getString(R.string.adapter_basket_cancel)
                }
                "Sale Cancel" -> {
                    binding.root.context.getString(R.string.adapter_cancel_sale)
                }
                else -> ""
            }
            binding.textViewOrderDate.text = item.orderDate
            binding.textViewOrderTime.text = item.orderTime
            binding.textViewOrderNo.text = item.orderReceiptNo
            binding.textViewMaliID.text = item.orderMaliId
            binding.textViewTerminalId.text = item.orderTerminalId
            binding.textViewOrderReceiptType.text = item.orderReceiptType
        }
    }

    fun updateOrderList(orders: List<Orders>) {
        orderList = orders
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemDocumentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(orderList[position])
    }
}