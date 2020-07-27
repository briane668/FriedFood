package com.wade.friedfood.recommend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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


//    這整個是一個funtion 最終被return後 才會運作
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRecommendBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel




        binding.spinner .onItemSelectedListener=
            object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> {
                        }
                        1 -> {
                            viewModel.sortShopByRate(viewModel.shop)
                            binding.recommendView.smoothScrollToPosition(0)
                        }
                        else -> {
                            viewModel.sortShopByComment(viewModel.shop)
                            binding.recommendView.smoothScrollToPosition(0)
                        }
                    }
                }


            }


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



    viewModel._shop.observe(viewLifecycleOwner, Observer {

        viewModel.addRatingComment(viewModel._shop)

    })








        return binding.root
    }
}
