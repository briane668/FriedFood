package com.wade.friedfood.detail


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels

import com.wade.friedfood.R
import com.wade.friedfood.databinding.FragmentDetailBinding
import com.wade.friedfood.ext.getVmFactory

class DetailFragment : Fragment() {



    private val viewModel by viewModels<DetailViewModel> { getVmFactory(DetailFragmentArgs.fromBundle(requireArguments()).shopKey) }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this


        binding.star.text="${viewModel.shop.value?.star}顆星"
        binding.viewModel=viewModel

        binding.otherImage.adapter= DeatailAdapter()



        return binding.root

    }



}
