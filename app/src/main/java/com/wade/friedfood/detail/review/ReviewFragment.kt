package com.wade.friedfood.detail.review

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.wade.friedfood.databinding.FragmentReviewBinding
import com.wade.friedfood.ext.getVmFactory

/**
 * A simple [Fragment] subclass.
 */
class ReviewFragment : DialogFragment() {

    private val viewModel by viewModels<ReviewViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragm

        val binding = FragmentReviewBinding.inflate(inflater, container, false)


        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel=viewModel

        binding.backImage.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.editText.isSingleLine = false

        binding.reviewStar1.setOnClickListener {
            viewModel.rating.value = 1
        }
        binding.reviewStar2.setOnClickListener {
            viewModel.rating.value = 2
        }
        binding.reviewStar3.setOnClickListener {
            viewModel.rating.value = 3
        }
        binding.reviewStar4.setOnClickListener {
            viewModel.rating.value = 4
        }
        binding.reviewStar5.setOnClickListener {
            viewModel.rating.value = 5
        }


//        傳進點下去button的位子
        binding.reviewButton.setOnClickListener {

        }

        viewModel.reviewFinish.observe(viewLifecycleOwner, Observer {
            if (it){
                dismiss()
            }
        })


        return binding.root
    }

}
