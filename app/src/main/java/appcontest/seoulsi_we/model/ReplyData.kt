package appcontest.seoulsi_we.model

import com.google.gson.annotations.SerializedName

/**
 * Created by nam on 2018. 9. 27..
 */
class ReplyData{

    @SerializedName("_id") var replyID : String? = null
    @SerializedName("pid") var pid : Int? = null
    @SerializedName("device_id") var deviceId: String? = null           // 기기아이디
    @SerializedName("editdate") var editDate : String? = null
    @SerializedName("regdate") var regDate : String? = null
    @SerializedName("text") var text : String? = null
    @SerializedName("like") var like : Int? = null
    @SerializedName("noLike") var noLike : Int? = null
}