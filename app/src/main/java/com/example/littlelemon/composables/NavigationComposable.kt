package com.example.littlelemon.composables

import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.littlelemon.Home
import com.example.littlelemon.Onboarding
import com.example.littlelemon.Profile



@Composable
fun Navigation(prefs: SharedPreferences, loggedInLiveData: MutableLiveData<Boolean>){
    val selected = loggedInLiveData.observeAsState(initial = false)
    val navController = rememberNavController()
    val start: String = if(selected.value == true){
        Home.route
    } else {
        Onboarding.route
    }
    NavHost(navController = navController, startDestination = start){
        composable(Onboarding.route){
            Onboarding(navController, prefs, loggedInLiveData)
        }
        composable(Home.route){
            Home()
        }
        composable(Profile.route){
            Profile()
        }
    }
}