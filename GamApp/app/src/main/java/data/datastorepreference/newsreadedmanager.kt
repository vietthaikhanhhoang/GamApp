package data.datastorepreference

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class newsreadedmanager (context: Context) {
    private val dataStore = context.createDataStore(name = "newsreaded_pref")

    val newsreaded: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference ->

            preference[NEWS_READED].toString()
        }

    suspend fun setNewsReaded(uiMode: String) {
        dataStore.edit { preferences ->
            preferences[NEWS_READED] = uiMode
        }
    }

    companion object {
        val NEWS_READED = preferencesKey<String>("NEWS_READED")
    }
}