package com.mehul.videoapplication


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobile.client.results.SignInResult
import com.amazonaws.mobile.client.results.SignInState
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.internal.Constants
import com.amazonaws.services.s3.model.ListObjectsRequest
import com.amazonaws.services.s3.model.ObjectListing
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId


class AuthenticatorActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authenticator)
        try {
            AWSMobileClient.getInstance().initialize(
                applicationContext,
                object : Callback<UserStateDetails> {
                    override fun onResult(userStateDetails: UserStateDetails) {

                        when (userStateDetails.userState) {
                            UserState.SIGNED_IN -> runOnUiThread {

                                Toast.makeText(
                                    this@AuthenticatorActivity,
                                    "Logged IN",
                                    Toast.LENGTH_LONG
                                ).show()
                              //  AWSMobileClient.getInstance().signOut()
                            }
                            UserState.SIGNED_OUT -> runOnUiThread {
                                Toast.makeText(
                                    this@AuthenticatorActivity,
                                    "Logged OUT",
                                    Toast.LENGTH_LONG
                                ).show()


                            }
                            else -> AWSMobileClient.getInstance().signOut()
                        }
                    }

                    override fun onError(e: java.lang.Exception) {
                        Log.e("INIT", e.toString())
                    }
                })




            AWSMobileClient.getInstance().signIn(
                "admin",
                "Mehul1992",
                null,
                object : Callback<SignInResult> {
                    override fun onResult(signInResult: SignInResult) {
                        runOnUiThread {
                            Log.d(
                                TAG,
                                "Sign-in callback state: " + signInResult.signInState
                            )
                            when (signInResult.signInState) {
                                SignInState.DONE -> {
                                    makeToast("Sign-in done.")

                                    var oibj = AWSMobileClient.getInstance()
                                    Log.e(TAG, AWSMobileClient.getInstance().username)    //String
                                    Log.e(
                                        TAG,
                                        AWSMobileClient.getInstance().isSignedIn.toString()
                                    )       //Boolean
                                    Log.e(TAG, AWSMobileClient.getInstance().identityId)   //String
                                    Log.e(TAG,AWSMobileClient.getInstance().credentials.toString())
                                    Log.e(TAG, AWSMobileClient.getInstance().tokens.toString())
                                    Log.e(TAG, AWSMobileClient.getInstance().tokens.idToken.tokenString)
                                      //  .s3Client(AmazonS3Client(AWSMobileClient.getInstance()))

                                    getPinpointManager(applicationContext)

                                    startActivity(Intent(this@AuthenticatorActivity,MainActivity::class.java))

                                }
                                SignInState.SMS_MFA -> makeToast("Please confirm sign-in with SMS.")
                                SignInState.NEW_PASSWORD_REQUIRED -> makeToast("Please confirm sign-in with new password.")
                                else -> makeToast("Unsupported sign-in confirmation: " + signInResult.signInState)
                            }
                        }

                    }

                    override fun onError(e: java.lang.Exception) {
                        Log.e(TAG, "Sign-in error", e)
                    }
                })

            /*   AWSMobileClient.getInstance().signIn(
                   "mehul1992",
                   "Mehul1992",
                   null,
                   object : Callback<SignInResult> {
                       override fun onResult(signInResult: SignInResult) {
                           runOnUiThread {
                               Log.d(
                                   TAG,
                                   "Sign-in callback state: " + signInResult.signInState
                               )
                               when (signInResult.signInState) {
                                   SignInState.DONE -> makeToast("Sign-in done.")
                                   SignInState.SMS_MFA -> makeToast("Please confirm sign-in with SMS.")
                                   SignInState.NEW_PASSWORD_REQUIRED -> {
                                       makeToast("Please confirm sign-in with new password.")
                                      *//* requiredAttributes -> ["userAttributes.family_name","userAttributes.name","userAttributes.middle_name"]*//*
                                    val attributes: MutableMap<String, String> =
                                        HashMap()
                                    val attributesPw: MutableMap<String, String> =
                                        HashMap()

                                    attributes["userAttributes.family_name"] = "Panchal"
                                   attributes["userAttributes.name"] = "Mehul"
                                    attributes["userAttributes.middle_name"] = "M"
                                    attributes["NEW_PASSWORD"] = "Mayur1992"




                                    AWSMobileClient.getInstance().confirmSignIn(
                                        attributes,
                                        object :
                                            Callback<SignInResult> {
                                            override fun onResult(signInResult: SignInResult) {
                                                Log.d(
                                                    TAG,
                                                    "Sign-in callback state: " + signInResult.signInState
                                                )
                                                when (signInResult.signInState) {
                                                    SignInState.DONE -> makeToast("Sign-in done.")
                                                    SignInState.SMS_MFA -> makeToast("Please confirm sign-in with SMS.")
                                                    else -> makeToast("Unsupported sign-in confirmation: " + signInResult.signInState)
                                                }
                                            }

                                            override fun onError(e: java.lang.Exception) {
                                                Log.e(TAG, "Sign-in error", e)
                                            }
                                        })
                                }
                                else -> makeToast("Unsupported sign-in confirmation: " + signInResult.signInState)
                            }
                        }
                    }

                    override fun onError(e: java.lang.Exception) {
                        Log.e(TAG, "Sign-in error", e)
                    }
                })*/
        } catch (e: Exception) {

            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()

        }
    }

    fun makeToast(msg: String) {
        Toast.makeText(this@AuthenticatorActivity, msg, Toast.LENGTH_LONG).show()

    }



    companion object{
        private var pinpointManager: PinpointManager? = null
        var TAG = "AuthQuickStart"
        fun getPinpointManager(applicationContext: Context?): PinpointManager? {
            if (pinpointManager == null) {
                val awsConfig = AWSConfiguration(applicationContext)
                AWSMobileClient.getInstance().initialize(
                    applicationContext,
                    awsConfig,
                    object : Callback<UserStateDetails> {
                        override fun onResult(userStateDetails: UserStateDetails) {
                            Log.i("INIT", userStateDetails.userState.name)
                        }

                        override fun onError(e: java.lang.Exception) {
                            Log.e("INIT", "Initialization error.", e)
                        }
                    })
                val pinpointConfig = PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    awsConfig
                )
                pinpointManager = PinpointManager(pinpointConfig)

                FirebaseInstanceId.getInstance().instanceId
                    .addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.w(TAG, "getInstanceId failed", task.exception)
                            return@OnCompleteListener
                        }
                        val token = task.result!!.token
                        Log.d(TAG, "Registering push notifications token: $token")
                        try {
                            pinpointManager!!.notificationClient.registerDeviceToken(token)
                        }catch (e: java.lang.Exception){
                            Log.e(TAG, "ex : ${e.message}")

                        }
                    })
            }
            return pinpointManager
        }

    }
}
