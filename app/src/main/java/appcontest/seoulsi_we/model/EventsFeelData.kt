package appcontest.seoulsi_we.model

import com.google.gson.annotations.SerializedName

/**
 * Created by nam on 2018. 9. 27..
 */
class EventsFeelData constructor(pid : Long){

    @SerializedName("pid") var pid : Long? = pid
    @SerializedName("like") var like : Boolean? = null
    @SerializedName("anger") var anger : Boolean? = null
    @SerializedName("cheer") var cheer : Boolean? = null
    @SerializedName("sad") var sad : Boolean? = null
    @SerializedName("noLike") var noLike : Boolean? = null

    init {
        like = false
        anger = false
        cheer = false
        sad = false
        noLike = false
    }

}