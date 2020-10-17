package io.limkhashing.customwidgets.models

data class Savings(
    val id: Int,
    val outletName: String,
    val expiryDesc: String,
    val amount: String,
    val isExpireSoon: Boolean
)