package appcontest.seoulsi_we.model

/**
 * Created by nam on 2018. 8. 12..
 */
class BannerData constructor(id: Int, imageUrl: String, time: Long, description: String) {

    // 메인의 배너로 표시할 슬라이드 뷰의 데이터 모델
    val bannerId = id
    val imageUrl = imageUrl
    val time = time
    val description = description

}