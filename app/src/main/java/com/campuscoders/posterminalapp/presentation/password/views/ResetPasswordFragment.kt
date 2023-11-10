package com.campuscoders.posterminalapp.presentation.password.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentPasswordResetBinding
import com.campuscoders.posterminalapp.presentation.login.views.LoginFragment
import com.campuscoders.posterminalapp.presentation.login.views.LoginTwoFragment
import com.campuscoders.posterminalapp.presentation.password.ResetPasswordViewModel
import com.campuscoders.posterminalapp.utils.Constants
import com.campuscoders.posterminalapp.utils.Constants.CELL_PHONE_NUMBER
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.toast

class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentPasswordResetBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ResetPasswordViewModel

    private var ftransaction: FragmentTransaction? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPasswordResetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ResetPasswordViewModel::class.java]
        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        binding.titleTextViewPasswordResetPhone.text = CELL_PHONE_NUMBER

        binding.outlinedButtonConfirm.setOnClickListener {
            if (areFieldsNotEmpty()) {
                viewModel.updateMainUserPassword(binding.textInputEditTextNewPassword.text.toString())
            }
        }

        binding.outlinedButtonCancel.setOnClickListener {
            ftransaction?.let {
                it.setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                it.replace(R.id.fragmentContainerView, LoginTwoFragment())
                it.commit()
            }
        }

        binding.textViewResendResetFragment.setOnClickListener {
            viewModel.startTimer()
        }

        observe()
    }

    private fun observe() {
        viewModel.statusIsUpdated.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBarResetPassword.hide()
                    ftransaction?.let { f ->
                        f.setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                        f.replace(R.id.fragmentContainerView, LoginFragment())
                        f.commit()
                    }
                }
                is Resource.Loading -> {
                    binding.progressBarResetPassword.show()
                }
                is Resource.Error -> {
                    binding.progressBarResetPassword.hide()
                    toast(requireContext(), it.message ?: "failed!", false)
                }
            }
        }
        viewModel.timer.observe(viewLifecycleOwner) {
            binding.textViewResendResetFragment.visibility = View.INVISIBLE
            binding.textViewCountDownTimerResetFragment.show()
            binding.textViewCountDownTimerResetFragment.text = it ?: "null"
        }
        viewModel.onFinish.observe(viewLifecycleOwner) {
            if (it) {
                binding.textViewResendResetFragment.show()
                binding.textViewCountDownTimerResetFragment.visibility = View.INVISIBLE
            }
        }
    }

    private fun areFieldsNotEmpty(): Boolean {
        if (binding.textInputEditTextTemporaryPin.text.toString() == "" ||
            binding.textInputEditTextNewPassword.text.toString() == "" ||
            binding.textInputEditTextNewPasswordAgain.text.toString() == ""
        ) {
            toast(requireContext(), "Lütfen, tüm alanları doldurun", false)
            return false
        } else if (binding.textInputEditTextNewPassword.text.toString() != binding.textInputEditTextNewPasswordAgain.text.toString()) {
            toast(requireContext(), "Parolalar eşleşmiyor", false)
            return false
        }
        else if (binding.textInputEditTextTemporaryPin.text.toString() != Constants.TEST_PIN) {
            toast(requireContext(), "Yanlış PIN", false)
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}