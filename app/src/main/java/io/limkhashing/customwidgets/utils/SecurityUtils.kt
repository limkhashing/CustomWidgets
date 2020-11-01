// Reference: https://stackoverflow.com/questions/29978849/determine-if-pin-password-or-pattern-used-at-android-phone-log-on
// https://stackoverflow.com/questions/7768879/check-whether-lock-was-enabled-or-not
package io.limkhashing.customwidgets.utils

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.provider.Settings

object SecurityUtils {
    fun isDeviceSecured(context: Context): Boolean {
        val keyguardManager: KeyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyguardManager.isDeviceSecure
        } else {
            isPatternEnabled(context) || isPassOrPinEnabled(keyguardManager)
        }
    }

    private fun isPatternEnabled(context: Context): Boolean {
        return Settings.System.getInt(context.contentResolver, Settings.System.LOCK_PATTERN_ENABLED, 0) == 1
    }

    private fun isPassOrPinEnabled(keyguardManager: KeyguardManager): Boolean {
        return keyguardManager.isKeyguardSecure
    }
}