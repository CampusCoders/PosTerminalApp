package com.campuscoders.posterminalapp.presentation.sale.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.campuscoders.posterminalapp.databinding.RecyclerItemBinding
import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.utils.glide
import com.campuscoders.posterminalapp.utils.placeHolderProgressBar

class CategoriesAdapter(): RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Categories>() {
        override fun areItemsTheSame(oldItem: Categories, newItem: Categories): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Categories, newItem: Categories): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var categoriesList: List<Categories>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(val binding: RecyclerItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Categories, position: Int) {
            binding.textViewItem.text = item.categoryName
            binding.imageViewItem.glide(item.categoryImage, placeHolderProgressBar(binding.root.context))
            binding.cardViewItem.setOnClickListener {
                onItemClickListener?.let {
                    it(item.categoryId)
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
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(categoriesList[position],position)
    }
}