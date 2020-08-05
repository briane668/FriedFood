package com.wade.friedfood.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wade.friedfood.NavigationDirections

import com.wade.friedfood.R
import com.wade.friedfood.data.ParcelableShop
import com.wade.friedfood.databinding.FragmentProfileBinding
import com.wade.friedfood.ext.getVmFactory
import com.wade.friedfood.map.MapAdapter
import com.wade.friedfood.util.UserManager

class ProfileFragment : Fragment() {



    private val viewModel by viewModels<ProfileViewModel> { getVmFactory() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProfileBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.recyclerCollect.adapter = ProfileAdapter(ProfileAdapter.OnClickListener {

            viewModel.displayShopDetails(it)
        },viewModel)

        viewModel.counts.observe(viewLifecycleOwner, Observer {
            binding.commentCounts.text ="已發表過${viewModel.counts.value}則評論"

        })
        




        viewModel.navigateToSelectedShop.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                // Must find the NavController from the Fragment
                val parcelableShop = ParcelableShop(
                    id =it.id,
                    name = it.name,
                    latitude= it.location?.latitude,
                    longitude = it.location?.longitude,
                    image=it.image,
                    recommend=it.recommend,
                    star=it.star,
                    address=it.address,
                    menuImage =it.menuImage,
                    otherImage = it.otherImage,
                    comment= it.comment,
                    menu = it.menu,
                    phone= it.phone
                )




                this.findNavController()
                    .navigate(NavigationDirections.actionGlobalDetailFragment(parcelableShop))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayShopDetailsComplete()
            }
        })






        return binding.root


    }


}
