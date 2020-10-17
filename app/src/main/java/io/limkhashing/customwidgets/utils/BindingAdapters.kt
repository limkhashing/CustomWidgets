package io.limkhashing.customwidgets.utils

import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import io.limkhashing.customwidgets.R

@BindingAdapter("android:src")
fun setImageViewResource(imageView: ImageView, resource: Int) {
    if (resource == 0) return
    imageView.setImageResource(resource)
}

@BindingAdapter("android:src")
fun setImageViewUrl(imageView: ImageView, url: String) {
//    Glide.with(imageView.context)
//        .load(if (url == "") R.drawable.ic_drawer_profile else url)
//        .error(R.drawable.ic_drawer_profile)
//        .into(imageView)
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
