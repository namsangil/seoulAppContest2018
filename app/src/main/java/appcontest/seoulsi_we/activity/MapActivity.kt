package appcontest.seoulsi_we.activity

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.ImageView
import android.widget.TextView
import appcontest.seoulsi_we.R
import appcontest.seoulsi_we.model.FeedData
import org.json.JSONArray
import org.json.JSONObject


class MapActivity : BaseActivity() {

    val TAG = "MapActivity"

    val handler = Handler()
    var webView: WebView? = null
    var locationManager: LocationManager? = null
    val url = "http://ec2-52-78-3-222.ap-northeast-2.compute.amazonaws.com"
    //    val url = "http://10.0.2.2"

    var itemView: ItemView? = null

    private var feedDatas : Array<FeedData> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val textView: TextView = findViewById(R.id.navigation_toolbar_title)
        textView.text = getString(R.string.realtime_map)


        webView = findViewById<WebView>(R.id.webview)
        initWebView(webView)

        itemView = ItemView(this@MapActivity, findViewById(R.id.map_feed_item))
        itemView?.view?.visibility = View.GONE

    }

    inner class ItemView constructor(context: Context, view: View) {
        private val context = context
        private val container: View? = view
        private var imageView: ImageView? = null
        private var title: TextView? = null
        private var date: TextView? = null
        private var likeCount: TextView? = null
        private var commentCount: TextView? = null
        private var feedID: Int? = null

        var view: View? = container
            get() = container

        init {
            imageView = container?.findViewById(R.id.map_feed_item_image)
            title = container?.findViewById(R.id.map_feed_item_title)
            date = container?.findViewById(R.id.map_tv_feed_item_time)
            likeCount = container?.findViewById(R.id.map_feed_tv_like_count)
            commentCount = container?.findViewById(R.id.map_feed_tv_comment_count)
        }

        fun setData(data: FeedData?) {
//            Picasso.with(context).load(data?.certImageUrl).into(imageView)
            title?.text = data?.title

            // TODO 컨버팅 해야함.
            date?.text = data?.date?.toString()
            likeCount?.text = data?.likeCount.toString()
            // TODO 좋아요 여부도 표시해야 함. mData?.isLike 를 가지고...
            commentCount?.text = data?.commentCount.toString()
            container?.visibility = View.VISIBLE
        }
    }

    private fun initWebView(webView: WebView?) {
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
        webView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                webView?.invalidate()
            }
        }
        webView?.loadUrl(url)

        webView?.addJavascriptInterface(AndroidBridge(handler, this), "android")

    }

    override fun onResume() {
        moveAroundLocation(null)
        setMarker()
        super.onResume()
    }

    private inner class AndroidBridge constructor(handler: Handler, context: Context) {
        val handler = handler
        val context = context


        @JavascriptInterface
        fun onSelectItem(arg: String) { // must be final
            handler.post({
                Log.d("android", "onSelectItem(" + arg + ")")
                try {
                    val id = Integer.parseInt(arg)

                    val data: FeedData? = findFeed(id)

                    if (null == data) {
                        itemView?.view?.visibility = View.GONE
                        return@post
                    }

                    handler.post({
                        itemView?.setData(data)
                    })


                } catch (e: NumberFormatException) {
                    return@post
                }
            })
        }
    }


//    var positions = [
//        {
//            id : 1,
//            lat : 37.551568,
//            lon : 126.972787
//        },
//        {
//            id : 2,
//            lat : 37.552568,
//            lon : 126.973787
//        },
//        {
//            id : 3,
//            lat : 37.550568,
//            lon : 126.971787
//        },
//        {
//            id : 4,
//            lat : 33.441357,
//            lon : 126.631591
//        }
//    ];

    private fun setMarker() {
        val arr = JSONArray()



        for (data in feedDatas) {
            val obj = JSONObject()
            obj.put("id", data.feedId)
            obj.put("lat", data.address!![0].lat)
            obj.put("lon", data.address!![0].lon)

            arr.put(obj)
        }

        Thread({
            try {
                Thread.sleep(2000)
                runOnUiThread {
                    webView?.loadUrl("javascript:setMarker(" + arr.toString() + ")")
                }
            } catch (e: InterruptedException) {

            }

        }).start()
    }


    private fun findFeed(id: Int): FeedData? {

        for (feedData in feedDatas) {
            if (feedData.feedId == id) {
                return feedData
            }
        }

        return null
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
    }

    fun setPosition(lat: Double?, lon: Double?) {
        webView?.loadUrl("javascript:panTo(" + lat + "," + lon + ")")
    }


    fun moveAroundLocation(v: View?) {
        Log.d(TAG, "moveAroundLocation")
        try {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1f, locationListener)
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1f, locationListener)
        } catch (e: SecurityException) {

        }
    }

    fun changeMapScale(v: View) {
        when (v.id) {
            R.id.map_scale_plus -> {
                // + 버튼을 누르는 경우 지도 확대를 한다.
                webView?.loadUrl("javascript:zoomIn()")

            }
            R.id.map_scale_minus -> {
                // - 버튼을 누르는 경우 지도 축소를 한다.
                webView?.loadUrl("javascript:zoomOut()")
            }
        }
    }

    private val locationListener = object : LocationListener {
        var responseCount = 0
        override fun onLocationChanged(location: Location?) {
            //현재 내 위치를 GeoPoint로 리턴한다.
            locationManager?.removeUpdates(this)
            setPosition(location?.latitude, location?.longitude)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }
    }

}



