package by.roadstatistics.ui

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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
import by.roadstatistics.database.firebase.FirebaseRepository
import by.roadstatistics.databinding.ItemLoginDialogBinding
import by.roadstatistics.services.LocService
import by.roadstatistics.ui.daysPart.DaysListFragment
import by.roadstatistics.ui.daysPart.pickedDay.PicketDayFragment
import by.roadstatistics.ui.mapPart.MapGeneralFragment
import by.roadstatistics.ui.settingsPart.SettingsFragment
import by.roadstatistics.utils.ChangeFragmentListener
import by.roadstatistics.utils.Constants.APP_START_COUNT
import by.roadstatistics.utils.Constants.APP_START_COUNT_KEY
import by.roadstatistics.utils.Constants.COLOR_KEY
import by.roadstatistics.utils.Constants.CURRENT_POLYLINE_COLOR
import by.roadstatistics.utils.Constants.FRAGMENT_PICKET_DAY
import by.roadstatistics.utils.Constants.MAP_LOOP
import by.roadstatistics.utils.Constants.MAP_LOOP_KEY
import by.roadstatistics.utils.Constants.USER_ID
import by.roadstatistics.utils.Constants.USER_ID_KEY
import by.roadstatistics.utils.MonthMapper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class NavMainActivity : AppCompatActivity(), ChangeFragmentListener {

    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var spinner: Spinner
    private val fireDatabase = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)
        setSupportActionBar(findViewById(R.id.toolbar_actionbar))
        setToolbarTitle(getString(R.string.fr_one_days_list))
        viewModelProvider = ViewModelProvider(this)
        spinner = findViewById(R.id.action_bar_spinner)
        getPreferences(MODE_PRIVATE).apply {
            MAP_LOOP = this.getFloat(MAP_LOOP_KEY, 11.0F)
            CURRENT_POLYLINE_COLOR = this.getInt(COLOR_KEY, R.color.black)
            APP_START_COUNT = this.getInt(APP_START_COUNT_KEY, 0) + 1
            USER_ID = this.getString(USER_ID_KEY, "0")!!
        }

        Log.i("FFFF", "USER_ID = $USER_ID")
        Log.i("FFFF", "APP_START_COUNT = $APP_START_COUNT")

        askLocationPermission()

        initSpinner(spinner)

        startService(Intent(this, LocService::class.java))

        beginFirstFragment()

        if (APP_START_COUNT == 1) {
            login()
        }

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

    private fun login() {
        val itemDialogBinding: ItemLoginDialogBinding = ItemLoginDialogBinding.inflate(
            LayoutInflater.from(this))
        AlertDialog.Builder(this).apply {
            setView(itemDialogBinding.root)
            setTitle("First run")
            setPositiveButton("Okay") { _, _ ->
                if (itemDialogBinding.namePoolEditText.text.toString().isNotEmpty()) {

                    val lastQuery: Query = fireDatabase.child("Users").orderByKey().limitToLast(1)
                    lastQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (ds in dataSnapshot.children) {
                                USER_ID = ds.child("id").value.toString()
                                Log.i("FFFF1", ds.child("id").value.toString())
                                Log.i("FFFF1", "Error, reboot app plz $USER_ID")
                                FirebaseRepository().putUserDoDatabase(ds.child("id").value.toString(), itemDialogBinding.namePoolEditText.text.toString())
                                ++APP_START_COUNT
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.i("FFFF", "Error, reboot app plz")
                        }
                    })
                }
            }
            setCancelable(false)
            create()
            show()
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
                viewModel.actualMonth(
                    parent?.getItemAtPosition(position).toString(),
                    applicationContext
                )
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
            FRAGMENT_PICKET_DAY -> {
                spinner.visibility = View.INVISIBLE
                supportFragmentManager.beginTransaction()
                    .replace<PicketDayFragment>(R.id.nav_host_fragment, "", bundle)
                    .addToBackStack(null)
                    .commit()
                setToolbarTitle(getString(R.string.fr_picket_day))
            }
        }
    }

    private fun askLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), 1000)
    }

    override fun onPause() {
        getPreferences(MODE_PRIVATE).edit().apply {
            putFloat(MAP_LOOP_KEY, MAP_LOOP)
            putInt(COLOR_KEY, CURRENT_POLYLINE_COLOR)
            putInt(APP_START_COUNT_KEY, APP_START_COUNT)
            putString(USER_ID_KEY, USER_ID)
        }.apply()
        super.onPause()
    }
}