package com.wade.friedfood.detail


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import app.appworks.school.publisher.util.Logger
import com.wade.friedfood.NavigationDirections

import com.wade.friedfood.databinding.FragmentDetailBinding
import com.wade.friedfood.ext.getVmFactory
import com.wade.friedfood.getDistance
import com.wade.friedfood.map.MapViewModel
import com.wade.friedfood.util.UserManager.ProfileData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class DetailFragment : Fragment() {


    private val viewModel by viewModels<DetailViewModel> {
        getVmFactory(
            DetailFragmentArgs
                .fromBundle(requireArguments()).shopKey
        )
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

        val m= getDistance(x ?: 0.toDouble(), y ?: 0.toDouble(), r ?: 0.toDouble(), s ?: 0.toDouble())
        val k = m.roundToInt()




        viewModel.coroutineScope.launch {

            val commentCount = viewModel.shop.value?.let { viewModel.getCommentsByShop(it) }


            binding.recommend.text="$commentCount 則評論"
            binding.executePendingBindings()
        }

        viewModel.coroutineScope.launch {

            val rating = viewModel.shop.value?.let { viewModel.getRatingByShop(it) }

            if (rating != null) {
                binding.ratingBar2.rating =rating.toFloat()
            }

                binding.star.text="$rating 顆星"

            binding.executePendingBindings()
        }

        binding.distance.text = "距離 ${k} 公尺"


        viewModel.comment.observe(viewLifecycleOwner, Observer {
            viewModel.sortComment(it)

        })











        return binding.root

    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        (activity as AppCompatActivity).bottomNavView.visibility = View.GONE
//        super.onCreate(savedInstanceState)
//
//
//    }
//
//
//    override fun onDestroy() {
//
//        (activity as AppCompatActivity).bottomNavView.visibility = View.VISIBLE
//
//        super.onDestroy()
//    }


}
