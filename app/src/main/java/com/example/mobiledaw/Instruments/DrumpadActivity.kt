package com.example.mobiledaw.Instruments
import android.content.Intent
import android.media.AudioAttributes
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.media.SoundPool
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageButton
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mobiledaw.MainActivity
import com.example.mobiledaw.R


class DrumpadActivity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private var soundMap: Map<Int, Int> = mutableMapOf()
    private var currentOctave: Int = 3 // Initial octave
    private var isRecording = false
    private val recordedNotes = mutableListOf<Pair<Int, Int>>()
    private var playbackIndex = 0
    private var lastRecordedNoteTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drumpad)

        // Initialize SoundPool
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(7) // Number of simultaneous sounds
            .setAudioAttributes(audioAttributes)
            .build()


        findViewById<Button>(R.id.drumpadHome).setOnClickListener { returnHome() }

        findViewById<Button>(R.id.square1).setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                    val pressure = event.getPressure(event.actionIndex)
                    playSound(R.id.square1, pressure)
                }
            }
            true
        }


        findViewById<Button>(R.id.square2).setOnClickListener { playSound(R.id.square2, 0.1F) }
        findViewById<Button>(R.id.square3).setOnClickListener { playSound(R.id.square3, 0.2F) }
        findViewById<Button>(R.id.square4).setOnClickListener { playSound(R.id.square4, 0.1F) }
        findViewById<Button>(R.id.square5).setOnClickListener { playSound(R.id.square5, 0.1F) }
        findViewById<Button>(R.id.square6).setOnClickListener { playSound(R.id.square6, 0.1F) }
        findViewById<Button>(R.id.square7).setOnClickListener { playSound(R.id.square7, 0.1F) }
        findViewById<Button>(R.id.square8).setOnClickListener { playSound(R.id.square8, 1F) }

        findViewById<ImageButton>(R.id.drumpadRecord).setOnClickListener { toggleRecording() }
        findViewById<ImageButton>(R.id.drumpadPlay).setOnClickListener { playRecordedNotes() }

        loadSounds()

    }

    private fun toggleRecording() {
        isRecording = !isRecording

        if (isRecording) {
            recordedNotes.clear()
            lastRecordedNoteTime = System.currentTimeMillis() // Record the time when recording starts
        }
    }

    private fun playSound(buttonId: Int, pressure: Float) {
        val soundId = soundMap[buttonId] ?: return
        val currentTime = System.currentTimeMillis()

        if (isRecording) {
            val timeBetweenNotes = currentTime - lastRecordedNoteTime
            lastRecordedNoteTime = currentTime // Update the last recorded note time
            recordedNotes.add(Pair(buttonId, timeBetweenNotes.toInt())) // Store the buttonId and timeBetweenNotes as a Pair
            println("recordedNotes after adding new note: $recordedNotes")
        }

        val volume = pressure.coerceIn(0.2f, 1.0f) // Limit volume to a reasonable range
        val streamId = soundPool.play(soundId, volume, volume, 1, 0, 1.0f)

        val shorterDuration = 300
        Handler().postDelayed({
            soundPool.stop(streamId)
        }, shorterDuration.toLong())
    }

    private fun playNextRecordedNote() {
        if (playbackIndex < recordedNotes.size) {
            val (noteId, timeBetweenNotes) = recordedNotes[playbackIndex]

            Handler().postDelayed({
                playSound(noteId, 1F)
                playbackIndex++
                playNextRecordedNote()
            }, timeBetweenNotes.toLong())
        }
    }

    private fun playRecordedNotes() {
        playbackIndex = 0
        playNextRecordedNote()
    }
    private fun loadSounds() {
        soundMap = mapOf(
            R.id.square1 to soundPool.load(this, R.raw.drum808, 1),
            R.id.square2 to soundPool.load(this, R.raw.drum809, 1),
            R.id.square3 to soundPool.load(this, R.raw.drumclap, 1),
            R.id.square4 to soundPool.load(this, R.raw.drumcrash, 1),
            R.id.square5 to soundPool.load(this, R.raw.drumcrash2, 1),
            R.id.square6 to soundPool.load(this, R.raw.drumhat, 1),
            R.id.square7 to soundPool.load(this, R.raw.drumkick1, 1),
            R.id.square8 to soundPool.load(this, R.raw.drumsnare1, 1)
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    val homeActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
        if(result.resultCode == RESULT_CANCELED){
            Log.d("MainActivity","Switch to Piano Activity Cancelled")
        }else{
            Log.d("MainActivity", "Switched to Piano activity")

        }
    }

    private fun returnHome(){
        val homeActivityIntent: Intent = Intent(this,MainActivity::class.java)
        homeActivityLauncher.launch(homeActivityIntent)
    }

}

