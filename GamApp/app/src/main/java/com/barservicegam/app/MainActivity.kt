package com.barservicegam.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.barservicegam.app.BuildConfig
import com.barservicegam.app.R
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var callbackManager: CallbackManager

    fun loginLoginButton()
    {
//        val loginButton = findViewById<LoginButton>(R.id.login_button)
//        loginButton.setPermissions(listOf("public_profile", "email"))
//        // If you are using in a fragment, call loginButton.setFragment(this);
//
//        // Callback registration
//        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
//            override fun onSuccess(result: LoginResult?) {
//                Log.d("TAG", "Success Login")
//            }
//
//            override fun onCancel() {
//                Toast.makeText(this@MainActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
//            }
//
//            override fun onError(error: FacebookException?) {
//                Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_LONG).show()
//            }
//
//        })
    }

    fun loginLoginManager()
    {
        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
        }


        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(result: LoginResult?) {
                if(result != null) {
                    Log.d("letsSee", "Facebook token: " + result!!.accessToken.token)
                    getUserProfile(result?.accessToken, result?.accessToken?.userId)
                }
            }

            override fun onCancel() {
                Toast.makeText(this@MainActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FacebookSdk.fullyInitialize()
        callbackManager = CallbackManager.Factory.create()

        //loginLoginButton()
        loginLoginManager()

//        val btn_Login = findViewById<Button>(R.id.login_button)
//        btn_Login.setOnClickListener{
//            //val sharedPreference:DataPreference = DataPreference(this)
//            //sharedPreference.save("name", "Nguyen Ba Viet")
//
//            val intent = Intent(this@MainActivity, DetailActivity::class.java)
//            startActivity(intent)
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("letsSee", "malsehnnnnnn: " + data)
    }

    @SuppressLint("LongLogTag")
    fun getUserProfile(token: AccessToken?, userId: String?) {

        val parameters = Bundle()
        parameters.putString(
                "fields",
                "id, first_name, middle_name, last_name, name, picture, email"
        )
        GraphRequest(token,
                "/$userId/",
                parameters,
                HttpMethod.GET,
                GraphRequest.Callback { response ->
                    val jsonObject = response.jsonObject

                    // Facebook Access Token
                    // You can see Access Token only in Debug mode.
                    // You can't see it in Logcat using Log.d, Facebook did that to avoid leaking user's access token.
                    if (BuildConfig.DEBUG) {
                        FacebookSdk.setIsDebugEnabled(true)
                        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                    }

                    // Facebook Id
                    if (jsonObject.has("id")) {
                        val facebookId = jsonObject.getString("id")
                        Log.i("Facebook Id: ", facebookId.toString())
                    } else {
                        Log.i("Facebook Id: ", "Not exists")
                    }


                    // Facebook First Name
                    if (jsonObject.has("first_name")) {
                        val facebookFirstName = jsonObject.getString("first_name")
                        Log.i("Facebook First Name: ", facebookFirstName)
                    } else {
                        Log.i("Facebook First Name: ", "Not exists")
                    }


                    // Facebook Middle Name
                    if (jsonObject.has("middle_name")) {
                        val facebookMiddleName = jsonObject.getString("middle_name")
                        Log.i("Facebook Middle Name: ", facebookMiddleName)
                    } else {
                        Log.i("Facebook Middle Name: ", "Not exists")
                    }


                    // Facebook Last Name
                    if (jsonObject.has("last_name")) {
                        val facebookLastName = jsonObject.getString("last_name")
                        Log.i("Facebook Last Name: ", facebookLastName)
                    } else {
                        Log.i("Facebook Last Name: ", "Not exists")
                    }


                    // Facebook Name
                    if (jsonObject.has("name")) {
                        val facebookName = jsonObject.getString("name")
                        Log.i("Facebook Name: ", facebookName)
                    } else {
                        Log.i("Facebook Name: ", "Not exists")
                    }


                    // Facebook Profile Pic URL
                    if (jsonObject.has("picture")) {
                        val facebookPictureObject = jsonObject.getJSONObject("picture")
                        if (facebookPictureObject.has("data")) {
                            val facebookDataObject = facebookPictureObject.getJSONObject("data")
                            if (facebookDataObject.has("url")) {
                                val facebookProfilePicURL = facebookDataObject.getString("url")
                                Log.i("Facebook Profile Pic URL: ", facebookProfilePicURL)
                            }
                        }
                    } else {
                        Log.i("Facebook Profile Pic URL: ", "Not exists")
                    }

                    // Facebook Email
                    if (jsonObject.has("email")) {
                        val facebookEmail = jsonObject.getString("email")
                        Log.i("Facebook Email: ", facebookEmail)
                    } else {
                        Log.i("Facebook Email: ", "Not exists")
                    }
                }).executeAsync()
    }

    fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        return isLoggedIn
    }

    fun logoutFB()
    {
        LoginManager.getInstance().logOut()
    }

}