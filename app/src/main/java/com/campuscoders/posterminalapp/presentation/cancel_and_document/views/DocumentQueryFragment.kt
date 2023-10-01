package com.campuscoders.posterminalapp.presentation.payment.views


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentQueryDocumentBinding
import com.campuscoders.posterminalapp.utils.Constants
import com.campuscoders.posterminalapp.utils.showProgressDialog


class DocumentQueryFragment : Fragment() {

    private var _binding: FragmentQueryDocumentBinding? = null
    private val binding get() = _binding!!
    private var ftransaction: FragmentTransaction? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQueryDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val Context = context

        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        binding.buttonQueryDocument.setOnClickListener {
            context?.showProgressDialog(Constants.QUERY_DOCUMENT)
            Handler(Looper.getMainLooper()).postDelayed({
                ftransaction?.let {
                    it.replace(
                        R.id.fragmentContainerViewCancelSaleEDocument,
                        DocumentQueryContentFragment()
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