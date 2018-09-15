package appcontest.seoulsi_we.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import appcontest.seoulsi_we.R
import appcontest.seoulsi_we.dialog.SelectLocationDialog
import java.util.*


class EnrolMyDemoActivity : BaseActivity(), SelectLocationDialog.SelectLocationDialogListener {

    var locationTextView: TextView? = null

    var enrolDemo = EnrolDemo()

    var dateTextView : TextView?= null
    var promoterEditText: EditText? = null
    var aimEditText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enrol_my_demo)

        val textView: TextView = findViewById(R.id.navigation_toolbar_title)
        textView.text = getString(R.string.enrol_demo)

        dateTextView = findViewById(R.id.enrol_time)
        promoterEditText = findViewById(R.id.enrol_promoter)
        aimEditText = findViewById(R.id.enrol_aim)

        locationTextView = findViewById(R.id.enrol_location)

    }

    fun onClicked(v: View) {
        when (v.id) {
        // 일시 등록
            R.id.enrol_time -> {
                val cal = Calendar.getInstance()
                val dialog = DatePickerDialog(this@EnrolMyDemoActivity, DatePickerDialog.OnDateSetListener { _, year, month, date ->

                    val dialog = TimePickerDialog(this@EnrolMyDemoActivity, TimePickerDialog.OnTimeSetListener { _, hour, min ->
                        cal.set(year, month, date, hour, min)
                        onSelectedTime(cal)
                    }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)  //마지막 boolean 값은 시간을 24시간으로 보일지 아닐지

                    dialog.show()
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE))

                dialog.datePicker.minDate = Date().time    //입력한 날짜 이전으로 클릭 안되게 옵션
                dialog.show()

            }
        // 장소 등록
            R.id.enrol_location -> {
                val dialog = SelectLocationDialog.newInstance()
                dialog.setListener(this@EnrolMyDemoActivity)
                dialog.show(fragmentManager, "DialogFragment")
            }
            R.id.send_demo -> {
                enrolDemo.promoter = promoterEditText?.text.toString()
                enrolDemo.aim = aimEditText?.text.toString()
                if (enrolDemo.isNotEmpty()) {
                    // TODO 데이터를 전송한다.
                    Toast.makeText(this@EnrolMyDemoActivity, "데이터를 전송합니다..", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@EnrolMyDemoActivity, "입력되지 않은 데이터가 있습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSelectedLocation(lat: String, lon: String, address: String) {
        enrolDemo.lat = lat
        enrolDemo.lon = lon
        enrolDemo.address = address

        locationTextView?.text = address
    }

    fun onSelectedTime(cal : Calendar){
        val value = String.format(getString(R.string.time_format),
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH)+1,
                cal.get(Calendar.DATE),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE))

        dateTextView?.text = value
        enrolDemo.time = cal.timeInMillis
    }

    class EnrolDemo {
        var time: Long? = null
        var lat: String? = null
        var lon: String? = null
        var address: String? = null
        var promoter: String? = null
        var aim: String? = null

        fun isNotEmpty(): Boolean {
            time ?: return false
            lat ?: return false
            lon ?: return false
            address ?: return false
            if (address!!.isEmpty()) {
                return false
            }

            promoter ?: return false
            if (promoter!!.isEmpty()) {
                return false
            }

            aim ?: return false
            if (aim!!.isEmpty()) {
                return false
            }

            return true
        }
    }
}
