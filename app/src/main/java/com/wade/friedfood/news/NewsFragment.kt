package com.wade.friedfood.news

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
import com.wade.friedfood.databinding.FragmentNewsBinding
import com.wade.friedfood.ext.getVmFactory
import com.wade.friedfood.ext.toParcelableShop


class NewsFragment : Fragment() {


val viewModel by viewModels<NewsViewModel> { getVmFactory() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentNewsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        binding.recyclerNews.adapter = NewsAdapter(NewsAdapter.OnClickListener {

            viewModel.displayShopDetails(it)
        }, viewModel)

        viewModel.navigateToSelectedShop.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(
                        NavigationDirections.actionGlobalDetailFragment(
                            it.shop!!.toParcelableShop()
                        )
                    )
                viewModel.displayShopDetailsComplete()
            }
        })


        return binding.root
    }



}
