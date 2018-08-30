package appcontest.seoulsi_we.activity

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.TextView
import android.widget.Toast
import appcontest.seoulsi_we.R


class MapActivity : AppCompatActivity() {

    val TAG = "MapActivity"

    val handler = Handler()
    var webView: WebView? = null
    var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val textView: TextView = findViewById(R.id.navigation_toolbar_title)
        textView.text = getString(R.string.realtime_map)


//        mapView?.settings?.javaScriptEnabled = true
//        mapView?.loadUrl("http://ec2-52-78-3-222.ap-northeast-2.compute.amazonaws.com")

        webView = findViewById<WebView>(R.id.webview)
        val settings = webView?.settings
        settings?.javaScriptEnabled = true
        settings?.loadWithOverviewMode = true
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
        webView?.webViewClient = object:WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                webView?.invalidate()
            }
        }
        webView?.loadUrl("http://ec2-52-78-3-222.ap-northeast-2.compute.amazonaws.com")
//        webView?.loadUrl("http://10.0.2.2")

        webView?.addJavascriptInterface(AndroidBridge(handler, this), "android")

//        webView?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                val width = webView?.width?.toString()
//                val height = webView?.height?.toString()
//                webView?.loadUrl("javascript:changeMapViewSize(" + width!! + "," + height!! + ")")
//                webView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
//            }
//        })
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

    fun addTraffic(v: View) {
        webView?.clearCache(true)
        trafficEnabled = !trafficEnabled
        if (trafficEnabled) {
            webView?.loadUrl("javascript:addTrafficInfo()")
        } else {
            webView?.loadUrl("javascript:removeTrafficInfo()")
        }
//
//        setPosition(String.format("%f",37.551568), String.format("%f",126.972787))
    }

    fun setPosition(lat: String, lon: String) {
//        webView?.loadUrl("javascript:panTo("+lat+","+lon+")")
    }

    fun backImageClick(v: View) {
        finish()
    }

    fun moveAroundLocation(v: View) {
        Log.d(TAG, "moveAroundLocation")
        try {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1f, locationListener)
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1f, locationListener)
        } catch (e: SecurityException) {

        }
//        mapView?.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true)
    }

    private val locationListener = object : LocationListener {
        var responseCount = 0
        override fun onLocationChanged(location: Location?) {
            if (3 < responseCount++) {
                locationManager?.removeUpdates(this)
            }
            webView?.loadUrl("javascript:panTo(" + location?.latitude + "," + location?.longitude + ")")
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }
    }


    //현재 내 위치를 GeoPoint로 리턴한다.


}



