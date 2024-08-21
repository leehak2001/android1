package ac.il.hit.familiychoreapp.family.presentation.components

import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A composable function that displays a single family member's information in a list item.
 *
 * This function includes options to update or delete the member.
 * When the update button is clicked, an update dialog is shown.
 *
 * @param member The family member to display.
 * @param onUpdateMember A callback to update the member's information, with the updated member and new values as parameters.
 * @param onDeleteMember A callback to delete the member.
 */
@Composable
fun FamilyMemberItem(
    member: FamilyMember,
    onUpdateMember: (FamilyMember, String, Int, Int) -> Unit,
    onDeleteMember: (FamilyMember) -> Unit
){
    var showUpdateDialog by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFD49A9A))
            .padding(8.dp),  // Reduced padding for a shorter height
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = member.profilePicture),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)  // Smaller image size
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))  // Reduced spacing
        Column {
            Text(
                text = member.name,
                fontSize = 16.sp,  // Smaller font size
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "${member.age} years old",
                fontSize = 12.sp,  // Smaller font size
                color = Color(0xFFF5E5DB)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            modifier = Modifier.weight(0.1f),
            onClick = { showUpdateDialog = true }
        ) {
            Icon(
                imageVector = Icons.Rounded.Create,
                contentDescription = "Update",
            )
        }

        IconButton(
            modifier = Modifier.weight(0.1f),
            onClick = { onDeleteMember(member) }) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Delete",
                tint = Color(0xFFF5E5DB)
            )
        }
    }

    if (showUpdateDialog) {
        UpdateMemberDialog(
            onDismiss = { showUpdateDialog = false },
            member = member
        ) { newName, newAge, newPic ->
            onUpdateMember(member, newName, newAge, newPic)
        }
    }
}