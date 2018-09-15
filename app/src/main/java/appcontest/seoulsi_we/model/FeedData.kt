package appcontest.seoulsi_we.model

import java.util.*

/**
 * Created by nam on 2018. 8. 9..
 */

/*등록번호: pid
일시: date
장소명: place
주소: location
시작시간:startTime
종료시간:endTime
주최자: host
목적: title
내용: content
상세내용: content_detail //불필요할 수도
접수증: certificate
등록일: regdate
수정일: editdate
호감수: like
댓글갯수: rpl_count*/

class FeedData {

    // 추후 구조 변경해야 함. 임시 구조
    val feedId: Int?
    val title: String?
    val subTitle: String?
    val date: Long?
    val startTime: Long?
    val endTime: Long?
    val content: String?
    val detailContent: String?
    val regDate: Long?
    val editDate: Long?
    val certImageUrl: String?
    val address: Array<DemoAddress>?
    val thumbnailImageUrl: String?
    val imageUrl: String?
    val likeCount: Int?
    val isLike: Boolean?
    val commentCount: Int?


    constructor(feedId: Int, title: String, subTitle: String, date: Long, startTime: Long, endTime: Long, content: String, detailContent: String,
                regDate: Long, editDate: Long, certImageUrl: String, address: Array<DemoAddress>, thumbnailImageUrl: String, imageUrl: String, likeCount: Int, isLike: Boolean, commentCount: Int) {
        this.feedId = feedId
        this.title = title
        this.subTitle = subTitle
        this.date = date
        this.startTime = startTime
        this.endTime = endTime
        this.content = content
        this.detailContent = detailContent
        this.regDate = regDate
        this.editDate = editDate
        this.certImageUrl = certImageUrl
        this.address = address
        this.thumbnailImageUrl = thumbnailImageUrl
        this.imageUrl = imageUrl
        this.likeCount = likeCount
        this.isLike = isLike
        this.commentCount = commentCount
    }

    companion object {
        val instance = object : ArrayList<FeedData>() {
            init {
                add(FeedData(1, "아시아나 항공\n4차 집회.", "‘OZKA면(오죽하면) 이러겠니’", Calendar.getInstance().timeInMillis, 100, 100, "content입니다.", "",
                        110100, 110100, "",
                        arrayOf(DemoAddress("서울특별시 용산구 용산동2가 1-727", "37.545696", "126.979326"), DemoAddress("용산중학교", "37.545912", "126.980972")),
                        "https://search3.kakaocdn.net/argon/0x200_85_hr/Fg49rrhUWre",
                        "https://search3.kakaocdn.net/argon/0x200_85_hr/Fg49rrhUWre",
                        22, true, 12))
            }
        }
    }
    class DemoAddress constructor(_address: String, _lat: String, _lon: String) {
        val address: String? = _address
        val lat: String? = _lat
        val lon: String? = _lon
    }
}