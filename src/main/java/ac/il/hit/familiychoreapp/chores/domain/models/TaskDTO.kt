package ac.il.hit.familiychoreapp.chores.domain.models

data class TaskDTO(
    val id: String,
    val title: String,
    val description: String,
    val date: Long,
    val isChecked: Boolean
)
