package com.main.app

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.barservicegam.app.BuildConfig
import com.barservicegam.app.R
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.fragmentcustom.*
import com.fragula.Navigator
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lib.*
import com.lib.eventbus.EventBusFire
import data.DataPreference
import data.PREFERENCE
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    //google
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    //phone
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    lateinit var callbackManager: CallbackManager

    lateinit var navigatorTabNews: Navigator
    lateinit var navigatorTabVideos: Navigator
    lateinit var navigatorTabExplore: Navigator
    lateinit var navigatorTabSetting: Navigator

    lateinit var layoutParent: ConstraintLayout
    lateinit var bottomView: BottomNavigationView

    var homePagerFragment = HomePagerFragment.newInstance("", "")
    var listVideoFragment = VideoPagerFragment.newInstance("", "")
    var exploreFragment = AutoSearchFragment.newInstance("", "")
    var settingFragment = SettingFragment.newInstance("", "")

    fun actionBack() {
        if(bottomView.selectedItemId == R.id.navigation_news) {
            if (navigatorTabNews.fragmentCount > 1) {
                navigatorTabNews.goToPreviousFragmentAndRemoveLast()
            } else {
                super.onBackPressed()
            }
        }
        else if(bottomView.selectedItemId == R.id.navigation_video) {
            if (navigatorTabVideos.fragmentCount > 1) {
                navigatorTabVideos.goToPreviousFragmentAndRemoveLast()
            } else {
                super.onBackPressed()
            }
        }
        else if(bottomView.selectedItemId == R.id.navigation_explore) {
            if (navigatorTabExplore.fragmentCount > 1) {
                navigatorTabExplore.goToPreviousFragmentAndRemoveLast()
            } else {
                super.onBackPressed()
            }
        } else if(bottomView.selectedItemId == R.id.navigation_setting) {
            if (navigatorTabSetting.fragmentCount > 1) {
                navigatorTabSetting.goToPreviousFragmentAndRemoveLast()
            } else {
                super.onBackPressed()
            }
        }

    }

    override fun onBackPressed() {
        if (navigatorTabNews.fragmentCount > 1) {
            navigatorTabNews.goToPreviousFragmentAndRemoveLast()
        } else {
            super.onBackPressed()
        }
    }

    fun getVideoThumbnail(videoPath: String?, width: Int, height: Int): Bitmap? {
        var bitmap: Bitmap? = null
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath!!, MediaStore.Video.Thumbnails.MICRO_KIND)
        if (bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(
                bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT
            )
        }
        return bitmap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth

        ////Setting callbackPhone
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("vietnb", "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w("vietnb", "onVerificationFailed", e)
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }
                Toast.makeText(this@MainActivity, "Lỗi xác thực SĐT", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("vietnb", "onCodeSent:$verificationId")
                // Save verification ID and resending token so we can use them later
//                storedVerificationId = verificationId
//                resendToken = token

                /////Tao credetal va signIn
                //user se nhap otp va goi ham nay vao: hien tai fake luon cho nhanh
                verifyPhoneNumberWithCode(verificationId, "888888")
            }
    }

        OrientationUtils.lockOrientationPortrait(this)
        setContentView(R.layout.activity_main2)
        Utils.hiddenBottomBar(this)
//        WindowCompat.setDecorFitsSystemWindows(window, true)

        val action: String? = intent?.action
        val data: Uri? = intent?.data

        Log.d("vietnb", "action: $action")
        Log.d("vietnb", "data: $data")

        var str = "https://suckhoedoisong.qltns.mediacdn.vn/324455921873985536/2021/9/10/bt-long-16312553669101597457462.mp4"
//        str = "https://media.tinmoi24.vn/24h/upload/3-2021/videoclip/2021-07-23/1626976327-layvosinhtu.m3u8"

        FacebookSdk.fullyInitialize()
        callbackManager = CallbackManager.Factory.create()

        layoutParent = findViewById(R.id.layoutParent)
        navigatorTabNews = findViewById(R.id.navigatorTabNews)
        navigatorTabVideos = findViewById(R.id.navigatorTabVideos)
        navigatorTabExplore = findViewById(R.id.navigatorTabExplore)
        navigatorTabSetting = findViewById(R.id.navigatorTabSetting)

        bottomView = findViewById(R.id.bottomView)

        bottomView.itemIconTintList = null

        layoutParent.setOnClickListener{
            this.hideSoftKeyboard()
        }

        if (savedInstanceState == null) {
            //navigatorTab.addFragment(BlankFragment())
            //navigatorTab.addFragment(ListVideoFragment.newInstance("", ""))
            //navigatorTab.addFragment(NestedGridViewFragment.newInstance("", ""))
//            navigatorTab.addFragment(GridViewFragment.newInstance("", ""))
//            navigatorTab.addFragment(AutoSearchFragment.newInstance("", ""))
//            navigatorTab.addFragment(ListWebsiteFragment.newInstance("", ""))

            navigatorTabNews.addFragment(homePagerFragment)
            navigatorTabVideos.addFragment(listVideoFragment)
            navigatorTabExplore.addFragment(exploreFragment)
            navigatorTabSetting.addFragment(settingFragment)
        } else {
            savedInstanceState.getInt(SELECTED_ITEM_KEY)?.let {
                selectTab(it)
            }
        }

        setBottomBarListener()
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
    }

    fun fullScreen(isFullScreen: Boolean) {
        if(isFullScreen) {
            Utils.hiddenStatusBarAndBottomBar(this)
            bottomView.hide()
        } else {
            bottomView.show()
            Utils.hiddenBottomBar(this)
        }
    }

    private fun setBottomBarListener() {
        bottomView.setOnNavigationItemSelectedListener {

            EventBus.getDefault().post(EventBusFire("pauseVideo", valueString = ""))

            return@setOnNavigationItemSelectedListener selectTab(it.itemId)
        }
    }

    private fun selectTab(tabId: Int) = when (tabId) {
        R.id.navigation_news -> {
            navigatorTabNews.visibility = View.VISIBLE
            navigatorTabVideos.visibility = View.INVISIBLE
            navigatorTabExplore.visibility = View.INVISIBLE
            navigatorTabSetting.visibility = View.INVISIBLE
            true
        }
        R.id.navigation_video -> {
            navigatorTabNews.visibility = View.INVISIBLE
            navigatorTabVideos.visibility = View.VISIBLE
            navigatorTabExplore.visibility = View.INVISIBLE
            navigatorTabSetting.visibility = View.INVISIBLE
            true
        }
        R.id.navigation_explore -> {
            navigatorTabNews.visibility = View.INVISIBLE
            navigatorTabVideos.visibility = View.INVISIBLE
            navigatorTabExplore.visibility = View.VISIBLE
            navigatorTabSetting.visibility = View.INVISIBLE
            true
        }
        R.id.navigation_setting -> {
            navigatorTabNews.visibility = View.INVISIBLE
            navigatorTabVideos.visibility = View.INVISIBLE
            navigatorTabExplore.visibility = View.INVISIBLE
            navigatorTabSetting.visibility = View.VISIBLE
            true
        }
        else -> false
    }

    companion object {
        const val SELECTED_ITEM_KEY = "SELECTED_ITEM_KEY"
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    ///login facebook

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("letsSee", "malsehnnnnnn: " + data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("vietnb", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("vietnb", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("vietnb", "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("vietnb", "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signin]

    private fun updateUI(user: FirebaseUser?) {
        Log.d("vietnb", "user: $user")

        var uid = ""
        var name = ""
        var avatar = ""

        if(user != null) {
            uid = user.uid
            name = user.displayName.toString()
            avatar = user.photoUrl.toString()

            if(name == null || name.isEmpty() || name == "null") {
                name = "SĐT"
            }
        }

        val sharedPreference: DataPreference = DataPreference(this)
        var jObject = JSONObject()
        jObject.put("name", name)
        jObject.put("avatar", avatar)
        sharedPreference.save(PREFERENCE.ACCOUNTUSER, jObject.toString())
        EventBus.getDefault().post(EventBusFire("loginSuccess", valueString = ""))
        settingFragment.refreshDataAccountUser()
    }

    fun loginFacebook()
    {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult?> {
            override fun onSuccess(result: LoginResult?) {
                if (result != null) {
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

    fun loginGoogle() {
       Log.d("vietnb", "login Google")
        signIn()
    }

    fun logoutGoogle(){
        Firebase.auth.signOut()
        googleSignInClient.signOut()
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
                var name = ""
                if (jsonObject.has("name")) {
                    name = jsonObject.getString("name")
                }


                // Facebook Profile Pic URL
                var avatar = ""
                if (jsonObject.has("picture")) {
                    val facebookPictureObject = jsonObject.getJSONObject("picture")
                    if (facebookPictureObject.has("data")) {
                        val facebookDataObject = facebookPictureObject.getJSONObject("data")
                        if (facebookDataObject.has("url")) {
                            avatar = facebookDataObject.getString("url")
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

                val sharedPreference: DataPreference = DataPreference(this)
                var jObject = JSONObject()
                jObject.put("name", name)
                jObject.put("avatar", avatar)
                sharedPreference.save(PREFERENCE.ACCOUNTUSER, jObject.toString())
                EventBus.getDefault().post(EventBusFire("loginSuccess", valueString = ""))
                settingFragment.refreshDataAccountUser()

            }).executeAsync()
    }

    fun logoutFB()
    {
        val sharedPreference:DataPreference = DataPreference(this)
        sharedPreference.save(PREFERENCE.ACCOUNTUSER, "")
        settingFragment.refreshDataAccountUser()
        LoginManager.getInstance().logOut()
    }

    fun shareVideo(url: String){
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, url)
        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        intent.type = "text/plain"
        val shareIntent = Intent.createChooser(intent, "Share File");

        startActivity(shareIntent);
    }

    ////Login Phone
    fun loginPhone(phoneNumber: String) {
        Log.d("vietnb", "login Phone")
        if(phoneNumber.isNotEmpty()) {
            var phone = "+84" + phoneNumber
            //verifyPhoneNumber
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    //lay duoc thong tin user
                    updateUI(user)

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
        // [END verify_with_code]
    }

    ///neu can resend otp
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        //https://github.com/firebase/snippets-android/blob/8184cba2c40842a180f91dcfb4a216e721cc6ae6/auth/app/src/main/java/com/google/firebase/quickstart/auth/kotlin/PhoneAuthActivity.kt#L105-L105
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

}