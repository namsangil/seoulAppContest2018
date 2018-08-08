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