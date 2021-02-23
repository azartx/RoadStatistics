package by.roadstatistics.ui

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
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
import by.roadstatistics.ui.daysPart.pickedDay.PicketDayFragment
import by.roadstatistics.ui.fragments.GlobalMapFragment
import by.roadstatistics.ui.mapPart.MapGeneralFragment
import by.roadstatistics.ui.settingsPart.SettingsFragment
import by.roadstatistics.utils.ChangeFragmentListener
import by.roadstatistics.utils.Constants.CURRENT_MONTH
import by.roadstatistics.utils.Constants.FRAGMENT_DAYS_LIST
import by.roadstatistics.utils.Constants.FRAGMENT_MAP_GENERAL
import by.roadstatistics.utils.Constants.FRAGMENT_PICKET_DAY
import by.roadstatistics.utils.Constants.FRAGMENT_SETTINGS
import by.roadstatistics.utils.Constants.MAP_LOOP
import by.roadstatistics.utils.Constants.MAP_LOOP_KEY
import by.roadstatistics.utils.MonthMapper
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavMainActivity : AppCompatActivity(), ChangeFragmentListener {

    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)
        setSupportActionBar(findViewById(R.id.toolbar_actionbar))
        setToolbarTitle(getString(R.string.fr_one_days_list))
        viewModelProvider = ViewModelProvider(this)
        spinner = findViewById(R.id.action_bar_spinner)
        MAP_LOOP = getPreferences(MODE_PRIVATE).getFloat(MAP_LOOP_KEY, 11.0F)

        askLocationPermission()

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
                    setToolbarTitle(getString(R.string.fr_one_days_list))
                    true
                }
                R.id.nav_map_global -> {
                    spinner.visibility = View.INVISIBLE
                    supportFragmentManager.beginTransaction()
                        .replace<MapGeneralFragment>(R.id.nav_host_fragment, "", null)
                        .addToBackStack(null)
                        .commit()
                    setToolbarTitle(getString(R.string.fr_map))
                    true
                }
                R.id.nav_settings -> {
                    spinner.visibility = View.INVISIBLE
                    supportFragmentManager.beginTransaction()
                        .replace<SettingsFragment>(R.id.nav_host_fragment, "", null)
                        .addToBackStack(null)
                        .commit()
                    setToolbarTitle(getString(R.string.fr_settings))
                    true
                }
                else -> false
            }
        }

    }

    private fun spinnerItemSelectedListener(spinner: Spinner, viewModel: ActivityViewModel) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.actualMonth(parent?.getItemAtPosition(position).toString(), applicationContext)
                Log.i("FFFF", parent?.getItemAtPosition(position).toString() + " yesss")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }
        }
    }

    private fun beginFirstFragment() {
        supportFragmentManager.beginTransaction()
            .add<DaysListFragment>(R.id.nav_host_fragment, "")
            .commit()
    }

    private fun initSpinner(spinner: Spinner) {
        viewModelProvider.get(ActivityViewModel::class.java).also { viewModel ->
            viewModel.monthListLiveData.observe(this, { list ->
                val monthsString = MonthMapper(list, applicationContext).monthsToString()
                val spinnerAdapter = SpinnerAdapter(this, R.id.dayOfWeek, monthsString)
                spinner.adapter = spinnerAdapter

                spinnerItemSelectedListener(spinner, viewModel)

            })
            viewModel.getMonthList(applicationContext)
        }
    }

    private fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onFragmentChange(fragmentId: Int, bundle: Bundle?) {
        when (fragmentId) {
            /*FRAGMENT_DAYS_LIST -> supportFragmentManager.beginTransaction()
                .replace<DaysListFragment>(R.id.nav_host_fragment, "", bundle).commit()
            FRAGMENT_MAP_GENERAL -> supportFragmentManager.beginTransaction()
                .replace<GlobalMapFragment>(R.id.nav_host_fragment, "", bundle).commit()
            FRAGMENT_SETTINGS -> supportFragmentManager.beginTransaction()
                .replace<SettingsFragment>(R.id.nav_host_fragment, "", bundle).commit()*/
            FRAGMENT_PICKET_DAY -> {
                supportFragmentManager.beginTransaction()
                    .replace<PicketDayFragment>(R.id.nav_host_fragment, "", bundle)
                    .addToBackStack(null)
                    .commit()
                setToolbarTitle(getString(R.string.fr_picket_day))
            }

        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.left_toolbar_menu, menu)
        return true
    }*/

    private fun askLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), 1000)
    }

    override fun onPause() {
        super.onPause()
        getPreferences(MODE_PRIVATE).edit().apply {
            putFloat(MAP_LOOP_KEY, MAP_LOOP).apply()
        }
    }
}