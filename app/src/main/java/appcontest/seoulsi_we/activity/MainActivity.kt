package appcontest.seoulsi_we.activity

import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewTreeObserver
import android.widget.GridView
import android.widget.Toast
import appcontest.seoulsi_we.FeedListAdapter
import appcontest.seoulsi_we.R
import appcontest.seoulsi_we.model.FeedData
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.DefaultSliderView


/**
 * Created by nam on 2018. 8. 7..
 */

class MainActivity : AppCompatActivity() {
    private var mDrawer: DrawerLayout? = null
    private var toolbar: Toolbar? = null
    private var nvDrawer: NavigationView? = null
    private var sliderShow: SliderLayout? = null
    private var feedListContainer: GridView? = null

    private var feedAdapter: FeedListAdapter? = null

    private var drawerToggle: ActionBarDrawerToggle? = null     // 메뉴 버튼

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
        nvDrawer?.viewTreeObserver?.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val rect = Rect()
                        val window = window
                        window.decorView.getWindowVisibleDisplayFrame(rect)
                        val statusBarHeight = rect.top
                        nvDrawer?.setPadding(0, statusBarHeight, 0, 0)
                        nvDrawer?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    }
                }
        )

        // 메인 슬라이드 뷰
        sliderShow = findViewById(R.id.slider)

        // 예제 임시 코드
        addSlideView(sliderShow, "https://search2.kakaocdn.net/argon/0x200_85_hr/ETtjfwGegTb", "강아지1")
        addSlideView(sliderShow, "https://search2.kakaocdn.net/argon/0x200_85_hr/HJf5a3OiJXf", "강아지2")
        addSlideView(sliderShow, "https://search4.kakaocdn.net/argon/0x200_85_hr/DCtJ1xT47kH", "강아지3")
        addSlideView(sliderShow, "https://search1.kakaocdn.net/argon/0x200_85_hr/LfVZqwj6fZI", "강아지4")

        sliderShow?.setCustomIndicator(findViewById(R.id.custom_indicator))


        // 피드 컨테이너

        feedListContainer = findViewById(R.id.feed_list_container)

        feedAdapter = FeedListAdapter()

        feedListContainer?.adapter = feedAdapter

        feedListContainer?.setOnItemClickListener({ parent, view, position, id ->
            Toast.makeText(this@MainActivity, "feed id : " + feedAdapter?.getItem(position)?.feedId, Toast.LENGTH_SHORT).show()

        })

        val feedList: ArrayList<FeedData> = ArrayList()
        feedList.add(FeedData(1, "제목입니다.", 1100100, "경기도 광명",
                "https://search3.kakaocdn.net/argon/0x200_85_hr/Fg49rrhUWre",
                "https://search3.kakaocdn.net/argon/0x200_85_hr/Fg49rrhUWre",
                22, true, 12))
        feedList.add(FeedData(2, "제목입니다.", 1100100, "경기도 광명",
                "https://search4.kakaocdn.net/argon/0x200_85_hr/1EQKLXaMZhj",
                "https://search4.kakaocdn.net/argon/0x200_85_hr/1EQKLXaMZhj",
                11, true, 11))
        feedList.add(FeedData(3, "제목입니다.", 1100100, "경기도 광명",
                "https://search1.kakaocdn.net/argon/0x200_85_hr/KZvo5lXazFQ",
                "https://search1.kakaocdn.net/argon/0x200_85_hr/KZvo5lXazFQ",
                3, true, 20))
        feedList.add(FeedData(4, "제목입니다.", 1100100, "경기도 광명",
                "https://search2.kakaocdn.net/argon/0x200_85_hr/7w1pIRNgnED",
                "https://search2.kakaocdn.net/argon/0x200_85_hr/7w1pIRNgnED",
                5, true, 1))
        feedList.add(FeedData(5, "제목입니다.", 1100100, "경기도 광명",
                "https://search2.kakaocdn.net/argon/0x200_85_hr/4aXYwB5z99N",
                "https://search2.kakaocdn.net/argon/0x200_85_hr/4aXYwB5z99N",
                7, true, 0))
        feedList.add(FeedData(6, "제목입니다.", 1100100, "경기도 광명",
                "https://search2.kakaocdn.net/argon/0x200_85_hr/2KgqzD4ecQf",
                "https://search2.kakaocdn.net/argon/0x200_85_hr/2KgqzD4ecQf",
                2, true, 9))
        feedList.add(FeedData(7, "제목입니다.", 1100100, "경기도 광명",
                "https://search3.kakaocdn.net/argon/0x200_85_hr/7BHqWs1H6fW",
                "https://search3.kakaocdn.net/argon/0x200_85_hr/7BHqWs1H6fW",
                10, true, 7))

        feedAdapter!!.setData(feedList)
        feedAdapter!!.notifyDataSetChanged()
    }

    fun addSlideView(view: SliderLayout?, url: String, name: String) {
        val slierView = DefaultSliderView(this@MainActivity)
        slierView.image(url)
        slierView.scaleType = BaseSliderView.ScaleType.CenterCrop
        slierView.setOnSliderClickListener({ _ ->
            Toast.makeText(this@MainActivity, String.format("%s", name), Toast.LENGTH_SHORT).show()
        })
        view?.addSlider(slierView)

    }

    fun onClickSlideMenu(v: View) {
        when (v.id) {
            R.id.about_seoulsiwi          // 서울시위란? 버튼
            -> Toast.makeText(this@MainActivity, getString(R.string.about_seoulsiwi), Toast.LENGTH_SHORT).show()
            R.id.about_demo               // 집회시위 관련정보 버튼
            -> Toast.makeText(this@MainActivity, getString(R.string.about_demo), Toast.LENGTH_SHORT).show()
            R.id.promotion_demo           // 집회시위 홍보하기 버튼
            -> Toast.makeText(this@MainActivity, getString(R.string.promotion_demo), Toast.LENGTH_SHORT).show()
            R.id.setting                  // 설정 버튼
            -> Toast.makeText(this@MainActivity, getString(R.string.setting), Toast.LENGTH_SHORT).show()
        }
        mDrawer!!.closeDrawers()
    }

//region 슬라이드 메뉴 관련 메소드


    private fun setupDrawerToggle(): ActionBarDrawerToggle {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle!!.syncState()
    }

    // 최신순, 참여순, 댓글순 버튼 이벤트
    fun onClickFeedOrder(v: View) {
        when (v.id) {
            R.id.tv_order_by_recent -> {
                Toast.makeText(this@MainActivity, "최신순", Toast.LENGTH_SHORT).show()
            }
            R.id.tv_order_by_join -> {
                Toast.makeText(this@MainActivity, "참여순", Toast.LENGTH_SHORT).show()
            }
            R.id.tv_order_by_comment -> {
                Toast.makeText(this@MainActivity, "댓글순", Toast.LENGTH_SHORT).show()
            }
            R.id.iv_collect -> {
                Toast.makeText(this@MainActivity, "모아보기", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 액션바 메뉴 버튼 이벤트
    fun onClickActionBarMenu(v: View) {
        when (v.id) {
            R.id.btn_search -> {
                Toast.makeText(this@MainActivity, "돋보기", Toast.LENGTH_SHORT).show()
            }
            R.id.btn_map -> {
                Toast.makeText(this@MainActivity, "지도", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStop() {
        sliderShow?.stopAutoCycle()
        super.onStop()
    }
}