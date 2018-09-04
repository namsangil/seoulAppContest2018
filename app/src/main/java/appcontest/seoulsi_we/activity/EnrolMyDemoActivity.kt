package appcontest.seoulsi_we.activity

import android.os.Bundle
import android.widget.TextView
import appcontest.seoulsi_we.R

class EnrolMyDemoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enrol_my_demo)

        val textView: TextView = findViewById(R.id.navigation_toolbar_title)
        textView.text = getString(R.string.enrol_demo)

    }
}
