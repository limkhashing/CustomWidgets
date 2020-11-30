package io.limkhashing.customwidgets.core

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import io.limkhashing.customwidgets.R
import io.limkhashing.customwidgets.databinding.LayoutBaseBinding
import io.limkhashing.customwidgets.extensions.DialogBuilder
import io.limkhashing.customwidgets.extensions.createLoader


abstract class BaseActivity : AppCompatActivity(), BaseViewModelEventObserver {
    private var alertDialog: AlertDialog? = null
    private var mViewModel: BaseViewModel? = null

    abstract val layoutResource: Int
    abstract fun bindViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViewModel()
        initToolbar(findViewById(R.id.toolbar))
        setupView()
    }

    protected open fun <T : ViewDataBinding?> initBinding(viewModel: BaseViewModel): T {
        val rootView = layoutInflater.inflate(R.layout.layout_base, window.decorView as ViewGroup, false) as DrawerLayout
        val baseBinding = DataBindingUtil.bind<LayoutBaseBinding>(rootView)
        if (viewModel.customToolbarLayout != 0) {
            val tb = DataBindingUtil.inflate<ViewDataBinding>(
                layoutInflater,
                viewModel.customToolbarLayout,
                rootView.findViewById(R.id.app_bar),
                true
            )
            tb.setVariable(BR.viewModel, viewModel)
        }
        // To set drawer navigation binding
//        if (viewModel.getNavigationViewLayout() != 0) {
//            val drawerNavBinding = DataBindingUtil.inflate<DrawerNavigationBinding>(
//                layoutInflater,
//                viewModel.getNavigationViewLayout(),
//                rootView,
//                true
//            )
//            val viewHeader = drawerNavBinding.navView.getHeaderView(0)
//            val drawerNavHeaderBinding = DataBindingUtil.bind<DrawerHeaderBinding>(viewHeader)
//            drawerNavHeaderBinding?.setVariable(BR.viewModel, viewModel)
//            drawerNavBinding.setVariable(BR.viewModel, viewModel)
//        }
        val binding: T = DataBindingUtil.inflate<T>(layoutInflater, layoutResource, rootView.findViewById(R.id.viewStub), true)
        binding!!.setVariable(BR.viewModel, viewModel)
        setContentView(rootView)
        baseBinding!!.viewModel = viewModel
        mViewModel = viewModel
        initViewModel(viewModel)
        return binding
    }

    open fun setupView() {

    }

    fun initToolbar(toolbar: Toolbar?) {
        toolbar ?: return
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.title = ""
    }

    override fun getActionContext(): Context = this

    override fun getActionActivity(): FragmentActivity = this

    override fun getLifecycleOwner(): LifecycleOwner = this

    override fun showDialog(dialogBuilder: DialogBuilder) {
        alertDialog = createLoader(dialogBuilder)
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
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        mViewModel!!.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    fun addFloatingView(floatingView: View?) {
        val viewStub: View = findViewById(R.id.viewStubBottom)
        val parent = viewStub.parent as ViewGroup
        val index = parent.indexOfChild(viewStub)
        parent.addView(floatingView, index)
    }

    fun showOrHideKeyboard(state: Int) {
        window.setSoftInputMode(state)
    }
}
