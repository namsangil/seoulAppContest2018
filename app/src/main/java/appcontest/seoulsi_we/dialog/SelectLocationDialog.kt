package appcontest.seoulsi_we.dialog

import android.app.DialogFragment
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ImageView
import android.widget.TextView
import appcontest.seoulsi_we.R

/**
 * Created by nam on 2018. 9. 8..
 */

class SelectLocationDialog : DialogFragment(), View.OnClickListener {
    private val TAG = "SelectLocationDialog"
    private val url = "http://ec2-52-78-3-222.ap-northeast-2.compute.amazonaws.com/enrol.html"

    interface SelectLocationDialogListener {
        fun onSelectedLocation(lat: String, lon: String, strAddress: String)
    }

    private val handler = Handler()
    private var webView: WebView? = null
    private var titleTextView: TextView? = null
    private var loadAddressTextView: TextView? = null
    private var addressTextView: TextView? = null
    private var cancelButton: ImageView? = null
    private var locationManager: LocationManager? = null
    private var listener: SelectLocationDialogListener? = null


    private var lat = ""            // 위도
    private var lon = ""            // 경도

    companion object {
        fun newInstance(): SelectLocationDialog {
            return SelectLocationDialog()
        }
    }

    fun setListener(_listener: SelectLocationDialogListener) {
        listener = _listener
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val v = inflater?.inflate(R.layout.dialog_select_location, container, true) as View

        locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        webView = v.findViewById(R.id.webview_dialog_select_location)
        initWebView(webView)
        titleTextView = v.findViewById(R.id.tv_dialog_select_location_title)
        loadAddressTextView = v.findViewById(R.id.tv_dialog_select_location_load_address)
        loadAddressTextView?.visibility = View.GONE
        loadAddressTextView?.setOnClickListener(this)
        addressTextView = v.findViewById(R.id.tv_dialog_select_location_address)
        addressTextView?.visibility = View.GONE
        addressTextView?.setOnClickListener(this)

        cancelButton = v.findViewById(R.id.btn_cancel_dialog_select_location)
        cancelButton?.setOnClickListener { dismiss() }

        moveAroundLocation(null)
        return v
    }

    private fun initWebView(webView: WebView?) {
        val settings = webView?.settings
        settings?.javaScriptEnabled = true
        settings?.loadWithOverviewMode = true
        settings?.useWideViewPort = true
        settings?.setSupportZoom(true)
        settings?.builtInZoomControls = false
        settings?.layoutAlgorithm = (WebSettings.LayoutAlgorithm.SINGLE_COLUMN)
        settings?.cacheMode = (WebSettings.LOAD_NO_CACHE)
        settings?.domStorageEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView?.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else {
            webView?.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }

        webView?.webChromeClient = WebChromeClient()
        webView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                webView?.invalidate()
            }
        }
        webView?.loadUrl(url)
        webView?.addJavascriptInterface(AndroidBridge(handler), "android")
    }

    fun moveAroundLocation(v: View?) {
        Log.d(TAG, "moveAroundLocation")
        try {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1f, locationListener)
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1f, locationListener)
        } catch (e: SecurityException) {

        }
    }

    private inner class AndroidBridge constructor(handler: Handler) {
        val handler = handler

        @JavascriptInterface
        fun onSelectItem(lat: String, lon: String, loadAddress: String, address: String) { // must be final
            handler.post({

                this@SelectLocationDialog.lat = lat
                this@SelectLocationDialog.lon = lon

                updateUI(loadAddress, address)
            })
        }
    }

    fun setPosition(lat: Double?, lon: Double?) {
        webView?.loadUrl("javascript:panTo(" + lat + "," + lon + ")")
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            //현재 내 위치를 GeoPoint로 리턴한다.
            locationManager?.removeUpdates(this)
            setPosition(location?.latitude, location?.longitude)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }
    }

    fun updateUI(loadAddress: String, address: String) {
        loadAddressTextView?.visibility = View.GONE
        addressTextView?.visibility = View.GONE

        if (loadAddress.isNotEmpty()) {
            loadAddressTextView?.visibility = View.VISIBLE
            loadAddressTextView?.text = loadAddress
        }

        if (address.isNotEmpty()) {
            addressTextView?.visibility = View.VISIBLE
            addressTextView?.text = address
        }
    }

    override fun onClick(v: View?) {
        val textView = v as TextView
        val address = textView.text.toString()
        listener?.onSelectedLocation(lat, lon, address)

        dismiss()
    }
}