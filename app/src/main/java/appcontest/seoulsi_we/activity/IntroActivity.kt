package appcontest.seoulsi_we.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import appcontest.seoulsi_we.R

// 깃 커밋 테스트
class IntroActivity : Activity() {

    private val handler = Handler()

    private val runnable = Runnable {
        val intent = Intent(this@IntroActivity, MainActivity::class.java)
        this@IntroActivity.startActivity(intent)
        this@IntroActivity.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
    }

    override fun onResume() {
        super.onResume()

        handler.postDelayed(runnable, 2500)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}
