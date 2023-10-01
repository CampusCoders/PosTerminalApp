package com.campuscoders.posterminalapp.presentation.login.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentLoginTwoBinding
import com.campuscoders.posterminalapp.presentation.MainActivity
import com.campuscoders.posterminalapp.presentation.login.LoginTwoViewModel
import com.campuscoders.posterminalapp.presentation.password.views.ForgetPasswordFragment
import com.campuscoders.posterminalapp.utils.Constants
import com.campuscoders.posterminalapp.utils.CustomSharedPreferences
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.showProgressDialog
import com.campuscoders.posterminalapp.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginTwoFragment : Fragment() {

    private var _binding: FragmentLoginTwoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginTwoViewModel

    private var ftransaction: FragmentTransaction? = null

    private var isAdmin: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[LoginTwoViewModel::class.java]
        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        getSharedPreferencesInfos()

        binding.card1Manager.setOnClickListener {
            clearTextFields()
            isAdmin = true
            binding.textInputLayoutUyeNo.hint = getString(R.string.uye_isyeri_no)
            binding.card1Manager.setCardBackgroundColor(resources.getColor(R.color.background))
            binding.card2Cashier.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.textViewCashier.setTextColor(resources.getColor(R.color.background))
            binding.textViewManager.setTextColor(resources.getColor(R.color.white))
            getSharedPreferencesInfos()
        }

        binding.card2Cashier.setOnClickListener {
            clearTextFields()
            isAdmin = false
            binding.textInputLayoutUyeNo.hint = getString(R.string.terminal_id)
            binding.card1Manager.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.card2Cashier.setCardBackgroundColor(resources.getColor(R.color.background))
            binding.textViewCashier.setTextColor(resources.getColor(R.color.white))
            binding.textViewManager.setTextColor(resources.getColor(R.color.background))
            getSharedPreferencesInfos()
        }

        binding.outlinedButtonLogin.setOnClickListener {
            if (areFieldsNotEmpty()) {
                viewModel.controlPassword(
                    binding.textInputEditTextUyeNo.text.toString(),
                    binding.textInputEditTextPassword.text.toString(),
                    isAdmin,
                    binding.checkBoxRememberMe.isChecked,
                    requireContext()
                )
            }
        }

        binding.outlinedButtonForgotPassword.setOnClickListener {
            if (isAdmin) {
                ftransaction?.let {
                    it.replace(R.id.fragmentContainerView, ForgetPasswordFragment())
                    it.addToBackStack(null)
                    it.commit()
                }
            } else {
                toast(requireContext(), getString(R.string.cashier_forgot_password_warn), false)
            }
        }

        observe()
    }

    private fun observe() {
        viewModel.statusControlPassword.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBarLoginTwo.hide()
                    context?.showProgressDialog(Constants.INFORMATIONS_VERIFYING)
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }, Constants.PROGRESS_BAR_DURATION.toLong())
                }

                is Resource.Loading -> {
                    binding.progressBarLoginTwo.show()
                }

                is Resource.Error -> {
                    binding.progressBarLoginTwo.hide()
                    context?.showProgressDialog(Constants.INFORMATIONS_VERIFYING)
                    Handler(Looper.getMainLooper()).postDelayed({
                        toast(requireContext(), it.message ?: "Error", false)
                    }, Constants.PROGRESS_BAR_DURATION.toLong())
                }
            }
        }
    }

    private fun getSharedPreferencesInfos() {
        binding.checkBoxRememberMe.isChecked = false
        val customSharedPreferences = CustomSharedPreferences(requireContext())
        val manager = customSharedPreferences.getMainUserLogin()["remember_me_manager"] as Boolean
        val cashier = customSharedPreferences.getMainUserLogin()["remember_me_terminal"] as Boolean
        if (manager && isAdmin) {
            binding.checkBoxRememberMe.isChecked = true
            binding.textInputEditTextUyeNo.setText(customSharedPreferences.getMainUserLogin()["uye_isyeri_no"].toString())
            binding.textInputEditTextPassword.setText(customSharedPreferences.getMainUserLogin()["password"].toString())
        } else if (cashier && !(isAdmin)) {
            binding.checkBoxRememberMe.isChecked = true
            binding.textInputEditTextUyeNo.setText(customSharedPreferences.getTerminalUserLogin()["terminal_id"].toString())
            binding.textInputEditTextPassword.setText(customSharedPreferences.getTerminalUserLogin()["password"].toString())
        }
    }

    private fun clearTextFields() {
        binding.textInputEditTextUyeNo.setText("")
        binding.textInputEditTextPassword.setText("")
    }

    private fun areFieldsNotEmpty(): Boolean {
        if (binding.textInputEditTextUyeNo.text.toString() == "" || binding.textInputEditTextPassword.text.toString() == "") {
            toast(requireContext(), getString(R.string.empty_fields), false)
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}