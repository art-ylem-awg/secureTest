package com.example.test.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.test.APP_ACTIVITY
import com.example.test.viewModel.HomeViewModel
import com.example.test.R
import com.example.test.databinding.HomeFragmentBinding
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private var compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
        setFields()
        APP_ACTIVITY.title = getString(R.string.set_code)

    }

    private fun setFields() {
        rxHandlerEditText()
        binding.editCode.setText(null, TextView.BufferType.EDITABLE);
        binding.editCode.hint = viewModel.getSharedPreferencesCode()
        binding.button.setOnClickListener {
            viewModel.editSharedPref(binding.editCode.text.toString())
        }
    }

    private fun createEditTextObservable(): Observable<CharSequence>{
        val observable: Observable<CharSequence> = Observable.create { emitter ->
            binding.editCode.doOnTextChanged { text, _, _, _ ->
                text?.let { emitter.onNext(it) }
            }
        }
        return observable
    }

    private fun rxHandlerEditText() {
        createEditTextObservable()
            .doOnNext {
                if (it.length == 4) {
                    binding.button.isClickable = true
                    binding.button.background =
                        resources.getDrawable(R.drawable.btn_bg_green)
                } else {
                    binding.button.isClickable = false
                    binding.button.background = resources.getDrawable(R.drawable.btn_bg)
                }
            }
            .doOnSubscribe { t: Disposable? ->
                t?.let { compositeDisposable.add(it) }
            }
            .subscribe()
    }

    private fun initialization() {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        compositeDisposable.dispose()
    }
}