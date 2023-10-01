package com.campuscoders.posterminalapp.presentation.login.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentVerificationBinding
import com.campuscoders.posterminalapp.presentation.login.VerificationViewModel
import com.campuscoders.posterminalapp.utils.Constants
import com.campuscoders.posterminalapp.utils.Constants.CELL_PHONE_NUMBER
import com.campuscoders.posterminalapp.utils.Constants.TEST_PIN
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.showProgressDialog
import com.campuscoders.posterminalapp.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerificationFragment : Fragment() {

    private var _binding: FragmentVerificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: VerificationViewModel
    private var ftransaction: FragmentTransaction? = null

    private val textViews = mutableListOf<TextView>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[VerificationViewModel::class.java]
        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        val phoneNumber = CELL_PHONE_NUMBER
        val formattedPhoneNumber = "+90 (" + phoneNumber.substring(0, 3) + ") " +
                phoneNumber.substring(3, 6) + " " +
                phoneNumber.substring(6, 8) + " " +
                phoneNumber.substring(8)
        binding.titleTextViewPhoneNumber.text = formattedPhoneNumber

        binding.buttonVerification.setOnClickListener {
            var control = true
            for (i in textViews.indices) {
                if (textViews[i].text.toString() != TEST_PIN[i].toString()) control = false
            }
            if (!control) {
                context?.showProgressDialog(Constants.PIN_VERIFYING)
                Handler(Looper.getMainLooper()).postDelayed({
                    toast(requireContext(), "Wrong PIN!", false)
                }, Constants.PROGRESS_BAR_DURATION.toLong())
            } else {
                context?.showProgressDialog(Constants.PIN_VERIFYING)
                Handler(Looper.getMainLooper()).postDelayed({
                    ftransaction?.let {
                        it.replace(R.id.fragmentContainerView, LoginTwoFragment())
                        it.commit()
                    }
                }, Constants.PROGRESS_BAR_DURATION.toLong())
            }
        }

        binding.textViewResend.setOnClickListener {
            viewModel.startTimer()
        }

        // Pin Number Buttons
        binding.buttonPinOne.setOnClickListener { onNumberClick("1") }
        binding.buttonPinTwo.setOnClickListener { onNumberClick("2") }
        binding.buttonPinThree.setOnClickListener { onNumberClick("3") }
        binding.buttonPinFour.setOnClickListener { onNumberClick("4") }
        binding.buttonPinFive.setOnClickListener { onNumberClick("5") }
        binding.buttonPinSix.setOnClickListener { onNumberClick("6") }
        binding.buttonPinSeven.setOnClickListener { onNumberClick("7") }
        binding.buttonPinEight.setOnClickListener { onNumberClick("8") }
        binding.buttonPinNine.setOnClickListener { onNumberClick("9") }
        binding.buttonPinZero.setOnClickListener { onNumberClick("0") }

        // Delete Buttons
        binding.buttonPinDeleteAll.setOnClickListener { onDeleteAllClick() }
        binding.buttonPinDelete.setOnClickListener { onDeleteClick(it) }

        // Text Views
        textViews.add(binding.textViewPinOne)
        textViews.add(binding.textViewPinTwo)
        textViews.add(binding.textViewPinThree)
        textViews.add(binding.textViewPinFour)
        textViews.add(binding.textViewPinFive)

        observe()
    }

    private fun observe() {
        viewModel.timer.observe(viewLifecycleOwner) {
            binding.textViewResend.visibility = View.INVISIBLE
            binding.textViewCountDownTimer.show()
            binding.textViewCountDownTimer.text = it ?: "null"
        }
        viewModel.onFinish.observe(viewLifecycleOwner) {
            if (it) {
                binding.textViewResend.show()
                binding.textViewCountDownTimer.visibility = View.INVISIBLE
            }
        }
    }


    private fun onNumberClick(number: String) {
        val textView = getNextEmptyPinTextView()
        textView?.text = number
    }

    private fun getNextEmptyPinTextView(): TextView? {
        return textViews.firstOrNull { it.text.isEmpty() }
    }

    private fun onDeleteClick(view: View) {
        val lastFilledTextView = getLastFilledPinTextView()
        if (lastFilledTextView != null) {
            if (view.id == R.id.buttonPinDelete) {
                lastFilledTextView.text = ""
            } else if (view.id == R.id.buttonPinDeleteAll) {
                val textView = getLastFilledPinTextView()
                textView?.text = ""
            }
        }
    }

    private fun onDeleteAllClick() {
        textViews.forEach { it.text = "" }
    }

    private fun getLastFilledPinTextView(): TextView? {
        return textViews.lastOrNull { it.text.isNotEmpty() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}