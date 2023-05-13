package com.dicoding.storyapp.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.dicoding.storyapp.R

class EmailEditText: AppCompatEditText, View.OnTouchListener {
    private var showError: Boolean = true

    private lateinit var clearButtonImage: Drawable

    private lateinit var warning: Drawable
    private lateinit var custom: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        warning =  ContextCompat.getDrawable(context, R.drawable.border_red) as Drawable
        custom =  ContextCompat.getDrawable(context, R.drawable.border_custom) as Drawable

        addTextChangedListener(onTextChanged = { s, _, _, _ ->
            if (Patterns.EMAIL_ADDRESS.matcher(s!!).matches()) {
                showError = false
                background = custom
                error = null
            } else {
                showError = true
                background = warning
                error = "Email yang dimasukkan tidak valid!"
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        return true
    }
}