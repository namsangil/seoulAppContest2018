package appcontest.seoulsi_we.customView

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import android.widget.*
import appcontest.seoulsi_we.R
import appcontest.seoulsi_we.model.FeedData
import com.squareup.picasso.Picasso

/**
 * Created by nam on 2018. 8. 8..
 */
class FeedItemView : LinearLayout {

    private val TAG = "FeedItemList"

    private var mContext: Context? = null
    private var image: ImageView? = null
    private var tvTitle: TextView? = null
    private var tvDate: TextView? = null
    private var tvAddress: TextView? = null

    private var btnLike: ImageView? = null
    private var tvLikeCount: TextView? = null
    private var btnComment: ImageView? = null
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

        image = view.findViewById(R.id.feed_iv_image)
        tvTitle = view.findViewById(R.id.feed_tv_demo_title)
        tvDate = view.findViewById(R.id.feed_tv_demo_date)
        tvAddress = view.findViewById(R.id.feed_tv_demo_address)

        btnLike = view.findViewById(R.id.feed_btn_like)
        tvLikeCount = view.findViewById(R.id.feed_tv_like_count)
        btnComment = view.findViewById(R.id.feed_btn_comment)
        tvCommentCount = view.findViewById(R.id.feed_tv_comment_count)

        feedInfoView = view.findViewById(R.id.feed_info_view)
        feedBottomView = view.findViewById(R.id.feed_bottom_view)


        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                layoutParams = AbsListView.LayoutParams(width, width)

                val bottomSize = (width * 0.15).toInt()
                val paddingSize = (bottomSize * 0.2).toInt()

                val params = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                params.setMargins(paddingSize, paddingSize, paddingSize, paddingSize)
                params.gravity = Gravity.BOTTOM

                feedInfoView?.layoutParams = params
                feedBottomView?.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, bottomSize)
                btnLike?.layoutParams = ConstraintLayout.LayoutParams(bottomSize, bottomSize)
                btnComment?.layoutParams = ConstraintLayout.LayoutParams(bottomSize, bottomSize)
                btnLike?.setPadding(paddingSize, paddingSize, paddingSize, paddingSize)
                btnComment?.setPadding(paddingSize, paddingSize, paddingSize, paddingSize)

                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

    }

    private fun applyData(mData: FeedData?) {
        Picasso.with(mContext).load(mData?.thumbnailImageUrl).into(image)
        tvTitle?.text = mData?.title
        tvDate?.text = mData?.date.toString()       // 변환 해야 한다.
        tvAddress?.text = mData?.address

        tvLikeCount?.text = mData?.likeCount.toString()
        // TODO 좋아요 여부도 표시해야 함. mData?.isLike 를 가지고...
        tvCommentCount?.text = mData?.commentCount.toString()
    }
}