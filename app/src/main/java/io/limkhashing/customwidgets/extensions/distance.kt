package io.limkhashing.customwidgets.extensions

import android.location.Location
import io.limkhashing.customwidgets.MainApplication
import io.limkhashing.customwidgets.R
import io.limkhashing.customwidgets.models.Outlet

fun Outlet.distance(lat : Double, lon : Double) :String{
    val result = FloatArray(1)
    Location.distanceBetween(this.latitude , this.longitude , lat , lon,result )
    return if(result[0] < 1000)
        MainApplication.instance.resources.getString(R.string.distance_meter, result[0])
    else
        MainApplication.instance.resources.getString(R.string.distance_kilometer, result[0] / 1000)
}