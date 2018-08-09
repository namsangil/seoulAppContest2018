package appcontest.seoulsi_we

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import appcontest.seoulsi_we.customView.FeedItemView
import appcontest.seoulsi_we.model.FeedData

/**
 * Created by nam on 2018. 8. 9..
 */
class FeedListAdapter : BaseAdapter() {

    private val feedList: ArrayList<FeedData> = ArrayList()

    fun setData(list: ArrayList<FeedData>) {
        feedList.clear()
        feedList.addAll(list)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var holder: ViewHolder? = null
        var retView: View? = null

        if (null == convertView) {
            retView = FeedItemView(parent?.context)
            holder = ViewHolder()

            holder.view = retView
            retView.tag = holder

        } else {
            holder = convertView.tag as ViewHolder
            retView = convertView
        }

        holder.view?.setData(feedList[position])

        return retView
    }

    override fun getItem(position: Int): FeedData {
        return feedList[position]
    }

    override fun getItemId(position: Int): Long {
        return feedList[position].feedId?.toLong() ?: 0
    }

    override fun getCount(): Int {
        return feedList.size
    }

    inner class ViewHolder {
        var view: FeedItemView? = null

    }

}
