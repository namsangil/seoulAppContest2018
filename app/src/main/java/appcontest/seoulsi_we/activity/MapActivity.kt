package appcontest.seoulsi_we.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.RelativeLayout
import appcontest.seoulsi_we.R
import net.daum.mf.map.api.MapView

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapview = MapView(this@MapActivity)
        val layout = findViewById<RelativeLayout>(R.id.map_view)
        layout.addView(mapview)
    }
}
