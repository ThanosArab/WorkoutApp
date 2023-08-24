package com.smartath.workoutapp

object Constants {

    fun defaultExerciseList(): ArrayList<ExerciseModel>{
        val exerciseList = ArrayList<ExerciseModel>()

        val jumpingJacks = ExerciseModel(1, "Jumping Jacks", R.drawable.ic_jumping_jacks, false, false)
        exerciseList.add(jumpingJacks)

        val wallSit = ExerciseModel(2, "Wall Sit", R.drawable.ic_wall_sit, false, false)
        exerciseList.add(wallSit)

        val pushUp = ExerciseModel(3, "Push Up", R.drawable.ic_push_up, false, false)
        exerciseList.add(pushUp)

        val abdominalCrunch = ExerciseModel(4, "Abdominal Crunch", R.drawable.ic_abdominal_crunch, false, false)
        exerciseList.add(abdominalCrunch)

        val stepUpOnChair = ExerciseModel(5, "Step-up Onto Chair", R.drawable.ic_step_up_onto_chair, false, false)
        exerciseList.add(stepUpOnChair)

        val squat = ExerciseModel(6, "Squat", R.drawable.ic_squat, false, false)
        exerciseList.add(squat)

        val tricepsDipOnChair = ExerciseModel(7, "Triceps Dip on Chair", R.drawable.ic_triceps_dip_on_chair, false, false)
        exerciseList.add(tricepsDipOnChair)

        val planck = ExerciseModel(8, "Planck", R.drawable.ic_plank, false, false)
        exerciseList.add(planck)

        val highKneesRunning = ExerciseModel(9, "High Knees Running In Place", R.drawable.ic_high_knees_running_in_place, false, false)
        exerciseList.add(highKneesRunning)

        val lunges = ExerciseModel(10, "Lunges", R.drawable.ic_lunge, false, false)
        exerciseList.add(lunges)

        val pushUpAndRotation = ExerciseModel(11, "Push up and Rotation", R.drawable.ic_jumping_jacks, false, false)
        exerciseList.add(pushUpAndRotation)

        val sidePlanck = ExerciseModel(12, "Side Planck", R.drawable.ic_side_plank, false, false)
        exerciseList.add(sidePlanck)

        return exerciseList
    }
}