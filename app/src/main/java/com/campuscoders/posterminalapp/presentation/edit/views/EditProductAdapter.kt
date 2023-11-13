package com.campuscoders.posterminalapp.presentation.edit.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.campuscoders.posterminalapp.databinding.RecyclerItemBinding
import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.utils.glide
import com.campuscoders.posterminalapp.utils.placeHolderProgressBar

class EditProductAdapter : RecyclerView.Adapter<EditProductAdapter.MyViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Products>() {
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var productsList: List<Products>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(val binding: RecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Products) {
            binding.textViewItem.text = item.productName
            binding.imageViewItem.glide(
                item.productImage,
                placeHolderProgressBar(binding.root.context)
            )
            binding.cardViewItem.setOnClickListener {
                onItemClickListener?.let {
                    it(item)
                }
            }
            binding.cardViewItem.setOnLongClickListener {
                onLongItemClickListener?.let {
                    it(item.productId)
                }
                false
            }
        }
    }

    private var onItemClickListener: ((Products) -> Unit)? = null
    private var onLongItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Products) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnLongItemClickListener(listener: (Int) -> Unit) {
        onLongItemClickListener = listener
    }

    fun updateProductsList(productId: Int) {
        productsList = productsList.filterIndexed { _, products ->
            products.productId != productId
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(productsList[position])
    }
}