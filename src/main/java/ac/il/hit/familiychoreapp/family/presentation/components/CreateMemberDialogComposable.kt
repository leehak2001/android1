package ac.il.hit.familiychoreapp.family.presentation.components

import ac.il.hit.familiychoreapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid

/**
 * Displays a dialog for creating a new family member.
 *
 * @param onDismiss Callback to be invoked when the dialog is dismissed.
 * @param onCreateMember Callback to be invoked when the user creates a new family member,
 * receiving the new member's name, age, and profile picture resource ID.
 */
@Composable
fun CreateMemberDialog(
    onDismiss: () -> Unit,
    onCreateMember: (String, Int, Int) -> Unit
) {
    var newName by remember { mutableStateOf("") }
    var newAge by remember { mutableStateOf("") }
    var newPic by remember { mutableStateOf(R.drawable.fox) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val profilePictures = listOf(
        R.drawable.fox, R.drawable.bull, R.drawable.cheek,
        R.drawable.crab, R.drawable.kipod, R.drawable.kuala,
        R.drawable.pig, R.drawable.tiger, R.drawable.whale
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add New Family Member") },
        text = {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newAge,
                    onValueChange = { newAge = it },
                    label = { Text("Age") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Select Profile Picture:")
                Spacer(modifier = Modifier.height(8.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(profilePictures.size) { index ->
                        val pic = profilePictures[index]
                        Image(
                            painter = painterResource(id = pic),
                            contentDescription = null,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .border(
                                    width = if (newPic == pic) 3.dp else 0.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                                .clickable { newPic = pic }
                        )
                    }
                }

                errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val ageInt = newAge.toIntOrNull()
                if (newName.isBlank() || ageInt == null) {
                    errorMessage = "Please provide valid name and age"
                } else {
                    onCreateMember(newName, ageInt, newPic)
                    onDismiss()
                }
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

