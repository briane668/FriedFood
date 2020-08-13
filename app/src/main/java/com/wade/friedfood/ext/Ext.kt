package app.appworks.school.stylish.ext

import android.graphics.Rect
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.DisplayMetrics
import android.view.TouchDelegate
import android.view.View
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.GeoPoint
import com.wade.friedfood.data.ParcelableShop
import com.wade.friedfood.data.Shop
import java.util.*



fun View.setTouchDelegate() {
    val parent = this.parent as View  // button: the view you want to enlarge hit area
    parent.post {
        val rect = Rect()
        this.getHitRect(rect)
        rect.top -= 100    // increase top hit area
        rect.left -= 100   // increase left hit area
        rect.bottom += 100 // increase bottom hit area
        rect.right += 100  // increase right hit area
        parent.touchDelegate = TouchDelegate(rect, this)
    }
}


fun Long.toDisplayFormat(): String {
    return SimpleDateFormat("yyyy.MM.dd hh:mm", Locale.TAIWAN).format(this)
}


//fun Number.toDp(): Float {
//    return this.toFloat() / (StylishApplication.instance.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
//}


//fun Shop.toParcelableShop(): ParcelableShop {
//
//    return Shop()
//}

fun ParcelableShop.toShop(): Shop {

    var geoPoint : GeoPoint? = GeoPoint(this.latitude!!, this.longitude!!)

    return Shop(
        id = this.id,
        name = this.name,
        location = geoPoint  ,
        latitude = 0,
        longitude = 0,
        image = this.image,
        recommend = this.recommend,
        star = this.star,
        address = this.address,
        menuImage = this.menuImage,
        otherImage = this.otherImage,
        comment = this.comment,
        menu = this.menu,
        phone= this.phone
    )
}

fun Shop.toParcelableShop(): ParcelableShop {



    return ParcelableShop(
        id = this.id,
        name = this.name,
        latitude = this.location?.latitude,
        longitude = this.location?.longitude,
        image = this.image,
        recommend = this.recommend,
        star = this.star,
        address = this.address,
        menuImage = this.menuImage,
        otherImage = this.otherImage,
        comment = this.comment,
        menu = this.menu,
        phone= this.phone
    )
}

