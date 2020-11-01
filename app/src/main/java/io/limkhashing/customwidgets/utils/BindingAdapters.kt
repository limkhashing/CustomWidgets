package io.limkhashing.customwidgets.utils

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.SwitchCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.bumptech.glide.request.RequestOptions
import io.limkhashing.customwidgets.utils.glide.GlideApp


@BindingAdapter("android:src")
fun setImageViewResource(imageView: ImageView, resource: Int) {
    if (resource == 0) return
    imageView.setImageResource(resource)
}

@BindingAdapter(value = ["android:src", "default", "roundAsCircle"], requireAll = false)
fun setImageViewUrl(imageView: ImageView, url: String?, drawable: Drawable?, roundAsCircle: Boolean?) {
    var requestBuilder = GlideApp.with(imageView.context)
        .asBitmap()
        .load(url)
        .placeholder(drawable)
    roundAsCircle?.let {
        if (it) requestBuilder = requestBuilder.apply(RequestOptions.circleCropTransform())
    }
    requestBuilder.into(imageView)
}

@BindingAdapter("visibility")
fun setVisibility(view: View, value: Boolean?) {
    value?.let {
        view.visibility = if (it) View.VISIBLE else View.GONE
    }
}

@BindingAdapter("onSpinnerItemClicked")
fun setOnSpinnerItemClickedListener(spinner: Spinner, result: ObservableField<String>) {
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            result.set(parent?.getItemAtPosition(position) as String)
        }
    }
}

@BindingAdapter("spinnerItemPosition")
fun setSpinnerItemSelectedPosition(spinner: Spinner, index: ObservableInt) {
    spinner.setSelection(index.get())
}

@BindingAdapter(value = ["htmlSpannedString", "placeHolderText"], requireAll = false)
fun setHtmlSpannedString(textView: TextView, @StringRes textResource: Int, placeHolderText: ObservableField<String>?) {
    val spannedString = HtmlCompat.fromHtml(
        textView.context.resources.getString(textResource, placeHolderText?.get()),
        HtmlCompat.FROM_HTML_MODE_COMPACT
    )
    textView.text = spannedString
}

@SuppressLint("ClickableViewAccessibility")
@BindingAdapter("onSwitchClicked")
fun setOnSwitchClicked(switchCompat: SwitchCompat, switchAction: SingleOnClickListener) {
    switchCompat.setOnTouchListener { view, event ->
        if (event.action == MotionEvent.ACTION_UP)
            switchAction.onClickAction.invoke(view)
        return@setOnTouchListener true
    }
}
