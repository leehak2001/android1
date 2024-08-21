package ac.il.hit.familiychoreapp.family.domain

import ac.il.hit.familiychoreapp.chores.domain.models.Task
import ac.il.hit.familiychoreapp.chores.domain.models.TaskDTO
import ac.il.hit.familiychoreapp.chores.domain.repositories.TaskRepository
import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
import ac.il.hit.familiychoreapp.family.domain.models.FamilyMemberDTO
import ac.il.hit.familiychoreapp.family.domain.repositories.FamilyRepository
import kotlinx.coroutines.runBlocking
import org.mongodb.kbson.ObjectId


/**
 * Extension function to convert a FamilyMember Realm object to its corresponding DTO.
 *
 * @return The FamilyMemberDTO representation of the FamilyMember object.
 */
fun FamilyMember.toDTO() = FamilyMemberDTO(
    id = _id.toHexString(),
    name = name,
    age = age,
    profilePicture = profilePicture,
)

/**
 * Extension function to convert a Task Realm object to its corresponding DTO.
 *
 * @return The TaskDTO representation of the Task object.
 */
fun Task.toDTO() = TaskDTO(
    id = _id.toHexString(),
    title = title,
    description = description,
    date = date,
    isChecked = isChecked
)

/**
 * Extension function to convert a FamilyMemberDTO to a Realm object.
 * Utilizes the FamilyRepository to find an existing member, updating it if found.
 * If not found, a new FamilyMember object is created.
 *
 * @return The FamilyMember Realm object representation of the FamilyMemberDTO.
 */
fun FamilyMemberDTO.toRealmObject(): FamilyMember {
    return runBlocking {
        val existingMember = FamilyRepository().getMemberById(id)
        if (existingMember != null) {
            // Update existing member with new values if necessary
            existingMember.apply {
                this.name = name
                this.age = age
                this.profilePicture = profilePicture
            }
        } else {
            // Create new member if it doesn't exist
            FamilyMember().apply {
                _id = ObjectId(id)
                this.name = name
                this.age = age
                this.profilePicture = profilePicture
            }
        }
    }
}

/**
 * Extension function to convert a TaskDTO to a Realm object.
 * Utilizes the TaskRepository to find an existing task, updating it if found.
 * If not found, a new Task object is created.
 *
 * @return The Task Realm object representation of the TaskDTO.
 */
fun TaskDTO.toRealmObject(): Task {
    return runBlocking {
        val existingTask = TaskRepository().getTaskById(id)
        if (existingTask != null) {
            // Update existing task with new values if necessary
            existingTask.apply {
                this.title = title
                this.description = description
                this.date = date
                this.isChecked = isChecked
            }
        } else {
            // Create new task if it doesn't exist
            Task().apply {
                _id = ObjectId(id)
                this.title = title
                this.description = description
                this.date = date
                this.isChecked = isChecked
            }
        }
    }
}


/**
 * Extension function to convert a list of TaskDTOs to a list of Task Realm objects.
 *
 * @return A list of Task Realm objects.
 */
fun List<TaskDTO>.toRealmObjects(): List<Task> {
    return this.map { it.toRealmObject() }
}
