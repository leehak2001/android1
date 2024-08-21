package ac.il.hit.familiychoreapp.chores.presentation.components

import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import java.util.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

/**
 * A composable function that displays a dialog for creating a new task.
 *
 * @param onDismissRequest Callback to be invoked when the dialog is dismissed.
 * @param onConfirm Callback to be invoked when the "Create" button is clicked. It passes the title,
 * description, selected date, and list of selected family members.
 * @param familyMembers List of family members available for task assignment.
 */
@Composable
fun CreateTaskDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (String, String, Date, List<FamilyMember>) -> Unit,
    familyMembers: List<FamilyMember> // List of family members from the database
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Date()) }
    var selectedMembers by remember { mutableStateOf<List<FamilyMember>>(emptyList()) }
    var datePickerDialog: DatePickerDialog? by remember { mutableStateOf(null) }
    var showError by remember { mutableStateOf(false) } // To show error message if needed

    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply { time = selectedDate }

    if (datePickerDialog == null) {
        datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val newCalendar = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                selectedDate = newCalendar.time
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Create New Task") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate),
                    modifier = Modifier
                        .clickable { datePickerDialog?.show() }
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
                    items(familyMembers.size) { index ->
                        val member = familyMembers[index]
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
            Button(
                onClick = {
                    if (selectedMembers.isEmpty()) {
                        showError = true
                    } else {
                        onConfirm(title, description, selectedDate, selectedMembers)
                        onDismissRequest()
                    }
                },
                enabled = selectedMembers.isNotEmpty() // Disable button if no members are selected
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

