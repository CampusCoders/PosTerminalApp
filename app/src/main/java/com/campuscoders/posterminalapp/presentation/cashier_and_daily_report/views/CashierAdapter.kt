package com.campuscoders.posterminalapp.presentation.cashier_and_daily_report.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.campuscoders.posterminalapp.databinding.RecyclerItemCashierBinding
import com.campuscoders.posterminalapp.domain.model.TerminalUsers

class CashierAdapter: RecyclerView.Adapter<CashierAdapter.MyViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<TerminalUsers>() {
        override fun areItemsTheSame(oldItem: TerminalUsers, newItem: TerminalUsers): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: TerminalUsers, newItem: TerminalUsers): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var cashiersList: List<TerminalUsers>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(val binding: RecyclerItemCashierBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TerminalUsers) {
            binding.textViewCashierTerminalId.text = item.terminalUserTerminalId
            binding.textViewCashierFullName.text = item.terminalUserFullName
            binding.textViewCashierDate.text = item.terminalUserDate
            binding.textViewCashierTime.text = item.terminalUserTime
            binding.materialCardViewCashierItem.setOnLongClickListener {
                onLongItemClickListener?.let {
                    it(item.terminalUserId)
                }
                false
            }
        }
    }

    private var onLongItemClickListener: ((Int) -> Unit)? = null

    fun setOnLongItemClickListener(listener: (Int) -> Unit) {
        onLongItemClickListener = listener
    }

    fun updateCashierList(terminalId: Int) {
        cashiersList = cashiersList.filterIndexed { _, terminalUsers ->
            terminalUsers.terminalUserId != terminalId
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemCashierBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return cashiersList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(cashiersList[position])
    }
}