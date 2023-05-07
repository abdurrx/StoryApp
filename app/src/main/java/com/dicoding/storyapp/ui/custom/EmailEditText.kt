package com.dicoding.storyapp.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
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
        hint = "Masukkan email anda"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        warning =  ContextCompat.getDrawable(context, R.drawable.border_red) as Drawable
        custom =  ContextCompat.getDrawable(context, R.drawable.border_custom) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    showError = false
                    background = custom
                    error = null
                } else {
                    showError = true
                    background = warning
                    error = "Email yang dimasukkan tidak valid!"
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }

    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        // Sets the Drawables (if any) to appear to the left of, above, to the right of, and below the text.
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        return true
    }
}