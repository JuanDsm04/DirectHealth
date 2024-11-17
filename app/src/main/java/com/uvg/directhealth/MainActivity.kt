package com.uvg.directhealth

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import com.uvg.directhealth.data.local.DataStoreUserPrefs
import com.uvg.directhealth.layouts.navigation.AppNavigation
import com.uvg.directhealth.ui.theme.DirectHealthTheme

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStoreUserPrefs = DataStoreUserPrefs(dataStore = applicationContext.dataStore)

        setContent {
            val navController = rememberNavController()

            DirectHealthTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AppNavigation(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        navController = navController,
                        dataStoreUserPrefs = dataStoreUserPrefs
                    )
                }
            }

        }
    }
}
