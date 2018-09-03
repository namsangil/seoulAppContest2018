package appcontest.seoulsi_we.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import appcontest.seoulsi_we.R

class IntroductionAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction_app)

        val textView: TextView = findViewById(R.id.navigation_toolbar_title)
        textView.text = getString(R.string.activity_name)

    }

    fun backImageClick(v: View) {
        finish()
    }

}
