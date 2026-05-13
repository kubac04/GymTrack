package com.example.gymtrack.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.gymtrack.data.local.dao.*
import com.example.gymtrack.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        WorkoutEntity::class,
        ExerciseEntity::class,
        WorkoutExerciseEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gymtrack_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(SeedCallback())
                .build()
                .also { INSTANCE = it }
            }
        }
    }

    private class SeedCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            seedExercises(db)
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            val cursor = db.query("SELECT COUNT(*) FROM Exercises")
            cursor.moveToFirst()
            val count = cursor.getInt(0)
            cursor.close()
            if (count == 0) seedExercises(db)
        }

        private fun seedExercises(db: SupportSQLiteDatabase) {
            data class Ex(val name: String, val desc: String, val steps: String, val group: String, val kcal: Double, val video: String)

            val list = listOf(
                Ex("Bench Press",
                    "Classic chest exercise targeting pectoral muscles.",
                    "1. Lie flat on a bench and grip the barbell slightly wider than shoulder-width.\n2. Lower the bar to your chest in a controlled motion.\n3. Press the bar back up to the starting position.\n4. Keep your feet flat on the floor and back slightly arched.",
                    "Chest", 0.55,
                    "https://www.youtube.com/embed/SCVCLChPQFY"),
                Ex("Squat",
                    "Fundamental lower-body exercise targeting quads, glutes and hamstrings.",
                    "1. Stand with feet shoulder-width apart, bar resting on your upper back.\n2. Brace your core and push your hips back as you bend your knees.\n3. Lower until thighs are at least parallel to the floor.\n4. Drive through your heels to return to standing position.",
                    "Legs", 0.85,
                    "https://www.youtube.com/embed/ultWZbUMPL8"),
                Ex("Deadlift",
                    "Full-body compound lift focusing on posterior chain strength.",
                    "1. Stand with feet hip-width apart, bar over mid-foot.\n2. Hinge at hips, bend knees and grip the bar outside your legs.\n3. Keep back flat, chest up, and pull the bar up along your legs.\n4. Lock out hips and knees at the top, then lower with control.",
                    "Back", 1.0,
                    "https://www.youtube.com/embed/op9kVnSso6Q"),
                Ex("Pull-Ups",
                    "Upper-body exercise targeting the lats and biceps.",
                    "1. Hang from a bar with palms facing away, slightly wider than shoulders.\n2. Pull your shoulder blades down and together.\n3. Drive your elbows toward your hips as you pull your chin above the bar.\n4. Lower yourself slowly back to a dead hang.",
                    "Back", 0.65,
                    "https://www.youtube.com/embed/eGo4IYlbE5g"),
                Ex("Incline Flye",
                    "Isolation exercise for the upper chest.",
                    "1. Set bench to 30–45 degree incline, hold dumbbells above chest.\n2. With a slight bend in elbows, lower dumbbells in an arc to the sides.\n3. Stretch the chest at the bottom of the movement.\n4. Squeeze the chest as you bring the dumbbells back together.",
                    "Chest", 0.4,
                    "https://www.youtube.com/embed/eozdVDA78K0"),
                Ex("Leg Extension",
                    "Isolation movement for the quadriceps.",
                    "1. Sit in the leg extension machine, pads resting on your lower shins.\n2. Extend your legs upward until fully straight.\n3. Hold briefly at the top, squeezing your quadriceps.\n4. Lower the weight slowly back to the starting position.",
                    "Legs", 0.3,
                    "https://www.youtube.com/embed/YyvSfVjQeL0"),
                Ex("Bulgarian Split Squat",
                    "Single-leg squat variation for quad and glute development.",
                    "1. Stand 2 feet in front of a bench, place rear foot on it.\n2. Lower your hips until front thigh is parallel to the floor.\n3. Keep your torso upright and front knee tracking over toes.\n4. Push through your front heel to return to the starting position.",
                    "Legs", 0.75,
                    "https://www.youtube.com/embed/2C-uNgKwPLE"),
                Ex("Shoulder Press",
                    "Overhead press targeting the deltoids and triceps.",
                    "1. Hold dumbbells at shoulder height, palms facing forward.\n2. Press the weights overhead until arms are fully extended.\n3. Keep core engaged and avoid arching your lower back.\n4. Lower the weights back to shoulder height with control.",
                    "Shoulders", 0.5,
                    "https://www.youtube.com/embed/qEwKCR5JCog"),
                Ex("Barbell Row",
                    "Compound back exercise for thickness and strength.",
                    "1. Hinge at hips with a slight knee bend, bar hanging at arm's length.\n2. Pull the bar toward your lower chest, leading with elbows.\n3. Squeeze shoulder blades together at the top.\n4. Lower the bar back with control.",
                    "Back", 0.6,
                    "https://www.youtube.com/embed/roCP6wCXPqo"),
                Ex("Bicep Curl",
                    "Classic isolation exercise for the biceps.",
                    "1. Stand with dumbbells in each hand, arms fully extended, palms up.\n2. Curl the weights up toward your shoulders, elbows close to sides.\n3. Squeeze your biceps at the top.\n4. Lower slowly back to the starting position.",
                    "Arms", 0.3,
                    "https://www.youtube.com/embed/ykJmrZ5v0Oo"),
                Ex("Tricep Dips",
                    "Bodyweight exercise targeting the triceps.",
                    "1. Position hands on parallel bars, arms straight.\n2. Lower your body by bending elbows until upper arms are parallel to floor.\n3. Keep torso upright to focus work on triceps.\n4. Push back up to starting position, locking out elbows.",
                    "Arms", 0.45,
                    "https://www.youtube.com/embed/6kALZikXxLc"),
                Ex("Lat Pulldown",
                    "Cable exercise for wide back development.",
                    "1. Sit at cable machine, grip bar wider than shoulder-width.\n2. Lean back slightly and pull bar down to your upper chest.\n3. Squeeze lats at bottom and hold briefly.\n4. Slowly return to start, fully extending arms.",
                    "Back", 0.5,
                    "https://www.youtube.com/embed/lueEJGjTuPQ")
            )

            list.forEach { ex ->
                db.execSQL(
                    "INSERT OR IGNORE INTO Exercises (name, description, steps, muscleGroup, kcalPerRep, videoUrl) VALUES (?, ?, ?, ?, ?, ?)",
                    arrayOf(ex.name, ex.desc, ex.steps, ex.group, ex.kcal, ex.video)
                )
            }
        }
    }
}
