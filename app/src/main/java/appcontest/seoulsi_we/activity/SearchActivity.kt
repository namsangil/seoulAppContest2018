package appcontest.seoulsi_we.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import appcontest.seoulsi_we.R
import appcontest.seoulsi_we.model.FeedData
import appcontest.seoulsi_we.service.HttpUtil
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SearchActivity : BaseActivity() {

    private var searchEditText: EditText? = null
    private val searchedData: ArrayList<FeedData> = ArrayList()
    private var searchedListView: RecyclerView? = null
    private var searchedFeedViewAdapter: SearchedFeedViewAdapter? = null
    private var fragmentContainer: FrameLayout? = null
    private var tabHost: TabHost? = null


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
        spec.setContent(R.id.tab1)
        spec.setIndicator("추천 검색어")
        host.addTab(spec)

        //Tab 2
        spec = host.newTabSpec("1")
        spec.setContent(R.id.tab2)
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
                search(searchEditText?.text?.toString())

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

        fragmentContainer = findViewById(android.R.id.tabcontent)

        searchedListView = findViewById(R.id.searched_list_view)
        searchedFeedViewAdapter = SearchedFeedViewAdapter()
        searchedListView?.adapter = searchedFeedViewAdapter
        searchedListView?.layoutManager = LinearLayoutManager(this@SearchActivity)
        searchedFeedViewAdapter?.notifyDataSetChanged()
    }

    private fun search(searchText: String?) {
        HttpUtil.getHttpService().searchEvents(searchText!!).enqueue(object : Callback<Array<FeedData>> {
            override fun onFailure(call: Call<Array<FeedData>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<Array<FeedData>>?, response: Response<Array<FeedData>>?) {
                searchedData.clear()
                for (data in response?.body()!!) {
                    searchedData.add(data)
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
            var feedID: Int = -1

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

    fun startDetailActivity(feedID: Int) {
        val intent = Intent(this@SearchActivity, DetailDemoActivity::class.java)
        intent.putExtra(DetailDemoActivity.FEED_ID_KEY, feedID)
        startActivity(intent)
    }
}
