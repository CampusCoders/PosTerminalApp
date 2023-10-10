package com.campuscoders.posterminalapp.presentation.cancel_and_document.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentCancelSaleAndQueryDocumentBinding
import com.campuscoders.posterminalapp.utils.Constants
import com.campuscoders.posterminalapp.utils.showProgressDialog

class CancelSaleAndQueryDocumentFragment : Fragment() {

    private var _binding: FragmentCancelSaleAndQueryDocumentBinding? = null
    private val binding get() = _binding!!

    private var ftransaction: FragmentTransaction? = null

    private var from: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCancelSaleAndQueryDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        arguments?.let {
            from = it.getString("from")
            if (from == "cancel_sale") {
                binding.topAppBar.title = "Satış İptal"
            } else {
                binding.topAppBar.title = "E-Belge Sorgulama"
            }
        }

        binding.buttonQuerySale.setOnClickListener {
            context?.showProgressDialog(Constants.QUERY_SALE)
            Handler(Looper.getMainLooper()).postDelayed({
                val documentDetailsFragment = DocumentDetailsFragment()
                val bundle = Bundle()
                if (from == "cancel_sale") {
                    bundle.putString("from","cancel_sale")
                } else {
                    bundle.putString("from","query_document")
                }
                documentDetailsFragment.arguments = bundle
                ftransaction?.let {
                    it.replace(R.id.fragmentContainerViewCancelSaleEDocument,documentDetailsFragment)
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