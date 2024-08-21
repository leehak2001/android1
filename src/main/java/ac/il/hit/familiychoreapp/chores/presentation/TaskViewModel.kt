package ac.il.hit.familiychoreapp.chores.presentation

import ac.il.hit.familiychoreapp.chores.domain.models.Task
import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
import ac.il.hit.familiychoreapp.chores.domain.repositories.TaskRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

/**
 * ViewModel class for managing tasks in the app.
 * Provides a layer between the UI and the TaskRepository, handling task-related operations.
 */
class TaskViewModel() : ViewModel() {

    private val taskRepository = TaskRepository()

    // MutableStateFlow to hold the list of tasks. Internal use within the ViewModel.
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())

    // Public StateFlow to expose the task list to the UI. Immutable.
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        // Fetch tasks when the ViewModel is initialized
        viewModelScope.launch {
            taskRepository.getAllTasks().collect { taskList ->
                _tasks.value = taskList
            }
        }
    }

    /**
     * Deletes a task from the repository and updates the task list in the ViewModel.
     *
     * @param task The Task object to delete.
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
            // update the tasks after deletion
            _tasks.value = taskRepository.getAllTasks().first()
        }
    }

    /**
     * Updates a task's details in the repository and updates the task list in the ViewModel.
     *
     * @param task The Task object to update.
     * @param newTitle The new title for the task.
     * @param newDescription The new description for the task.
     * @param newDate The new date for the task.
     * @param selectedMembers The list of FamilyMember objects associated with the task.
     */
    fun updateTask(task: Task, newTitle: String, newDescription: String, newDate: Date, selectedMembers: List<FamilyMember>) {
        viewModelScope.launch {
            taskRepository.updateTask(task, newTitle, newDescription, newDate, selectedMembers)
            // update the tasks after the update
            _tasks.value = taskRepository.getAllTasks().first()
        }
    }

    /**
     * Updates the completion status of a task in the repository and updates the task list in the ViewModel.
     *
     * @param task The Task object to update.
     * @param isChecked The new completion status for the task.
     */
    fun updateTaskCompletion(task: Task, isChecked: Boolean) {
        viewModelScope.launch {
            taskRepository.updateTaskCompletion(task, isChecked)
            // update the tasks after completion status change
            _tasks.value = taskRepository.getAllTasks().first()
        }
    }

    /**
     * Creates a new task in the repository and updates the task list in the ViewModel.
     *
     * @param title The title for the new task.
     * @param description The description for the new task.
     * @param date The date for the new task.
     * @param selectedMembers The list of FamilyMember objects associated with the task.
     */
    fun createTask(title: String, description: String, date: Date, selectedMembers: List<FamilyMember>) {
        viewModelScope.launch {
            taskRepository.createTask(title, description, date, selectedMembers)
            // update the tasks after creation
            _tasks.value = taskRepository.getAllTasks().first()
        }
    }
}
