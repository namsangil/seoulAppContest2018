package appcontest.seoulsi_we.activity

import android.content.Context
import android.content.Intent
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
import android.widget.Toast
import appcontest.seoulsi_we.R
import appcontest.seoulsi_we.model.FeedData
import appcontest.seoulsi_we.service.HttpUtil
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MapActivity : BaseActivity() {

    val TAG = "MapActivity"

    val handler = Handler()
    var webView: WebView? = null
    var locationManager: LocationManager? = null
    val mapServerUrl = "http://ec2-52-78-3-222.ap-northeast-2.compute.amazonaws.com"
    //    val mapServerUrl = "http://10.0.2.2"

    var itemView: ItemView? = null

    private var feedList: ArrayList<FeedData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val textView: TextView = findViewById(R.id.navigation_toolbar_title)
        textView.text = getString(R.string.demo_location_map)


        webView = findViewById<WebView>(R.id.webview)
        initWebView(webView)
        Toast.makeText(this@MapActivity, "빨강(정체), 노랑(서행), 초록(원활)로\n실시간 교통정보가 표시됩니다", Toast.LENGTH_LONG).show()

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
        private var feedID: Long? = null

        var view: View? = container
            get() = container

        init {
            imageView = container?.findViewById(R.id.map_feed_item_image)
            title = container?.findViewById(R.id.map_feed_item_title)
            date = container?.findViewById(R.id.map_tv_feed_item_time)
            likeCount = container?.findViewById(R.id.map_feed_tv_like_count)
            commentCount = container?.findViewById(R.id.map_feed_tv_comment_count)

            view.setOnClickListener(View.OnClickListener {
                val intent = Intent(this@MapActivity, DetailDemoActivity::class.java)
                intent.putExtra(DetailDemoActivity.FEED_ID_KEY, feedID)
                this@MapActivity.startActivity(intent)
            })

        }

        fun setData(data: FeedData?) {
            Picasso.with(context).load(String.format("%s%s", HttpUtil.URL, data?.imageUrl)).into(imageView)
            title?.text = data?.title

            val calendar = Calendar.getInstance()

            calendar.timeInMillis = data?.date!!
            val AmPmStr: String
//        calendar.get(Calendar.AM_PM)
            if (Calendar.AM == calendar.get(Calendar.AM_PM)) {
                AmPmStr = "오전"
            } else {
                AmPmStr = "오후"
            }

            date?.text = String.format(this@MapActivity.getString(R.string.feed_list_time_format),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DATE),
                    AmPmStr,
                    calendar.get(Calendar.HOUR)
            )

            likeCount?.text = data?.likeCount.toString()
            // TODO 좋아요 여부도 표시해야 함. mData?.isLike 를 가지고...
            commentCount?.text = data?.commentCount.toString()
            container?.visibility = View.VISIBLE

            feedID = data?.feedId
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
        webView?.loadUrl(mapServerUrl)

        webView?.addJavascriptInterface(AndroidBridge(handler, this), "android")

        moveAroundLocation(null)
        getData()
    }

    private fun getData() {
        HttpUtil.getHttpService().getEvents(0).enqueue(object : Callback<Array<FeedData>> {
            override fun onFailure(call: Call<Array<FeedData>>?, t: Throwable?) {
                Log.d("namsang", "fail")
            }

            override fun onResponse(call: Call<Array<FeedData>>, response: Response<Array<FeedData>>?) {
                Log.d("namsang", "response")
                feedList.clear()
                for (data in response?.body()!!) {
                    if (true == data.isConfirm) {
                        feedList.add(data)
                    }
                }
                setMarker()
            }
        })

    }

    override fun onResume() {
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
                    val id = arg.toLong()

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



        for (data in feedList) {
            val obj = JSONObject()
            obj.put("id", data.feedId.toString())
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


    private fun findFeed(id: Long): FeedData? {

        for (feedData in feedList) {
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



