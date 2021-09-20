package data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type

object PREFERENCE {
    const val BETTINGMATCH = "BETTINGMATCH"
    const val ACCOUNTUSER = "ACCOUNTUSER"

    const val AUTOPLAY = "AUTOPLAY"
    const val BRIGHT = "BRIGHT"

    const val ARRAYNAMEWEBSITE = "ARRAYNAMEWEBSITE"
    const val ARRAYWEBSITETOPIC = "ARRAYWEBSITETOPIC"
    const val ARRAYDICTWIDCATEGORY = "ARRAYDICTWIDCATEGORY"
    const val ARRAYCATEGORYHOT = "ARRAYCATEGORYHOT"

    const val MAPIDNAMEWEBSITE = "MAPIDNAMEWEBSITE"

    const val ARRAYCATEGORYVIDEO = "ARRAYCATEGORYVIDEO"
}

class DataPreference(val context: Context) {
    private val PREFS_NAME = "kotlincodes"
    val sharedPref: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

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

    fun save(KEY_NAME: String, value: Map<Int, String>) {
        val editor: SharedPreferences.Editor = sharedPref.edit()

        val gson = Gson()
        val strValue = gson.toJson(value).toString()
        val jsonObject = JSONObject(strValue)
        val jsonString = jsonObject.toString()
        editor.putString(KEY_NAME, jsonString)
        editor.commit()
    }

    fun getValueMap(KEY_NAME: String): Map<Int, String> {
        val value = sharedPref.getString(KEY_NAME, "")!!
        if(value.isNotEmpty()) {
            val outputMap = mutableMapOf<Int, String>()

            val jsonObject = JSONObject(value)
            val keysItr: Iterator<String> = jsonObject.keys()
            while (keysItr.hasNext()) {
                val key = keysItr.next()
                outputMap[key.toInt()] = jsonObject.getString(key.toString())
            }

            return outputMap
        }

        return mutableMapOf<Int, String>()
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