package com.example.test.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import com.example.test.R
import java.util.*

class CodeView(
    context: Context, attrs: AttributeSet
) : LinearLayout(context, attrs) {


    private val DEFAULT_CODE_LENGTH = 4
    var mCodeViews: MutableList<CheckBox> = ArrayList()
    var mCode = ""
    private var mCodeLength = DEFAULT_CODE_LENGTH

    init {
        View.inflate(context, R.layout.view_code_pf_lockscreen, this)
        setUpCodeViews()
    }

    private fun setUpCodeViews() {
        removeAllViews()
        mCodeViews.clear()
        mCode = ""
        for (i in 0 until mCodeLength) {
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.view_pf_code_checkbox, null) as CheckBox
            val layoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val margin = resources.getDimensionPixelSize(R.dimen.code_fp_margin)
            layoutParams.setMargins(margin, margin, margin, margin)
            view.layoutParams = layoutParams
            view.isChecked = false
            addView(view)
            mCodeViews.add(view)
        }
    }

    fun input(number: String): Int {
        if (mCode.length == mCodeLength) {
            return mCode.length
        }
        mCodeViews[mCode.length].toggle()
        mCode += number

        return mCode.length
    }

    fun delete(): Int {
        if (mCode.isEmpty()) {
            return mCode.length
        }
        mCode = mCode.substring(0, mCode.length - 1)
        mCodeViews[mCode.length].toggle()
        return mCode.length
    }

    fun deleteAll(): Int {
        mCode = ""
        for (i in 0..3) {
            mCodeViews[i].toggle()
        }
        return 0
    }

}