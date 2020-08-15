package com.wade.friedfood.detail


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import app.appworks.school.stylish.ext.toShop
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.wade.friedfood.NavigationDirections
import com.wade.friedfood.R
import com.wade.friedfood.data.ParcelableShop
import com.wade.friedfood.data.Shop
import com.wade.friedfood.data.source.remote.PublisherRemoteDataSource
import com.wade.friedfood.databinding.FragmentDetailBinding
import com.wade.friedfood.ext.getVmFactory
import com.wade.friedfood.getDistance
import com.wade.friedfood.map.MapViewModel
import com.wade.friedfood.util.UserManager.ProfileData
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.math.roundToInt


class DetailFragment : Fragment() {


    private val viewModel by viewModels<DetailViewModel> {
        getVmFactory(
            DetailFragmentArgs
                .fromBundle(requireArguments()).shopKey
        )
    }

    private val callback = OnMapReadyCallback { googleMap ->


        val x = viewModel.shop.value?.latitude
        val y = viewModel.shop.value?.longitude
        val sydney = LatLng(x!!, y!!)
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

        binding.viewModel = viewModel

        binding.backImage.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.recommendButton.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalReviewFragment(viewModel.shop.value!!))
        }

        binding.menuBotton.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalMenuFragment(viewModel.shop.value!!))
        }


        binding.collectButton.setOnClickListener {

            viewModel.shop.value?.let {

                val shop: Shop = it.toShop()

                viewModel.collectShop(ProfileData, shop)
            }
        }

        viewModel.collectDone.observe(viewLifecycleOwner, Observer {
            if (it == 1) {
                viewModel._userData.value?.let { it1 -> viewModel.getUserData(it1) }
                findNavController().navigate(R.id.addSuccessFragment)
            }
        })


        binding.otherImage.adapter = DetailAdapter()

        binding.recyclerDetailComment.adapter = DetailCommentAdapter(viewModel)

        val x = MapViewModel.userPosition.value?.latitude
        val y = MapViewModel.userPosition.value?.longitude
        val r = viewModel.shop.value?.latitude
        val s = viewModel.shop.value?.longitude

        val m =
            getDistance(x ?: 0.toDouble(), y ?: 0.toDouble(), r ?: 0.toDouble(), s ?: 0.toDouble())
        val distance = m.roundToInt()

        binding.distance.text = "距離 ${distance} 公尺"


        viewModel.coroutineScope.launch {
            val commentCount = viewModel.shop.value?.let {
                val shop: Shop = it.toShop()
                viewModel.getCommentsByShop(shop)
            }
            binding.recommend.text = "$commentCount 則評論"
            binding.executePendingBindings()
        }

        viewModel.coroutineScope.launch {
            val rating = viewModel.shop.value?.let {
                val shop: Shop = it.toShop()
                viewModel.getRatingByShop(shop)
            }
            if (rating != null) {
                binding.ratingBar2.rating = rating.toFloat()
            }
            binding.executePendingBindings()
        }


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

            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${viewModel.shop.value?.phone}"))
            startActivity(intent)

        }


        viewModel._userData.observe(viewLifecycleOwner, Observer {
            viewModel.collectAble(it)

        })




        return binding.root

    }


    override fun onResume() {
        viewModel.shop.value?.let { viewModel.getComments(it) }
        super.onResume()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).bottomNavView.visibility = View.GONE
        super.onCreate(savedInstanceState)


    }


    override fun onDestroy() {

        (activity as AppCompatActivity).bottomNavView.visibility = View.VISIBLE

        super.onDestroy()
    }


    fun intent2FriendList(message: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse("line://msg/text/?$message")
        startActivity(intent)
    }
//


}


