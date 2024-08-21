package ac.il.hit.familiychoreapp.chores.presentation.components

import ac.il.hit.familiychoreapp.chores.domain.models.Task
import ac.il.hit.familiychoreapp.family.domain.toDTO
import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
import ac.il.hit.familiychoreapp.navigation.Screen
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.gson.Gson

private const val TAG = "FamilyMemberGridPopup"

/**
 * A composable function that displays a popup grid of family members for selection.
 * This popup is used to navigate to the SearchScreen with the selected member's tasks.
 *
 * @param familyMembers List of family members available for selection.
 * @param onDismiss Callback to be invoked when the dialog is dismissed.
 * @param navController Controller for handling navigation between screens.
 * @param tasks List of tasks associated with the family members.
 */
@Composable
fun FamilyMemberGridPopup(
    familyMembers: List<FamilyMember>,
    onDismiss: () -> Unit,
    navController: NavHostController, // for navigation
    tasks: List<Task> // to pass tasks to the SearchScreen
) {
    val selectedMember = remember { mutableStateOf<FamilyMember?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Choose a family member to view the full task list of") },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(familyMembers) { member ->
                    val isSelected = selectedMember.value == member
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable {
                                selectedMember.value = member
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
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedMember.value?.let { member ->
                        // Convert to DTOs

                        val gson = Gson()
                        val memberDTO = member.toDTO()

                        // Serialize DTOs
                        val memberJson = gson.toJson(memberDTO)

                        Log.d(TAG, "Selected memberDTO: $memberDTO")
                        Log.d(TAG, "Serialized memberJson: $memberJson")

                        // Navigate to SearchScreen with serialized JSON arguments
                        navController.navigate(Screen.Search.passArgs(memberJson))
                        onDismiss()
                    }
                },
                enabled = selectedMember.value != null
            ) {
                Text(text = "OK")
            }
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
