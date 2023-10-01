package com.campuscoders.posterminalapp.presentation.login.views

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
import com.campuscoders.posterminalapp.databinding.FragmentLoginBinding
import com.campuscoders.posterminalapp.domain.model.MainUser
import com.campuscoders.posterminalapp.presentation.login.LoginViewModel
import com.campuscoders.posterminalapp.utils.Constants.CELL_PHONE_NUMBER
import com.campuscoders.posterminalapp.utils.Constants.FIRST_NAME
import com.campuscoders.posterminalapp.utils.Constants.LAST_NAME
import com.campuscoders.posterminalapp.utils.Constants.MEMBER_STORE
import com.campuscoders.posterminalapp.utils.Constants.PASSWORD
import com.campuscoders.posterminalapp.utils.Constants.TERMINAL_ID
import com.campuscoders.posterminalapp.utils.Constants.VKN_TCKN
import com.campuscoders.posterminalapp.utils.CustomSharedPreferences
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.toast
import com.campuscoders.posterminalapp.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    private var ftransaction: FragmentTransaction? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        val customSharedPreferences = CustomSharedPreferences(requireContext())
        saveMainUserIfNotExist(customSharedPreferences)

        binding.buttonLogin.setOnClickListener {
            areTestCredentialsValid()
        }

        binding.radioButtonGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_button_dev -> {
                    clearAllFields()
                }
                R.id.radio_button_test -> {
                    binding.textInputEditTerminalId.setText(TERMINAL_ID)
                    binding.textInputEditTextTCKN.setText(VKN_TCKN)
                    binding.textInputEditTextMemberStore.setText(MEMBER_STORE)
                    binding.textInputEditTextPassword.setText(PASSWORD)
                    disableTextFields()
                }
                R.id.radio_button_custom -> {
                    clearAllFields()
                }
            }
        }

        observer()
    }

    private fun observer() {
        viewModel.statusInsertMainUser.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBarLogin.hide()
                    toast(requireContext(), "SUCCESS! INSERT MAIN USER", false)
                }
                is Resource.Loading -> {
                    binding.progressBarLogin.show()
                }
                is Resource.Error -> {
                    binding.progressBarLogin.hide()
                    toast(requireContext(), it.message ?: "Error!", true)
                }
            }
        }
        viewModel.statusControlMainUser.observe(viewLifecycleOwner) { it ->
            when (it) {
                is Resource.Success -> {
                    binding.progressBarLogin.hide()
                    context?.showProgressDialog(Constants.INFORMATIONS_VERIFYING)
                    Handler(Looper.getMainLooper()).postDelayed({
                        ftransaction?.let { f ->
                            f.replace(R.id.fragmentContainerView, VerificationFragment())
                            f.commit()
                        }
                    }, Constants.PROGRESS_BAR_DURATION.toLong())
                }
                is Resource.Loading -> {
                    binding.progressBarLogin.show()
                }
                is Resource.Error -> {
                    binding.progressBarLogin.hide()
                    context?.showProgressDialog(Constants.INFORMATIONS_VERIFYING)
                    Handler(Looper.getMainLooper()).postDelayed({
                    toast(requireContext(), it.message ?: "*Error*", false)
                    }, Constants.PROGRESS_BAR_DURATION.toLong())
                }
            }
        }
        viewModel.statusInsertTerminalUser.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBarLogin.hide()
                    toast(requireContext(), "SUCCESS! INSERT TERMINAL USER", false)
                }
                is Resource.Loading -> {
                    binding.progressBarLogin.show()
                }
                is Resource.Error -> {
                    binding.progressBarLogin.hide()
                    toast(requireContext(), it.message ?: "Error!", false)
                }
            }
        }
    }

    private fun saveMainUserIfNotExist(sharedPreferences: CustomSharedPreferences) {
        if (sharedPreferences.getControl()!!) {
            viewModel.saveMainUser(
                MainUser(
                    mainUserTerminalId = TERMINAL_ID,
                    mainUserVknTckn = VKN_TCKN,
                    mainUserUyeIsyeriNo = MEMBER_STORE,
                    mainUserPassword = PASSWORD,
                    mainUserCellphoneNumber = CELL_PHONE_NUMBER,
                    mainUserFirstName = FIRST_NAME,
                    mainUserLastName = LAST_NAME,
                )
            )
            sharedPreferences.setControl(false)
            sharedPreferences.setMainUserLogin(TERMINAL_ID, VKN_TCKN, MEMBER_STORE, PASSWORD)
        }
    }

    // Control TextFields
    private fun areTestCredentialsValid() {
        val terminalId = binding.textInputEditTerminalId.text.toString()
        val tckn = binding.textInputEditTextTCKN.text.toString()
        val memberStore = binding.textInputEditTextMemberStore.text.toString()
        val password = binding.textInputEditTextPassword.text.toString()

        if (terminalId == "" || tckn == "" || memberStore == "" || password == "") {
            toast(requireContext(), requireActivity().getString(R.string.empty_fields), true)
        } else {
            viewModel.controlMainUser(
                MainUser(
                    mainUserTerminalId = terminalId,
                    mainUserVknTckn = tckn,
                    mainUserUyeIsyeriNo = memberStore,
                    mainUserPassword = password
                )
            )
        }
    }

    // TextFields false
    private fun disableTextFields() {
        binding.textInputEditTerminalId.isEnabled = false
        binding.textInputEditTextTCKN.isEnabled = false
        binding.textInputEditTextMemberStore.isEnabled = false
        binding.textInputEditTextPassword.isEnabled = false
    }

    // TextFields true
    private fun enableTextFields() {
        binding.textInputEditTerminalId.isEnabled = true
        binding.textInputEditTextTCKN.isEnabled = true
        binding.textInputEditTextMemberStore.isEnabled = true
        binding.textInputEditTextPassword.isEnabled = true
    }

    private fun clearAllFields() {
        binding.textInputEditTerminalId.text?.clear()
        binding.textInputEditTextTCKN.text?.clear()
        binding.textInputEditTextMemberStore.text?.clear()
        binding.textInputEditTextPassword.text?.clear()
        enableTextFields()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}