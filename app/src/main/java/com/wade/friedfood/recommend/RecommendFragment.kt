package com.wade.friedfood.recommend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wade.friedfood.NavigationDirections
import com.wade.friedfood.ext.getVmFactory
import com.wade.friedfood.databinding.FragmentRecommendBinding
import com.wade.friedfood.detail.DetailFragmentArgs
import com.wade.friedfood.map.MapAdapter

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






        binding.recommendView.adapter= ShopAdapter(ShopAdapter.OnClickListener{
            viewModel.displayShopDetails(it)
        },viewModel)

        viewModel.navigateToSelectedShop.observe(viewLifecycleOwner, Observer {
            if ( it != null ) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(NavigationDirections.actionGlobalDetailFragment(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayShopDetailsComplete()
            }
        })





        return binding.root
    }
}
