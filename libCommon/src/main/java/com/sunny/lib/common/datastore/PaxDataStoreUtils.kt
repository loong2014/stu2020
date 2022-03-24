//package com.sunny.lib.common.datastore
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.booleanPreferencesKey
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.emptyPreferences
//import androidx.datastore.preferences.preferencesDataStore
//import com.sunny.lib.base.utils.ContextProvider
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.map
//import timber.log.Timber
//
//val Context.dataStoreDev: DataStore<Preferences> by preferencesDataStore(name = "pax_prefs_dev")
//
///**
// * https://developer.android.com/jetpack/androidx/releases/datastore
// *
// * https://developer.android.com/topic/libraries/architecture/datastore
// *
// * https://developer.android.com/codelabs/android-preferences-datastore#0
// */
//object PaxDataStoreUtils {
//
//    val DEBUG_HOST = booleanPreferencesKey("debug_host")
//
//    val dataStore: DataStore<Preferences> by lazy {
//        ContextProvider.appContext.dataStoreDev
//    }
//
//    val debugHostFlow: Flow<Boolean> = dataStore.data
//        .catch { e ->
//            Timber.e("getInt error :$e")
//            emit(emptyPreferences())
//        }.map { preferences ->
//            preferences[DEBUG_HOST] ?: false
//        }
//
//    suspend fun updateDebugHost(debug: Boolean) {
//        dataStore.edit { settings ->
//            settings[DEBUG_HOST] = debug
//        }
//    }
//}