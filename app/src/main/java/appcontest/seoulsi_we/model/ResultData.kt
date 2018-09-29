package appcontest.seoulsi_we.model

import com.google.gson.annotations.SerializedName

/**
 * Created by nam on 2018. 9. 25..
 */
class ResultData {

    enum class ResultCommand constructor(value: Int) {
        FAIL(2), SUCCESS(1);

        val Value = value

        companion object {
            fun findCommand(value: Int): ResultCommand {
                for (command in ResultCommand.values()) {
                    if (command.Value == value) {
                        return command
                    }
                }

                return ResultCommand.FAIL
            }
        }
    }

    @SerializedName("result") var resultCode: Int? = null     // 0 : fail , 1 : success
}