package appcontest.seoulsi_we.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import appcontest.seoulsi_we.R
import appcontest.seoulsi_we.dialog.SelectLocationDialog


class EnrolMyDemoActivity : BaseActivity(), SelectLocationDialog.SelectLocationDialogListener {

    val LOCATION_ACTIVITY_RESULT_CODE = 1000

    var locationTextView : TextView? = null

    var enrolDemo = EnrolDemo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enrol_my_demo)

        val textView: TextView = findViewById(R.id.navigation_toolbar_title)
        textView.text = getString(R.string.enrol_demo)

        locationTextView = findViewById(R.id.enrol_location)

    }

    fun onClicked(v: View) {
        when (v.id) {
        // 일시 등록
            R.id.enrol_time -> {

            }
        // 장소 등록
            R.id.enrol_location -> {
                //TODO 위치 선택 액티비티를 통해 주소, 경도 위도를 가져온다.
                val dialog = SelectLocationDialog.newInstance()
                dialog.setListener(this@EnrolMyDemoActivity)
                dialog.show(fragmentManager, "DialogFragment")
            }
        // 주최자 등록
            R.id.enrol_promoter -> {

            }
        }
    }

    override fun onSelectedLocation(lat: String, lon: String, address: String) {
        enrolDemo.lat = lat
        enrolDemo.lon = lon
        enrolDemo.address = address

        locationTextView?.text = address
    }

    class EnrolDemo{
        var time:Long? =null
        var lat : String? = null
        var lon : String? = null
        var address : String? = null
        var promoter : String? = null
        var goal : String? = null
    }
}
