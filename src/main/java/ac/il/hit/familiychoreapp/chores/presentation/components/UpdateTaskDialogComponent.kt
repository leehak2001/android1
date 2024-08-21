package ac.il.hit.familiychoreapp.chores.presentation.components

import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
import ac.il.hit.familiychoreapp.chores.domain.models.Task
import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.grid.items  // Import the correct items function

/**
 * A composable function that displays a dialog for updating an existing task.
 *
 * @param onDismiss Callback to be invoked when the dialog is dismissed.
 * @param task The task to be updated.
 * @param familyMembers List of family members available for task assignment.
 * @param onUpdateTask Callback to be invoked when the task is updated, passing the updated task data.
 */
@Composable
fun UpdateTaskDialog(
    onDismiss: () -> Unit,
    task: Task,
    familyMembers: List<FamilyMember>,
    onUpdateTask: (Task, String, String, Date, List<FamilyMember>) -> Unit
) {
    var newTitle by remember { mutableStateOf(task.title) }
    var newDescription by remember { mutableStateOf(task.description) }
    var newDate by remember { mutableStateOf(task.getDate()) }
    var selectedMembers by remember { mutableStateOf(task.enrolledFamilyMembers.toList()) }
    var datePickerDialog by remember { mutableStateOf<DatePickerDialog?>(null) }
    var showError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Update Task") },
        text = {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newDescription,
                    onValueChange = { newDescription = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(newDate),
                    modifier = Modifier
                        .clickable {
                            datePickerDialog?.show()
                        }
                        .padding(8.dp),
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Family Members Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(familyMembers) { member ->
                        val isSelected = selectedMembers.contains(member)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable {
                                    selectedMembers = if (isSelected) {
                                        selectedMembers - member
                                    } else {
                                        selectedMembers + member
                                    }
                                }
                        ) {
                            Image(
                                painter = painterResource(id = member.profilePicture),
                                contentDescription = member.name,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = 2.dp,
                                        color = if (isSelected) Color.Blue else Color.Transparent,
                                        shape = CircleShape
                                    )
                            )
                            Text(
                                text = member.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                if (showError) {
                    Text(
                        text = "Please select at least one family member.",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Text(
                text = "OK",
                modifier = Modifier
                    .clickable {
                        if (selectedMembers.isEmpty()) {
                            showError = true
                        } else {
                            onUpdateTask(task, newTitle, newDescription, newDate, selectedMembers)
                            onDismiss()
                        }
                    }
                    .padding(16.dp)
            )
        },
        dismissButton = {
            Text(
                text = "Cancel",
                modifier = Modifier
                    .clickable { onDismiss() }
                    .padding(16.dp)
            )
        }
    )

    // Setup DatePickerDialog
    if (datePickerDialog == null) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance().apply { time = newDate }
        datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val newCalendar = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                newDate = newCalendar.time
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }
}
