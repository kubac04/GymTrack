package com.example.gymtrack.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.gymtrack.data.local.dao.*
import com.example.gymtrack.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gymtrack_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(SeedCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class SeedCallback(private val context: Context) : Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            CoroutineScope(Dispatchers.IO).launch {
                val dao = getDatabase(context).exerciseDao()
                if (dao.getCount() == 0) {
                    seedExercises(dao)
                }
            }
        }

        private suspend fun seedExercises(dao: ExerciseDao) {
            val exercises = listOf(
                ExerciseEntity(
                    name = "Bench Press",
                    description = "Classic chest exercise targeting pectoral muscles.",
                    steps = "1. Lie flat on a bench and grip the barbell slightly wider than shoulder-width.\n2. Lower the bar to your chest in a controlled motion.\n3. Press the bar back up to the starting position.\n4. Keep your feet flat on the floor and back slightly arched.",
                    muscleGroup = "Chest",
                    kcalPerRep = 0.55,
                    videoUrl = "https://www.youtube.com/embed/SCVCLChPQFY"
                ),
                ExerciseEntity(
                    name = "Squat",
                    description = "Fundamental lower-body exercise targeting quads, glutes and hamstrings.",
                    steps = "1. Stand with feet shoulder-width apart, bar resting on your upper back.\n2. Brace your core and push your hips back as you bend your knees.\n3. Lower until thighs are at least parallel to the floor.\n4. Drive through your heels to return to standing position.",
                    muscleGroup = "Legs",
                    kcalPerRep = 0.85,
                    videoUrl = "https://www.youtube.com/embed/ultWZbUMPL8"
                ),
                ExerciseEntity(
                    name = "Deadlift",
                    description = "Full-body compound lift focusing on posterior chain strength.",
                    steps = "1. Stand with feet hip-width apart, bar over mid-foot.\n2. Hinge at hips, bend knees and grip the bar outside your legs.\n3. Keep back flat, chest up, and pull the bar up along your legs.\n4. Lock out hips and knees at the top, then lower with control.",
                    muscleGroup = "Back",
                    kcalPerRep = 1.0,
                    videoUrl = "https://www.youtube.com/embed/op9kVnSso6Q"
                ),
                ExerciseEntity(
                    name = "Pull-Ups",
                    description = "Upper-body exercise targeting the lats and biceps.",
                    steps = "1. Hang from a bar with palms facing away, slightly wider than shoulders.\n2. Pull your shoulder blades down and together.\n3. Drive your elbows toward your hips as you pull your chin above the bar.\n4. Lower yourself slowly back to a dead hang.",
                    muscleGroup = "Back",
                    kcalPerRep = 0.65,
                    videoUrl = "https://www.youtube.com/embed/eGo4IYlbE5g"
                ),
                ExerciseEntity(
                    name = "Incline Flye",
                    description = "Isolation exercise for the upper chest.",
                    steps = "1. Set bench to 30–45 degree incline, hold dumbbells above chest.\n2. With a slight bend in elbows, lower dumbbells in an arc to the sides.\n3. Stretch the chest at the bottom of the movement.\n4. Squeeze the chest as you bring the dumbbells back together.",
                    muscleGroup = "Chest",
                    kcalPerRep = 0.4,
                    videoUrl = "https://www.youtube.com/embed/eozdVDA78K0"
                ),
                ExerciseEntity(
                    name = "Leg Extension",
                    description = "Isolation movement for the quadriceps.",
                    steps = "1. Sit in the leg extension machine, pads resting on your lower shins.\n2. Extend your legs upward until fully straight.\n3. Hold briefly at the top, squeezing your quadriceps.\n4. Lower the weight slowly back to the starting position.",
                    muscleGroup = "Legs",
                    kcalPerRep = 0.3,
                    videoUrl = "https://www.youtube.com/embed/YyvSfVjQeL0"
                ),
                ExerciseEntity(
                    name = "Bulgarian Split Squat",
                    description = "Single-leg squat variation for quad and glute development.",
                    steps = "1. Stand 2 feet in front of a bench, place rear foot on it.\n2. Lower your hips until front thigh is parallel to the floor.\n3. Keep your torso upright and front knee tracking over toes.\n4. Push through your front heel to return to the starting position.",
                    muscleGroup = "Legs",
                    kcalPerRep = 0.75,
                    videoUrl = "https://www.youtube.com/embed/2C-uNgKwPLE"
                ),
                ExerciseEntity(
                    name = "Shoulder Press",
                    description = "Overhead press targeting the deltoids and triceps.",
                    steps = "1. Hold dumbbells at shoulder height, palms facing forward.\n2. Press the weights overhead until arms are fully extended.\n3. Keep core engaged and avoid arching your lower back.\n4. Lower the weights back to shoulder height with control.",
                    muscleGroup = "Shoulders",
                    kcalPerRep = 0.5,
                    videoUrl = "https://www.youtube.com/embed/qEwKCR5JCog"
                ),
                ExerciseEntity(
                    name = "Barbell Row",
                    description = "Compound back exercise for thickness and strength.",
                    steps = "1. Hinge at hips with a slight knee bend, bar hanging at arm's length.\n2. Pull the bar toward your lower chest, leading with elbows.\n3. Squeeze shoulder blades together at the top.\n4. Lower the bar back with control.",
                    muscleGroup = "Back",
                    kcalPerRep = 0.6,
                    videoUrl = "https://www.youtube.com/embed/roCP6wCXPqo"
                ),
                ExerciseEntity(
                    name = "Bicep Curl",
                    description = "Classic isolation exercise for the biceps.",
                    steps = "1. Stand with dumbbells in each hand, arms fully extended, palms up.\n2. Curl the weights up toward your shoulders, elbows close to sides.\n3. Squeeze your biceps at the top.\n4. Lower slowly back to the starting position.",
                    muscleGroup = "Arms",
                    kcalPerRep = 0.3,
                    videoUrl = "https://www.youtube.com/embed/ykJmrZ5v0Oo"
                ),
                ExerciseEntity(
                    name = "Tricep Dips",
                    description = "Bodyweight exercise targeting the triceps.",
                    steps = "1. Position hands on parallel bars, arms straight.\n2. Lower your body by bending elbows until upper arms are parallel to floor.\n3. Keep torso upright to focus work on triceps.\n4. Push back up to starting position, locking out elbows.",
                    muscleGroup = "Arms",
                    kcalPerRep = 0.45,
                    videoUrl = "https://www.youtube.com/embed/6kALZikXxLc"
                ),
                ExerciseEntity(
                    name = "Lat Pulldown",
                    description = "Cable exercise for wide back development.",
                    steps = "1. Sit at cable machine, grip bar wider than shoulder-width.\n2. Lean back slightly and pull bar down to your upper chest.\n3. Squeeze lats at bottom and hold briefly.\n4. Slowly return to start, fully extending arms.",
                    muscleGroup = "Back",
                    kcalPerRep = 0.5,
                    videoUrl = "https://www.youtube.com/embed/lueEJGjTuPQ"
                ),
                ExerciseEntity(
                    name = "Leg Press",
                    description = "Machine compound exercise targeting quads, hamstrings and glutes.",
                    steps = "1. Sit in the leg press machine with feet shoulder-width apart on the platform.\n2. Unlock the safety handles and lower the platform by bending your knees to 90 degrees.\n3. Press through your heels to extend your legs fully without locking out knees.\n4. Control the descent and repeat.",
                    muscleGroup = "Legs",
                    kcalPerRep = 0.7,
                    videoUrl = "https://www.youtube.com/embed/IZxyjW7MPJQ"
                ),
                ExerciseEntity(
                    name = "Romanian Deadlift",
                    description = "Hip-hinge movement targeting hamstrings and glutes.",
                    steps = "1. Stand with feet hip-width apart, hold barbell in front of thighs.\n2. Push hips back and lower the bar along your legs, keeping back flat.\n3. Lower until you feel a strong stretch in your hamstrings.\n4. Drive hips forward to return to standing, squeezing glutes at the top.",
                    muscleGroup = "Legs",
                    kcalPerRep = 0.8,
                    videoUrl = "https://www.youtube.com/embed/JCXUYuzwNrM"
                ),
                ExerciseEntity(
                    name = "Lateral Raise",
                    description = "Isolation exercise for the lateral deltoids.",
                    steps = "1. Stand with dumbbells at your sides, slight bend in elbows.\n2. Raise both arms out to the sides until parallel to the floor.\n3. Pause briefly at the top, controlling the movement.\n4. Slowly lower the dumbbells back to starting position.",
                    muscleGroup = "Shoulders",
                    kcalPerRep = 0.25,
                    videoUrl = "https://www.youtube.com/embed/3VcKaXpzqRo"
                ),
                ExerciseEntity(
                    name = "Face Pull",
                    description = "Cable exercise for rear deltoids and rotator cuff health.",
                    steps = "1. Set cable pulley to head height, attach rope handle.\n2. Grip rope with both hands, palms facing in, step back.\n3. Pull rope toward your face, flaring elbows out to sides.\n4. Squeeze rear delts at the end, then return with control.",
                    muscleGroup = "Shoulders",
                    kcalPerRep = 0.3,
                    videoUrl = "https://www.youtube.com/embed/rep-qVOkqgk"
                ),
                ExerciseEntity(
                    name = "Hammer Curl",
                    description = "Curl variation targeting brachialis and brachioradialis.",
                    steps = "1. Hold dumbbells with a neutral grip (palms facing each other).\n2. Keeping elbows at your sides, curl the weights up toward your shoulders.\n3. Squeeze at the top of the movement.\n4. Lower slowly back to the starting position.",
                    muscleGroup = "Arms",
                    kcalPerRep = 0.3,
                    videoUrl = "https://www.youtube.com/embed/zC3nLlEvin4"
                ),
                ExerciseEntity(
                    name = "Skull Crusher",
                    description = "Isolation exercise for triceps using a barbell or EZ-bar.",
                    steps = "1. Lie on a bench, hold barbell with narrow grip directly above chest.\n2. Keeping upper arms fixed, lower the bar toward your forehead by bending elbows.\n3. Stop just before the bar touches your head.\n4. Extend elbows to press the bar back to the starting position.",
                    muscleGroup = "Arms",
                    kcalPerRep = 0.35,
                    videoUrl = "https://www.youtube.com/embed/d_KZxkY_0cM"
                ),
                ExerciseEntity(
                    name = "Push-Up",
                    description = "Classic bodyweight exercise for chest, shoulders and triceps.",
                    steps = "1. Start in a high plank position, hands slightly wider than shoulders.\n2. Lower your chest to the floor, keeping elbows at a 45-degree angle.\n3. Keep your core tight and body in a straight line throughout.\n4. Push through your palms to return to the starting position.",
                    muscleGroup = "Chest",
                    kcalPerRep = 0.35,
                    videoUrl = "https://www.youtube.com/embed/IODxDxX7oi4"
                ),
                ExerciseEntity(
                    name = "Seated Cable Row",
                    description = "Cable exercise for middle back thickness and strength.",
                    steps = "1. Sit at cable row machine, feet on pads, knees slightly bent.\n2. Grip the handle and sit upright with a neutral spine.\n3. Pull the handle toward your lower abdomen, squeezing shoulder blades.\n4. Slowly extend arms back to starting position with control.",
                    muscleGroup = "Back",
                    kcalPerRep = 0.5,
                    videoUrl = "https://www.youtube.com/embed/GZbfZ033f74"
                )
            )
            exercises.forEach { dao.insertExercise(it) }
        }
    }
}
