package io.limkhashing.customwidgets.extensions

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import io.limkhashing.customwidgets.R
import io.limkhashing.customwidgets.utils.DrawableUtils
import io.limkhashing.customwidgets.utils.ViewState


/**
 * Alert Dialog implementation for all.
 * Customize with style required
 */
fun Context.createAlertDialog(
    msg: String,
    @StringRes title: Int = 0,
    @StringRes positiveText: Int = 0,
    @StringRes negativeText: Int = 0,
    @StringRes neutralText: Int = 0,
    okAction: () -> Unit = {},
    cancelAction: () -> Unit = {},
    neutralAction: () -> Unit = {},
    cancelable: Boolean = false,
    viewgrp: ViewGroup? = null
): AlertDialog {
    return let<Context, AlertDialog?> {
        val resource = resources
        AlertDialog.Builder(it)
            .apply {
                setMessage(msg)
                setPositiveButton(
                    if (positiveText == 0) resources.getString(R.string.cta_ok)
                    else resources.getString(positiveText)
                )
                { _, _ -> okAction.invoke() }
                if (negativeText != 0)
                    setNegativeButton(resources.getString(negativeText))
                    { _, _ -> cancelAction.invoke() }

                if (neutralText != 0)
                    setNeutralButton(resources.getString(neutralText))
                    { _, _ -> neutralAction.invoke() }

                if (title != 0)
                    setCustomTitle(
                        TextView(this@createAlertDialog).apply {
                            text =
                                resource.getString(title)
//                  typeface = rubikTypeface
                            setTextColor(ContextCompat.getColor(it, R.color.black))
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                resources.getDimension(R.dimen.text_size_20)
                            )
                            setPadding(
                                resources.getDimension(R.dimen.spacing_huge).toInt(),
                                resources.getDimension(R.dimen.spacing_huge).toInt(),
                                resources.getDimension(R.dimen.spacing_huge).toInt(),
                                paddingBottom
                            )
                        })
                if (viewgrp != null)
                    setView(viewgrp)
            }
            .setCancelable(cancelable)
            .create()
            .apply {
                setOnShowListener {
                    val btnPositive = getButton(Dialog.BUTTON_POSITIVE)
//            btnPositive.typeface = rubikTypeface
                    btnPositive.isAllCaps = false

                    val btnNegative = getButton(Dialog.BUTTON_NEGATIVE)
                    btnNegative.isAllCaps = false

                    val btnNeutral = getButton(Dialog.BUTTON_NEUTRAL)
                    btnNeutral.isAllCaps = false
                }
            }
    }!!
}

fun AppCompatActivity.createLoader(
    state: ViewState,
    title: String? = null,
    msg: String? = null,
    cancelable: Boolean = false,
    iconResID: Int,
    showNegativeButton: Boolean = false,
    showPositiveButton: Boolean = false,
    negativeButtonText: String = resources.getString(R.string.cta_dismiss),
    positiveButtonText: String = resources.getString(R.string.cta_retry),
    negativeAction: () -> Unit = {},
    positiveAction: () -> Unit = {}
): AlertDialog {
    return let {
        val v = LayoutInflater.from(it).inflate(R.layout.view_alert_loader, null)
            .apply {
                val layoutIcon = findViewById<View>(R.id.layout_icon)
                val stateImage = findViewById<ImageView>(R.id.iv_icon)
                val progressBar = findViewById<ProgressBar>(R.id.iv_progress)
                val titleTextView = findViewById<TextView>(R.id.tv_title)
                val descTextView = findViewById<TextView>(R.id.tv_desc)
                val positiveTextView = findViewById<TextView>(R.id.tv_positive)
                val negativeTextView = findViewById<TextView>(R.id.tv_negative)
                val dividerVertical = findViewById<View>(R.id.divider_v)
                val dividerHorizontal = findViewById<View>(R.id.divider_h)

                titleTextView.text = title
                descTextView.text = msg
                positiveTextView.text = positiveButtonText
                negativeTextView.text = negativeButtonText

                titleTextView.visibility = if (title != null) View.VISIBLE else View.GONE
                descTextView.visibility = if (msg != null) View.VISIBLE else View.GONE
                dividerHorizontal.visibility = if (showNegativeButton || showPositiveButton) View.VISIBLE else View.GONE
                dividerVertical.visibility = if (showNegativeButton && showPositiveButton) View.VISIBLE else View.GONE
                positiveTextView.visibility = if (showNegativeButton) View.VISIBLE else View.GONE
                negativeTextView.visibility = if (showPositiveButton) View.VISIBLE else View.GONE

                when (state) {
                    ViewState.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        stateImage.setImageDrawable(
                            DrawableUtils.createDrawable(
                                GradientDrawable.OVAL,
                                0,
                                R.dimen.size_3,
                                R.color.white_with_alpha_30,
                                0
                            )
                        )
                    }
                    else -> {
                        progressBar.visibility = View.INVISIBLE
                        setShowOrHideIcon(iconResID, layoutIcon, stateImage)
                    }
                }
            }
        AlertDialog.Builder(it)
            .setView(v)
            .create()
            .apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setCancelable(cancelable)

                v.findViewById<TextView>(R.id.tv_negative).setOnClickListener { negativeAction.invoke() }
                v.findViewById<TextView>(R.id.tv_positive).setOnClickListener { positiveAction.invoke() }
            }
    }
}

fun AppCompatActivity.setShowOrHideIcon(iconResID: Int, layoutIcon: View, stateImage: ImageView) {
    if (iconResID != 0)  {
        layoutIcon.visibility = View.VISIBLE
        stateImage.setImageDrawable(ResourcesCompat.getDrawable(resources, iconResID, null))
    }
    else layoutIcon.visibility = View.GONE
}
