package data.datastorepreference

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class AUTOPLAY_MODE {
    ON, OFF
}

class settingmanager (context: Context) {
    private val dataStore = context.createDataStore(name = "settings_pref")

    val autoplayMode: Flow<AUTOPLAY_MODE> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference ->
            val isAutoPlay = preference[IS_AUTOPLAY_MODE] ?: false

            when (isAutoPlay) {
                true -> AUTOPLAY_MODE.ON
                false -> AUTOPLAY_MODE.OFF
            }
        }

    suspend fun setAutoPlayMode(uiMode: AUTOPLAY_MODE) {
        dataStore.edit { preferences ->
            preferences[IS_AUTOPLAY_MODE] = when (uiMode) {
                AUTOPLAY_MODE.OFF -> false
                AUTOPLAY_MODE.ON -> true
            }
        }
    }

    companion object {
        val IS_AUTOPLAY_MODE = preferencesKey<Boolean>("AUTO_PLAY")
    }
}