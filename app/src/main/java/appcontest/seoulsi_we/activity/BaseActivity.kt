package appcontest.seoulsi_we.activity

import android.support.v7.app.AppCompatActivity
import android.view.View

/**
 * Created by nam on 2018. 9. 4..
 */

open class BaseActivity : AppCompatActivity(){

    fun backImageClick(v: View) {
        finish()
    }
}