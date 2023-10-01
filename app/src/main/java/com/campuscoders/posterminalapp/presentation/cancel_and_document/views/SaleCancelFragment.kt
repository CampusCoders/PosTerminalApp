package com.campuscoders.posterminalapp.presentation.payment.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentCancelSaleBinding
import com.campuscoders.posterminalapp.utils.Constants
import com.campuscoders.posterminalapp.utils.showProgressDialog

class SaleCancelFragment : Fragment() {

    private var _binding: FragmentCancelSaleBinding? = null
    private val binding get() = _binding!!
    private var ftransaction: FragmentTransaction? = null

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCancelSaleBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val Context = context

        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        binding.buttonQuerySale.setOnClickListener {
            context?.showProgressDialog(Constants.QUERY_SALE)
            Handler(Looper.getMainLooper()).postDelayed({
                ftransaction?.let {
                    it.replace(
                        R.id.fragmentContainerViewCancelSaleEDocument,
                        SaleCancelContentFragment()
                    )
                    it.addToBackStack(null)
                    it.commit()
                }
            }, Constants.PROGRESS_BAR_DURATION.toLong())
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}