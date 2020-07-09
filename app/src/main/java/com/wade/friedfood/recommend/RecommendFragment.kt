package com.wade.friedfood.recommend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wade.friedfood.ext.getVmFactory
import com.wade.friedfood.databinding.FragmentRecommendBinding
import com.wade.friedfood.detail.DetailFragmentArgs

class RecommendFragment : Fragment() {

    private val viewModel by viewModels<RecommendViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRecommendBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.recommendView.adapter = ShopAdapter()





        return binding.root
    }
}
