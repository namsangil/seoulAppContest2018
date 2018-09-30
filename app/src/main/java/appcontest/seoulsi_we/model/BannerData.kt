package appcontest.seoulsi_we.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by nam on 2018. 8. 12..
 */
class BannerData{


    // 메인의 배너로 표시할 슬라이드 뷰의 데이터 모델
    @SerializedName("today") var todayBanner: Banner? = null
    @SerializedName("yesterday") var yesterdayBanner: Banner? = null
    @SerializedName("recentWeek") var recentWeekBanner: Array<Banner>? = null



    inner class Banner {
        @SerializedName("Date") var date: Date? = null
        @SerializedName("Contents") var contents: Array<String>? = null
    }

}
