// Reference: https://github.com/anonymous-ME/OTPView
package io.limkhashing.customwidgets.widgets

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity.CENTER
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import io.limkhashing.customwidgets.R
import java.util.*

class OtpView : LinearLayout, View.OnFocusChangeListener, TextWatcher, View.OnKeyListener {
    private var numberOfOtp = 4
    private var fieldsList: ArrayList<EditText>? = null
    private var otpListener: OTPListener? = null

    private val currentFocusIndex: Int
        get() {
            var index = -1
            for (i in 0 until numberOfOtp)
                if (fieldsList!![i].isFocused) {
                    index = i
                    break
                }
            return index
        }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        var lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.gravity = CENTER
        this.orientation = HORIZONTAL
        this.layoutParams = lp
        fieldsList = ArrayList()

        // Adding OTP Fields
        for (i in 0 until numberOfOtp) {
            val field = EditText(context)
            field.textAlignment = View.TEXT_ALIGNMENT_CENTER
            field.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            field.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            field.inputType = InputType.TYPE_CLASS_NUMBER
            field.tag = "EditText:$i"
            field.setBackgroundResource(R.drawable.otp_bg)
            field.setPadding(getPX(10f), getPX(10f), getPX(10f), getPX(10f))

            lp = LayoutParams(getPX(65f), ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.setMargins(getPX(20f), getPX(8f), getPX(16f), getPX(8f))
            field.layoutParams = lp
            field.isCursorVisible = true
            field.onFocusChangeListener = this
            field.addTextChangedListener(this)
            field.setOnKeyListener(this)

            fieldsList!!.add(field)
            this.addView(field)
        }
        selectEditText(fieldsList!![0])
    }

    fun setOTPText(otpText: String) {
        for (i in 0 until numberOfOtp)
            fieldsList!![i].setText(otpText[i].toString())
        selectEditText(fieldsList!![fieldsList!!.size - 1])
        if (this.otpListener != null)
            this.otpListener!!.onOTPEntered(getOTPText())
    }

    private fun getOTPText(): String {
        val otpString = StringBuffer()
        for (editText in fieldsList!!) {
            otpString.append(editText.text)
        }
        return otpString.toString()
    }

    fun setFieldCount(fieldCount: Int) {
        this.numberOfOtp = fieldCount
    }

    private fun selectEditText(edit_txt: EditText) {
        edit_txt.isEnabled = true
        edit_txt.isFocusableInTouchMode = true
        edit_txt.requestFocus()

        val inputMethodManager = edit_txt.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(edit_txt, InputMethodManager.SHOW_IMPLICIT)

        edit_txt.isActivated = true
        edit_txt.isPressed = true

        for (editText in fieldsList!!)
            editText.isEnabled = editText.isFocused
    }

    fun setOTPListener(otpListener: OTPListener) {
        this.otpListener = otpListener
    }

    private fun getPX(dip: Float): Int {
        val r = resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            r.displayMetrics
        ).toInt()
    }

    private fun moveFocusForward() {
        try {
            selectEditText(fieldsList!![currentFocusIndex + 1])
            fieldsList!![currentFocusIndex].setText(" ")
            val nextEditText = fieldsList!![currentFocusIndex]
            nextEditText.setSelection(nextEditText.text.length)
        } catch (e: Exception) {
        }
    }

    private fun moveFocusBackwards() {
        try {
            selectEditText(fieldsList!![currentFocusIndex - 1])
        } catch (e: Exception) {
        }
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        (v as EditText).setSelection(v.text.length)
        if (hasFocus) {
            v.setBackgroundResource(R.drawable.otp_active_bg)
        } else {
            if (v.text.isEmpty())
                v.setBackgroundResource(R.drawable.otp_bg)
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (currentFocusIndex == numberOfOtp - 1)
            try {
                Integer.parseInt(getOTPText())
                otpListener!!.onOTPEntered(getOTPText())
            } catch (e: Exception) {
            }

        var currentFocusEditText = fieldsList!![currentFocusIndex]
        if (s.isEmpty()) {
            currentFocusEditText.text.clear()
            currentFocusEditText.setSelection(currentFocusEditText.text.length)
            moveFocusBackwards()
            return
        }

        if (currentFocusIndex < numberOfOtp - 1) {
            if (s.length == 1) {
                if (s[0] != ' ')
                    moveFocusForward()
            } else if (s.length > 1) {
                val nextOtpNumber = s.toString()[s.length - 1]
                currentFocusEditText = fieldsList!![currentFocusIndex]
                currentFocusEditText.setText(nextOtpNumber.toString())
                currentFocusEditText.setSelection(currentFocusEditText.text.length)
            }
        } else if (s.length > 1) {
            val lastOtpNumber = s.toString()[s.length - 1]
            currentFocusEditText.setText(lastOtpNumber.toString())
            currentFocusEditText.setSelection(currentFocusEditText.text.length)
        }
    }

    override fun afterTextChanged(s: Editable) {}

    override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
        return false
    }

    interface OTPListener {
        fun onOTPEntered(otpText: String)
    }

    enum class OtpState() {
        VERIFY_OTP,
        RESEND_OTP
    }
}