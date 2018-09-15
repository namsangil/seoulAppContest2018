package appcontest.seoulsi_we.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import appcontest.seoulsi_we.R

class DetailDemoActivity : AppCompatActivity() {

    companion object {
        val FEED_ID_KEY = "feedID"
    }

    private var feedID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_demo)

        feedID = intent.getIntExtra(FEED_ID_KEY, -1)

        if (feedID!! < 0) {
            throw IllegalArgumentException("0 이상이 피드 아이디를 넘겨받아야 합니다.")
        }

        Toast.makeText(this@DetailDemoActivity, feedID.toString(), Toast.LENGTH_SHORT).show()

    }
}
