package appcontest.seoulsi_we.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.*
import appcontest.seoulsi_we.R
import appcontest.seoulsi_we.customView.CustomWebView
import appcontest.seoulsi_we.model.FeedData
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class DetailDemoActivity : AppCompatActivity() {

    companion object {
        val FEED_ID_KEY = "feedID"
    }

    val handler = Handler()
    val url = "http://ec2-52-78-3-222.ap-northeast-2.compute.amazonaws.com"

    private var feedImageView: ImageView? = null
    private var subTitleTextview: TextView? = null
    private var titleTextView: TextView? = null
    private var likeImageView: ImageView? = null
    private var shareImageView: ImageView? = null

    private var contentTextView: TextView? = null

    private var timeTextView: TextView? = null
    private var startLocationContainer: View? = null
    private var startLocationtextView: TextView? = null
    private var endLocationContainer: View? = null
    private var endLocationTextView: TextView? = null

    private var webView: CustomWebView? = null
    private var cheerView: View? = null
    private var sadView: View? = null
    private var angerView: View? = null
    private var unLikeView: View? = null

    private var commentEditText: EditText? = null
    private var commentEnrolButton: Button? = null

    private var commentContainer: LinearLayout? = null

    private var data: FeedData? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_demo)

        val feedID = intent.getIntExtra(FEED_ID_KEY, -1)

        if (feedID < 0) {
            throw IllegalArgumentException("0 이상이 피드 아이디를 넘겨받아야 합니다.")
        }

        initView()

        data = FeedData.instance[0]

        updateUI(data!!)

        initWebView(webView)


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

        val arr = JSONArray()

        val obj1 = JSONObject()
        obj1.put("id", "1")
        obj1.put("lat", "37.551568")
        obj1.put("lon", "126.972787")

        arr.put(obj1)

        val obj2 = JSONObject()
        obj2.put("id", "2")
        obj2.put("lat", "37.552568")
        obj2.put("lon", "126.973787")

        arr.put(obj2)


        Thread({
            try {
                Thread.sleep(2000)
                runOnUiThread {
                    webView?.loadUrl("javascript:setStartEndMarker(" + arr.toString() + ")")
                }
            } catch (e: InterruptedException) {

            }

        }).start()
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
                    val id = Integer.parseInt(arg)

                } catch (e: NumberFormatException) {
                    return@post
                }
            })
        }
    }

    fun updateUI(data: FeedData) {
        if (null != data.imageUrl) {
            Picasso.with(this@DetailDemoActivity).load(data.imageUrl).into(feedImageView)
        }

        if (null != data.subTitle) {
            subTitleTextview?.text = data.subTitle
        }

        if (null != data.title) {
            titleTextView?.text = data.title
        }

        if (null != data.content) {
            contentTextView?.text = data.content
        }

        if (null != data.date) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = data.date
            timeTextView?.text = String.format(getString(R.string.time_format_end_hour),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DATE),
                    calendar.get(Calendar.HOUR))
        }

        val addArr = data.address

        if (null != addArr) {
            if (addArr.isNotEmpty()) {
                startLocationtextView?.text = addArr[0].address
            }

            if (2 <= addArr.size) {
                endLocationTextView?.text = addArr[addArr.size - 1].address
            } else {
                endLocationTextView?.visibility = View.GONE
            }
        }


    }

    fun initView() {
        feedImageView = findViewById(R.id.detail_activity_demo_image)
        subTitleTextview = findViewById(R.id.detail_activity_demo_subtitle)
        titleTextView = findViewById(R.id.detail_activity_demo_title)
        likeImageView = findViewById(R.id.btn_detail_activity_demo_like)
        shareImageView = findViewById(R.id.btn_detail_activity_demo_share)

        contentTextView = findViewById(R.id.detail_activity_content)
        timeTextView = findViewById(R.id.detail_activity_time)
        startLocationContainer = findViewById(R.id.detail_activity_start_location_container)
        startLocationtextView = findViewById(R.id.detail_activity_start_location)
        endLocationContainer = findViewById(R.id.detail_activity_end_location_container)
        endLocationTextView = findViewById(R.id.detail_activity_end_location)

        webView = findViewById(R.id.detail_activity_webview)

        webView?.setOnTouchListener(View.OnTouchListener { view, motionEvent ->


            return@OnTouchListener false
        })

        cheerView = findViewById(R.id.detail_activity_cheer_view)
        sadView = findViewById(R.id.detail_activity_sad_view)
        angerView = findViewById(R.id.detail_activity_anger_view)
        unLikeView = findViewById(R.id.detail_activity_unlike_view)

        commentEditText = findViewById(R.id.detail_activity_input_comment)
        commentEnrolButton = findViewById(R.id.detail_activity_enrol_comment)
        commentContainer = findViewById(R.id.detail_activity_comment_container)

    }

    fun onClickButton(v: View) {

        when (v.id) {
            R.id.btn_detail_demo_close -> {
                this@DetailDemoActivity.finish()
            }
            R.id.detail_activity_cheer_view -> {
                //TODO 응원해요
            }
            R.id.detail_activity_sad_view -> {
                //TODO 슬퍼요
            }
            R.id.detail_activity_anger_view -> {
                //TODO 화나요
            }
            R.id.detail_activity_unlike_view -> {
                //TODO 별로에요
            }
            R.id.btn_detail_activity_demo_like -> {
                //TODO 즐겨찾기 추가
            }
            R.id.btn_detail_activity_demo_share -> {
                //TODO 공유하기
                val sendIntent = Intent()
                sendIntent.type = "text/plain"
                sendIntent.putExtra(Intent.EXTRA_TEXT, "TEST")
                val chooser: Intent = Intent.createChooser(sendIntent, "")
                startActivity(chooser)
            }
        }
    }
}
