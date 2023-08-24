# WorkoutApp
WorkoutApp is a Kotlin Application in which you can go through 12 different exercises defined by your own custom duration and a rest time between them.

Consists from: 
- Exercise Tab, in which you can pause, resume, skip or redo an exercise running with a CountDownTimer.
- BMI Tab, in order to calculate your BMI and your fitness in both Metric and US units.
- History Tab, for recording your progress via a Room Database.

![workout_1](https://github.com/ThanosArab/WorkoutApp/assets/75016979/09748684-c062-4e93-8402-1f6d4fcd2865) ![workout_2](https://github.com/ThanosArab/WorkoutApp/assets/75016979/0304a4b7-675a-4d69-a96e-c45ba478789a)
![workout_3](https://github.com/ThanosArab/WorkoutApp/assets/75016979/695c8550-bb2e-41ad-b47e-2556576b7460) ![workout_4](https://github.com/ThanosArab/WorkoutApp/assets/75016979/16d7d1a0-a016-4217-bc66-0f2238caef16)


## Dependencies
    def room_version = "2.5.2"
    def activity_version = "1.7.2"

    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    implementation "androidx.activity:activity-ktx:$activity_version"

    implementation 'androidx.core:core-ktx:1.10.1'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'

## Launch
Download the zip file or launch the application via Github.
