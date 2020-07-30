package com.wade.friedfood.detail.review

import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wade.friedfood.NavigationDirections

import com.wade.friedfood.databinding.FragmentReviewBinding
import com.wade.friedfood.ext.getVmFactory
import kotlinx.android.synthetic.main.activity_main.*

/**
 * A simple [Fragment] subclass.
 */
class ReviewFragment : DialogFragment() {

    private val viewModel by viewModels<ReviewViewModel> {
        getVmFactory(ReviewFragmentArgs
            .fromBundle(requireArguments()).shopkey) }

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

            viewModel.prepareSendReview()

        }

        viewModel.reviewFinish.observe(viewLifecycleOwner, Observer {
            if (it){
                dismiss()
            }
        })

//navigateup 不會重新創造fragment
//        viewModel.sendSuccess.observe(viewLifecycleOwner, Observer {
//            findNavController().navigate(NavigationDirections.actionGlobalDetailFragment(viewModel.shop.value!!))
//            Toast.makeText(context, "評價成功", Toast.LENGTH_SHORT).show()
//            Log.d("sendSuccess","sendSuccess$it")
//            it.apply {
//                0
//            }
//        })



        return binding.root
    }


//
//        override fun onCreate(savedInstanceState: Bundle?) {
//        (activity as AppCompatActivity).bottomNavView.visibility = View.GONE
//        super.onCreate(savedInstanceState)
//
//
//    }
//
//
//    override fun onDestroy() {
//
//        (activity as AppCompatActivity).bottomNavView.visibility = View.VISIBLE
//
//        super.onDestroy()
//    }

}
