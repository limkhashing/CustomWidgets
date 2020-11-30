package io.limkhashing.customwidgets.core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import io.limkhashing.customwidgets.extensions.DialogBuilder
import io.limkhashing.customwidgets.extensions.createLoader


abstract class BaseFragment : DialogFragment(), BaseViewModelEventObserver {
    private var mContentView: View? = null

    protected abstract val layoutResource : Int

    private var alertDialog: AlertDialog? = null

    abstract fun bindViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContentView = inflater.inflate(layoutResource, container, false)
        return mContentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        setupView(savedInstanceState)
    }

    open fun setupView(savedInstanceState: Bundle?) {

    }

    protected open fun <T : ViewDataBinding?> initBinding(viewModel: BaseViewModel): T {
        val binding = DataBindingUtil.bind<T>(mContentView!!)
        binding!!.setVariable(BR.viewModel, viewModel)
        initViewModel(viewModel)
        return binding
    }

    override fun getActionContext(): Context = requireContext()

    override fun getLifecycleOwner(): LifecycleOwner = this

    override fun getActionActivity(): FragmentActivity = requireActivity()

    override fun showDialog(dialogBuilder: DialogBuilder) {
        alertDialog = requireContext().createLoader(dialogBuilder)
        alertDialog?.let {
            if (!it.isShowing) {
                it.show()
            }
        }
    }

    override fun dismissLoader() {
        alertDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    override fun finishView() {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }
}