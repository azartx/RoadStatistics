package by.roadstatistics.ui

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.add
import androidx.fragment.app.replace
import by.roadstatistics.R
import by.roadstatistics.adapters.SpinnerAdapter
import by.roadstatistics.services.LocService
import by.roadstatistics.ui.daysPart.DaysListFragment
import by.roadstatistics.ui.fragments.GlobalMapFragment
import by.roadstatistics.ui.mapPart.MapGeneralFragment
import by.roadstatistics.ui.settingsPart.SettingsFragment
import by.roadstatistics.utils.ChangeFragmentListener
import by.roadstatistics.utils.Constants.FRAGMENT_DAYS_LIST
import by.roadstatistics.utils.Constants.FRAGMENT_MAP_GENERAL
import by.roadstatistics.utils.Constants.FRAGMENT_SETTINGS
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavMainActivity : AppCompatActivity(), ChangeFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)
        setSupportActionBar(findViewById(R.id.toolbar_actionbar))
        setToolbarTitle("Список дней")

        askLocationPermission()

        val lad = SpinnerAdapter(
            context = this,
            R.id.dayOfWeek,
            listOf("qwer", "qwer", "qwer", "qasdwer")
        )
        findViewById<Spinner>(R.id.action_bar_spinner).adapter = lad

        lad.notifyDataSetChanged()

        val inte = Intent(this, LocService::class.java)
            startService(inte)




        supportFragmentManager.beginTransaction()
            .add<DaysListFragment>(R.id.nav_host_fragment)
            .commit()

        val bn = findViewById<BottomNavigationView>(R.id.nav_view)

        bn.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_days -> {
                    supportFragmentManager.beginTransaction()
                        .replace<DaysListFragment>(R.id.nav_host_fragment, "", null)
                        .commit()
                    setToolbarTitle("Список дней")
                    true
                }
                R.id.nav_map_global -> {
                    supportFragmentManager.beginTransaction()
                        .replace<MapGeneralFragment>(R.id.nav_host_fragment, "", null)
                        .addToBackStack(null)
                        .commit()
                    setToolbarTitle("Карта")
                    true
                }
                R.id.nav_settings -> {
                    supportFragmentManager.beginTransaction()
                        .replace<SettingsFragment>(R.id.nav_host_fragment, "", null)
                        .addToBackStack(null)
                        .commit()
                    setToolbarTitle("Настройки")
                    true
                }
                else -> false
            }
        }

    }

    private fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onFragmentChange(fragmentId: Int, bundle: Bundle?) {
        when (fragmentId) {
            FRAGMENT_DAYS_LIST -> supportFragmentManager.beginTransaction()
                .replace<DaysListFragment>(R.id.nav_host_fragment, "", bundle).commit()
            FRAGMENT_MAP_GENERAL -> supportFragmentManager.beginTransaction()
                .replace<GlobalMapFragment>(R.id.nav_host_fragment, "", bundle).commit()
            FRAGMENT_SETTINGS -> supportFragmentManager.beginTransaction()
                .replace<SettingsFragment>(R.id.nav_host_fragment, "", bundle).commit()

        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.left_toolbar_menu, menu)
        return true
    }*/

    private fun askLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), 1000)
    }


}