package appcontest.seoulsi_we.customView

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView

/**
 * Created by nam on 2018. 9. 15..
 */

open class CustomWebView : WebView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        requestDisallowInterceptTouchEvent(true)
        return super.onTouchEvent(event)
    }
}
