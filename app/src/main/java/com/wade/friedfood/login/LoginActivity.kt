package com.wade.friedfood.login

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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

    override fun onCreate(savedInstanceState: Bundle?) {


//        我要存token 然後傳進嗎?

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
//        email_login_button.setOnClickListener {
//            signinAndSignup()
//
//        }
        google_sign_in_button.setOnClickListener {
            //First step
            googleLogin()
        }
//        facebook_login_button.setOnClickListener {
//            //First step
//            facebookLogin()
//        }
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("298524614147-2p3a8qd9ebpbl2gt0s436u18s6ca034f.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        //printHashKey()
        callbackManager = CallbackManager.Factory.create()


        viewModel.noUser.observe(this, androidx.lifecycle.Observer {
            Log.d("timeToSign", "$it")

            it?.let {
                if (it == true) {
                    viewModel.coroutineScope.launch {
                        viewModel.login(ProfileData)
                    }
                } else if (it == false) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }else{

                }
            }
        }
        )


        viewModel.signInSuccess.observe(this,androidx.lifecycle.Observer{
            if (it == 1){


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

    fun printHashKey() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i("TAG", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("TAG", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("TAG", "printHashKey()", e)
        }

    }

    fun googleLogin() {
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
    }

    fun facebookLogin() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    //Second step
                    handleFacebookAccessToken(result?.accessToken)
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {

                }

            })
    }

    fun handleFacebookAccessToken(token: AccessToken?) {
        var credential = FacebookAuthProvider.getCredential(token?.token!!)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    //Third step
                    //Login
                    moveMainPage(task.result?.user)
                } else {
                    //Show the error message
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    //    這邊拿資料
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_LOGIN_CODE) {
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                var account = result.signInAccount
//                資料
//                    account.
                //Second step
                firebaseAuthWithGoogle(account)
            }
        }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        var credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Login
                    moveMainPage(task.result?.user)
                } else {
                    //Show the error message
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

//    fun signinAndSignup() {
//        auth?.createUserWithEmailAndPassword(
//            email_edittext.text.toString(),
//            password_edittext.text.toString()
//        )
//            ?.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    //Creating a user account
//                    moveMainPage(task.result?.user)
//                } else if (task.exception?.message.isNullOrEmpty()) {
//                    //Show the error message
//                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
//                } else {
//                    //Login if you have account
//                    signinEmail()
//                }
//            }
//    }

//    fun signinEmail() {
//        auth?.signInWithEmailAndPassword(
//            email_edittext.text.toString(),
//            password_edittext.text.toString()
//        )
//            ?.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    //Login
//                    moveMainPage(task.result?.user)
//                } else {
//                    //Show the error message
//                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
//                }
//            }
//    }

//    這邊拿資料

//    先寫個where equal 找有沒有這個使用者 沒有在家

    fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {


            ProfileData.name = user.displayName.toString()
            ProfileData.picture = user.photoUrl.toString()
            ProfileData.email = user.email.toString()
            ProfileData.id = user.uid
            viewModel.addAble(ProfileData)


        }
    }

    fun permission(){
        ActivityCompat.requestPermissions(this, arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE), 0)
    }



}