package app.appworks.school.stylish.ext

import android.graphics.Rect
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.DisplayMetrics
import android.view.TouchDelegate
import android.view.View
import androidx.annotation.RequiresApi
import java.util.*


/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Provides [List] [Product] to convert to [List] [OrderProduct] format
 */


/**
 * Increase touch area of the view/button .
 */
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