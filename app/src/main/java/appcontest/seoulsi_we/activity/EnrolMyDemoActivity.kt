package appcontest.seoulsi_we.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import appcontest.seoulsi_we.R
import appcontest.seoulsi_we.dialog.SelectLocationDialog
import appcontest.seoulsi_we.model.FeedData
import kotlinx.android.synthetic.main.enrol_place_view.view.*
import java.util.*


class EnrolMyDemoActivity : BaseActivity(), SelectLocationDialog.SelectLocationDialogListener {

    var firstLocationView: LinearLayout? = null

    var enrolFeedData = FeedData()

    var dateTextView: TextView? = null
    var promoterEditText: EditText? = null
    var aimEditText: EditText? = null
    var addPlaceButton: ImageView? = null

    var placeContainer: LinearLayout? = null
    val placeList: ArrayList<FeedData.AddressData> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enrol_my_demo)

        val textView: TextView = findViewById(R.id.navigation_toolbar_title)
        textView.text = getString(R.string.enrol_demo)

        dateTextView = findViewById(R.id.enrol_time)
        promoterEditText = findViewById(R.id.enrol_promoter)
        aimEditText = findViewById(R.id.enrol_aim)

        placeContainer = findViewById(R.id.enrol_location_container)
        firstLocationView = findViewById(R.id.first_location_view)
        firstLocationView?.demo_place_name?.setOnClickListener {
            onClicked(firstLocationView?.demo_place_name!!)
        }
        // 첫번째 장소는 삭제 기능이 없다.
        firstLocationView?.findViewById<ImageView>(R.id.delete_demo_place)?.visibility = View.GONE
        firstLocationView?.findViewById<ImageView>(R.id.add_demo_place_alias)?.visibility = View.GONE
        firstLocationView?.findViewById<ImageView>(R.id.add_demo_place_alias)?.tag = 0
        firstLocationView?.findViewById<ImageView>(R.id.add_demo_place_alias)?.setOnClickListener { view ->
            val idx = view.tag as Int
            if (0 < placeList.size) {
                showAliasDialog(firstLocationView!!, idx, placeList[0])
            }
        }
        addPlaceButton = findViewById(R.id.enrol_demo_add_place_button)
        addPlaceButton?.visibility = View.GONE

    }

    fun onClicked(v: View) {
        when (v.id) {
        // 일시 등록
            R.id.enrol_time -> {
                val cal = Calendar.getInstance()
                val dialog = DatePickerDialog(this@EnrolMyDemoActivity, DatePickerDialog.OnDateSetListener { _, year, month, date ->

                    val dialog = TimePickerDialog(this@EnrolMyDemoActivity, TimePickerDialog.OnTimeSetListener { _, hour, min ->
                        cal.set(year, month, date, hour, min)
                        val dialog = TimePickerDialog(this@EnrolMyDemoActivity, TimePickerDialog.OnTimeSetListener { _, hour, min ->
                            val endTime = Calendar.getInstance()

                            endTime.set(year, month, date, hour, min)

                            onSelectedTime(cal, endTime)

                        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)

                        dialog.show()
                        Toast.makeText(this@EnrolMyDemoActivity, "집회 종료 시간을 선택해주세요.", Toast.LENGTH_SHORT).show()

                    }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)  //마지막 boolean 값은 시간을 24시간으로 보일지 아닐지

                    dialog.show()
                    Toast.makeText(this@EnrolMyDemoActivity, "집회 시작 시간을 선택해주세요.", Toast.LENGTH_SHORT).show()

                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE))

                dialog.datePicker.minDate = Date().time    //입력한 날짜 이전으로 클릭 안되게 옵션
                dialog.show()
                Toast.makeText(this@EnrolMyDemoActivity, "집회 일자를 선택해주세요.", Toast.LENGTH_SHORT).show()

            }
        // 장소 등록
            R.id.demo_place_name -> {
                val dialog = SelectLocationDialog.newInstance()
                dialog.isFirstLocation = true
                dialog.setListener(this@EnrolMyDemoActivity)
                dialog.show(fragmentManager, "DialogFragment")
            }
            R.id.send_demo -> {
                enrolFeedData.host = promoterEditText?.text.toString()
                enrolFeedData.content = aimEditText?.text.toString()
                if (enrolFeedData.isEmptyForEnrol()) {
                    // TODO 데이터를 전송한다.
                    Toast.makeText(this@EnrolMyDemoActivity, "데이터를 전송합니다..", Toast.LENGTH_SHORT).show()
                } else {
                      Toast.makeText(this@EnrolMyDemoActivity, "입력되지 않은 데이터가 있습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.enrol_demo_add_place_button -> {
                val dialog = SelectLocationDialog.newInstance()
                dialog.setListener(this@EnrolMyDemoActivity)
                dialog.show(fragmentManager, "DialogFragment")
            }

        }
    }

    override fun onSelectedLocation(lat: String, lon: String, location: String, isFirstLocation: Boolean) {

        val address = FeedData.AddressData(lat.toDouble(), lon.toDouble(), location, "")

        if (true == isFirstLocation) {
            if (0 < placeList.size) {
                placeList.removeAt(0)
            }
            placeList.add(0, address)
            firstLocationView?.add_demo_place_alias?.visibility = View.VISIBLE
        } else {
            placeList.add(address)
        }

        enrolFeedData.address = placeList.toTypedArray()

        if (true == isFirstLocation) {
            firstLocationView?.demo_place_name?.text = address.location
            firstLocationView?.demo_place_name?.tag = placeList.size - 1 // index
        } else {
            val v = LayoutInflater.from(this@EnrolMyDemoActivity).inflate(R.layout.enrol_place_view, null, false)
            v.demo_place_name.text = address.location
            v.delete_demo_place.tag = placeList.size - 1
            v.delete_demo_place.setOnClickListener { view ->
                val idx = view.tag as Int
                placeList.removeAt(idx)
                placeContainer?.removeViewAt(idx)
                // 객체에 반영
                enrolFeedData.address = placeList.toTypedArray()
            }
            v.add_demo_place_alias.tag = placeList.size - 1
            v.add_demo_place_alias.setOnClickListener { view ->
                val idx = view.tag as Int
                // 별명을 입력할 다이얼로그 호출
                showAliasDialog(v, idx, address)

            }
            placeContainer?.addView(v, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        }

        // 하나라도 추가하게 되면, 장소 추가 버튼이 보여진다.
        addPlaceButton?.visibility = View.VISIBLE

    }

    fun showAliasDialog(v: View, idx: Int, address: FeedData.AddressData) {
        val dialog = AlertDialog.Builder(this@EnrolMyDemoActivity)
        dialog.setMessage("장소 이름을 입력해주세요. ex) XX빌딩 정문")

        val editText = EditText(this@EnrolMyDemoActivity)
        editText.setText(placeList[idx].placeName)
        editText.gravity = Gravity.CENTER_HORIZONTAL
        editText.selectAll()
        editText.setBackgroundResource(0)
        dialog.setView(editText)

        dialog.setPositiveButton("확인", ({ dialog, _ ->
            val alias = editText.text.toString()
            placeList[idx].placeName = alias

            if (alias.isEmpty()) {
                v.demo_place_name.text = address.location
            } else {
                v.demo_place_name.text = address.location + "(" + alias + ")"
            }
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)

        }))

        dialog.setNegativeButton("취소", null)

        val alertDialog = dialog.create()

        if (null != alertDialog.window) {
            alertDialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }

        alertDialog.show()
    }

    fun onSelectedTime(startCalendar: Calendar, endCalendar: Calendar) {
        val value = String.format(getString(R.string.enrol_demo_time_format),
                startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH) + 1,
                startCalendar.get(Calendar.DATE),
                startCalendar.get(Calendar.HOUR_OF_DAY),
                endCalendar.get(Calendar.HOUR_OF_DAY)
        )

        dateTextView?.text = value
        enrolFeedData.date = startCalendar.timeInMillis
        enrolFeedData.startTime = startCalendar.get(Calendar.HOUR_OF_DAY)
        enrolFeedData.endTime = endCalendar.get(Calendar.HOUR_OF_DAY)
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
