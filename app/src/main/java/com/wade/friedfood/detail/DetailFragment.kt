package com.wade.friedfood.detail


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.wade.friedfood.NavigationDirections
import com.wade.friedfood.databinding.FragmentDetailBinding
import com.wade.friedfood.ext.getVmFactory
import com.wade.friedfood.getDistance
import com.wade.friedfood.map.MapViewModel
import com.wade.friedfood.util.UserManager.ProfileData
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class DetailFragment : Fragment() {


    private val viewModel by viewModels<DetailViewModel> {
        getVmFactory(
            DetailFragmentArgs
                .fromBundle(requireArguments()).shopKey
        )
    }
    private var map: GoogleMap? = null

    private val callback = OnMapReadyCallback { googleMap ->


        val x = viewModel.shop.value?.location?.latitude
        val y = viewModel.shop.value?.location?.longitude




        val sydney = LatLng(x!!, y!!)

//        this.map = googleMap
//        this.map!!.setMinZoomPreference(14.0f)
//
//
//        this.map!!.addMarker(MarkerOptions().position(sydney))
//
//        this.map!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        googleMap.setMinZoomPreference(15.0f)


        googleMap.addMarker(MarkerOptions().position(sydney))

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.backImage.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalMapsFragment())
        }

        binding.viewModel = viewModel

        binding.recommendButton.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalReviewFragment(viewModel.shop.value!!))

        }

        binding.menuBotton.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalMenuFragment(viewModel.shop.value!!))

        }


        binding.collectButton.setOnClickListener {

            viewModel.shop.value?.let { it1 -> viewModel.collectShop(ProfileData, it1) }

            Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show()
        }


        binding.otherImage.adapter = DetailAdapter()

        binding.recyclerDetailComment.adapter = DetailCommentAdapter()


        val x = MapViewModel.userPosition.value?.latitude
        val y = MapViewModel.userPosition.value?.longitude
        val r = viewModel.shop.value?.location?.latitude
        val s = viewModel.shop.value?.location?.longitude

        val m =
            getDistance(x ?: 0.toDouble(), y ?: 0.toDouble(), r ?: 0.toDouble(), s ?: 0.toDouble())
        val k = m.roundToInt()




        viewModel.coroutineScope.launch {

            val commentCount = viewModel.shop.value?.let { viewModel.getCommentsByShop(it) }


            binding.recommend.text = "$commentCount 則評論"
            binding.executePendingBindings()
        }

        viewModel.coroutineScope.launch {

            val rating = viewModel.shop.value?.let { viewModel.getRatingByShop(it) }

            if (rating != null) {
                binding.ratingBar2.rating = rating.toFloat()
            }

            binding.star.text = "$rating 顆星"

            binding.executePendingBindings()
        }

        binding.distance.text = "距離 ${k} 公尺"


        viewModel.comment.observe(viewLifecycleOwner, Observer {
            viewModel.sortComment(it)

        })





        binding.mapView2.onCreate(savedInstanceState)
        binding.mapView2.onResume();
        binding.mapView2.getMapAsync(callback)


        binding.view.setOnClickListener {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=$r,$s")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        binding.callButton.setOnClickListener {

                val intent = Intent(Intent.ACTION_DIAL,Uri.parse("tel:${viewModel.shop.value?.phone}"))
                startActivity(intent)

        }






        return binding.root

    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        (activity as AppCompatActivity).bottomNavView.visibility = View.GONE
//        super.onCreate(savedInstanceState)
//
//
//    }
//
////評價的頁面比較晚被destroy
//
//    override fun onDestroy() {
//
//        (activity as AppCompatActivity).bottomNavView.visibility = View.VISIBLE
//
//        super.onDestroy()
//    }


}
