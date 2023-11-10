package com.campuscoders.posterminalapp.presentation.main.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentMainBinding
import com.campuscoders.posterminalapp.presentation.CancelSaleEDocumentActivity
import com.campuscoders.posterminalapp.presentation.EditActivity
import com.campuscoders.posterminalapp.presentation.SaleActivity
import com.campuscoders.posterminalapp.utils.Constants
import com.campuscoders.posterminalapp.utils.showProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = Intent(requireActivity(), CancelSaleEDocumentActivity::class.java)

        binding.cardSaleStart.setOnClickListener {
            context?.showProgressDialog(Constants.LOADING_MALI_ID)
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(requireActivity(), SaleActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }, Constants.PROGRESS_BAR_DURATION.toLong())
        }

        binding.cardSaleCancel.setOnClickListener {
            intent.putExtra("navigation", "1")
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        }

        binding.cardDocumentQuery.setOnClickListener {
            intent.putExtra("navigation", "2")
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        }

        binding.cardShowProducts.setOnClickListener {
            val intent = Intent(requireActivity(), EditActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}