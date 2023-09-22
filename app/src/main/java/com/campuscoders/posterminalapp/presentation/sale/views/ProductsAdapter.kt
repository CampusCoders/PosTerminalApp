package com.campuscoders.posterminalapp.presentation.sale.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.campuscoders.posterminalapp.databinding.RecyclerItemBinding
import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.utils.glide
import com.campuscoders.posterminalapp.utils.placeHolderProgressBar

class ProductsAdapter(): RecyclerView.Adapter<ProductsAdapter.MyViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Products>() {
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var products: List<Products>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(val binding: RecyclerItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Products, position: Int) {
            binding.textViewItem.text = item.productName?:"<null>"
            binding.imageViewItem.glide(item.productImage, placeHolderProgressBar(binding.root.context))
            binding.cardViewItem.setOnClickListener {
                onItemClickListener?.let {
                    it(item.productId)
                }
            }
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(products[position],position)
    }
}