package data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type

object PREFERENCE {
    const val BETTINGMATCH = "BETTINGMATCH"
    const val ACCOUNTUSER = "ACCOUNTUSER"

    const val AUTOPLAY = "AUTOPLAY"
    const val BRIGHT = "BRIGHT"
}

class DataPreference(val context: Context) {
    private val PREFS_NAME = "kotlincodes"
    val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getValueArray(KEY_NAME: String): ArrayList<JSONObject> {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        if(sharedPref.getString(KEY_NAME, "")!!.isNotEmpty()) {
            val json: String = sharedPref.getString(KEY_NAME, null)!!
            val type: Type = object : TypeToken<java.util.ArrayList<JSONObject?>?>() {}.getType()
            return Gson().fromJson(json, type)
        }
        return ArrayList()
    }



    fun save(KEY_NAME: String, text: String) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putString(KEY_NAME, text)

        editor!!.commit()
    }

    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putInt(KEY_NAME, value)

        editor.commit()
    }

    fun save(KEY_NAME: String, status: Boolean) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putBoolean(KEY_NAME, status!!)

        editor.commit()
    }

    fun getValueString(KEY_NAME: String): String? {

        return sharedPref.getString(KEY_NAME, null)
    }

    fun getValueJSON(KEY_NAME: String): JSONObject? {
        val value = sharedPref.getString(KEY_NAME, "")!!
        if(value.isNotEmpty()) {
            return JSONObject(value)
        }

        return null
    }

    fun getValueInt(KEY_NAME: String): Int {

        return sharedPref.getInt(KEY_NAME, 0)
    }

    fun getValueBoolean(KEY_NAME: String, defaultValue: Boolean): Boolean {
        return sharedPref.getBoolean(KEY_NAME, defaultValue)
    }

    fun clearSharedPreference() {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        //sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        editor.clear()
        editor.commit()
    }

    fun removeValue(KEY_NAME: String) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.remove(KEY_NAME)
        editor.commit()
    }
}