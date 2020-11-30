package io.limkhashing.customwidgets.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date?.formatYear(): String {
    if (this == null) return ""
    return SimpleDateFormat("yyyy", Locale.getDefault()).format(this)
}

fun Date?.format(): String {
    if (this == null) return ""
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this)
}

fun Date?.formatPretty(): String {
    if (this == null) return ""
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(this)
}

fun String?.toDate(): Date? {
    if (this == null) return null
    return try {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(this)
    } catch (e: Throwable) {
        null
    }
}

fun Date?.formatRFC3339(): String {
    if (this == null) return ""
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ZZZZZ", Locale.getDefault()).format(this)
}
