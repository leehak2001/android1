/*
 * Repository: Manages data access and persistence, providing a clean API for data operations.
 * It abstracts the complexities of data sources like databases or network services,
 * ensuring that data handling logic is centralized and reusable.
 */

package ac.il.hit.familiychoreapp.chores.domain.repositories

import ac.il.hit.familiychoreapp.MyApp
import ac.il.hit.familiychoreapp.chores.domain.models.Task
import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import java.util.Date

class TaskRepository() {

    private val realm = MyApp.realm

    // Get a task by its ID
    suspend fun getTaskById(id: String): Task? {
        return realm.query<Task>("_id = $0", ObjectId(id)).first().find()
    }

    /*
     * Converts the Realm results to a Flow for asynchronous updates.
       A Flow is a type in Kotlin that represents a stream of data which can be emitted asynchronously over time.
     */
    fun getAllTasks(): Flow<List<Task>> {
        return realm.query<Task>()
            .asFlow()
            .map { resultsChange ->
                resultsChange.list // Directly access the list
            }
    }

    /*
     * A suspended function in Kotlin is a function that can pause its execution and resume later without blocking the current thread
     */
    suspend fun deleteTask(task: Task) {
        realm.write {
            // Find the latest version of the task (if it exists) else exit write.
            val latestTask = findLatest(task) ?: return@write
            delete(latestTask)
        }
    }

    suspend fun updateTask(task: Task, newTitle: String, newDescription: String, newDate: Date, selectedMembers: List<FamilyMember>) {
        realm.write {
            val latestTask = findLatest(task)
            if (latestTask != null) {
                latestTask.title = newTitle
                latestTask.description = newDescription
                latestTask.setDate(newDate)
                latestTask.enrolledFamilyMembers.clear()
                latestTask.enrolledFamilyMembers.addAll(selectedMembers.map { findLatest(it) ?: it })
            }
        }
    }

    suspend fun updateTaskCompletion(task: Task, isChecked: Boolean) {
        realm.write {
            val latestTask = findLatest(task)
            if (latestTask != null) {
                latestTask.isChecked = isChecked
            }
        }
    }

    suspend fun createTask(title: String, description: String, date: Date, selectedMembers: List<FamilyMember>) {
        realm.write {
            val task = Task().apply {
                this.title = title
                this.description = description
                setDate(date)
                isChecked = false
                enrolledFamilyMembers.addAll(selectedMembers.map { findLatest(it) ?: it })
            }
            // Copies the task to Realm, overwriting any existing object with the same primary key.
            copyToRealm(task, updatePolicy = UpdatePolicy.ALL)
        }
    }
}
