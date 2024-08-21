package ac.il.hit.familiychoreapp.family.presentation

import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
import ac.il.hit.familiychoreapp.family.domain.repositories.FamilyRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel class for managing family members in the app.
 * Provides a layer between the UI and the FamilyRepository, handling family member-related operations.
 */
class FamilyViewModel() : ViewModel() {

    private val familyRepository = FamilyRepository()

    // MutableStateFlow to hold the list of family members. Internal use within the ViewModel.
    private val _members = MutableStateFlow<List<FamilyMember>>(emptyList())

    // Public StateFlow to expose the family members list to the UI. Immutable.
    val members: StateFlow<List<FamilyMember>> = _members.asStateFlow()

    init {
        // Fetch family members when the ViewModel is initialized
        viewModelScope.launch {
            familyRepository.getAllMembers().collect { memberList ->
                _members.value = memberList
            }
        }
    }

    /**
     * Deletes a family member from the repository and updates the member list in the ViewModel.
     *
     * @param member The FamilyMember object to delete.
     */
    fun deleteMember(member: FamilyMember) {
        viewModelScope.launch {
            familyRepository.deleteMember(member)
            // update the members after deletion
            _members.value = familyRepository.getAllMembers().first()
        }
    }

    /**
     * Updates a family member's details in the repository and updates the member list in the ViewModel.
     *
     * @param member The FamilyMember object to update.
     * @param newName The new name for the family member.
     * @param newAge The new age for the family member.
     * @param newProfilePicture The new profile picture for the family member.
     */
    fun updateMember(member: FamilyMember, newName: String, newAge: Int, newPic: Int) {
        viewModelScope.launch {
            val result = familyRepository.updateMember(member, newName, newAge, newPic)
            if (result) {
                // update the members after the update
                _members.value = familyRepository.getAllMembers().first()
            }
        }
    }

    /**
     * Creates a new family member in the repository and updates the member list in the ViewModel.
     *
     * @param name The name for the new family member.
     * @param age The age for the new family member.
     * @param profilePicture The profile picture for the new family member.
     */
    fun createMember(name: String, age: Int, profilePicture: Int) {
        viewModelScope.launch {
            val result = familyRepository.createMember(name, age, profilePicture)
            if (result) {
                // update the members after creation
                _members.value = familyRepository.getAllMembers().first()
            }
        }
    }
}
