package io.limkhashing.customwidgets.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import io.limkhashing.customwidgets.deeplink.DeepLinkActivity
import io.limkhashing.customwidgets.R


interface BaseViewModelEventObserver : ActionViewModel {

    fun getLifecycleOwner(): LifecycleOwner

    fun getActionContext(): Context

    fun getActionActivity() : FragmentActivity

    fun initViewModel(viewModel: BaseViewModel) {
        observeEvent(viewModel)
    }

    private fun observeEvent(viewModel: BaseViewModel) {
        viewModel.baseActionEvent.observe(getLifecycleOwner(), { it ->
            it?.let {
                when (it) {
                    is ShowDialogEvent -> showDialog(it.dialogBuilder)
                    is DismissLoaderEvent -> dismissLoader()
                    is ShowDialogFragmentEvent -> showDialogFragment(it.baseDialogFragment, it.fragmentTag)
                    is AddFragmentEvent -> addFragment(it.baseFragment, it.fragmentTag)
                    is DismissFragmentEvent -> dismissFragment(it.fragmentTag)
                    is ShowToastEvent -> showToast(it.message)
                    is StartActivityEvent<*> -> startActivity(it.clazz, it.data)
                    is StartActivityFlagEvent<*> -> startActivityFlag(it.clazz, it.data, it.flag)
                    is FinishEvent -> finishView()
                }
            }
        })
    }

    override fun <T> startActivity(clazz: Class<T>) {
        startActivity(clazz, null)
    }

    override fun <T> startActivity(clazz: Class<T>, data: Bundle?) {
        val intent = Intent(getActionContext(), clazz)
        if (data != null)
            intent.putExtras(data)
        getActionContext().startActivity(intent)
    }

    override fun <T> startActivityFlag(clazz: Class<T>, data: Bundle?, flag: Int) {
        val intent = Intent(getActionContext(), clazz)
        intent.flags = flag
        if (data != null)
            intent.putExtras(data)
        getActionContext().startActivity(intent)
    }

    override fun showToast(msg: String) {
        Toast.makeText(getActionContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun showDialogFragment(baseDialogFragment: BaseDialogFragment, fragmentTag: String) {
        baseDialogFragment.show(getActionActivity().supportFragmentManager, fragmentTag)
    }

    override fun addFragment(baseDialogFragment: BaseFragment, fragmentTag: String) {
        getActionActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_bottom_in_slow, R.anim.slide_bottom_out_slow, R.anim.slide_bottom_in_slow, R.anim.slide_bottom_out_slow)
            .add(android.R.id.content, baseDialogFragment, fragmentTag)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    override fun dismissFragment(fragmentTag: String) {
        val fragmentManager = getActionActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_bottom_in_slow, R.anim.slide_bottom_out_slow, R.anim.slide_bottom_in_slow, R.anim.slide_bottom_out_slow)
        getActionActivity().supportFragmentManager.findFragmentByTag(fragmentTag)?.let { fragmentManager.remove(it) }
        fragmentManager.commitAllowingStateLoss()
    }

    override fun startDeepLinkActivity(uri: String) {
        val intent = Intent(getActionContext() , DeepLinkActivity::class.java)
        val bundle = DeepLinkActivity.getBundle()
        intent.putExtras(bundle)
        try {
            intent.data = Uri.parse(uri)
            getActionContext().startActivity(intent)
        } catch (e: Exception) {
        }
    }
}