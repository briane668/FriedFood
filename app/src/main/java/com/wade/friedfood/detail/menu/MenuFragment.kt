package com.wade.friedfood.detail.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import com.wade.friedfood.databinding.FragmentMenuBinding

import com.wade.friedfood.detail.review.ReviewViewModel
import com.wade.friedfood.ext.getVmFactory

/**
 * A simple [Fragment] subclass.
 */
class MenuFragment : Fragment() {


    private val viewModel by viewModels<ReviewViewModel> {
        getVmFactory(
            MenuFragmentArgs.fromBundle(
                requireArguments()
            ).shopKey) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding = FragmentMenuBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel=viewModel


        binding.backImage3.setOnClickListener {
            findNavController().navigateUp()
        }



        binding.recyclerviewMenu.adapter = MenuAdapter()












        return binding.root
    }

}
