package ac.il.hit.familiychoreapp.chores.presentation.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import ac.il.hit.familiychoreapp.R
import ac.il.hit.familiychoreapp.chores.presentation.TaskViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController


/**
 * Composable function that displays the Home screen with task statistics.
 *
 * @param navController The navigation controller to handle navigation actions.
 * @param taskViewModel The ViewModel associated with this screen. Default is obtained using [viewModel].
 */
@Composable
fun HomeScreen(navController: NavHostController,
               taskViewModel: TaskViewModel = viewModel()
) {
    val tasks by taskViewModel.tasks.collectAsState()

    /*
     A composable that creates a blueprint for the screen elements.
     It also has the option to "save" slots for "special" components like the bottom navigation bar for app-level navigation.
     We chose to use it because Scaffold automatically handles padding around the content area to avoid overlap with the bottom bar.
  */
    Scaffold() { padding ->

        val painter = painterResource(id = R.drawable.homescreen)

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painter,
                contentDescription = "pink screen with 2 circels",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .padding(bottom=30.dp) // Add padding to ensure content is not too close to edges
            ) {
                Spacer(modifier = Modifier.weight(0.3f))
                TopSection()
                Spacer(modifier = Modifier.weight(0.7f)) // Pushes the main section to the center
                Box(
                    modifier = Modifier
                        .fillMaxWidth(), // Ensures the MainSection Box takes full width
                    contentAlignment = Alignment.Center
                ) {
                    MainSection(completedTasksCount = tasks.count { it.isChecked },
                        totalTasksCount = tasks.size)
                }
                Spacer(modifier = Modifier.weight(1f)) // Pushes the bottom text to the bottom
                BottomSection()
            }
        }
    }
}



/**
 * Composable function that displays a top section with a title on the Home screen.
 */
@Composable
fun TopSection() {
    Text(
        text = "Your Personal Family Chores Assistant",
        fontSize = 45.sp,
        lineHeight = (45).sp, // Increase line height for better spacing
        fontWeight = FontWeight.Bold,
        color = Color(0xFFAE8D8D),
        modifier = Modifier
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

/**
 * Composable function that displays the main section on the Home screen with task completion statistics.
 *
 * @param completedTasksCount The number of completed tasks.
 * @param totalTasksCount The total number of tasks.
 */
@Composable
fun MainSection(completedTasksCount: Int, totalTasksCount: Int) {
    Box(
        contentAlignment = Alignment.Center, // Centers the content inside the Box
        modifier = Modifier
            .size(200.dp)
            .fillMaxSize() // Makes the Box fill the parent (home screen page)
    ) {
        Text(
            text = "$completedTasksCount / $totalTasksCount tasks has been completed",
            fontSize = 32.sp,
            lineHeight = (32 * 1.1).sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF78D79),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Composable function that displays the bottom section on the Home screen with a motivational message.
 */
@Composable
fun BottomSection() {
    Box(
        modifier = Modifier
            .size(170.dp)
    ){
        Text(
            text = "Let's organize this house!",
            fontSize = 40.sp,
            lineHeight = (40 ).sp,
            color = Color(0xFFF5E5DB),
            fontFamily = FontFamily.Cursive,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
        )
    }
}


