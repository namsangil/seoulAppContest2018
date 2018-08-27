package appcontest.seoulsi_we.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import appcontest.seoulsi_we.R


class MapActivity : AppCompatActivity() {

    val TAG = "MapActivity"

    val handler = Handler()
    var webView : WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val textView: TextView = findViewById(R.id.navigation_toolbar_title)
        textView.text = getString(R.string.realtime_map)


//        mapView?.settings?.javaScriptEnabled = true
//        mapView?.loadUrl("http://ec2-52-78-3-222.ap-northeast-2.compute.amazonaws.com")

        webView = findViewById<WebView>(R.id.webview)
        val settings = webView?.settings
        settings?.javaScriptEnabled = true
//        settings?.loadWithOverviewMode = true
        settings?.useWideViewPort = true
        settings?.setSupportZoom(true)
        settings?.builtInZoomControls = false
        settings?.layoutAlgorithm = (WebSettings.LayoutAlgorithm.SINGLE_COLUMN)
        settings?.cacheMode = (WebSettings.LOAD_NO_CACHE)
        settings?.domStorageEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView?.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else {
            webView?.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }

        webView?.webChromeClient = WebChromeClient()
        webView?.loadUrl("http://ec2-52-78-3-222.ap-northeast-2.compute.amazonaws.com")

        webView?.addJavascriptInterface(AndroidBridge(handler, this), "android")


    }

    //    @android.webkit.JavascriptInterface
    private class AndroidBridge constructor(handler: Handler, context: Context) {
        val handler = handler
        val context = context


        @JavascriptInterface
        fun onSelectItem(arg: String) { // must be final
            handler.post({
                Log.d("android", "onSelectItem(" + arg + ")")
                Toast.makeText(context, arg, Toast.LENGTH_SHORT).show()
            })
        }
    }

    private var trafficEnabled = false

    fun addTraffic(v : View){
        webView?.clearCache(true)
        trafficEnabled = !trafficEnabled
        if(trafficEnabled){
            webView?.loadUrl("javascript:addTrafficInfo()")
        }
        else{
            webView?.loadUrl("javascript:removeTrafficInfo()")
        }
//
//        setPosition(String.format("%f",37.551568), String.format("%f",126.972787))
    }

    fun setPosition(lat : String, lon : String){
//        webView?.loadUrl("javascript:panTo("+lat+","+lon+")")
    }

    fun backImageClick(v: View) {
        finish()
    }

    fun moveAroundLocation(v: View) {
        Log.d(TAG, "moveAroundLocation")
//        mapView?.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true)
    }

    //현재 내 위치를 GeoPoint로 리턴한다.


}



