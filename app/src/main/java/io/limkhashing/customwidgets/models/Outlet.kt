package io.limkhashing.customwidgets.models

import com.google.gson.annotations.SerializedName

data class Outlet(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("telephone")
    val telephone: String,
    @SerializedName("vpa")
    val vpa: String,
//    @SerializedName("outlet_timing")
//    val businessHour: BusinessHour,
//    @SerializedName("company")
//    val company: Company,
    @SerializedName("gallery_images")
    val galleryImages: List<String>,
//    @SerializedName("announcements")
//    val announcements: List<Announcement>,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("favorited")
    val favorited: Boolean,
//    @SerializedName("share_url_details")
//    val share: Share
)