package appcontest.seoulsi_we.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import appcontest.seoulsi_we.FeedListAdapter
import appcontest.seoulsi_we.R
import appcontest.seoulsi_we.activity.DetailDemoActivity.Companion.FEED_ID_KEY
import appcontest.seoulsi_we.customView.CustomSliderView
import appcontest.seoulsi_we.model.BannerData
import appcontest.seoulsi_we.model.FeedData
import appcontest.seoulsi_we.service.HttpUtil
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Created by nam on 2018. 8. 7..
 */

class MainActivity : BaseActivity() {
    private val PERMISSIONS_CODE = 1101         // 위치 퍼미션

    companion object {
        var mainWidth: Int? = null
    }

    private var mDrawer: DrawerLayout? = null
    private var toolbar: Toolbar? = null
    private var nvDrawer: NavigationView? = null
    private var sliderShow: SliderLayout? = null
    private var feedListContainer: GridView? = null

    private var feedAdapter: FeedListAdapter? = null

    private var drawerToggle: ActionBarDrawerToggle? = null     // 메뉴 버튼

    private var toggleFeedOrderTextViews: Array<TextView>? = null   // 등록순, 인기순, 댓글순 ui변경을 위함

    private var feedList: ArrayList<FeedData> = ArrayList()
    private var bannerData: BannerData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 액션바 등록
        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = ""
        setSupportActionBar(toolbar)

        mDrawer = findViewById(R.id.drawer_layout)
        drawerToggle = setupDrawerToggle()
        mDrawer!!.addDrawerListener(drawerToggle!!)

        // 슬라이드 뷰 등록
        nvDrawer = findViewById(R.id.nvView)

        // 뷰 로딩이 완료되면, status bar 높이만큼 떨어뜨린다.
//        nvDrawer?.viewTreeObserver?.addOnGlobalLayoutListener(
//                object : ViewTreeObserver.OnGlobalLayoutListener {
//                    override fun onGlobalLayout() {
//                        val rect = Rect()
//                        val window = window
//                        window.decorView.getWindowVisibleDisplayFrame(rect)
//                        val statusBarHeight = rect.top
//                        nvDrawer?.setPadding(0, statusBarHeight, 0, 0)
//                        nvDrawer?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
//                    }
//                }
//        )

        // 메인 슬라이드 뷰
        sliderShow = findViewById(R.id.slider)

        // 등록순, 인기순, 댓글순 버튼
        toggleFeedOrderTextViews = arrayOf(findViewById(R.id.tv_order_by_recent), findViewById(R.id.tv_order_by_join), findViewById(R.id.tv_order_by_comment))

        sliderShow?.setCustomIndicator(findViewById(R.id.custom_indicator))

        // 피드 컨테이너
        feedListContainer = findViewById(R.id.feed_list_container)
        feedAdapter = FeedListAdapter()
        feedListContainer?.adapter = feedAdapter
        feedListContainer?.setOnItemClickListener({ parent, view, position, id ->
            val intent = Intent(this@MainActivity, DetailDemoActivity::class.java)
            intent.putExtra(FEED_ID_KEY, feedAdapter?.getItem(position)?.feedId)
            startActivity(intent)
        })

        feedAdapter!!.setData(feedList)
        feedAdapter!!.notifyDataSetChanged()

        getData(0)
        getBanner()

    }

    private fun getData(condition: Int) {
        feedList.clear()
        feedAdapter!!.setData(feedList)
        feedAdapter!!.notifyDataSetChanged()

        HttpUtil.getHttpService().getEvents(condition).enqueue(object : Callback<Array<FeedData>> {
            override fun onFailure(call: Call<Array<FeedData>>?, t: Throwable?) {
                Log.d("namsang", "fail")
            }

            override fun onResponse(call: Call<Array<FeedData>>, response: Response<Array<FeedData>>?) {
                Log.d("namsang", "response")
                for (data in response?.body()!!) {
                    if (true == data.isConfirm) {
                        feedList.add(data)
                    }
                }

                feedAdapter!!.setData(feedList)
                feedAdapter!!.notifyDataSetChanged()
            }
        })

    }

    fun addSlideView(view: SliderLayout?, data: BannerData.Banner) {
        val sliderView = CustomSliderView(this@MainActivity, data, object : CustomSliderView.CustomSliderViewListener {
            override fun onSliderClicked(data: BannerData.Banner) {
                val intent = Intent(this@MainActivity, BannerActivity::class.java)

                intent.putExtra(BannerActivity.DEMO_DATA_KEY, data.contents)
                intent.putExtra(BannerActivity.DEMO_DATE_KEY, data.date)

                startActivity(intent)
            }
        })
        sliderView.scaleType = BaseSliderView.ScaleType.CenterCrop
        view?.addSlider(sliderView)

    }

    fun onClickSlideMenu(v: View) {
        when (v.id) {
            R.id.about_seoulsiwi          // 서울시위란? 버튼
            -> {
                mDrawer!!.closeDrawer(nvDrawer!!, false)
                val intent = Intent(this@MainActivity, IntroductionAppActivity::class.java)
                startActivity(intent)
            }
            R.id.about_demo               // 집회시위 관련정보 버튼
            -> {
                mDrawer!!.closeDrawer(nvDrawer!!, false)
                val intent = Intent(this@MainActivity, AboutDemoActivity::class.java)
                startActivity(intent)
            }
            R.id.promotion_demo           // 집회시위 홍보하기 버튼
            -> {
                mDrawer!!.closeDrawer(nvDrawer!!, false)
                val intent = Intent(this@MainActivity, EnrolMyDemoActivity::class.java)
                startActivity(intent)
            }
            R.id.setting                  // 설정 버튼
            -> {
                mDrawer!!.closeDrawers()
//                Toast.makeText(this@MainActivity, getString(R.string.setting), Toast.LENGTH_SHORT).show()
            }
            R.id.btn_navigation_close
            -> {
                mDrawer!!.closeDrawers()
                // 결국 닫아지므로 여기서는 아무것도 하지 않는다.
            }

        }
        mDrawer!!.closeDrawers()
    }

    private fun getBanner() {
        HttpUtil.getHttpService().getBanners().enqueue(object : Callback<BannerData> {
            override fun onFailure(call: Call<BannerData>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<BannerData>?, response: Response<BannerData>?) {
                bannerData = response?.body()
                updateBanner()
            }
        })
    }

    private fun updateBanner() {
        // 임시데이터) BannerData 클래스를 넘겨서 채운다.
        if (null != bannerData?.todayBanner) {
            addSlideView(sliderShow, bannerData?.todayBanner!!)
        }

        if (null != bannerData?.yesterdayBanner) {
            addSlideView(sliderShow, bannerData?.yesterdayBanner!!)
        }

        if (null != bannerData?.recentWeekBanner) {
            for (banner in bannerData?.recentWeekBanner!!) {
                addSlideView(sliderShow, banner)
            }
        }

    }

//region 슬라이드 메뉴 관련 메소드


    private fun setupDrawerToggle(): ActionBarDrawerToggle {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger search without it.
        return ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle!!.syncState()
    }

    // 등록순, 참여순, 댓글순 버튼 이벤트
    fun onClickFeedOrder(v: View) {
        when (v.id) {
            R.id.tv_order_by_recent -> {
//                Toast.makeText(this@MainActivity, "등록순", Toast.LENGTH_SHORT).show()
                getData(0)
            }
            R.id.tv_order_by_join -> {
//                Toast.makeText(this@MainActivity, "참여순", Toast.LENGTH_SHORT).show()
                getData(1)
            }
            R.id.tv_order_by_comment -> {
//                Toast.makeText(this@MainActivity, "댓글순", Toast.LENGTH_SHORT).show()
                getData(2)
            }
            R.id.iv_collect -> {
                Toast.makeText(this@MainActivity, "모아보기", Toast.LENGTH_SHORT).show()
            }
        }
        updateFeedOrderButtonUI(toggleFeedOrderTextViews!!, v.id)
    }

    fun updateFeedOrderButtonUI(textButtons: Array<TextView>, selectedTextViewId: Int) {
        for (textButton in textButtons) {
            if (textButton.id == selectedTextViewId) {
                // 눌린 텍스트 버튼이면, 색을 #85b237, 스타일을 bold로 변경
                textButton.setTextColor(Color.parseColor("#85b237"))
                textButton.setTypeface(textButton.typeface, Typeface.BOLD)
            } else {
                textButton.setTextColor(resources.getColor(android.R.color.tab_indicator_text))
                textButton.setTypeface(textButton.typeface, Typeface.NORMAL)
            }
        }
    }

    // 액션바 메뉴 버튼 이벤트
    fun onClickActionBarMenu(v: View) {
        when (v.id) {
            R.id.btn_search -> {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
            R.id.btn_map -> {
//                Toast.makeText(this@MainActivity, "지도", Toast.LENGTH_SHORT).show()
                if (true == checkLocationPermission()) {
                    startActivity(Intent(this@MainActivity, MapActivity::class.java))
                }

            }
        }
    }

    fun checkLocationPermission(): Boolean {
        // M 버전 이상이면 묻지 않고 true 리턴
        if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_CODE)
                return false
            }
        }
        return true
    }

    override fun onStop() {
        sliderShow?.stopAutoCycle()
        super.onStop()
    }


}