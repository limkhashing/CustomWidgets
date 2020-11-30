package io.limkhashing.customwidgets.core

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import io.limkhashing.customwidgets.utils.SingleOnClickListener

interface BaseViewModel : ActionViewModel,
    ToolbarViewModel,
    DrawerNavViewModel,
    ContentViewViewModel,
    LoadingViewViewModel,
    EmptyViewViewModel {
    val baseActionEvent: MutableLiveData<BaseEvent>

    fun onError()

    fun handleError(error: Throwable, title: String? = null)

    fun onLoading()

    fun onContent()

    fun onEmpty()

    fun appVersion(): String

    val onDrawerProfileClicked: SingleOnClickListener

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}