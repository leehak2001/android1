/*
 * Repository: Manages data access and persistence, providing a clean API for data operations.
 * It abstracts the complexities of data sources like databases or network services,
 * ensuring that data handling logic is centralized and reusable.
 */


package ac.il.hit.familiychoreapp.family.domain.repositories

import ac.il.hit.familiychoreapp.MyApp
import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

/**
 * FamilyRepository is responsible for managing data access and persistence
 * related to family members in the app. It provides methods to create, read,
 * update, and delete family member records within a Realm database,
 * ensuring that all data operations are centralized and reusable.
 */
class FamilyRepository() {

    // Instance of Realm database
    private val realm = MyApp.realm

    /**
     * Retrieves a family member by their ID.
     *
     * @param id The unique identifier of the family member to retrieve.
     * @return The FamilyMember object if found, or null if no match is found.
     */
    suspend fun getMemberById(id: String): FamilyMember? {
        return realm.query<FamilyMember>("_id = $0", ObjectId(id)).first().find()
    }

    /**
     * Retrieves all family members from the database as a flow of lists.
     * This allows for observing changes to the family members in real-time.
     *
     * @return A Flow emitting lists of FamilyMember objects.
     */
    fun getAllMembers(): Flow<List<FamilyMember>> {
        return realm.query<FamilyMember>()
            .asFlow()
            .map { resultsChange ->
                resultsChange.list // Directly access the list
            }
    }

    /**
     * Deletes a family member from the database.
     *
     * @param member The FamilyMember object to delete.
     */
    suspend fun deleteMember(member: FamilyMember) {
        realm.write {
            val latestMember = findLatest(member) ?: return@write
            delete(latestMember)
        }
    }

    /**
     * Updates the details of an existing family member in the database.
     *
     * @param member The FamilyMember object to update.
     * @param newName The new name for the family member.
     * @param newAge The new age for the family member.
     * @param newPic The new profile picture resource ID for the family member.
     * @return True if the update was successful, false if the update failed or the new details are a duplicate.
     */
    suspend fun updateMember(member: FamilyMember, newName: String, newAge: Int, newPic: Int): Boolean {
        return try {
            realm.write {
                val isDuplicate = realm.query<FamilyMember>(
                    "name = $0 AND age = $1 AND profilePicture = $2",
                    newName, newAge, newPic
                ).first().find() != null

                if (isDuplicate) {
                    return@write false
                }

                val latestMember = findLatest(member)
                latestMember?.apply {
                    this.name = newName
                    this.age = newAge
                    this.profilePicture = newPic
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }


    /**
     * Creates a new family member in the database.
     *
     * @param name The name of the new family member.
     * @param age The age of the new family member.
     * @param profilePicture The profile picture resource ID for the new family member.
     * @return True if the creation was successful, false if the new member's details are a duplicate or the operation failed.
     */
    suspend fun createMember(name: String, age: Int, profilePicture: Int): Boolean {
        return try {
            realm.write {
                val isDuplicate = realm.query<FamilyMember>(
                    "name = $0 AND age = $1 AND profilePicture = $2",
                    name, age, profilePicture
                ).first().find() != null

                if (isDuplicate) {
                    return@write false
                }

                val member = FamilyMember().apply {
                    this.name = name
                    this.age = age
                    this.profilePicture = profilePicture
                }
                copyToRealm(member, updatePolicy = UpdatePolicy.ALL)
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
