package com.campuscoders.posterminalapp.presentation.cancel_and_document.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.campuscoders.posterminalapp.databinding.FragmentDocumentDetailsBinding

class DocumentDetailsFragment: Fragment() {

    private var _binding: FragmentDocumentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val from = it.getString("from")
            if (from == "cancel_sale") {
                // "geri" "satış iptal et"
            } else {
                // "geri" "göster" "gönder"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}