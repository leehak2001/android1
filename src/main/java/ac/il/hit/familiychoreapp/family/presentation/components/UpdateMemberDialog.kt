
package ac.il.hit.familiychoreapp.family.presentation.components

import ac.il.hit.familiychoreapp.R
import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
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


/**
 * A composable function that displays a dialog to update a family member's information.
 *
 * This dialog allows the user to update the name, age, and select a new profile picture for the member.
 * It includes validation to ensure that a valid age is provided before allowing the update.
 *
 * @param onDismiss A callback to dismiss the dialog.
 * @param member The family member to update.
 * @param onUpdateMember A callback to update the member's information, with the updated name, age, and profile picture passed as parameters.
 */
@Composable
fun UpdateMemberDialog(
    onDismiss: () -> Unit,
    member: FamilyMember,
    onUpdateMember: (String, Int, Int) -> Unit
) {
    var newName by remember { mutableStateOf(member.name) }
    var newAge by remember { mutableStateOf(member.age.toString()) }
    var newPic by remember { mutableStateOf(member.profilePicture) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val profilePictures = listOf(
        R.drawable.fox, R.drawable.bull, R.drawable.cheek,
        R.drawable.crab, R.drawable.kipod, R.drawable.kuala,
        R.drawable.pig, R.drawable.tiger, R.drawable.whale
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Update Family Member") },
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
                    onValueChange = { newAge = it.filter { char -> char.isDigit() } },
                    label = { Text("Age") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Choose Profile Picture:")
                Spacer(modifier = Modifier.height(8.dp))

                Column {
                    profilePictures.chunked(3).forEach { rowPictures ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            rowPictures.forEach { pic ->
                                ProfilePictureOption(
                                    picture = pic,
                                    isSelected = newPic == pic,
                                    onSelect = { newPic = it }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
            }
        },
        confirmButton = {
            Text(
                text = "OK",
                modifier = Modifier
                    .clickable {
                        val age = newAge.toIntOrNull()
                        if (age != null) {
                            onUpdateMember(newName, age, newPic)
                            onDismiss()
                        } else {
                            errorMessage = "Invalid age. Please enter a valid number."
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
}



@Composable
fun ProfilePictureOption(
    picture: Int,
    isSelected: Boolean,
    onSelect: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clickable { onSelect(picture) }
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                shape = CircleShape
            )
            .padding(4.dp)
    ) {
        Image(
            painter = painterResource(id = picture),
            contentDescription = null,
            modifier = Modifier.clip(CircleShape)
        )
    }
}

