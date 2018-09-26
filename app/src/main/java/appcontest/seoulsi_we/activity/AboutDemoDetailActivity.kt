package appcontest.seoulsi_we.activity

import android.os.Bundle
import android.widget.TextView
import appcontest.seoulsi_we.R

class AboutDemoDetailActivity : BaseActivity() {

    companion object {
        val HOW_TO_REPORT_TYPE = 1
        val DEMO_NOTICE_TYPE = 2
        val DEMO_RULE_TYPE = 3
        val ABOUT_DEMO_TYPE_INTENT_KEY = "aboutDemoType"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_demo_detail)

        val titleTextView: TextView = findViewById(R.id.navigation_toolbar_title)
        val bodyTextView : TextView = findViewById(R.id.activity_about_demo_detail_text)

        val type = intent.getIntExtra(ABOUT_DEMO_TYPE_INTENT_KEY, 1)

        when(type){
            HOW_TO_REPORT_TYPE -> {
                titleTextView.text = getString(R.string.how_to_report)
                bodyTextView.text = getString(R.string.how_to_report_data)
            }
            DEMO_NOTICE_TYPE -> {
                titleTextView.text = getString(R.string.demo_notice)
                bodyTextView.text = getString(R.string.demo_notice_data)

            }
            DEMO_RULE_TYPE -> {
                titleTextView.text = getString(R.string.demo_rule)
                bodyTextView.text = getString(R.string.demo_rule_data)

            }
        }



    }
}
