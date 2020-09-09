package com.wade.friedfood


import android.location.Location
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wade.friedfood.databinding.ActivityMainBinding
import com.wade.friedfood.ext.getVmFactory
import com.wade.friedfood.map.MapsFragmentDirections
import com.wade.friedfood.profile.ProfileViewModel
import com.wade.friedfood.util.UserManager


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> { getVmFactory() }




    private lateinit var binding: ActivityMainBinding


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                findNavController(this,R.id.NavHostFragment).navigate(MapsFragmentDirections.actionGlobalMapsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_recommend -> {

                findNavController(this,R.id.NavHostFragment).navigate(NavigationDirections.actionGlobalRecommendFragment())
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_profile -> {

                findNavController(this,R.id.NavHostFragment).navigate(NavigationDirections.actionGlobalProfileFragment())
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_news -> {

                findNavController(this,R.id.NavHostFragment).navigate(NavigationDirections.actionGlobalNewsFragment())
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel.login(UserManager.ProfileData)





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








