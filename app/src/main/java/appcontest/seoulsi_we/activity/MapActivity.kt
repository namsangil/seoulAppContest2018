package appcontest.seoulsi_we.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.TextView
import appcontest.seoulsi_we.R
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapActivity : AppCompatActivity() {

    val TAG = "MapActivity"
    var mapView: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val textView: TextView = findViewById(R.id.navigation_toolbar_title)
        textView.text = getString(R.string.realtime_map)

//        val layout = findViewById<RelativeLayout>(R.id.map_view)
//        mapView = MapView(this@MapActivity)
//        layout.addView(mapView)

        val webView = findViewById<WebView>(R.id.map_view)
        webView.settings.javaScriptEnabled = true
        webView.settings.allowUniversalAccessFromFileURLs = true
        webView.webChromeClient = WebChromeClient()

        webView.loadUrl("file:///android_asset/www/webView.html")
//        layout.loadUrl("")

    }

    fun backImageClick(v: View) {
        finish()
    }

    fun moveAroundLocation(v: View) {
        Log.d(TAG, "moveAroundLocation")
        mapView?.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);
    }

    //현재 내 위치를 GeoPoint로 리턴한다.


}



