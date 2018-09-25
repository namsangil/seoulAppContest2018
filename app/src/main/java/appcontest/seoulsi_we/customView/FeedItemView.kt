package appcontest.seoulsi_we.customView

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import android.widget.*
import appcontest.seoulsi_we.R
import appcontest.seoulsi_we.Utils
import appcontest.seoulsi_we.model.FeedData
import com.squareup.picasso.Picasso
import java.util.*


/**
 * Created by nam on 2018. 8. 8..
 */
class FeedItemView : LinearLayout {

    private val TAG = "FeedItemList"

    private var mContext: Context? = null

    // 이미지 부분
    private var imageContainer: FrameLayout? = null
    private var image: ImageView? = null
    private var tvTitle: TextView? = null
    private var tvAddress: TextView? = null


    // 바텀 뷰
    private var ivTimer: ImageView? = null
    private var tvTime: TextView? = null
    private var btnLike: LinearLayout? = null
    private var tvLikeCount: TextView? = null
    private var btnComment: LinearLayout? = null
    private var tvCommentCount: TextView? = null

    private var feedInfoView: LinearLayout? = null
    private var feedBottomView: LinearLayout? = null

    constructor(context: Context?) : super(context) {
        mContext = context
        init()
    }

    public fun setData(data: FeedData) {
        applyData(data)
    }

    private fun init() {
        // 레이아웃 불러와서
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_feed_layout, this@FeedItemView, true)

        imageContainer = view.findViewById(R.id.feed_image_container)
        image = view.findViewById(R.id.feed_iv_image)
        tvTitle = view.findViewById(R.id.feed_tv_demo_title)
        tvAddress = view.findViewById(R.id.feed_tv_demo_address)

        ivTimer = view.findViewById(R.id.feed_item_timer)
        tvTime = view.findViewById(R.id.feed_start_demo_time)

        btnLike = view.findViewById(R.id.feed_btn_like)
        tvLikeCount = view.findViewById(R.id.feed_tv_like_count)
        btnComment = view.findViewById(R.id.feed_btn_comment)
        tvCommentCount = view.findViewById(R.id.feed_tv_comment_count)

        feedInfoView = view.findViewById(R.id.feed_info_view)
        feedBottomView = view.findViewById(R.id.feed_bottom_view)

        if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
            val drawable = resources.getDrawable(R.drawable.feed_item_round_rectangle_image) as GradientDrawable
            image?.background = drawable
            image?.clipToOutline = true
        }

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {


                // 기기별 해상도가 달라서 wrap_content로 크기를 지정하면, 화면이 작은 기기에서는 뷰를 벗어나는 경우가 발생한다.
                // 픽셀로 계산하여 직접 크기들을 지정한다.

                layoutParams = AbsListView.LayoutParams(width, LayoutParams.WRAP_CONTENT)
                imageContainer?.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, width)

                // bottom 뷰 사이즈를 0.15배로 설정하고
                val bottomSize = (width * 0.15).toInt()
                val bottomLayoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, bottomSize)

                // bottom 뷰의 top margin을 bottom Size의 0.3배로 설정
                bottomLayoutParams.topMargin = (bottomSize * 0.3).toInt()
                feedBottomView?.layoutParams = bottomLayoutParams

                // 나머지 텍스트 뷰들의 텍스트 크기를 지정
                val timerParams = ConstraintLayout.LayoutParams(bottomSize, bottomSize)
                ivTimer?.layoutParams = timerParams
                val timerPadding = (bottomSize * 0.12).toInt()
                ivTimer?.setPadding(timerPadding, timerPadding, timerPadding, timerPadding)
                val textDp = Utils.convertPixelsToDp((bottomSize * 0.4).toFloat(), mContext)
                tvTime?.setTextSize(TypedValue.COMPLEX_UNIT_SP, textDp)
                tvTime?.setPadding((bottomSize * 0.2).toInt(), 0, 0, 0)
                tvLikeCount?.setTextSize(TypedValue.COMPLEX_UNIT_SP, textDp)
                tvCommentCount?.setTextSize(TypedValue.COMPLEX_UNIT_SP, textDp)


                val tvCommentLayoutParams = btnComment?.layoutParams as ConstraintLayout.LayoutParams
                tvCommentLayoutParams.leftMargin = (bottomSize * 0.1).toInt()


                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

    }

    private fun applyData(mData: FeedData?) {
        Picasso.with(mContext).load(mData?.imageUrl).into(image)
        tvTitle?.text = mData?.title
        tvAddress?.text = mData?.address?.get(0)?.placeName

        val calendar = Calendar.getInstance()
        calendar.time = Utils.getSomeDate(mData?.date)
        calendar.add(Calendar.HOUR, 9)
        val AmPmStr: String
//        calendar.get(Calendar.AM_PM)
        if (Calendar.AM == calendar.get(Calendar.AM_PM)) {
            AmPmStr = "오전"
        } else {
            AmPmStr = "오후"
        }

        tvTime?.text = String.format(mContext!!.getString(R.string.feed_list_time_format),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE),
                AmPmStr,
                calendar.get(Calendar.HOUR)
                )

        tvLikeCount?.text = mData?.likeCount.toString()
        // TODO 좋아요 여부도 표시해야 함. mData?.isLike 를 가지고...
        tvCommentCount?.text = mData?.commentCount.toString()
    }
}