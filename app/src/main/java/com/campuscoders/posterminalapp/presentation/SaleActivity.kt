package com.campuscoders.posterminalapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.ActivitySaleBinding
import com.campuscoders.posterminalapp.presentation.sale.views.BarcodeScannerActivity
import com.campuscoders.posterminalapp.presentation.sale.views.ShoppingCartFragment
import com.campuscoders.posterminalapp.utils.Constants
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.showProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaleActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySaleBinding

    private var isFabMenuOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingActionButtonBarcode.setOnClickListener {
            val intent = Intent(this, BarcodeScannerActivity::class.java)
            startActivity(intent)
        }

        binding.materialCardViewShoppingCart.setOnClickListener {
            if (binding.textViewShoppingCartItemCount.text != "0") {
                val ftransaction = supportFragmentManager.beginTransaction()

                ftransaction.replace(R.id.fragmentContainerViewSaleActivity, ShoppingCartFragment())
                ftransaction.addToBackStack(null)
                ftransaction.commit()

                showProgressDialog(Constants.SHOPPING_CART)

                binding.floatingActionButtonMainAdd.hide()
                binding.floatingActionButtonAdd.hide()
                binding.floatingActionButtonSearch.hide()
                binding.floatingActionButtonBarcode.hide()
                binding.extendedFabSettings.hide()
                binding.floatingActionButtonBack.hide()
            } else {
                Toast.makeText(this, "Sepet boş, ürün ekleyiniz.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.extendedFabSettings.hide()
        binding.floatingActionButtonSearch.hide()
        binding.floatingActionButtonBarcode.hide()
        binding.floatingActionButtonAdd.hide()

        binding.floatingActionButtonMainAdd.setOnClickListener {
            toggleFabMenu()
        }

        binding.floatingActionButtonAdd.setOnClickListener{

        }

        binding.extendedFabSettings.setOnClickListener {
            toggleFabMenu()
        }
        binding.floatingActionButtonBack.setOnClickListener {
            // pop fragment
        }
    }

    fun setShoppingCartTotal(total: String) {
        if (total == "null") {
            binding.materialCardViewtotal.hide()
        } else {
            binding.materialCardViewtotal.show()
            val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.animation_slide_up)
            binding.textViewTotal.startAnimation(slideUpAnimation)
            binding.textViewTotal.text = total
        }
    }

    fun setShoppingCart(shoppingCartItemCount: String) {

        if (shoppingCartItemCount == "0") {
            binding.linearLayoutShoppingCartItemBackground.hide()
        } else {
            binding.linearLayoutShoppingCartItemBackground.show()

            val newSizeText = shoppingCartItemCount
            val currentSizeText = binding.textViewShoppingCartItemCount.text.toString()

            if (currentSizeText != newSizeText) {
                val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.animation_slide_up)
                binding.textViewShoppingCartItemCount.startAnimation(slideUpAnimation)
            } else {
                val shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.animaton_shake)
                binding.animationContainer.startAnimation(shakeAnimation)
            }

            binding.textViewShoppingCartItemCount.text = newSizeText
        }

        /*
        val size = hashMapFromProducts.size
        if (size == 0) {
            binding.linearLayoutShoppingCartItemBackground.hide()
        } else {
            binding.linearLayoutShoppingCartItemBackground.show()
            val newSizeText = size.toString()
            val currentSizeText = binding.textViewShoppingCartItemCount.text.toString()

            if (currentSizeText != newSizeText) {
                val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.animation_slide_up)
                binding.textViewShoppingCartItemCount.startAnimation(slideUpAnimation)
            } else {
                val shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.animaton_shake)
                binding.animationContainer.startAnimation(shakeAnimation)
            }

            binding.textViewShoppingCartItemCount.text = newSizeText
        }
        hashmap = hashMapFromProducts

         */
    }

    fun setEnabledShoppingCartIcon(isEnabled: Boolean) {
        binding.imageViewShoppingCart.isEnabled = isEnabled
    }

    fun changeSaleActivityTopBarTitle(newTitle: String) {
        binding.topAppBarSaleActivity.title = newTitle
    }

    private fun toggleFabMenu() {
        if (isFabMenuOpen) {
            binding.extendedFabSettings.hide()
            binding.floatingActionButtonSearch.hide()
            binding.floatingActionButtonBarcode.hide()
            binding.floatingActionButtonAdd.hide()
            binding.floatingActionButtonMainAdd.show()
        } else {
            binding.extendedFabSettings.show()
            binding.floatingActionButtonSearch.show()
            binding.floatingActionButtonBarcode.show()
            binding.floatingActionButtonAdd.show()
            binding.floatingActionButtonMainAdd.hide()
        }
        isFabMenuOpen = !isFabMenuOpen
    }
}