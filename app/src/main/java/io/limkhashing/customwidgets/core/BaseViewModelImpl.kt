package io.limkhashing.customwidgets.core

import android.app.Application
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import io.limkhashing.customwidgets.BuildConfig
import io.limkhashing.customwidgets.R
import io.limkhashing.customwidgets.core.*
import io.limkhashing.customwidgets.extensions.DialogBuilder
import io.limkhashing.customwidgets.extensions.handleAPIError
import io.limkhashing.customwidgets.utils.DrawableUtils
import io.limkhashing.customwidgets.utils.SingleOnClickListener
import io.limkhashing.customwidgets.utils.ViewState

open class BaseViewModelImpl(application: Application) : AndroidViewModel(application),
    BaseViewModel {
    val resource: Resources = application.resources

    private val mErrorVisibility = ObservableBoolean()
    private val mLoadingVisibility = ObservableBoolean()
    private val mContentVisibility = ObservableBoolean()
    private val mEmptyViewVisibility = ObservableBoolean()

    // error
    private val mErrorIcon = ObservableField<Drawable>()
    private val mErrorTitle = ObservableField<String>()
    private val mErrorDescription = ObservableField<String>()
    private val mErrorRetryButtonText = ObservableField<String>()

    // empty view
    private val mEmptyViewIconVisibility = ObservableBoolean()
    private val mEmptyViewIcon = ObservableInt()
    private val mEmptyViewTitle = ObservableField<String>()
    private val mEmptyViewDescription = ObservableField<String>()
    private val mEmptyViewButtonText = ObservableField<String>()

    // loading
    private val mLoadingDescription = ObservableField<String>()

    // toolbar
    private val mNavigationIcon = ObservableInt(R.drawable.ic_arrow_back)
    private val mToolbarTitleIcon = ObservableInt()
    private val mTitleColor = ObservableInt(resource.getColor(R.color.black))
    private val mToolbarBackground = ObservableField(DrawableUtils.createColorDrawable(R.color.white))
    private val mToolbarExpandedTitle = ObservableField<String>()
    private val mToolbarExpandedSubtitle = ObservableField<String>()
    private val mToolbarTitle = ObservableField<String>()
    private val mToolbarTitleEnabled = ObservableBoolean()
    private val mToolbarSubtitle = ObservableField<String>()
    private val mToolbarSubtitleEnabled = ObservableBoolean(false)
    private val mToolbarExpandedTitleVisibility = ObservableBoolean(false)
    private val mToolbarExpandedSubtitleVisibility = ObservableBoolean(false)
    private val mToolbarTitleIconVisibility = ObservableBoolean(false)

    // drawer navigation
    private val mDrawerProfileName = ObservableField<String>()
    private val mDrawerProfileSubtitle = ObservableField<String>()
    private val mDrawerProfilePicture = ObservableField<String>()

    //region EVENT LISTENER
    override val baseActionEvent = MutableLiveData<BaseEvent>()

    override fun showDialog(dialogBuilder: DialogBuilder) {
        postEvent(
            ShowDialogEvent(dialogBuilder)
        )
    }

    override fun showDialogFragment(baseDialogFragment: BaseDialogFragment, fragmentTag: String) {
        postEvent(ShowDialogFragmentEvent(baseDialogFragment, fragmentTag))
    }

    override fun addFragment(baseDialogFragment: BaseFragment, fragmentTag: String) {
        postEvent(AddFragmentEvent(baseDialogFragment, fragmentTag))
    }

    override fun dismissFragment(fragmentTag : String) {
        postEvent(DismissFragmentEvent(fragmentTag))
    }

    override fun dismissLoader() {
        mLoadingVisibility.set(false)
        postEvent(DismissLoaderEvent())
    }

    override fun showToast(msg: String) {
        postEvent(ShowToastEvent(msg))
    }

    override fun finishView() {
        postEvent(FinishEvent())
    }

    override fun handleError(error: Throwable, title: String?) {
        error.handleAPIError {
            showDialog(
                DialogBuilder(
                    ViewState.ERROR,
                    title ?: "Oops, something wrong happened",
                    it,
                    showNegativeButton = true
                )
            )
        }
    }

    override fun <T> startActivity(clazz: Class<T>) {
        startActivity(clazz, null)
    }

    override fun <T> startActivity(clazz: Class<T>, data: Bundle?) {
        postEvent(StartActivityEvent(clazz, data))
    }

    override fun startDeepLinkActivity(uri: String) {
        postEvent(StartDeepLinkActivityEvent(uri))
    }

    fun postEvent(event: BaseEvent) {
        // LiveData will take last value only if postValue in a short time frame,
        // so need set value using main thread
        Handler(Looper.getMainLooper()).post {
            baseActionEvent.value = event
        }
    }

    override fun <T> startActivityFlag(clazz: Class<T>, data: Bundle?, flag: Int) {
        baseActionEvent.postValue(StartActivityFlagEvent(clazz, data, flag))
    }

    //regionend
    override fun onError() {
        mErrorVisibility.set(true)
        mLoadingVisibility.set(false)
        mContentVisibility.set(false)
        mEmptyViewVisibility.set(false)
    }

    override fun onLoading() {
        showDialog(DialogBuilder(ViewState.LOADING))
        mLoadingVisibility.set(true)
        mErrorVisibility.set(false)
        mContentVisibility.set(false)
        mEmptyViewVisibility.set(false)
    }

    override fun onContent() {
        dismissLoader()
        mContentVisibility.set(true)
        mLoadingVisibility.set(false)
        mErrorVisibility.set(false)
        mEmptyViewVisibility.set(false)
    }

    override fun onEmpty() {
        dismissLoader()
        mEmptyViewVisibility.set(true)
        mContentVisibility.set(false)
        mLoadingVisibility.set(false)
        mErrorVisibility.set(false)
    }

    override fun getCustomToolbarLayout(): Int {
        return R.layout.toolbar_base_flat
    }

    override fun getNavigationViewLayout(): Int {
        return 0
    }

    override fun setToolbarTitle(p0: String?) {
        mToolbarTitle.set(p0)
    }

    override fun setToolbarSubtitle(s: String) {
        mToolbarSubtitle.set(s)
        mToolbarSubtitleEnabled.set(!TextUtils.isEmpty(s))
    }

    override fun getOnOffsetChangedListener(): OnOffsetChangedListener {
        return OnOffsetChangedListener { appBarLayout: AppBarLayout?, verticalOffset: Int -> }
    }

    override fun getTitleIcon(): ObservableInt {
        return mToolbarTitleIcon
    }

    override fun getExpandedTitle(): ObservableField<String> {
        return mToolbarExpandedTitle
    }

    override fun getExpandedSubtitle(): ObservableField<String> {
        return mToolbarExpandedSubtitle
    }

    override fun setTitleIcon(resource: Int) {
        if (resource != 0)
            mToolbarTitleIcon.set(resource)
        mToolbarTitleIconVisibility.set(true)
    }

    override fun setExpandedTitle(s: String) {
        mToolbarExpandedTitle.set(s)
        mToolbarExpandedTitleVisibility.set(!TextUtils.isEmpty(s))
    }

    override fun setExpandedSubtitle(s: String) {
        mToolbarExpandedSubtitle.set(s)
        mToolbarExpandedSubtitleVisibility.set(!TextUtils.isEmpty(s))
    }

    override fun getExpandedTitleVisibility(): ObservableBoolean {
        return mToolbarExpandedTitleVisibility
    }

    override fun getExpandedSubtitleVisibility(): ObservableBoolean {
        return mToolbarExpandedSubtitleVisibility
    }

    override fun getTitleIconVisibility(): ObservableBoolean {
        return mToolbarTitleIconVisibility
    }

    override fun getToolbarBackground(): ObservableField<Drawable> {
        return mToolbarBackground
    }

    override fun getTitleColor(): ObservableInt {
        return mTitleColor
    }

    override fun setTitleColor(@ColorRes resource: Int) {
        mTitleColor.set(this.resource.getColor(resource))
    }

    override fun setToolbarBackground(drawable: Drawable) {
        mToolbarBackground.set(drawable)
    }

    /**
     * Override here for usage
     */
    override fun onOptionsItemSelected(p0: MenuItem) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    override fun getToolbarTitle(): ObservableField<String> {
        return mToolbarTitle
    }

    override fun getToolbarSubtitle(): ObservableField<String> {
        return mToolbarSubtitle
    }

    override fun getNavigationIcon(): ObservableInt {
        return mNavigationIcon
    }

    override fun setNavigationIcon(p0: Int) {
        mNavigationIcon.set(p0)
    }

    override fun getToolbarTitleEnabled(): ObservableBoolean {
        return mToolbarTitleEnabled
    }

    override fun getToolbarSubtitleVisibility(): ObservableBoolean {
        return mToolbarSubtitleEnabled
    }

    /**
     * Override here for usage loading view
     */
    override fun getLoadingDescription(): ObservableField<String> {
        return mLoadingDescription
    }

    override fun getLoadingVisibility(): ObservableBoolean {
        return mLoadingVisibility
    }

    /**
     * Override here for content view
     */
    override fun getContentVisibility(): ObservableBoolean {
        return mContentVisibility
    }

//    /**
//     * Override here for usage error view
//     */
//    override fun onRetryClicked(p0: View?) {
//
//    }
//
//    override fun getErrorVisibility(): ObservableBoolean {
//        return mErrorVisibility
//    }
//
//    override fun getErrorTitle(): ObservableField<String> {
//        return mErrorTitle
//    }
//
//    override fun getRetryButtonText(): ObservableField<String> {
//        return mErrorRetryButtonText
//    }
//
//    override fun getErrorDescription(): ObservableField<String> {
//        return mErrorDescription
//    }
//
//    override fun getErrorIcon(): ObservableField<Drawable> {
//        return mErrorIcon
//    }

    /**
     * Override here for usage empty view
     */
    override fun getEmptyViewIcon(): ObservableInt {
        return mEmptyViewIcon
    }

    override fun getEmptyViewTitle(): ObservableField<String> {
        return mEmptyViewTitle
    }

    override fun getEmptyViewDescription(): ObservableField<String> {
        return mEmptyViewDescription
    }

    override fun getEmptyViewButtonText(): ObservableField<String> {
        return mEmptyViewButtonText
    }

    override fun getEmptyViewVisibility(): ObservableBoolean {
        return mEmptyViewVisibility
    }

    override fun getEmptyViewIconVisibility(): ObservableBoolean {
        return mEmptyViewIconVisibility
    }

    override fun setEmptyViewIconVisibility(visibility: Boolean) {
        mEmptyViewIconVisibility.set(visibility)
    }

    override fun setEmptyViewIcon(drawableResource: Int) {
        setEmptyViewIconVisibility(true)
        mEmptyViewIcon.set(drawableResource)
    }

    override fun setEmptyViewTitle(title: String) {
        mEmptyViewTitle.set(title)
    }

    override fun setEmptyViewDescription(desc: String) {
        mEmptyViewDescription.set(desc)
    }

    override fun setEmptyViewButtonText(buttonText: String) {
        mEmptyViewButtonText.set(buttonText)
    }

    override fun onEmptyViewButtonClicked(var1: View) {
    }

    override fun appVersion(): String {
        return "Fave v${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})"
    }

    override fun setDrawerProfilePicture(url: String?) {
        mDrawerProfilePicture.set(url)
    }

    override fun getDrawerProfilePicture(): ObservableField<String> {
        return mDrawerProfilePicture
    }

    override fun setDrawerProfileName(s: String) {
        mDrawerProfileName.set(s)
    }

    override fun getDrawerProfileName(): ObservableField<String> {
        return mDrawerProfileName
    }

    override fun setDrawerProfileSubtitle(s: String) {
        mDrawerProfileSubtitle.set(s)
    }

    override fun getDrawerProfileSubtitle(): ObservableField<String> {
        return mDrawerProfileSubtitle
    }

    override val onDrawerProfileClicked = SingleOnClickListener {

    }
}
