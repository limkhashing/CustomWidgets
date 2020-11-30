package io.limkhashing.customwidgets.deeplink

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.TaskStackBuilder
import androidx.core.os.bundleOf

// Hilt Injection
@AndroidEntryPoint
class DeepLinkActivity : AppCompatActivity(){

    @Inject
    lateinit var userInteractor: UserInteractor

    //From Notification or DeepLink
    private var createNewTask = true

    companion object {
        private const val EXTRA_CREATE_NEW_TASK = "EXTRA_CREATE_NEW_TASK"

        fun getBundle() : Bundle{
            return bundleOf(EXTRA_CREATE_NEW_TASK to false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deeplinkHandler = DeepLinkHandler()
        val uri = intent.data
        createNewTask = intent.getBooleanExtra(EXTRA_CREATE_NEW_TASK, true)
        try {
            val targetedActivity = deeplinkHandler.getIntentForDeepLink(this,
                                                                        uri,
                                                                        userInteractor,
                                                                        createNewTask)


            startTargetActivity(targetedActivity.first, targetedActivity.second)
        } catch (e: DeepLinkHandler.LoginRequiredException) {
            startActivity(Intent(this, SplashActivity::class.java))
        }
        finish()
    }


    private fun startTargetActivity(intent: Intent?, parentActivityIntent: Intent?) {
        if (intent != null) {
            if (createNewTask && parentActivityIntent != null) {
                TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(intent)
                    .startActivities()
            } else
                startActivity(intent)
        } else {
            // Do nothing if it is call from activity, means it is not valid and
            // to prevent recreate task stack
            if (createNewTask)
                startActivity(Intent(this, SplashActivity::class.java))
        }
    }

}