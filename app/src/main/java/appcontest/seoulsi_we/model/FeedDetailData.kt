package appcontest.seoulsi_we.model

import com.google.gson.annotations.SerializedName

/**
 * Created by nam on 2018. 9. 27..
 */
class FeedDetailData{
    @SerializedName("data1") var feedData: FeedData? = null           // 피드 데이터
    @SerializedName("data2") var replyData: Array<ReplyData>? = null           // 댓글
    @SerializedName("data3") var eventsFeelData: EventsFeelData? = null           // 피드에 대한 감정
}