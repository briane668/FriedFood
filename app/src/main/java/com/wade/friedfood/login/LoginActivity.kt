package com.wade.friedfood.login

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import app.appworks.school.stylish.ext.getVmFactory
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.wade.friedfood.MainActivity
import com.wade.friedfood.R
import com.wade.friedfood.data.User
import com.wade.friedfood.databinding.ActivityLoginBinding
import com.wade.friedfood.network.LoadApiStatus
import com.wade.friedfood.util.UserManager.ProfileData
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class LoginActivity() : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    var googleSignInClient: GoogleSignInClient? = null
    var GOOGLE_LOGIN_CODE = 9001
    var callbackManager: CallbackManager? = null
    private val viewModel by viewModels<LoginViewModel> { getVmFactory() }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        google_sign_in_button.setOnClickListener {
            //First step
            googleLogin()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("298524614147-2p3a8qd9ebpbl2gt0s436u18s6ca034f.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        callbackManager = CallbackManager.Factory.create()

        viewModel.status.observe(this, androidx.lifecycle.Observer {
            it?.let {
                when(it) {
                    LoadApiStatus.LOADING -> {
                        binding.lottieLogin.visibility = View.VISIBLE

                    }
//                    LoadApiStatus.DONE -> moveMainPage(auth?.currentUser)
//                    else -> d(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing))
                }
            }
        })



        viewModel.signInSuccess.observe(this, androidx.lifecycle.Observer {
            if (it == 1) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }
        })
        permission()
    }

    override fun onStart() {
        super.onStart()

        moveMainPage(auth?.currentUser)
    }

    private fun googleLogin() {
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_LOGIN_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val account = result.signInAccount
                firebaseAuthWithGoogle(account)
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = User(
                        id = task.result?.user?.uid.toString(),
                        picture = task.result?.user?.photoUrl.toString(),
                        name = task.result?.user?.displayName.toString(),
                        email = task.result?.user?.email.toString()
                    )
                    //Login
                    moveMainPage(task.result?.user)
                } else {
                    //Show the error message
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            ProfileData.name = user.displayName.toString()
            ProfileData.picture = user.photoUrl.toString()
            ProfileData.email = user.email.toString()
            ProfileData.id = user.uid
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    private fun permission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ), 0
        )
    }


}