package ac.il.hit.familiychoreapp.navigation

import ac.il.hit.familiychoreapp.family.domain.toRealmObject
import ac.il.hit.familiychoreapp.chores.presentation.screens.ChoreListScreen
import ac.il.hit.familiychoreapp.chores.presentation.screens.HomeScreen
import ac.il.hit.familiychoreapp.chores.presentation.screens.SearchScreen
import ac.il.hit.familiychoreapp.family.domain.models.FamilyMemberDTO
import ac.il.hit.familiychoreapp.family.presentation.screens.FamilyProfileScreen
import ac.il.hit.familiychoreapp.navigation.screens.BottomNavigationBar
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson

private const val TAG = "Navigation"

/**
 * Composable function that sets up the app's navigation using a [NavController].
 * It defines the navigation graph and routes for different screens.
 */
@Composable
fun Navigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.ChoreList.route) {
                ChoreListScreen(navController = navController)
            }
            composable(Screen.FamiliyProfile.route) {
                FamilyProfileScreen(navController = navController)
            }

            composable(
                route = Screen.Search.route,
                arguments = listOf(
                    navArgument("memberJson") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val gson = Gson()

                // Extract the JSON strings
                val memberJson = backStackEntry.arguments?.getString("memberJson")

                Log.d(TAG, "memberJson: $memberJson")

                // Deserialize DTOs
                val memberDTO = gson.fromJson(memberJson, FamilyMemberDTO::class.java)

                Log.d(TAG, "memberDTO: $memberDTO")

                // Convert DTOs back to Realm objects
                val member = memberDTO.toRealmObject()

                Log.d(TAG, "member: $member")

                // Pass the objects to SearchScreen
                SearchScreen(
                    member = member,
                    navController = navController
                )
            }


        }

    }
}

