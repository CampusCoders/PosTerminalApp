package com.campuscoders.posterminalapp.presentation

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.ActivitySaleBinding
import com.campuscoders.posterminalapp.presentation.sale.BaseViewModel
import com.campuscoders.posterminalapp.presentation.sale.views.BarcodeScannerActivity
import com.campuscoders.posterminalapp.presentation.sale.views.ShoppingCartFragment
import com.campuscoders.posterminalapp.utils.Constants
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.showProgressDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaleActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySaleBinding

    private lateinit var viewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[BaseViewModel::class.java]

        binding.materialCardViewShoppingCart.setOnClickListener {
            if (binding.textViewShoppingCartItemCount.text != "0") {
                val ftransaction = supportFragmentManager.beginTransaction()
                ftransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                ftransaction.replace(R.id.fragmentContainerViewSaleActivity, ShoppingCartFragment())
                ftransaction.addToBackStack(null)
                ftransaction.commit()

                showProgressDialog(Constants.SHOPPING_CART)
            } else {
                Toast.makeText(this, "Sepet boş, ürün ekleyiniz.", Toast.LENGTH_SHORT).show()
            }
        }

        observe()
    }

    private fun observe() {
        viewModel.statusAddProduct.observe(this) {
            when(it) {
                is Resource.Success -> {
                    Snackbar.make(binding.root, "${it.data?.productName?:""} ${resources.getString(R.string.add_product)}", Snackbar.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    println("::: loading :::")
                }
                is Resource.Error -> {
                    Toast.makeText(this,it.message?:"Error Product",Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.statusShoppingCartQuantity.observe(this) {
            setShoppingCart(it.toString())
        }
        viewModel.statusTotal.observe(this) {
            setShoppingCartTotal(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    val barcode = it.getStringExtra(Constants.BARCODE)
                    viewModel.addProductByBarcode(barcode?:"")
                }
            } else {
                data?.let {
                    val message = it.getStringExtra(Constants.BARCODE)
                    Toast.makeText(
                        this, "Camera initialization error: $message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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
    }

    fun setEnabledShoppingCartIcon(isEnabled: Boolean) {
        binding.imageViewShoppingCart.isEnabled = isEnabled
    }

    fun changeSaleActivityTopBarTitle(newTitle: String) {
        binding.topAppBarSaleActivity.title = newTitle
    }

    fun goToBarcodeActivity() {
        val intent = Intent(this, BarcodeScannerActivity::class.java)
        startActivityForResult(intent, Constants.REQUEST_CODE)
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
    }
}