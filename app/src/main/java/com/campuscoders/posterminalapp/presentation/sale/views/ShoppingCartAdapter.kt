package com.campuscoders.posterminalapp.presentation.sale.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.campuscoders.posterminalapp.databinding.RecyclerItemShoppingCartBinding
import com.campuscoders.posterminalapp.domain.model.ShoppingCart
import com.campuscoders.posterminalapp.utils.glide
import com.campuscoders.posterminalapp.utils.placeHolderProgressBar

class ShoppingCartAdapter : RecyclerView.Adapter<ShoppingCartAdapter.MyViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<ShoppingCart>() {
        override fun areItemsTheSame(oldItem: ShoppingCart, newItem: ShoppingCart): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ShoppingCart, newItem: ShoppingCart): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var shoppingCartList: List<ShoppingCart>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(val binding: RecyclerItemShoppingCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShoppingCart, position: Int) {
            binding.included.imageViewItem.glide(
                item.productImage,
                placeHolderProgressBar(binding.root.context)
            )
            binding.textViewServiceProductBarcode.text = item.productBarcode
            binding.included.textViewItem.text = item.productName
            binding.textViewQuantity.text = item.productQuantity
            binding.textViewCost.text = item.productPrice + "," + item.productPriceCent
            binding.materialCardViewMinus.setOnClickListener {
                onMinusClickListener?.let {
                    it(item.productId, position)
                }
            }
            binding.materialCardViewAdd.setOnClickListener {
                onAddClickListener?.let {
                    it(item.productId)
                }
            }
            binding.materialCardViewRemove.setOnClickListener {
                onRemoveClickListener?.let {
                    it(position, item.productId)
                }
            }
        }
    }

    private var onMinusClickListener: ((String, Int) -> Unit)? = null
    private var onAddClickListener: ((String) -> Unit)? = null
    private var onRemoveClickListener: ((Int,String) -> Unit)? = null

    fun setOnMinusClickListener(listener: (String, Int) -> Unit) {
        onMinusClickListener = listener
    }
    fun setOnAddClickListener(listener: (String) -> Unit) {
        onAddClickListener = listener
    }
    fun setOnRemoveClickListener(listener: (Int, String) -> Unit) {
        onRemoveClickListener = listener
    }

    fun notifyChanges() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RecyclerItemShoppingCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return shoppingCartList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(shoppingCartList[position], position)
    }
}