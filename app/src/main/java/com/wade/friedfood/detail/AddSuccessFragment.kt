package com.wade.friedfood.detail

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.wade.friedfood.R



/**
 * A simple [Fragment] subclass.
 * Use the [AddSuccessFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddSuccessFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val success= Runnable { dismiss() }
        val hand= Handler()
        hand.postDelayed(success,2000)


        return inflater.inflate(R.layout.fragment_add_success, container, false)
    }


}
