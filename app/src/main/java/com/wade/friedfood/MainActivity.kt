package com.wade.friedfood


import android.location.Location
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.wade.friedfood.databinding.ActivityMainBinding
import kotlin.math.*


class MainActivity : AppCompatActivity() {


    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }


    private lateinit var binding: ActivityMainBinding


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                findNavController(this,R.id.myNavHostFragment).navigate(R.id.mapsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_recommend -> {

                findNavController(this,R.id.myNavHostFragment).navigate(R.id.recommendFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_menu -> {

                findNavController(this,R.id.myNavHostFragment).navigate(R.id.profileFragment)
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val db = FirebaseFirestore.getInstance()
//        db.collection("vender")
//            .get()
//            .addOnSuccessListener { documents ->







        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupBottomNav()



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.current_place_menu, menu)
        return true
    }
    private fun setupBottomNav() {
        binding.bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val menuView = binding.bottomNavView.getChildAt(0) as BottomNavigationMenuView
        val itemView = menuView.getChildAt(2) as BottomNavigationItemView
//        val bindingBadge = BadgeBottomBinding.inflate(LayoutInflater.from(this), itemView, true)
//        bindingBadge.lifecycleOwner = this
//        bindingBadge.viewModel = viewModel
    }

    companion object {
        private const val  EARTH_RADIUS = 6378.137
    }

    fun GetDistance(
        lat1: Double,
        lng1: Double,
        lat2: Double,
        lng2: Double
    ): Double {
        val radLat1: Double = rad(lat1)
        val radLat2: Double = rad(lat2)
        val a = radLat1 - radLat2
        val b: Double = rad(lng1) - rad(lng2)
        var s: Double = 2 * asin(
            sqrt(
                sin(a / 2).pow(2) +
                        cos(radLat1) * cos(radLat2) * sin(b / 2).pow(2)
            )
        )
        s *= EARTH_RADIUS

        s= ((s * 10000).roundToInt() /10000).toDouble()
        return s
    }




}


fun getDistance(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Double {
    val results = FloatArray(1)
    Location.distanceBetween(lat1, lon1, lat2, lon2, results)
    return results[0].toDouble()
}




    private const val  EARTH_RADIUS = 6378.137


fun GetDistance(
    lat1: Double,
    lng1: Double,
    lat2: Double,
    lng2: Double
): Double {
    val radLat1: Double = rad(lat1)
    val radLat2: Double = rad(lat2)
    val a = radLat1 - radLat2
    val b: Double = rad(lng1) - rad(lng2)
    var s: Double = 2 * asin(
        sqrt(
            sin(a / 2).pow(2) +
                    cos(radLat1) * cos(radLat2) * sin(b / 2).pow(2)
        )
    )
    s *= EARTH_RADIUS

    s= ((s * 10000).roundToLong() /10000).toDouble()
    return s
}





private  fun rad(d: Double): Double {
    return d * Math.PI / 180.0
}


