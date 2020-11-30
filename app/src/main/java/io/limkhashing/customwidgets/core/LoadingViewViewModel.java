package io.limkhashing.customwidgets.core;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

public interface LoadingViewViewModel {
    ObservableField<String> getLoadingDescription();

    ObservableBoolean getLoadingVisibility();
}
