package io.limkhashing.customwidgets.deeplink

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.NavUtils


class DeepLinkHandler {
    companion object {
        const val OUTLET = "/outlet"
        const val PARAMS_OUTLET_ID = "outlet_id"
    }


    private fun isLoginRequiredForDeeplink(path: String?): Boolean {
        if (path.isNullOrEmpty()) {
            return false
        }
        return when (path) {
            OUTLET -> false
            else -> true
        }
    }

    fun getIntentForDeepLink(
        context: Context,
        uri: Uri?,
        userInteractor: UserInteractor,
        createNewTask: Boolean
    ): Pair<Intent?, Intent?> {

        var intent: Intent? = null

        // here check whether Login is Required For Deeplink
        uri?.let {
            if (isLoginRequiredForDeeplink(it.path)) {
                when {
                    userInteractor.isUserLogin() -> {
                    }
                    else -> throw LoginRequiredException()
                }
            }

            intent = when (it.path) {
                OUTLET -> {
                    it.getQueryParameter(PARAMS_OUTLET_ID)?.let {
                        try {
                            val id = Integer.parseInt(it)
                            val bundle = OutletActivity.getBundle(id)
                            Intent(context, OutletActivity::class.java).apply { putExtras(bundle) }
                        } catch (e: Exception) {
                            Logger.logException(e)
                            null
                        }
                    }
                }
                else -> null
            }
        }
        return Pair(intent, getParentActivityIntent(context, intent?.component?.className ?: ""))
    }


    private fun getParentActivityIntent(context: Context, className: String): Intent? {
        try {
            val act = Class.forName(className)
            return NavUtils.getParentActivityIntent(context, act)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    class InvalidDeepLinkException internal constructor() : Exception()

    class LoginRequiredException internal constructor() : Exception()
}


/*
To Test Deep Link, you can use ADB to send the intent:
adb shell am start -a android.intent.action.VIEW -d "favein://com.pinelabs.fave/outlet?outlet_id=100"
favein://com.pinelabs.fave/outlet?outlet_id=1
*/