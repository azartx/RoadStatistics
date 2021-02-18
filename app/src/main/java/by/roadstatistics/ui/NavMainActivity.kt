package by.roadstatistics.ui

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.add
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
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
import by.roadstatistics.utils.MonthMapper
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavMainActivity : AppCompatActivity(), ChangeFragmentListener {

    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var onSpinnerItemClickListener: SpinnerAdapter.OnSpinnerItemClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)
        setSupportActionBar(findViewById(R.id.toolbar_actionbar))
        setToolbarTitle("Список дней")
        viewModelProvider = ViewModelProvider(this)
        val spinner: Spinner = findViewById(R.id.action_bar_spinner)

        askLocationPermission()

        onSpinnerItemClickListener = object : SpinnerAdapter.OnSpinnerItemClickListener {
            override fun onSpinnerItemClick(month: String) {
                Log.i("FFFF", month)
                //viewModel.actualMonth(month, applicationContext)
            }
        }

        initSpinner(spinner)


        startService(Intent(this, LocService::class.java))

        beginFirstFragment()

        val bn = findViewById<BottomNavigationView>(R.id.nav_view)


        bn.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_days -> {
                    initSpinner(spinner)
                    spinner.visibility = View.VISIBLE
                    supportFragmentManager.beginTransaction()
                        .replace<DaysListFragment>(R.id.nav_host_fragment, "", null)
                        .commit()
                    setToolbarTitle("Список дней")
                    true
                }
                R.id.nav_map_global -> {
                    spinner.visibility = View.INVISIBLE
                    supportFragmentManager.beginTransaction()
                        .replace<MapGeneralFragment>(R.id.nav_host_fragment, "", null)
                        .addToBackStack(null)
                        .commit()
                    setToolbarTitle("Карта")
                    true
                }
                R.id.nav_settings -> {
                    spinner.visibility = View.INVISIBLE
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

    private fun beginFirstFragment() {
        //Bundle().putParcelableArrayList()


        supportFragmentManager.beginTransaction()
            .add<DaysListFragment>(R.id.nav_host_fragment, "")
            .commit()
    }

    private fun initSpinner(spinner: Spinner) {
        viewModelProvider.get(ActivityViewModel::class.java).also { viewModel ->
            viewModel.monthListLiveData.observe(this, { list ->
                val monthsString = MonthMapper(list, applicationContext).monthsToString()



                val spinnerAdapter = SpinnerAdapter(this, R.id.dayOfWeek, monthsString, onSpinnerItemClickListener)
                spinner.adapter = spinnerAdapter

            })
            viewModel.getMonthList(applicationContext)
        }
    }

    private fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onFragmentChange(fragmentId: Int, bundle: Bundle?) {
        /*when (fragmentId) {
            FRAGMENT_DAYS_LIST -> supportFragmentManager.beginTransaction()
                .replace<DaysListFragment>(R.id.nav_host_fragment, "", bundle).commit()
            FRAGMENT_MAP_GENERAL -> supportFragmentManager.beginTransaction()
                .replace<GlobalMapFragment>(R.id.nav_host_fragment, "", bundle).commit()
            FRAGMENT_SETTINGS -> supportFragmentManager.beginTransaction()
                .replace<SettingsFragment>(R.id.nav_host_fragment, "", bundle).commit()

        }*/
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.left_toolbar_menu, menu)
        return true
    }*/

    private fun askLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), 1000)
    }

}