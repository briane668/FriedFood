package com.wade.friedfood.detail


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wade.friedfood.NavigationDirections

import com.wade.friedfood.databinding.FragmentDetailBinding
import com.wade.friedfood.ext.getVmFactory
import kotlinx.android.synthetic.main.activity_main.*

class DetailFragment : Fragment() {


    private val viewModel by viewModels<DetailViewModel> {
        getVmFactory(
            DetailFragmentArgs.fromBundle(
                requireArguments()
            ).shopKey
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.backImage.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.star.text = "${viewModel.shop.value?.star}顆星"
        binding.viewModel = viewModel

        binding.recommendButton.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalReviewFragment())

        }

        binding.otherImage.adapter = DetailAdapter()
        binding.recyclerDetailComment.adapter = DetailCommentAdapter()


        return binding.root

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).bottomNavView.visibility = View.GONE
        super.onCreate(savedInstanceState)


    }


    override fun onDestroy() {

        (activity as AppCompatActivity).bottomNavView.visibility = View.VISIBLE

        super.onDestroy()
    }


}
