package ac.il.hit.familiychoreapp.navigation.screens


import ac.il.hit.familiychoreapp.navigation.Screen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FamilyRestroom
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Composable function that displays the bottom navigation bar.
 *
 * @param navController The NavHostController used to navigate between screens.
 */
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.ChoreList,
        Screen.FamiliyProfile
    )

    NavigationBar {
        items.forEach { screen ->
            //check if the screen is corentliy being "active" aka we are on at the moment
            val isSelected = navController.currentBackStackEntryAsState().value?.destination?.route == screen.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        // Avoid multiple copies of the same destination when re-selecting the same item
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple instances of the same destination when navigating
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                    }
                },
                icon = {
                    val icon = when (screen) {
                        Screen.Home -> Icons.Rounded.Home
                        Screen.ChoreList -> Icons.Rounded.Checklist
                        Screen.FamiliyProfile -> Icons.Rounded.FamilyRestroom
                        else -> Icons.Rounded.Home // Default icon or a placeholder
                    }
                    Icon(imageVector = icon, contentDescription = screen.route)
                },
                label = { Text(text = screen.route) }
            )
        }
    }
}
