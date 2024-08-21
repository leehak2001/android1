package ac.il.hit.familiychoreapp.chores.presentation.components

import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
import ac.il.hit.familiychoreapp.chores.domain.models.Task
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow

/**
 * Displays an individual chore list item with a title, description, check status, and associated family members.
 *
 * @param task The task data associated with this chore item.
 * @param onUpdateTask Function to handle updating the task.
 * @param onDeleteTask Function to handle deleting the task.
 * @param onCheckChanged Function to handle checkbox status change.
 * @param familyMembers List of family members associated with the task.
 * @param modifier Modifier to be applied to the chore list item layout.
 */
@Composable
fun ChoreListItem(
    task: Task,
    onUpdateTask: (Task, String, String, Date, List<FamilyMember>) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onCheckChanged: (Task, Boolean) -> Unit,
    familyMembers: List<FamilyMember>,
    modifier: Modifier = Modifier
) {
    var showAllMembers by remember { mutableStateOf(false) }
    var showFullText by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFD49A9A))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Checkbox(
            checked = task.isChecked,
            onCheckedChange = {isChecked ->
                onCheckChanged(task, isChecked)},
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFFEAB8A4),
                uncheckedColor = Color(0xFF847778),
                checkmarkColor = Color.White
            ),
            modifier = Modifier.padding(end = 8.dp)
                .weight(0.1f)
        )

        // Display only two family members and indicate more if necessary
        // Family Members Images and Notification Circle
        Box(modifier = Modifier.weight(0.2f)) {
            Row(modifier= Modifier.clickable { showAllMembers = true }) {
                task.enrolledFamilyMembers.take(2).forEach { member ->
                    memberImage(member, 25)
                }

                if (task.enrolledFamilyMembers.size > 2) {
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


        IconButton(
            modifier = Modifier.weight(0.1f),
            onClick = { showUpdateDialog = true }
        ) {
            Icon(
                imageVector = Icons.Rounded.Edit,
                contentDescription = "Update",
            )
        }

        IconButton(
            modifier = Modifier.weight(0.1f),
            onClick = { onDeleteTask(task) }) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Delete",
                tint = Color(0xFFF5E5DB)
            )
        }
    }

    // Show all family members in a dialog
    if (showAllMembers) {
        Dialog(onDismissRequest = { showAllMembers = false }, properties = DialogProperties()) {
            Column(modifier = Modifier.padding(16.dp)) {
                task.enrolledFamilyMembers.forEach { member ->
                    memberImage(member, 100)
                }
            }
        }
    }

    // Show full text in a dialog
     if (showFullText) {
        MyDialog(
            showFullText = showFullText,
            onDismissRequest = { showFullText = false },
            task = task
        )
    }

    if (showUpdateDialog) {
        UpdateTaskDialog(
            onDismiss = { showUpdateDialog = false },
            task = task,
            familyMembers = familyMembers,
            onUpdateTask= onUpdateTask
        )
    }
}

/**
 * Displays a dialog with the full task title, description, and date.
 *
 * @param showFullText Boolean value indicating if the full text should be shown.
 * @param onDismissRequest Function to handle the dialog dismiss request.
 * @param task The task whose full details are to be displayed.
 */
@Composable
fun MyDialog(
    showFullText: Boolean,
    onDismissRequest: () -> Unit,
    task: Task
) {
    // Define the date format you want to use
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    // Format the date as a string
    val formattedDate = dateFormat.format(task.getDate())

    if (showFullText) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = task.title) },
            text = {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = formattedDate,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(text = task.description)
                }
            },
            confirmButton = {
                Text(
                    text = "OK",
                    modifier = Modifier
                        .clickable { onDismissRequest() }
                        .padding(16.dp)
                )
            }
        )
    }
}


/**
 * Displays a small red notification circle, indicating additional members.
 *
 * @param show Boolean value indicating if the notification circle should be shown.
 */
@Composable
fun ShowNotificationCircle(show: Boolean) {
    if (show) {
        Canvas(
            modifier = Modifier.size(8.dp),
            onDraw = {
                // Assuming a screen density of 320 dpi
                val scale = 320f / 160f
                val radiusPx = 4f * scale
                drawCircle(
                    color = Color.Red,
                    radius = radiusPx,
                    center = Offset(
                        x = size.width - radiusPx,
                        y = radiusPx
                    )
                )
            }
        )
    }
}

/**
 * Displays a circular profile picture of a family member.
 *
 * @param member The family member whose profile picture is to be displayed.
 * @param size The size of the profile picture in dp.
 */
@Composable
fun memberImage(member: FamilyMember, size: Int){
    Image(
            painter = painterResource(id = member.profilePicture),
            contentDescription = "profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(size.dp)
                .clip(CircleShape)
            )
}

