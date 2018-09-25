package appcontest.seoulsi_we.model

import com.google.gson.annotations.SerializedName

/**
 * Created by nam on 2018. 8. 9..
 */

/*
등록번호: pid
일시: date
장소: [ 장소명 : place
       위도 : lat
       경도 : lon
       ] -> 이동 경로 포함
시작시간:startTime
종료시간:endTime
주최자: host
목적: title
부제: subTitle
내용: content
상세내용: content_detail //불필요할 수도
접수증: certificate
이미지 경로: imageUrl
등록일: regdate
수정일: editdate
호감수: like
댓글갯수: rpl_count
응원개수: cheer_count
슬픔개수: sad_count
화남개수: anger_count
별로개수: noLike_count

호감여부: isLike
응원여부: isCheer
슬픔여부: isSad
화남여부: isAnger
별로여부: isNoLike
*/

class FeedData {

    // 추후 구조 변경해야 함. 임시 구조
    @SerializedName("pid") var feedId: Int? = null           // 등록번호
    @SerializedName("title") var title: String? = null
    @SerializedName("subtitle") var subTitle: String? = null
    @SerializedName("date") var date: String? = null
    @SerializedName("regdate") var regDate: String?= null
    @SerializedName("editdate") var editDate: String? = null
    @SerializedName("startTime") var startTime: Long? = null
    @SerializedName("endTime") var endTime: Long? = null
    @SerializedName("content") var content: String? = null
    @SerializedName("certificate") var certImageUrl: String? = null
    @SerializedName("place") var address: Array<AddressData>? = null
    // 0 : 장소명칭, 1 : 주소, 2 : 위도, 3 : 경도
    @SerializedName("like") var likeCount: Int? = null
    @SerializedName("rpl_count") var commentCount: Int? = null
    @SerializedName("cheer_count") var cheerCount : Int? = null
    @SerializedName("sad_count") var sadCount : Int? = null
    @SerializedName("anger_count") var angerCount : Int? = null
    @SerializedName("noLike_count") var noLikeCount : Int? = null


    inner class AddressData{
        @SerializedName("lat") var lat : Double? = null
        @SerializedName("lng") var lon : Double? = null
        @SerializedName("location") var location : String? = null
        @SerializedName("placeName") var placeName : String? = null

    }
}