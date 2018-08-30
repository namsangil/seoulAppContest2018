package appcontest.seoulsi_we.model

/**
 * Created by nam on 2018. 8. 9..
 */

class FeedData {

    // 추후 구조 변경해야 함. 임시 구조
    val feedId: Int?
    val title: String?
    val date: Long?
    val address: String?
    val thumbnailImageUrl: String?
    val imageUrl: String?
    val likeCount: Int?
    val isLike: Boolean?
    val commentCount: Int?

    companion object {
        val instance = object : ArrayList<FeedData>() {
            init {
                add(FeedData(1, "아시아나 항공\n4차 집회.", 1100100, "경기도 광명",
                        "https://search3.kakaocdn.net/argon/0x200_85_hr/Fg49rrhUWre",
                        "https://search3.kakaocdn.net/argon/0x200_85_hr/Fg49rrhUWre",
                        22, true, 12))
                add(FeedData(2, "아시아나 항공\n4차 집회.", 1100100, "경기도 광명",
                        "https://search4.kakaocdn.net/argon/0x200_85_hr/1EQKLXaMZhj",
                        "https://search4.kakaocdn.net/argon/0x200_85_hr/1EQKLXaMZhj",
                        11, true, 11))
                add(FeedData(3, "제목입니다.", 1100100, "경기도 광명",
                        "https://search1.kakaocdn.net/argon/0x200_85_hr/KZvo5lXazFQ",
                        "https://search1.kakaocdn.net/argon/0x200_85_hr/KZvo5lXazFQ",
                        3, true, 20))
                add(FeedData(4, "제목입니다.", 1100100, "경기도 광명",
                        "https://search2.kakaocdn.net/argon/0x200_85_hr/7w1pIRNgnED",
                        "https://search2.kakaocdn.net/argon/0x200_85_hr/7w1pIRNgnED",
                        5, true, 1))
                add(FeedData(5, "제목입니다.", 1100100, "경기도 광명",
                        "https://search2.kakaocdn.net/argon/0x200_85_hr/4aXYwB5z99N",
                        "https://search2.kakaocdn.net/argon/0x200_85_hr/4aXYwB5z99N",
                        7, true, 0))
                add(FeedData(6, "제목입니다.", 1100100, "경기도 광명",
                        "https://search2.kakaocdn.net/argon/0x200_85_hr/2KgqzD4ecQf",
                        "https://search2.kakaocdn.net/argon/0x200_85_hr/2KgqzD4ecQf",
                        2, true, 9))
                add(FeedData(7, "제목입니다.", 1100100, "경기도 광명",
                        "https://search3.kakaocdn.net/argon/0x200_85_hr/7BHqWs1H6fW",
                        "https://search3.kakaocdn.net/argon/0x200_85_hr/7BHqWs1H6fW",
                        10, true, 7))
            }
        }


    }

    constructor(feedId: Int, title: String, date: Long, address: String, thumbnailImageUrl: String, imageUrl: String, likeCount: Int, isLike: Boolean, commentCount: Int) {
        this.feedId = feedId
        this.title = title
        this.date = date
        this.address = address
        this.thumbnailImageUrl = thumbnailImageUrl
        this.imageUrl = imageUrl
        this.likeCount = likeCount
        this.isLike = isLike
        this.commentCount = commentCount
    }

}