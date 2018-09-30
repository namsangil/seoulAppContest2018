package appcontest.seoulsi_we.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import appcontest.seoulsi_we.Consts
import appcontest.seoulsi_we.R
import appcontest.seoulsi_we.model.FeedData
import appcontest.seoulsi_we.service.HttpUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.searched_item_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SearchActivity : BaseActivity() {

    private var searchEditText: EditText? = null
    private var searchButton: ImageView? = null
    private val searchedData: ArrayList<FeedData> = ArrayList()
    private var searchedListView: RecyclerView? = null
    private var searchedFeedViewAdapter: SearchedFeedViewAdapter? = null
    private var fragmentContainer: FrameLayout? = null
    private var tabHost: TabHost? = null

    private var recentContainer: LinearLayout? = null
    private var recommandContainer: LinearLayout? = null

    private val SPLIT_CHAR_DATA = "&&"
    private val SPLIT_CHAR_OBJECT = "!!"

    private val recentSearchedDataList: ArrayList<RecentSearchData> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initView()
    }

    private fun initView() {
        val host = findViewById<View>(R.id.tabHost) as TabHost
        tabHost = host
        host.setup()
        host.setOnTabChangedListener(object : TabHost.OnTabChangeListener {

            override fun onTabChanged(tabId: String?) {

                for (i in 0..host.tabWidget.childCount - 1) {
//                    host.tabWidget.getChildAt(i).setBackgroundColor(Color.parseColor("#FFFFFF")) // unselected
                    val tv = host.tabWidget.getChildAt(i).findViewById<TextView>(android.R.id.title) //Unselected Tabs
                    tv.setTextColor(Color.parseColor("#dddddd"))
                }

//                host.tabWidget.getChildAt(host.currentTab).setBackgroundColor(Color.parseColor("#FFFFFF")); // selected
                val tv = host.currentTabView.findViewById<TextView>(android.R.id.title)  //for Selected Tab
                tv.setTextColor(Color.parseColor("#8ac81e"))
            }
        })

        //Tab 1
        var spec: TabHost.TabSpec = host.newTabSpec("0")
        spec.setContent(R.id.recommand_container_scrollview)
        spec.setIndicator("추천 검색어")
        host.addTab(spec)

        //Tab 2
        spec = host.newTabSpec("1")
        spec.setContent(R.id.recent_searched_container_scrollview)
        spec.setIndicator("최근 검색어")

        host.addTab(spec)

        val widget: TabWidget = host.findViewById(android.R.id.tabs)
        val firstTabView: View = widget.getChildTabViewAt(0)
        val firstTextView: TextView = firstTabView.findViewById(android.R.id.title)
        firstTextView.textSize = 20f
        firstTextView.typeface = (Typeface.DEFAULT_BOLD)

        val secondTabView: View = widget.getChildTabViewAt(1)
        val secondTextView: TextView = secondTabView.findViewById(android.R.id.title)
        secondTextView.textSize = 20f
        secondTextView.typeface = (Typeface.DEFAULT_BOLD)

        searchEditText = findViewById(R.id.navigation_toolbar_search)
        searchEditText?.setOnEditorActionListener({ v, actionId, event ->
            if (EditorInfo.IME_ACTION_SEARCH == actionId) {
                val text = searchEditText?.text?.toString()

                var isExistKeyword = false
                var idx = 0
                for (searchData in recentSearchedDataList) {
                    if (searchData.keyWord == text) {
                        isExistKeyword = true
                        break
                    }
                    idx++
                }

                val calendar = Calendar.getInstance()
                val date = String.format("%02d.%02d", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))

                // 리스트에 존재하면 날짜만 변경하고 다시 저장
                if (true == isExistKeyword) {
                    recentSearchedDataList[idx] = RecentSearchData(text!!, date)
                } else {
                    // 존재하지 않으면 추가로 저장
                    recentSearchedDataList.add(RecentSearchData(text!!, date))
                }

                saveRecentSearchedKeywords(this@SearchActivity, recentSearchedDataList)

                search(text)
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        })

        searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                tabHost?.visibility = View.VISIBLE
                searchedListView?.visibility = View.GONE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        searchButton = findViewById(R.id.navigation_toolbar_search_button)
        searchButton?.setOnClickListener { search(searchEditText?.text.toString()) }

        fragmentContainer = findViewById(android.R.id.tabcontent)
        recommandContainer = findViewById(R.id.recommand_container)
        recentContainer = findViewById(R.id.recent_searched_container)

        recentSearchedDataList.addAll(loadRecentSearchedKeywords(this@SearchActivity))

        searchedListView = findViewById(R.id.searched_list_view)
        searchedFeedViewAdapter = SearchedFeedViewAdapter()
        searchedListView?.adapter = searchedFeedViewAdapter
        searchedListView?.layoutManager = LinearLayoutManager(this@SearchActivity)
        val divider = DividerItemDecoration(this@SearchActivity, LinearLayoutManager(this@SearchActivity).orientation)
        searchedListView?.addItemDecoration(divider)
        searchedFeedViewAdapter?.notifyDataSetChanged()

        updateUI()
    }

    private fun updateUI() {

        recentContainer?.removeAllViews()
        var index = 0
        for (data in recentSearchedDataList) {
            val v = LayoutInflater.from(this@SearchActivity).inflate(R.layout.searched_item_layout, null, false)
            v.tag = index
            v.searched_view_keyword.text = data.keyWord
            v.searched_view_date.text = data.date
            v.searched_view_delete.tag = index
            v.searched_view_delete.setOnClickListener({ view ->
                val idx: Int = view.tag as Int
                recentSearchedDataList.removeAt(idx)
                saveRecentSearchedKeywords(this@SearchActivity, recentSearchedDataList)
            })

            v.setOnClickListener({ view ->
                val idx = view.tag as Int
                searchEditText?.setText(recentSearchedDataList[idx].keyWord)
                search(recentSearchedDataList[idx].keyWord)

                val recentSearchedData = recentSearchedDataList[idx]
                recentSearchedDataList.removeAt(idx)
                recentSearchedDataList.add(0, recentSearchedData)
                saveRecentSearchedKeywords(this@SearchActivity, recentSearchedDataList)

            })
            recentContainer?.addView(v, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
            index++
        }

    }

    private fun search(searchText: String?) {

        // 키보드 숨김
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText?.windowToken, 0)

        HttpUtil.getHttpService().searchEvents(searchText!!).enqueue(object : Callback<Array<FeedData>> {
            override fun onFailure(call: Call<Array<FeedData>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<Array<FeedData>>?, response: Response<Array<FeedData>>?) {
                searchedData.clear()
                for (data in response?.body()!!) {
                    if (true == data.isConfirm) {
                        searchedData.add(data)
                    }
                }
                searchedFeedViewAdapter?.setData(searchedData)
                searchedFeedViewAdapter?.notifyDataSetChanged()
                tabHost?.visibility = View.GONE
                searchedListView?.visibility = View.VISIBLE
            }
        })
    }


    inner class SearchedFeedViewAdapter : RecyclerView.Adapter<SearchedFeedViewAdapter.ViewHolder>() {

        private val feedList: ArrayList<FeedData> = ArrayList()

        fun setData(feedDatas: ArrayList<FeedData>) {
            feedList.clear()
            feedList.addAll(feedDatas)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var holder: ViewHolder? = null
            var retView: View = LayoutInflater.from(this@SearchActivity).inflate(R.layout.feed_item_for_map, null, false)

            holder = ViewHolder(retView)

            return holder
        }

        override fun getItemCount(): Int {
            return feedList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.updateUI(feedList[position])
        }

        inner class ViewHolder constructor(v: View) : RecyclerView.ViewHolder(v) {
            var view: View = v
            var imageView: ImageView? = null
            var titleTextView: TextView? = null
            var dateTextView: TextView? = null
            var likeCountTextView: TextView? = null
            var replyCountTextView: TextView? = null
            var feedID: Long = -1

            init {
                imageView = view.findViewById(R.id.map_feed_item_image)
                titleTextView = view.findViewById(R.id.map_feed_item_title)
                dateTextView = view.findViewById(R.id.map_tv_feed_item_time)
                likeCountTextView = view.findViewById(R.id.map_feed_tv_like_count)
                replyCountTextView = view.findViewById(R.id.map_feed_tv_comment_count)

                view.setOnClickListener {
                    startDetailActivity(feedID)
                }
            }

            fun updateUI(data: FeedData?) {
                feedID = data?.feedId!!
                Picasso.with(this@SearchActivity).load(String.format("%s%s", HttpUtil.URL, data?.imageUrl)).into(imageView)
                titleTextView?.text = data?.title

                val calendar = Calendar.getInstance()

                calendar.timeInMillis = data?.date!!
                val AmPmStr: String
                if (Calendar.AM == calendar.get(Calendar.AM_PM)) {
                    AmPmStr = "오전"
                } else {
                    AmPmStr = "오후"
                }

                dateTextView?.text = String.format(this@SearchActivity.getString(R.string.feed_list_time_format),
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DATE),
                        AmPmStr,
                        calendar.get(Calendar.HOUR)
                )

                likeCountTextView?.text = data?.likeCount.toString()
                replyCountTextView?.text = data?.commentCount.toString()
            }
        }
    }

    fun startDetailActivity(feedID: Long) {
        val intent = Intent(this@SearchActivity, DetailDemoActivity::class.java)
        intent.putExtra(DetailDemoActivity.FEED_ID_KEY, feedID)
        startActivity(intent)
    }

    fun loadRecentSearchedKeywords(activity: Activity): ArrayList<RecentSearchData> {
        val list = ArrayList<RecentSearchData>()
        val sharedPreference = activity.getSharedPreferences(Consts.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
        val data = sharedPreference.getString(Consts.RECENT_SEARCH_SHARED_PREFERENCE_KEY, "")

        if (data.isEmpty()) {
            return list
        }

        val array = data.split(SPLIT_CHAR_OBJECT)
        for (searchedData in array) {
            if (searchedData.isEmpty()) {
                break
            }
            val items = searchedData.split(SPLIT_CHAR_DATA)
            val recentSearchedData = RecentSearchData(items[0], items[1])
            list.add(recentSearchedData)
        }

        return list
    }

    fun saveRecentSearchedKeywords(activity: Activity, recentSearchDatas: ArrayList<RecentSearchData>) {
        val saveStr = StringBuilder()
        for (recentSearchData in recentSearchDatas) {
            saveStr.append(recentSearchData.keyWord)
                    .append(SPLIT_CHAR_DATA)
                    .append(recentSearchData.date)
                    .append(SPLIT_CHAR_OBJECT)
        }

        val sharedPreference = activity.getSharedPreferences(Consts.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(Consts.RECENT_SEARCH_SHARED_PREFERENCE_KEY, saveStr.toString())

        editor.apply()

        updateUI()
    }

    inner class RecentSearchData constructor(keyword: String, date: String) {
        val keyWord = keyword
        val date = date
    }
}
