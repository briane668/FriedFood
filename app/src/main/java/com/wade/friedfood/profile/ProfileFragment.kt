package com.wade.friedfood.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wade.friedfood.ext.toParcelableShop
import com.wade.friedfood.NavigationDirections

import com.wade.friedfood.databinding.FragmentProfileBinding
import com.wade.friedfood.ext.getVmFactory

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
                it.toParcelableShop()
                this.findNavController()
                    .navigate(NavigationDirections.actionGlobalDetailFragment(it.toParcelableShop()))
                viewModel.displayShopDetailsComplete()
            }
        })






        return binding.root


    }


}
