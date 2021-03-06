package appcontest.seoulsi_we.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.*
import android.widget.*
import appcontest.seoulsi_we.Consts
import appcontest.seoulsi_we.R
import appcontest.seoulsi_we.customView.CustomWebView
import appcontest.seoulsi_we.model.EventsFeelData
import appcontest.seoulsi_we.model.FeedDetailData
import appcontest.seoulsi_we.model.ResultData
import appcontest.seoulsi_we.service.HttpUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.demo_comment_view.view.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    private var cheerCountTextView: TextView? = null
    private var sadCountTextView: TextView? = null
    private var angerCountTextView: TextView? = null
    private var unLikeCountTextView: TextView? = null


    private var commentEditText: EditText? = null
    private var commentEnrolButton: Button? = null

    private var commentContainer: LinearLayout? = null
    private var detailEventData: FeedDetailData? = null

    private var feedID: Long? = null
    private var isInitialize : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_demo)

        feedID = intent.getLongExtra(FEED_ID_KEY, -1)

        if (feedID!! < 0) {
            throw IllegalArgumentException("0 이상이 피드 아이디를 넘겨받아야 합니다.")
        }

        initView()

        initWebView(webView)

        getData(feedID!!, Consts.DEVICE_ID)

    }

    private fun getData(feedID: Long, deviceID: String) {
        HttpUtil.getHttpService().getEvent(feedID, deviceID).enqueue(object : Callback<FeedDetailData> {
            override fun onFailure(call: Call<FeedDetailData>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<FeedDetailData>?, response: Response<FeedDetailData>?) {
                detailEventData = response?.body()
                if (null == detailEventData?.eventsFeelData) {
                    detailEventData?.eventsFeelData = EventsFeelData(detailEventData?.feedData?.feedId!!)
                }
                updateUI(detailEventData!!)
            }
        })
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

    fun updateUI(data: FeedDetailData) {
        if (null != data.feedData?.imageUrl) {
            Picasso.with(this@DetailDemoActivity).load(String.format("%s%s", HttpUtil.URL, data.feedData?.imageUrl)).into(feedImageView)
        }

        if (null != data.feedData?.subTitle) {
            subTitleTextview?.text = data.feedData?.subTitle
        }

        if (null != data.feedData?.title) {
            titleTextView?.text = data.feedData?.title
        }

        if (null != data.feedData?.content) {
            contentTextView?.text = data.feedData?.content
        }

        if (null != data.feedData?.date) {
            val calendar = Calendar.getInstance()

            calendar.timeInMillis = data.feedData?.date!!
            timeTextView?.text = String.format(getString(R.string.time_format_end_hour),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DATE),
                    calendar.get(Calendar.HOUR))
        }


        val addArr = data.feedData?.address

        if (null != addArr) {
            if (addArr.isNotEmpty()) {
                if (addArr[0].placeName!!.isNotEmpty()) {
                    startLocationtextView?.text = addArr[0].placeName
                } else {
                    startLocationtextView?.text = addArr[0].location
                }
            }

            if (2 <= addArr.size) {
                if (addArr[addArr.size - 1].placeName!!.isNotEmpty()) {
                    endLocationTextView?.text = addArr[addArr.size - 1].placeName
                } else {
                    endLocationTextView?.text = addArr[addArr.size - 1].location
                }
            } else {
                endLocationContainer?.visibility = View.GONE
            }
        }

        if(true == data.eventsFeelData?.like){
            likeImageView?.setImageResource(R.drawable.heart_red)
        }
        else{
            likeImageView?.setImageResource(R.drawable.heart)
        }

        cheerCountTextView?.text = data.feedData?.cheerCount?.toString()
        sadCountTextView?.text = data.feedData?.sadCount?.toString()
        angerCountTextView?.text = data.feedData?.angerCount?.toString()
        unLikeCountTextView?.text = data.feedData?.noLikeCount?.toString()


        val arr = JSONArray()

        for (address in data.feedData?.address!!) {
            val obj = JSONObject()
            obj.put("id", "0")
            obj.put("lat", address.lat?.toString())
            obj.put("lon", address.lon?.toString())
            arr.put(obj)
        }

        if (!isInitialize) {
            isInitialize = true
            Thread({
                try {
                    Thread.sleep(1000)
                    runOnUiThread {
                        webView?.loadUrl("javascript:setStartEndMarker(" + arr.toString() + ")")
                    }
                } catch (e: InterruptedException) {

                }

            }).start()
        }

        commentContainer?.removeAllViews()

        for (comment in data.replyData!!) {
            val v = LayoutInflater.from(this@DetailDemoActivity).inflate(R.layout.demo_comment_view, null, false)
            var deviceID = comment.deviceId
            if(6 < deviceID?.length!!){
                deviceID = deviceID.substring(deviceID.length - 6, deviceID.length)
            }

            v.comment_id.text = deviceID
            v.comment_textbox.text = comment.text

            commentContainer?.addView(v, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
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

        cheerCountTextView = findViewById(R.id.detail_activity_cheer_textview)
        sadCountTextView = findViewById(R.id.detail_activity_sad_textview)
        angerCountTextView = findViewById(R.id.detail_activity_anger_textview)
        unLikeCountTextView = findViewById(R.id.detail_activity_unlike_textview)

        commentEditText = findViewById(R.id.detail_activity_input_comment)
        commentEnrolButton = findViewById(R.id.detail_activity_enrol_comment)
        commentContainer = findViewById(R.id.detail_activity_comment_container)

    }

    fun onClickButton(v: View) {

        when (v.id) {
            R.id.btn_detail_demo_close -> {
                this@DetailDemoActivity.finish()
            }
            R.id.btn_detail_activity_demo_like -> {
                setFeel(Feel.LIKE, detailEventData?.eventsFeelData?.like!!)
            }
            R.id.detail_activity_unlike_view -> {
                setFeel(Feel.NOLIKE, detailEventData?.eventsFeelData?.noLike!!)
            }
            R.id.detail_activity_cheer_view -> {
                setFeel(Feel.CHEER, detailEventData?.eventsFeelData?.cheer!!)
            }
            R.id.detail_activity_sad_view -> {
                setFeel(Feel.SAD, detailEventData?.eventsFeelData?.sad!!)
            }
            R.id.detail_activity_anger_view -> {
                setFeel(Feel.ANGER, detailEventData?.eventsFeelData?.anger!!)
            }
            R.id.btn_detail_activity_demo_share -> {
                val sendIntent = Intent()
                sendIntent.type = "text/plain"
                sendIntent.putExtra(Intent.EXTRA_TEXT, "TEST")
                val chooser: Intent = Intent.createChooser(sendIntent, "")
                startActivity(chooser)
            }
            R.id.detail_activity_enrol_comment -> {
                writeReply()
            }

        }
    }

    enum class Feel constructor(value: Int) {
        LIKE(0),
        NOLIKE(1),
        CHEER(2),
        SAD(3),
        ANGER(4);

        val Value = value
    }

    fun setFeel(feel: Feel, flag: Boolean) {
        HttpUtil.getHttpService().setFeel(feedID!!, Consts.DEVICE_ID, feel.Value, flag).enqueue(object : Callback<ResultData> {
            override fun onFailure(call: Call<ResultData>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<ResultData>?, response: Response<ResultData>?) {
                val resultCommand = ResultData.ResultCommand.findCommand(response?.body()?.resultCode!!)
                when (resultCommand) {
                    ResultData.ResultCommand.SUCCESS -> {
                        getData(feedID!!, Consts.DEVICE_ID)
                    }
                    ResultData.ResultCommand.FAIL -> {

                    }
                }
            }
        })
    }

    fun writeReply() {
        val text = commentEditText?.text?.toString()
        if (text!!.isEmpty()) {
            return
        }

        HttpUtil.getHttpService().writeReply(feedID!!, Consts.DEVICE_ID, text).enqueue(object : Callback<ResultData> {
            override fun onFailure(call: Call<ResultData>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<ResultData>?, response: Response<ResultData>?) {
                val resultCommand = ResultData.ResultCommand.findCommand(response?.body()?.resultCode!!)
                when (resultCommand) {
                    ResultData.ResultCommand.SUCCESS -> {
                        getData(feedID!!, Consts.DEVICE_ID)
                        commentEditText?.setText("")
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(commentEditText?.windowToken, 0)

                    }
                    ResultData.ResultCommand.FAIL -> {

                    }

                }
            }
        })
    }

    fun pan(v: View) {
        when (v.id) {
            R.id.detail_activity_start_location_container -> {
                detailEventData?.feedData?.address ?: return
                if (0 < detailEventData?.feedData?.address?.size!!) {
                    val addressData = detailEventData?.feedData?.address!![0]
                    setPosition(addressData.lat, addressData.lon)
                }
            }
            R.id.detail_activity_end_location_container -> {
                detailEventData?.feedData?.address ?: return
                if (0 < detailEventData?.feedData?.address?.size!!) {
                    val size = detailEventData?.feedData?.address?.size!!
                    val addressData = detailEventData?.feedData?.address!![size - 1]
                    setPosition(addressData.lat, addressData.lon)
                }
            }
        }
    }

    fun setPosition(lat: Double?, lon: Double?) {
        webView?.loadUrl("javascript:panTo(" + lat + "," + lon + ")")
    }
}
