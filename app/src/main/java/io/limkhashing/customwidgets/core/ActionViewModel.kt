package io.limkhashing.customwidgets.core

import android.os.Bundle
import io.limkhashing.customwidgets.extensions.DialogBuilder

interface ActionViewModel {

    fun showDialog(dialogBuilder: DialogBuilder)

    fun showDialogFragment(
        baseDialogFragment: BaseDialogFragment,
        fragmentTag: String
    )

    fun addFragment(
        baseDialogFragment: BaseFragment,
        fragmentTag: String
    )

    fun dismissFragment(fragmentTag: String)

    fun dismissLoader()

    fun showToast(msg: String)

    fun finishView()

    fun <T> startActivity(clazz: Class<T>)

    fun <T> startActivity(clazz: Class<T>, data: Bundle? = null)

    fun <T> startActivityFlag(clazz: Class<T>, data: Bundle? = null, flag: Int)

    fun startDeepLinkActivity(uri: String)
}
