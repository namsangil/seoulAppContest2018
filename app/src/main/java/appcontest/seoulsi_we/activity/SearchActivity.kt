package appcontest.seoulsi_we.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TabHost
import android.widget.TextView
import appcontest.seoulsi_we.R


class SearchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val host = findViewById<View>(R.id.tabHost) as TabHost
        host.setup()
        host.setOnTabChangedListener(object : TabHost.OnTabChangeListener {


            override fun onTabChanged(tabId: String?) {

                for (i in 0..host.tabWidget.childCount - 1) {
                    host.tabWidget.getChildAt(i).setBackgroundColor(Color.parseColor("#FFFFFF")) // unselected
                    val tv = host.tabWidget.getChildAt(i).findViewById<TextView>(android.R.id.title) //Unselected Tabs
                    tv.setTextColor(Color.parseColor("#dddddd"))
                }

                host.tabWidget.getChildAt(host.currentTab).setBackgroundColor(Color.parseColor("#FFFFFF")); // selected
                val tv = host.currentTabView.findViewById<TextView>(android.R.id.title)  //for Selected Tab
                tv.setTextColor(Color.parseColor("#8ac81e"))
            }
        })

        //Tab 1
        var spec: TabHost.TabSpec = host.newTabSpec("Tab One")
        spec.setContent(R.id.tab1)
        spec.setIndicator("Tab One")
        host.addTab(spec)

        //Tab 2
        spec = host.newTabSpec("Tab Two")
        spec.setContent(R.id.tab2)
        spec.setIndicator("Tab Two")
        host.addTab(spec)

    }
}
