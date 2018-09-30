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
import java.util.*

/**
 * Created by nam on 2018. 8. 12..
 */
class CustomSliderView constructor(context: Context, data: BannerData.Banner, listener: CustomSliderViewListener) : BaseSliderView(context), View.OnClickListener {

    val mListener = listener
    val mData = data

    override fun getView(): View {

        val view = LayoutInflater.from(mContext).inflate(R.layout.slider_item_view, null, false)
        val title = view.findViewById<LinearLayout>(R.id.slider_item_title_container)
        val tvTitleTime = view.findViewById<TextView>(R.id.slider_item_title_time)
        val tvTitleInfo = view.findViewById<TextView>(R.id.slider_item_title_info)
        val imageView = view.findViewById<ImageView>(R.id.slider_item_image)
//        Picasso.with(mContext).load(mData.imageUrl).into(imageView)


        title.setOnClickListener(this@CustomSliderView)
        imageView.setOnClickListener(this@CustomSliderView)

        val date = Calendar.getInstance()
        date.time = mData.date!!
        date.add(Calendar.HOUR_OF_DAY, -9)
        val todayDate = Calendar.getInstance()

        var printText = ""

        tvTitleInfo.text = mContext.getString(R.string.demo_list)

        if(date.get(Calendar.YEAR) == todayDate.get(Calendar.YEAR)){
            if(date.get(Calendar.MONTH)+1 == todayDate.get(Calendar.MONTH) +1){
                if(date.get(Calendar.DATE) == todayDate.get(Calendar.DATE)){
                    printText = mContext.getString(R.string.word_today)
                }
                else {
                    // 어제일 수 있고, 그 이전일 수 있다.
                    val tmpDate = Calendar.getInstance()
                    tmpDate.add(Calendar.DATE, -1)
                    if(date.get(Calendar.DATE) == tmpDate.get(Calendar.DATE)){
                        printText = mContext.getString(R.string.word_yesterday)
                    }
                    else{
                        printText = String.format(mContext.getString(R.string.format_date),
                                date.get(Calendar.YEAR),
                                date.get(Calendar.MONTH)+1,
                                date.get(Calendar.DATE))
                    }
                }
                tvTitleTime.text = printText
                return view
            }
        }

        printText = String.format(mContext.getString(R.string.format_date),
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH)+1,
                date.get(Calendar.DATE))

        tvTitleTime.text = printText


        return view
    }

    override fun onClick(v: View?) {
        mListener.onSliderClicked(mData)
    }

    interface CustomSliderViewListener {
        fun onSliderClicked(data: BannerData.Banner)
    }
}