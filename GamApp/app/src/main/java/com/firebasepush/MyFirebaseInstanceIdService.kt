package com.firebasepush
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseInstanceIdService : FirebaseMessagingService() {

    val TAG = "PushNotifService"
    lateinit var name: String

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        // Mengambil token perangkat
        Log.d("vietnb", "Token day roi nhe ini: $p0")

        // Jika ingin mengirim push notifcation ke satu atau sekelompok perangkat,
        // simpan token ke server di sini.
    }

}