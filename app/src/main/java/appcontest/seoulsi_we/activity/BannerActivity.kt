package appcontest.seoulsi_we.activity

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import appcontest.seoulsi_we.R
import java.util.*

class BannerActivity : BaseActivity() {

    companion object {
        val DEMO_DATA_KEY = "demoList"
        val DEMO_DATE_KEY = "demoDate"
    }

    private var demoListContainer: LinearLayout? = null

    private var demoList: Array<String>? = null
    private var demoDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)

        demoListContainer = findViewById(R.id.activity_banner_demo_list_container)

        demoDate = intent.getSerializableExtra(DEMO_DATE_KEY) as Date
        demoList = intent.getStringArrayExtra(DEMO_DATA_KEY)


        val calendar = Calendar.getInstance()
        calendar.time = demoDate
        calendar.add(Calendar.HOUR_OF_DAY, -9)

        val textView: TextView = findViewById(R.id.navigation_toolbar_title)
        textView.text = String.format(getString(R.string.format_date_for_banner_activity),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE))

        for (demo in demoList!!) {
            val textView = TextView(this@BannerActivity)
            textView.setPadding(15, 40, 15, 40)
            textView.text = demo
            demoListContainer?.addView(textView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        }
    }
}
