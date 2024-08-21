package ac.il.hit.familiychoreapp


import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
import ac.il.hit.familiychoreapp.chores.domain.models.Task
import android.app.Application
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

/**
 * Application class for initializing the Realm database.
 * This class sets up the Realm instance with the appropriate schema on application start.
 */
class MyApp: Application() {

    /* declering a static member realm that will be initialized later, but guaranteed
     to be assigned a value before it's used */
    companion object {
        lateinit var realm: Realm
    }

    /**
     * Initializes the Realm database with the schema for Task and FamilyMember classes
     * when the application is created.
     */
    override fun onCreate() {
        super.onCreate()
        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(
                    Task::class,
                    FamilyMember :: class,
                )
            )
        )
    }
}

//to use this code only when we want to restart the db after a concreate change as it deletes it.

//package ac.il.hit.familiychoreapp
//
//import ac.il.hit.familiychoreapp.chores.domain.models.Task
//import ac.il.hit.familiychoreapp.family.domain.models.FamilyMember
//import android.app.Application
//import io.realm.kotlin.Realm
//import io.realm.kotlin.RealmConfiguration
//import java.io.File
//
//class MyApp : Application() {
//
//    companion object {
//        lateinit var realm: Realm
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//
//        // Define the Realm configuration
//        val config = RealmConfiguration.Builder(schema = setOf(Task::class, FamilyMember::class)).build()
//
//        // Get the path of the Realm file
//        val realmFile = File(config.path)
//
//        // Delete the existing Realm file
//        if (realmFile.exists()) {
//            realmFile.deleteRecursively()
//        }
//
//        // Initialize Realm with the new configuration
//        realm = Realm.open(config)
//    }
//}
