package ac.il.hit.familiychoreapp.chores.presentation.screens



import ac.il.hit.familiychoreapp.chores.presentation.components.ChoreListItem
import ac.il.hit.familiychoreapp.chores.presentation.components.CreateTaskDialog
import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
import ac.il.hit.familiychoreapp.chores.domain.models.Task
import ac.il.hit.familiychoreapp.chores.presentation.TaskViewModel
import ac.il.hit.familiychoreapp.chores.presentation.components.FamilyMemberGridPopup
import ac.il.hit.familiychoreapp.family.presentation.FamilyViewModel
import ac.il.hit.familiychoreapp.navigation.Screen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.gson.Gson
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Composable function that displays the Chore List screen.
 *
 * @param navController The navigation controller to handle navigation actions.
 * @param taskViewModel The ViewModel associated with task management. Default is obtained using [viewModel].
 * @param familyViewModel The ViewModel associated with family management. Default is obtained using [viewModel].
 */
@Composable
fun ChoreListScreen(
    navController: NavHostController,
    taskViewModel: TaskViewModel = viewModel(),
    familyViewModel: FamilyViewModel = viewModel()
) {
    val tasks by taskViewModel.tasks.collectAsState()
    val members by familyViewModel.members.collectAsState(initial = emptyList())

    var showCreateTaskDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Date()) } // Initialize with the current date
    var showSearchPopup by remember { mutableStateOf(false) }

    // Convert selectedDate to timestamp at the start of the day
    val selectedTimestamp = Calendar.getInstance().apply {
        time = selectedDate
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    // Filter tasks based on the selected date
    val filteredChoreItems = tasks.filter {
        val taskDate = Calendar.getInstance().apply {
            timeInMillis = it.date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        taskDate == selectedTimestamp
    }

    Scaffold(
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = { showSearchPopup = true },
                    containerColor = Color(0xFFAE8D8D),
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonSearch,
                        contentDescription = "Search Family Member"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(
                    onClick = { showCreateTaskDialog = true },
                    containerColor = Color(0xFFAE8D8D),
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add Chore"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5E5DB))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(10.dp)
            ) {
                TopSectione { newDate ->
                    selectedDate = newDate
                }
                Spacer(modifier = Modifier.weight(0.1f))
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ChoreList(choreItems = filteredChoreItems,
                        onUpdateTask = { task, newName, newDescription, newDate, selectedMembers ->
                            taskViewModel.updateTask(task, newName, newDescription, newDate, selectedMembers)},
                        onDeleteTask = { task -> taskViewModel.deleteTask(task) },
                        familyMembers=members,
                        onCheckChanged={ task, isChecked -> taskViewModel.updateTaskCompletion(task, isChecked) },)
                }
                Spacer(modifier = Modifier.weight(0.1f))
            }
        }

        if (showSearchPopup) {
            FamilyMemberGridPopup(
                familyMembers = members,
                navController = navController, // Pass the NavController here
                onDismiss = { showSearchPopup = false },
                tasks = tasks // Pass tasks here to the popup
            )
        }

        // Show Create Task Dialog if triggered
        if (showCreateTaskDialog) {
            CreateTaskDialog(
                onDismissRequest = { showCreateTaskDialog = false },
                onConfirm = { title, description, date, selectedMembers ->
                    taskViewModel.createTask(title, description, date, selectedMembers)
                    showCreateTaskDialog = false
                },
                familyMembers = members // Pass the family members here
            )
        }
    }


}


/**
 * Composable function that displays the top section of the Chore List screen with date selection.
 *
 * @param onDateChanged Callback invoked when the date is changed.
 */
@Composable
fun TopSectione(onDateChanged: (Date) -> Unit) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val calendar = remember { Calendar.getInstance() }
    var currentDate by remember { mutableStateOf(dateFormat.format(calendar.time)) }

    // Function to update the date
    fun updateDate(days: Int) {
        calendar.add(Calendar.DATE, days)
        val newDate = calendar.time
        currentDate = dateFormat.format(newDate)
        onDateChanged(newDate)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFAE8D8D))
            .height(150.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.3f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIos,
                    contentDescription = "Go Back",
                    tint = Color(0xFFF5E5DB),
                    modifier = Modifier
                        .clickable { updateDate(-1) } // Move to previous date
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.4f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentDate, // Display the current date
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF5E5DB),
                    textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.3f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowForwardIos,
                    contentDescription = "Go Forward",
                    tint = Color(0xFFF5E5DB),
                    modifier = Modifier
                        .clickable { updateDate(1) } // Move to next date
                )
            }
        }
    }
}

/**
 * Composable function that displays a list of chore tasks using a LazyColumn.
 *
 * @param choreItems A list of [Task] objects to display.
 * @param onUpdateTask Callback to update a task.
 * @param onDeleteTask Callback to delete a task.
 * @param onCheckChanged Callback invoked when a task's completion status is changed.
 * @param familyMembers A list of [FamilyMember] objects associated with tasks.
 */
@Composable
fun ChoreList(
    choreItems: List<Task>,
    onUpdateTask: (Task, String, String, Date, List<FamilyMember>) -> Unit,  // Update here to accept Date
    onDeleteTask: (Task) -> Unit,
    onCheckChanged: (Task, Boolean) -> Unit,
    familyMembers: List<FamilyMember>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        items(choreItems) { choreItem ->
            ChoreListItem(task = choreItem, onUpdateTask = onUpdateTask,
                onDeleteTask = onDeleteTask, familyMembers=familyMembers,
                onCheckChanged=onCheckChanged)
        }
    }
}
