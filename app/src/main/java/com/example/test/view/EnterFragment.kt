package com.example.test.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.test.APP_ACTIVITY
import com.example.test.AppAuthenticationCallback
import com.example.test.R
import com.example.test.databinding.EnterFragmentBinding
import com.example.test.timer
import com.example.test.viewModel.EnterViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.enter_fragment.view.*
import java.util.concurrent.Executor

class EnterFragment : Fragment() {

    private lateinit var viewModel: EnterViewModel
    private var _binding: EnterFragmentBinding? = null
    private val binding get() = _binding!!
    private var compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EnterFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
        checkCode()
        APP_ACTIVITY.title = getString(R.string.enter)

        binding.keyboard.button0.setOnClickListener { onNumberClick(it) }
        binding.keyboard.button1.setOnClickListener { onNumberClick(it) }
        binding.keyboard.button2.setOnClickListener { onNumberClick(it) }
        binding.keyboard.button3.setOnClickListener { onNumberClick(it) }
        binding.keyboard.button4.setOnClickListener { onNumberClick(it) }
        binding.keyboard.button5.setOnClickListener { onNumberClick(it) }
        binding.keyboard.button6.setOnClickListener { onNumberClick(it) }
        binding.keyboard.button7.setOnClickListener { onNumberClick(it) }
        binding.keyboard.button8.setOnClickListener { onNumberClick(it) }
        binding.keyboard.button9.setOnClickListener { onNumberClick(it) }
        binding.keyboard.buttonDelete.setOnClickListener {
            val codeLength: Int = binding.keyboard.codeView.delete()
            configureRightButton(codeLength)
        }
        binding.keyboard.buttonFingerPrint.setOnClickListener {
            viewModel.auth(
                success = { authUser(viewModel.executor) },
                noneEnrolled = { prepareNumberLayout() })
        }
    }

    private fun prepareNumberLayout() {
        val codeLength: Int = binding.keyboard.codeView.delete()
        configureRightButton(codeLength)
    }

    private fun onNumberClick(it: View) {
        var str = ""
        if (it is TextView) {
            str = it.text.toString()
        }
        val codeLength: Int = binding.keyboard.codeView.input(str)
        configureRightButton(codeLength)
        checkCodeIsCorrect()
    }

    private fun checkCodeIsCorrect() {
        if (binding.keyboard.codeView.mCode.length == 4) {
            if (binding.keyboard.codeView.mCode == viewModel.getSharedPreferencesCode()) {
                APP_ACTIVITY.navController.navigate(R.id.action_enterFragment_to_homeFragment)
            } else {
                Toast.makeText(APP_ACTIVITY, getString(R.string.code_incorrect), Toast.LENGTH_SHORT)
                    .show()
                val codeLength: Int = binding.keyboard.codeView.deleteAll()
                configureRightButton(codeLength)
            }
        }
    }

    private fun checkCode() {
        viewModel.isCodeExist(
            exist = {
                viewModel.auth(
                    success = { authUser(viewModel.executor) },
                    noneEnrolled = { prepareNumberLayout() })
            },
            notExist = {
                APP_ACTIVITY.navController.navigate(
                    R.id.action_enterFragment_to_homeFragment
                )
            })
    }

    private fun configureRightButton(codeLength: Int) {
        if (codeLength > 0) {
            binding.keyboard.buttonDelete.visibility = View.VISIBLE
        } else {
            binding.keyboard.buttonDelete.visibility = View.GONE
        }
    }

    private fun initialization() {
        viewModel = ViewModelProvider(this).get(EnterViewModel::class.java)
    }

    private fun authUser(executor: Executor) {
        val biometricPrompt = BiometricPrompt(
            this, executor,
            AppAuthenticationCallback(
                APP_ACTIVITY,
                onError = { prepareNumberLayout() },
                onSucceeded = {
                    APP_ACTIVITY.navController.navigate(
                        R.id.action_enterFragment_to_homeFragment
                    )
                })
        )
        biometricPrompt.authenticate(viewModel.initPromptInfo())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        compositeDisposable.dispose()
    }
}