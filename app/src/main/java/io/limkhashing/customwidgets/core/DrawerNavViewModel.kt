package io.limkhashing.customwidgets.core

import androidx.databinding.ObservableField

interface DrawerNavViewModel {
    fun setDrawerProfilePicture(url: String?)
    fun getDrawerProfilePicture(): ObservableField<String>
    fun setDrawerProfileName(s: String)
    fun getDrawerProfileName(): ObservableField<String>
    fun setDrawerProfileSubtitle(s: String)
    fun getDrawerProfileSubtitle(): ObservableField<String>
}
