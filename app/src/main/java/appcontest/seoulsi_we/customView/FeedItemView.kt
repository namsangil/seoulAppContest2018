package appcontest.seoulsi_we.customView

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import appcontest.seoulsi_we.R
import appcontest.seoulsi_we.model.FeedData
import com.squareup.picasso.Picasso

/**
 * Created by nam on 2018. 8. 8..
 */
class FeedItemView : LinearLayout {

    private val TAG = "FeedItemList"
    private var mData: FeedData? = null

    private var mContext: Context? = null
    private var image: ImageView? = null
    private var tvTitle: TextView? = null
    private var tvDate: TextView? = null
    private var tvAddress: TextView? = null

    private var btnLike: ImageView? = null
    private var tvLikeCount: TextView? = null
    private var btnComment: ImageView? = null
    private var tvCommentCount: TextView? = null

    constructor(context: Context, data: FeedData) : super(context) {
        mContext = context
        mData = data
        init()
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

        applyData()
    }

    private fun applyData() {
        Picasso.with(mContext).load(mData?.thumbnailImageUrl).into(image)
        tvTitle?.text = mData?.title
        tvDate?.text = mData?.date.toString()       // 변환 해야 한다.
        tvAddress?.text = mData?.address

        tvLikeCount?.text = mData?.likeCount.toString()
        // TODO 좋아요 여부도 표시해야 함. mData?.isLike 를 가지고...
        tvCommentCount?.text = mData?.commentCount.toString()

        // TODO height 파라미터를 지정해주면 높이가 변경된다. 정사각형모양으로 맞추도록 수정하자
        layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1000)
    }
}