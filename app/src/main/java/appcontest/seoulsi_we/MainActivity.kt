package appcontest.seoulsi_we

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast

/**
 * Created by nam on 2018. 8. 7..
 */

class MainActivity : AppCompatActivity(){
    private var mDrawer: DrawerLayout? = null
    private var toolbar: Toolbar? = null
    private var nvDrawer: NavigationView? = null

    private var drawerToggle: ActionBarDrawerToggle? = null     // 메뉴 버튼

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 액션바 등록
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        mDrawer = findViewById(R.id.drawer_layout)
        drawerToggle = setupDrawerToggle()
        mDrawer!!.addDrawerListener(drawerToggle!!)

        // 슬라이드 뷰 등록
        nvDrawer = findViewById(R.id.nvView)
        setupDrawerContent(nvDrawer)
    }

    //region 슬라이드 메뉴 관련 메소드

    private fun setupDrawerContent(navigationView: NavigationView?) {
        navigationView!!.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }

    private fun selectDrawerItem(menuItem: MenuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        //        Fragment fragment = null;
        //        Class fragmentClass;

        // 각 슬라이드 메뉴의 버튼을 클릭하면 이벤트 발생
        when (menuItem.itemId) {
            R.id.about_seoulsiwi          // 서울시위란? 버튼
            -> Toast.makeText(this@MainActivity, getString(R.string.about_seoulsiwi), Toast.LENGTH_SHORT).show()
            R.id.about_demo               // 집회시위 관련정보 버튼
            -> Toast.makeText(this@MainActivity, getString(R.string.about_demo), Toast.LENGTH_SHORT).show()
            R.id.promotion_demo           // 집회시위 홍보하기 버튼
            -> Toast.makeText(this@MainActivity, getString(R.string.promotion_demo), Toast.LENGTH_SHORT).show()
        }

        //        try {
        //            fragment = (Fragment) fragmentClass.newInstance();
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        //
        //        // Insert the fragment by replacing any existing fragment
        //        FragmentManager fragmentManager = getSupportFragmentManager();
        //        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        //
        //        // Highlight the selected item has been done by NavigationView
        //        menuItem.setChecked(true);
        //        // Set action bar title
        //        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer!!.closeDrawers()
    }

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

}