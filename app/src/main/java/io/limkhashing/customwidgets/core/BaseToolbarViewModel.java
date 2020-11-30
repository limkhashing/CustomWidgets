package io.limkhashing.customwidgets.core;

import android.view.MenuItem;

import androidx.annotation.DrawableRes;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.google.android.material.appbar.AppBarLayout;

public interface BaseToolbarViewModel {
    AppBarLayout.OnOffsetChangedListener getOnOffsetChangedListener();

    int getCustomToolbarLayout();

    ObservableField<String> getToolbarTitle();

    ObservableInt getNavigationIcon();

    ObservableBoolean getToolbarTitleEnabled();

    void onOptionsItemSelected(MenuItem var1);

    void setToolbarTitle(String var1);

    void setNavigationIcon(@DrawableRes int var1);
}
