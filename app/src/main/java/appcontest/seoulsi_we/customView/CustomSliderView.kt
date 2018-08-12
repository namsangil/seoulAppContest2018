package appcontest.seoulsi_we.customView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import appcontest.seoulsi_we.R
import appcontest.seoulsi_we.model.BannerData
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.squareup.picasso.Picasso

/**
 * Created by nam on 2018. 8. 12..
 */
class CustomSliderView constructor(context: Context, data : BannerData, listener: CustomSliderViewListener) : BaseSliderView(context), View.OnClickListener {

    val mListener = listener
    val mData = data

    override fun getView(): View {

        val view = LayoutInflater.from(mContext).inflate(R.layout.slider_item_view, null, false)
        val title = view.findViewById<LinearLayout>(R.id.slider_item_title_container)
        val tvTitleTime = view.findViewById<TextView>(R.id.slider_item_title_time)
        val tvTitleInfo = view.findViewById<TextView>(R.id.slider_item_title_info)
        val imageView = view.findViewById<ImageView>(R.id.slider_item_image)
        Picasso.with(mContext).load(mData.imageUrl).into(imageView)


        title.setOnClickListener(this@CustomSliderView)
        imageView.setOnClickListener(this@CustomSliderView)


        tvTitleTime.text = mData.time.toString()
        tvTitleInfo.text = mData.description

        return view
    }

    override fun onClick(v: View?) {
        mListener.onSliderClicked(mData)
    }

    interface CustomSliderViewListener {
        fun onSliderClicked(data : BannerData)
    }
}