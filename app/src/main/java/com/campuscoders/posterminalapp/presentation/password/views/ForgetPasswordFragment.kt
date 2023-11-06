package com.campuscoders.posterminalapp.presentation.password.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.campuscoders.posterminalapp.presentation.login.views.LoginTwoFragment
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentForgetPasswordBinding
import com.campuscoders.posterminalapp.presentation.password.ForgetPasswordViewModel
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.toast

class ForgetPasswordFragment : Fragment() {

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ForgetPasswordViewModel

    private var ftransaction: FragmentTransaction? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ForgetPasswordViewModel::class.java]
        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        binding.outlinedButtonCancel.setOnClickListener {
            ftransaction?.let {
                it.replace(R.id.fragmentContainerView, LoginTwoFragment())
                it.commit()
                // try popupbackstack
            }
        }
        binding.outlinedButtonConfirm.setOnClickListener {
            if (areFieldsNotEmpty()) {
                viewModel.controlMainUserCellPhone(
                    binding.textInputEditTextTCKN.text.toString(),
                    binding.textInputEditTextPhone.text.toString())
            }
        }

        observe()
    }

    private fun observe() {
        viewModel.statusIsMatched.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarForgetPassword.hide()
                    toast(requireContext(),"MATCHED!",false)
                    ftransaction?.let {f->
                        f.replace(R.id.fragmentContainerView, ResetPasswordFragment())
                        f.commit()
                    }
                }
                is Resource.Loading -> {
                    binding.progressBarForgetPassword.show()
                }
                is Resource.Error -> {
                    binding.progressBarForgetPassword.hide()
                    toast(requireContext(),it.message?:"ERROR!",false)
                }
            }
        }
    }

    private fun areFieldsNotEmpty(): Boolean {
        if (binding.textInputEditTextTCKN.text.toString() == "" || binding.textInputEditTextPhone.text.toString() == "") {
            toast(requireContext(), "Lütfen, tüm alanları doldurun.", false)
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}