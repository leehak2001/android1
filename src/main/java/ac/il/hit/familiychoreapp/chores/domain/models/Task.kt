package ac.il.hit.familiychoreapp.chores.domain.models

import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.Date

class Task : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var title: String = ""
    var description: String =""
    var date: Long = Date().time // Store the date as a Long (timestamp)
    var isChecked: Boolean = false
    var enrolledFamilyMembers: RealmList<FamilyMember> = realmListOf()

    // Helper method to get a Date object from the timestamp
    fun getDate(): Date {
        return Date(date)
    }

    // Helper method to set the Date object
    fun setDate(date: Date): Long {
        this.date = date.time
        return this.date
    }
}