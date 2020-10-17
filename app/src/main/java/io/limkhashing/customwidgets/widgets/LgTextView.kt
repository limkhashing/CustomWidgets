package io.limkhashing.customwidgets.widgets

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import io.limkhashing.customwidgets.R
import java.util.regex.Matcher
import java.util.regex.Pattern


class LgTextView : AppCompatTextView {
    private val transKeyObf = ObservableField<String>()
    private val input = ObservableField<Array<out String>>()
    private val boldRegex = Pattern.compile("\\*\\*(.*?)\\*\\*")
    private val linkRegex = Pattern.compile("\\{\\{(.*?)\\}\\}")

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs, 0, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs, defStyleAttr, defStyleAttr)
    }

    var transKey = ""
        set(value) {
            if (value.isNotEmpty())
                transKeyObf.set(transKey)

            field = value
        }
    var transKeyId = 0
        set(value) {
            if (value != 0) {
                transKeyObf.set(context!!.getString(value))
            }
            field = value
        }

    var arguments = emptyArray<String>()
        set(value) {
            input.set(value)
            field = value
        }

    var onLinkClickAction :  () -> Unit = {}

    init {
        val str = SpannableStringBuilder("Proceed")
        if (str.contains("**")) {
            val matcher = boldRegex.matcher(str)
            while (matcher.find()) {
                matcher.replaceFirst(Matcher.quoteReplacement(matcher.group(1)))
                str.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                str.replace( matcher.end()-2, matcher.end(),"")
                str.replace( matcher.start(), matcher.start()+2,"")
                matcher.reset(str)
            }
        }
        if (str.contains("{{") && str.contains("}}")) {
            val matcher = linkRegex.matcher(str)
            while (matcher.find()) {
                matcher.replaceFirst(Matcher.quoteReplacement(matcher.group(1)))
                movementMethod = LinkMovementMethod.getInstance()
                str.setSpan(object : ClickableSpan() {
                    override fun onClick(textView: View) {
                        onLinkClickAction.invoke()
                    }

                    override fun updateDrawState(textpaint: TextPaint) {
                        super.updateDrawState(textpaint)
                        textpaint.isUnderlineText = false
                    }
                }, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                str.replace( matcher.end()-2, matcher.end(),"")
                str.replace( matcher.start(), matcher.start()+2,"")
                matcher.reset(str)
            }
        }
        text = str

//        Observable.combineLatest<String, String, Array<out String>, String>(
//                transKeyObf.observe(),
//                (context?.applicationContext as FaveBizApplication).applicationDataManager.language().asObservable(),
//                input.observe().startWith(emptyArray()),
//                Function3 { key, language, inp ->
//                    (context?.applicationContext as FaveBizApplication).stringProvider.getTranslatedString(key, *inp)
//                }
//        ).subscribe({
//            val str = SpannableStringBuilder(it)
//            if (str.contains("**")) {
//                val matcher = boldRegex.matcher(str)
//                while (matcher.find()) {
//                    matcher.replaceFirst(Matcher.quoteReplacement(matcher.group(1)))
//                    str.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                    str.replace( matcher.end()-2, matcher.end(),"")
//                    str.replace( matcher.start(), matcher.start()+2,"")
//                    matcher.reset(str)
//                }
//            }
//            if (str.contains("{{") && str.contains("}}")) {
//                val matcher = linkRegex.matcher(str)
//                while (matcher.find()) {
//                    matcher.replaceFirst(Matcher.quoteReplacement(matcher.group(1)))
//                    movementMethod = LinkMovementMethod.getInstance()
//                    str.setSpan(object : ClickableSpan() {
//                        override fun onClick(textView: View) {
//                            onLinkClickAction.invoke()
//                        }
//
//                        override fun updateDrawState(textpaint: TextPaint) {
//                            super.updateDrawState(textpaint)
//                            textpaint.isUnderlineText = false
//                        }
//                    }, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//                    str.replace( matcher.end()-2, matcher.end(),"")
//                    str.replace( matcher.start(), matcher.start()+2,"")
//                    matcher.reset(str)
//                }
//            }
//            text = str
//        }, {
//            Log.d("LgTextView", "Something wrong" )
//        })
    }

    fun initView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        attrs?.let {
            val styledAttributes = context?.obtainStyledAttributes(it, R.styleable.LgTextView, 0, 0)
            val transKeyAttr = styledAttributes?.getString(R.styleable.LgTextView_transKey)
            val transKeyIdAttr = styledAttributes?.getResourceId(R.styleable.LgTextView_transKeyId, 0)
            transKeyAttr?.let {
                transKeyObf.set(transKeyAttr)
            }
            if (transKeyIdAttr != 0) {
                transKeyObf.set(context!!.getString(transKeyIdAttr!!))
            }
            styledAttributes.recycle()
        }
        setLinkTextColor(ContextCompat.getColor(context!!, R.color.light_blue))
    }

    fun registerTransInput(key: String, vararg param: String) {
        transKeyObf.set(key)
        input.set(param)
    }

}