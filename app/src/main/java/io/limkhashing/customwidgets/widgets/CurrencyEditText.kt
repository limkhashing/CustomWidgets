package io.limkhashing.customwidgets.widgets

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import io.limkhashing.customwidgets.R
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt


class CurrencyEditText : AppCompatEditText {
    private var current = ""
    private val editText = this@CurrencyEditText

    // properties
    private var CurrencySymbol = "RM"
    private var Separator = ","
    private var Spacing = false
    private var Delimiter = false
    private var Decimals = true

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, defStyleAttr)
    }

    fun init(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs != null)
            setCustomAttribute(attrs)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
                if (s.toString() != current) {
                    editText.removeTextChangedListener(this)
                    var cleanString = s.toString().replace("[$,.]".toRegex(), "").replace(CurrencySymbol.toRegex(), "").replace("\\s+".toRegex(), "")
                    cleanString = cleanString.filter { char -> char.isDigit() }
                    if (cleanString == "") cleanString = "0" // For Indonesia currency to fallback since rupiah don't have cents

                    if (cleanString.isNotEmpty()) {
                        try {
                            val currencyFormat: String = if (Spacing) {
                                if (Delimiter) {
                                    "$CurrencySymbol. "
                                } else {
                                    "$CurrencySymbol "
                                }
                            } else {
                                if (Delimiter) {
                                    "$CurrencySymbol."
                                } else {
                                    CurrencySymbol
                                }
                            }
                            val parsed: Double
                            val parsedInt: Int
                            val formatted: String
                            if (Decimals) {
                                parsed = cleanString.toDouble()
                                formatted = NumberFormat.getCurrencyInstance().format(parsed / 100).replace(NumberFormat.getCurrencyInstance().currency?.symbol!!, currencyFormat)
                            } else {
                                parsedInt = cleanString.toInt()
                                formatted = currencyFormat + NumberFormat.getNumberInstance(Locale.US).format(parsedInt)
                            }
                            current = formatted

                            // if decimals are turned off and Separator is set as anything other than commas..
                            if (Separator != "," && !Decimals) {
                                //..replace the commas with the new separator
                                editText.setText(formatted.replace(",".toRegex(), Separator))
                            } else {
                                // since no custom separators were set, proceed with comma separation
                                editText.setText(formatted)
                            }
                            editText.setSelection(formatted.length)
                        } catch (e: NumberFormatException) {
                        }
                    }
                    editText.addTextChangedListener(this)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun setCustomAttribute(attrs: AttributeSet) {
        val styledAttributes = context?.obtainStyledAttributes(attrs, R.styleable.CurrencyEditText, 0, 0)
        val currencyAttr = styledAttributes?.getString(R.styleable.CurrencyEditText_currencySymbol)
                ?: CurrencySymbol
        val delimiterAttr = styledAttributes?.getBoolean(R.styleable.CurrencyEditText_delimiter, false)
                ?: Delimiter
        val spacingAttr = styledAttributes?.getBoolean(R.styleable.CurrencyEditText_spacing, true)
                ?: Spacing
        val decimalsAttr = styledAttributes?.getBoolean(R.styleable.CurrencyEditText_decimals, true)
                ?: Decimals
        val separatorAttr = styledAttributes?.getString(R.styleable.CurrencyEditText_separator)
                ?: Separator
        setCurrencySymbol(currencyAttr)
        setDelimiter(delimiterAttr)
        setSpacing(spacingAttr)
        setDecimals(decimalsAttr)
        setSeparator(separatorAttr)
        styledAttributes?.recycle()
    }

    /*
    *
    */
    val cleanDoubleValue: Double
        get() {
            var value = 0.0
            if (Decimals) {
                value = editText.text.toString().replace("[$,]".toRegex(), "").replace(CurrencySymbol.toRegex(), "").toDouble()
            } else {
                val cleanString = editText.text.toString().replace("[$,.]".toRegex(), "").replace(CurrencySymbol.toRegex(), "").replace("\\s+".toRegex(), "")
                try {
                    value = cleanString.toDouble()
                } catch (e: NumberFormatException) {
                }
            }
            return value
        }

    val cleanIntValue: Int
        get() {
            var value = 0
            if (Decimals) {
                val doubleValue = editText.text.toString().replace("[$,]".toRegex(), "").replace(CurrencySymbol.toRegex(), "").toDouble()
                value = doubleValue.roundToInt()
            } else {
                val cleanString = editText.text.toString().replace("[$,.]".toRegex(), "").replace(CurrencySymbol.toRegex(), "").replace("\\s+".toRegex(), "")
                try {
                    value = cleanString.toInt()
                } catch (e: NumberFormatException) {
                }
            }
            return value
        }

    fun setDecimals(value: Boolean) {
        Decimals = value
    }

    fun setCurrencySymbol(currencySymbol: String) {
        CurrencySymbol = currencySymbol
    }

    fun setSpacing(value: Boolean) {
        Spacing = value
    }

    fun setDelimiter(value: Boolean) {
        Delimiter = value
    }

    /**
     * Separator allows a custom symbol to be used as the thousand separator. Default is set as comma (e.g: 20,000)
     *
     *
     * Custom Separator cannot be set when Decimals is set as `true`. Set Decimals as `false` to continue setting up custom separator
     *
     * @value is the custom symbol sent in place of the default comma
     */
    fun setSeparator(value: String) {
        Separator = value
    }
}