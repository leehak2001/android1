package ac.il.hit.familiychoreapp.family.presentation.screens

import ac.il.hit.familiychoreapp.family.presentation.components.CreateMemberDialog
import ac.il.hit.familiychoreapp.family.presentation.components.FamilyMemberItem
import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
import ac.il.hit.familiychoreapp.family.presentation.FamilyViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController


/**
 * Composable function that displays the Family Profile screen.
 *
 * @param navController The navigation controller to handle navigation actions.
 * @param familyViewModel The ViewModel associated with this screen. Default is obtained using [viewModel].
 */
@Composable
fun FamilyProfileScreen(
    navController: NavHostController,
    familyViewModel: FamilyViewModel = viewModel()
) {

    val members by familyViewModel.members.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5E5DB))
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Family Members",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF847778)
                    )
                }

                FamilyMembersList(familyMembers = members,
                    onUpdateMember={ member, newName, newAge, newPic ->
                                        familyViewModel.updateMember(member, newName, newAge, newPic)
                                    },
                    onDeleteMember= { member -> familyViewModel.deleteMember(member) })


                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF847778)
                    )
                ) {
                    Text(text = "Add a new family member")
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (showDialog) {
                    CreateMemberDialog(
                        onDismiss = { showDialog = false },
                        onCreateMember = { name, age, profilePicture ->
                                        familyViewModel.createMember(name, age, profilePicture)
                                    }
                    )
                }
            }
        }
    }
}

/**
 * Composable function that displays a list of family members using a LazyColumn.
 *
 * @param familyMembers A list of [FamilyMember] objects to display.
 * @param onUpdateMember Callback to update a family member.
 * @param onDeleteMember Callback to delete a family member.
 */
@Composable
fun FamilyMembersList(familyMembers: List<FamilyMember>, onUpdateMember: (FamilyMember, String, Int, Int) -> Unit,
                      onDeleteMember: (FamilyMember) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(familyMembers) { member ->
            FamilyMemberItem(member, onUpdateMember, onDeleteMember)
        }
    }
}
