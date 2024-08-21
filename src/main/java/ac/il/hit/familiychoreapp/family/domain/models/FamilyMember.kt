package ac.il.hit.familiychoreapp.family.domain.models

import ac.il.hit.familiychoreapp.R
import ac.il.hit.familiychoreapp.chores.domain.models.Task
import io.realm.kotlin.ext.backlinks
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

//RealmObject- to tell the realm db that we want this class to be a raelm object(table)
//amilyMember many to many Task
class FamilyMember: RealmObject {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var name: String = ""
    var age: Int = 0
    var profilePicture: Int = R.drawable.fox  // resource ID
    //beacuse wa want to have a list of it's corrent tasks
    val enrolledTasks: RealmResults<Task> by backlinks(Task::enrolledFamilyMembers)
}
