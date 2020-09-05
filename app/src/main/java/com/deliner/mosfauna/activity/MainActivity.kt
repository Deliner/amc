package com.deliner.mosfauna.activity

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.deliner.mosfauna.R
import com.deliner.mosfauna.fragment.GuideFragment
import com.deliner.mosfauna.fragment.ScoreFragment
import com.deliner.mosfauna.system.CoreConst
import com.deliner.mosfauna.utils.LoginManager
import com.deliner.mosfauna.utils.StaticHandler
import com.deliner.mosfauna.utils.Utils
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.materialdrawer.iconics.iconicsIcon
import com.mikepenz.materialdrawer.iconics.withIcon
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.*
import com.mikepenz.materialdrawer.util.addStickyDrawerItems
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : CommonActivity() {
    private lateinit var slider: MaterialDrawerSliderView

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var headerView: AccountHeaderView

    private var currentPage: PageTypes? = null

    override fun handleServiceMessage(msg: Message) {
        when (msg.what) {
            CoreConst.ON_LOGOUT -> {
                LoginManager.getInstance(applicationContext).deleteUser()
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(Intent(this, GreetActivity::class.java))
            }
            else -> super.handleServiceMessage(msg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setHomeButtonEnabled(true)

        slider = findViewById(R.id.activity_main_drawer)

        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            root,
            findViewById(R.id.toolbar),
            com.mikepenz.materialdrawer.R.string.material_drawer_open,
            com.mikepenz.materialdrawer.R.string.material_drawer_close
        )
        root.addDrawerListener(actionBarDrawerToggle)

        initSlider()

        attachFragment(PageTypes.GUIDE)
    }

    private fun initSlider() {
        val user = LoginManager.getInstance(applicationContext).getCurrentUser()!!
        val profile = ProfileDrawerItem().apply {
            nameText = user.login; descriptionText = user.mail; identifier = 100
        }

        headerView = AccountHeaderView(this).apply {
            attachToSliderView(slider)
            addProfiles(
                profile,
//                ProfileSettingDrawerItem().apply {
//                    nameText = "Выйти";
//                    iconicsIcon = GoogleMaterial.Icon.gmd_delete;
//                    identifier = 1001
//                }
            )
            onAccountHeaderListener = { _, profile, _ ->
                if (profile.identifier == 1001L) {
                    Message.obtain(handler, CoreConst.ON_LOGOUT).sendToTarget()
                }
                false
            }
        }

        slider.apply {
            itemAdapter.add(
                PrimaryDrawerItem().withIdentifier(1).withName("Гид")
                    .withIcon(GoogleMaterial.Icon.gmd_home),
                PrimaryDrawerItem().withIdentifier(2).withName("Рейтинг")
                    .withIcon(GoogleMaterial.Icon.gmd_show_chart),
            )
            addStickyDrawerItems(
                SecondaryDrawerItem().withName("Cвязаться с разработчиками")
                    .withIcon(GoogleMaterial.Icon.gmd_message).withIdentifier(6),
            )
            selectedItemIdentifier = 1
        }

        slider.onDrawerItemClickListener = { _, drawerItem, _ ->
            when (drawerItem.identifier) {
                1L -> {
                    attachFragment(PageTypes.GUIDE)
                    supportActionBar!!.title = "ГИД"
                }
                2L -> {
                    attachFragment(PageTypes.SCORE)
                    supportActionBar!!.title = "РЕЙТИНГ"
                }
                6L -> Utils.shareTextToEmail(
                    this,
                    arrayOf("deliner.work@gmail.com"),
                    "MOSFAUNA",
                    ""
                )
            }
            false
        }
    }

    private fun attachFragment(type: PageTypes) {
        if (currentPage != type) {
            val fragment = when (type) {
                PageTypes.GUIDE -> GuideFragment()
                PageTypes.SCORE -> ScoreFragment()
            }
            updateFragment(fragment)
            currentPage = type
        }
    }

    private fun updateFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_main_fragment_holder, fragment)
            .commit()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        actionBarDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onPause() {
        handler.dropCallback(this)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        actionBarDrawerToggle.syncState()
        handler.setCallback(this)
    }

    override fun onSaveInstanceState(_outState: Bundle) {
        var outState = _outState
        outState = slider.saveInstanceState(outState)
        outState = headerView.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (root.isDrawerOpen(slider)) {
            root.closeDrawer(slider)
        } else {
            super.onBackPressed()
        }
    }

    private enum class PageTypes { GUIDE, SCORE }

    companion object {
        private val handler = StaticHandler()
    }
}