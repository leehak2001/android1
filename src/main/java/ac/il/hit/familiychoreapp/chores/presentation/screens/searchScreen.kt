package ac.il.hit.familiychoreapp.chores.presentation.screens

import ac.il.hit.familiychoreapp.chores.domain.models.Task
import ac.il.hit.familiychoreapp.chores.presentation.components.MyDialog
import ac.il.hit.familiychoreapp.chores.presentation.components.ShowNotificationCircle
import ac.il.hit.familiychoreapp.chores.presentation.components.memberImage
import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign

private const val TAG = "SearchScreen"

/**
 * Composable function that displays the Search screen for a specific family member.
 *
 * @param member The [FamilyMember] to display.
 * @param navController The navigation controller to handle navigation actions.
 */
@Composable
fun SearchScreen(
    member: FamilyMember,
    navController: NavHostController
) {
    val tasksfilter by remember { mutableStateOf(member.enrolledTasks) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Go back icon outside the rectangle
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .background(Color.White)
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = "Go Back"
            )
        }

        // Inner rectangle
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 65.dp, start = 10.dp, end = 16.dp, bottom = 16.dp) // padding to ensure visibility of bottom
                .background(Color.LightGray, RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Profile picture outside the rectangle
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .offset(y = (-40).dp)
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.Black)
                        .padding(3.dp)
                ) {
                    Image(
                        painter = painterResource(id = member.profilePicture),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(3.dp)
                    )
                }

                // Top part with member information
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.White)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = member.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Age: ${member.age}",
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "Completed Tasks: ${tasksfilter.count { it.isChecked }} / ${tasksfilter.size}",
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp)) // Spacing before the tasks list

                // Lower part with tasks in a lazy column
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tasksfilter) { task ->
                        TaskItem(
                            task = task,
                            familyMembers = task.enrolledFamilyMembers,
                            onCheckChanged = {  },
                            onClick = {  }
                        )
                    }
                }
            }
        }
    }
}


/**
 * Composable function that displays a single task item in the task list.
 *
 * @param task The [Task] to display.
 * @param familyMembers A list of [FamilyMember] objects associated with the task.
 * @param onCheckChanged Callback invoked when the task's completion status is changed.
 * @param onClick Callback invoked when the task item is clicked.
 * @param modifier Modifier to be applied to the task item layout.
 */
@Composable
fun TaskItem(
    task: Task,
    familyMembers: List<FamilyMember>,
    onCheckChanged: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showFullText by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFD49A9A))
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Checkbox(
            checked = task.isChecked,
            onCheckedChange = { isChecked -> onCheckChanged(isChecked) },
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFFEAB8A4),
                uncheckedColor = Color(0xFF847778),
                checkmarkColor = Color.White
            ),
            modifier = Modifier
                .padding(end = 8.dp)
                .weight(0.1f)
        )

        // Display only two family members and indicate more if necessary
        // Family Members Images and Notification Circle
        Box(modifier = Modifier.weight(0.2f)) {
            Row(modifier= Modifier.clickable { showFullText = true }) {
                familyMembers.take(2).forEach { member ->
                    memberImage(member, 25)
                }

                if (familyMembers.size > 2) {
                    ShowNotificationCircle(show = true)
                }
            }
        }

        Spacer(modifier = Modifier.width(5.dp))
        Column(modifier = Modifier
            .weight(0.4f)
            .clickable { showFullText = true })
        {
            Text(
                text = task.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = task.description,
                fontSize = 14.sp,
                color = Color(0xFFF5E5DB),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.weight(0.1f))

        Text(
            text = task.getDate().toString(),
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.weight(0.2f),
            textAlign = TextAlign.End
        )
    }

    // Show full text in a dialog
    if (showFullText) {
        MyDialog(
            showFullText = showFullText,
            onDismissRequest = { showFullText = false },
            task = task
        )
    }
}
