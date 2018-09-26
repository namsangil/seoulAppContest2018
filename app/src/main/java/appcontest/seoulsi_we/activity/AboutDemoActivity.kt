package appcontest.seoulsi_we.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import appcontest.seoulsi_we.R

class AboutDemoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_demo)

        val textView: TextView = findViewById(R.id.navigation_toolbar_title)
        textView.text = getString(R.string.about_demo)

    }

    fun onClickedView(v: View) {
        val intent = Intent(this@AboutDemoActivity, AboutDemoDetailActivity::class.java)
        when (v.id) {
            R.id.activity_about_demo_how_to_report_view -> {
                intent.putExtra(AboutDemoDetailActivity.ABOUT_DEMO_TYPE_INTENT_KEY, AboutDemoDetailActivity.HOW_TO_REPORT_TYPE)
            }
            R.id.activity_about_demo_notice_view -> {
                intent.putExtra(AboutDemoDetailActivity.ABOUT_DEMO_TYPE_INTENT_KEY, AboutDemoDetailActivity.DEMO_NOTICE_TYPE)
            }
            R.id.activity_about_demo_rule_view -> {
                intent.putExtra(AboutDemoDetailActivity.ABOUT_DEMO_TYPE_INTENT_KEY, AboutDemoDetailActivity.DEMO_RULE_TYPE)
            }
        }
        startActivity(intent)
    }
}
