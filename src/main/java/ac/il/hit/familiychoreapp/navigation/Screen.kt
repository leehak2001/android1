package ac.il.hit.familiychoreapp.navigation

/**
 * Sealed class representing the different screens in the app's navigation.
 * Each screen has a unique route used for navigation.
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ChoreList : Screen("chore_list")
    object FamiliyProfile : Screen("family_profile")
    /**
     * Represents the Search screen. This screen requires a member's JSON string as a parameter.
     *
     * @param member The JSON string representing a family member.
     * @return The route with the member's JSON string as an argument.
     */
    object Search : Screen("search/{memberJson}") {
        fun passArgs(member: String) = "search/$member"
    }
}