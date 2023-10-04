package com.campuscoders.posterminalapp.presentation.payment.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.campuscoders.posterminalapp.databinding.FragmentSelectCartTypeBinding

class SelectCartTypeFragment : Fragment() {

    private var _binding: FragmentSelectCartTypeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectCartTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardViewMCR.setOnClickListener {
            var intent = Intent(requireActivity(), MCRType::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        binding.cardViewICC.setOnClickListener {
            var intent = Intent(requireActivity(), ICCType::class.java)
            startActivity(intent)
            requireActivity().finish()

        }
        binding.cardViewNFC.setOnClickListener {
            var intent = Intent(requireActivity(), NFCType::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}