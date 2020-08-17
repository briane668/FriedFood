package com.wade.friedfood.detail.review

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.storage.FirebaseStorage
import com.wade.friedfood.NavigationDirections

import com.wade.friedfood.databinding.FragmentReviewBinding
import com.wade.friedfood.detail.DetailFragment
import com.wade.friedfood.ext.getVmFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_review.*
import java.io.File
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ReviewFragment : DialogFragment() {

    var uploadImage :String = ""


    lateinit var saveUri: Uri

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
            if (viewModel.comment.value == null) {
                Toast.makeText(context, "請輸入評論", Toast.LENGTH_SHORT).show()
            }else {

                viewModel.prepareSendReview(uploadImage)

            }


        }



        viewModel.sendSuccess.observe(viewLifecycleOwner, Observer {
            findNavController().navigateUp()
            Toast.makeText(context, "評價成功", Toast.LENGTH_SHORT).show()
            Log.d("sendSuccess","sendSuccess$it")
            it.apply {
                0
            }
        })


        binding.addPhoto.setOnClickListener {

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PHOTO_FROM_GALLERY)

        }

binding.camera.setOnClickListener {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    //外部定義變數

    val tmpFile = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        System.currentTimeMillis().toString() + ".jpg")
    val uriForCamera = FileProvider.getUriForFile(requireContext(), "com.wade.friedfood.fileprovider", tmpFile)

    //將 Uri 存進變數供後續 onActivityResult 使用
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForCamera)

    if (uriForCamera != null) {
        saveUri = uriForCamera
    }

    startActivityForResult(intent, PHOTO_FROM_CAMERA)


}


        return binding.root
    }


    private companion object {
        val PHOTO_FROM_GALLERY = 1
        val PHOTO_FROM_CAMERA = 2
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PHOTO_FROM_GALLERY -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {

                        saveUri = data!!.data!!
                        imageView2.setImageURI(saveUri)

                        uploadImage()

                    }
                    Activity.RESULT_CANCELED -> { }
                }
            }
            PHOTO_FROM_CAMERA -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        imageView2.setImageURI(saveUri)
                        uploadImage()

                    }
                    Activity.RESULT_CANCELED -> { }
                }
            }
        }
    }


    fun uploadImage() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        saveUri?.let { uri ->
            ref.putFile(uri)
                .addOnSuccessListener {
//                    upload_info_text.text = "上傳成功"

                    ref.downloadUrl.addOnSuccessListener {
                        uploadImage = it.toString()
                        Log.d("12345","$it")
                    }
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
//                    upload_progress.progress = progress

                    if (progress >= 100) {
                        reviewButton.isClickable= true

                    }else{
                        Toast.makeText(context, "照片上傳中", Toast.LENGTH_SHORT).show()
                        reviewButton.isClickable= false
                    }



                }
        }
    }



}
