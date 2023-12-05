package com.example.mobiledaw.Instruments
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.media.SoundPool
import android.os.Environment
import android.os.Handler
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mobiledaw.MainActivity
import com.example.mobiledaw.R
import java.io.File
import java.io.IOException

class PianoActivity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private var soundMap: Map<Int, Int> = mutableMapOf()
    private var currentOctave: Int = 3 // Initial octave
    private var isRecording = false
    private val recordedNotes = mutableListOf<Pair<Int, Int>>()
    private var playbackIndex = 0
    private var lastRecordedNoteTime = 0L





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.piano)

        // Initialize SoundPool
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(7) // Number of simultaneous sounds
            .setAudioAttributes(audioAttributes)
            .build()


        findViewById<Button>(R.id.buttonOctaveUp).setOnClickListener { octaveUp() }
        findViewById<Button>(R.id.buttonOctaveDown).setOnClickListener { octaveDown() }
        findViewById<Button>(R.id.pianoHome).setOnClickListener { returnHome() }

        findViewById<Button>(R.id.button1).setOnClickListener { playSound(R.id.button1) }
        findViewById<Button>(R.id.button2).setOnClickListener { playSound(R.id.button2) }
        findViewById<Button>(R.id.button3).setOnClickListener { playSound(R.id.button3) }
        findViewById<Button>(R.id.button4).setOnClickListener { playSound(R.id.button4) }
        findViewById<Button>(R.id.button5).setOnClickListener { playSound(R.id.button5) }
        findViewById<Button>(R.id.button6).setOnClickListener { playSound(R.id.button6) }
        findViewById<Button>(R.id.button7).setOnClickListener { playSound(R.id.button7) }
        findViewById<Button>(R.id.button8).setOnClickListener { playSound(R.id.button8) }
        findViewById<Button>(R.id.button9).setOnClickListener { playSound(R.id.button9) }
        findViewById<Button>(R.id.button10).setOnClickListener { playSound(R.id.button10) }
        findViewById<Button>(R.id.button11).setOnClickListener { playSound(R.id.button11) }
        findViewById<Button>(R.id.button12).setOnClickListener { playSound(R.id.button12) }
        findViewById<Button>(R.id.button13).setOnClickListener { playSound(R.id.button13) }
        findViewById<Button>(R.id.button14).setOnClickListener { playSound(R.id.button14) }

        findViewById<Button>(R.id.blackButton1).setOnClickListener { playSound(R.id.blackButton1) }
        findViewById<Button>(R.id.blackButton2).setOnClickListener { playSound(R.id.blackButton2) }
        findViewById<Button>(R.id.blackButton3).setOnClickListener { playSound(R.id.blackButton3) }
        findViewById<Button>(R.id.blackButton4).setOnClickListener { playSound(R.id.blackButton4) }
        findViewById<Button>(R.id.blackButton5).setOnClickListener { playSound(R.id.blackButton5) }
        findViewById<Button>(R.id.blackButton6).setOnClickListener { playSound(R.id.blackButton6) }
        findViewById<Button>(R.id.blackButton7).setOnClickListener { playSound(R.id.blackButton7) }
        findViewById<Button>(R.id.blackButton8).setOnClickListener { playSound(R.id.blackButton8) }
        findViewById<Button>(R.id.blackButton9).setOnClickListener { playSound(R.id.blackButton9) }
        findViewById<Button>(R.id.blackButton10).setOnClickListener { playSound(R.id.blackButton10) }

        findViewById<Button>(R.id.buttonRecord).setOnClickListener { toggleRecording() }
        findViewById<Button>(R.id.buttonPlayback).setOnClickListener { playRecordedNotes() }

        loadSounds()

    }

    private fun toggleRecording() {
        isRecording = !isRecording

        if (isRecording) {
            recordedNotes.clear() // Clear existing recorded notes when starting a new recording
            lastRecordedNoteTime = System.currentTimeMillis()
        }
    }

    private fun playRecordedNotes() {
        playbackIndex = 0
        playNextRecordedNote()
    }

    private fun playNextRecordedNote() {
        if (playbackIndex < recordedNotes.size) {
            val (noteId, timeBetweenNotes) = recordedNotes[playbackIndex]
            playSound(noteId)

            Handler().postDelayed({
                playbackIndex++
                playNextRecordedNote()
            }, timeBetweenNotes.toLong())
        }
    }







    private fun loadSounds() {
        soundMap = mapOf(
            R.id.button1 to soundPool.load(this, getFirstOctave(currentOctave, "c"), 1),
            R.id.button2 to soundPool.load(this, getFirstOctave(currentOctave, "d"), 1),
            R.id.button3 to soundPool.load(this, getFirstOctave(currentOctave, "e"), 1),
            R.id.button4 to soundPool.load(this, getFirstOctave(currentOctave, "f"), 1),
            R.id.button5 to soundPool.load(this, getFirstOctave(currentOctave, "g"), 1),
            R.id.button6 to soundPool.load(this, getFirstOctave(currentOctave, "a"), 1),
            R.id.button7 to soundPool.load(this, getFirstOctave(currentOctave, "b"), 1),
            R.id.button8 to soundPool.load(this, getSecondOctave(currentOctave, "c"), 1),
            R.id.button9 to soundPool.load(this, getSecondOctave(currentOctave, "d"), 1),
            R.id.button10 to soundPool.load(this, getSecondOctave(currentOctave, "e"), 1),
            R.id.button11 to soundPool.load(this, getSecondOctave(currentOctave, "f"), 1),
            R.id.button12 to soundPool.load(this, getSecondOctave(currentOctave, "g"), 1),
            R.id.button13 to soundPool.load(this, getSecondOctave(currentOctave, "a"), 1),
            R.id.button14 to soundPool.load(this, getSecondOctave(currentOctave, "b"), 1),

            R.id.blackButton1 to soundPool.load(this, getFirstOctave(currentOctave, "db"), 1),
            R.id.blackButton2 to soundPool.load(this, getFirstOctave(currentOctave, "eb"), 1),
            R.id.blackButton3 to soundPool.load(this, getFirstOctave(currentOctave, "gb"), 1),
            R.id.blackButton4 to soundPool.load(this, getFirstOctave(currentOctave, "ab"), 1),
            R.id.blackButton5 to soundPool.load(this, getFirstOctave(currentOctave, "bb"), 1),
            R.id.blackButton6 to soundPool.load(this, getSecondOctave(currentOctave, "db"), 1),
            R.id.blackButton7 to soundPool.load(this, getSecondOctave(currentOctave, "eb"), 1),
            R.id.blackButton8 to soundPool.load(this, getSecondOctave(currentOctave, "gb"), 1),
            R.id.blackButton9 to soundPool.load(this, getSecondOctave(currentOctave, "ab"), 1),
            R.id.blackButton10 to soundPool.load(this, getSecondOctave(currentOctave, "bb"), 1)

        )
    }

    private fun getFirstOctave(octave: Int, note: String): Int {
        val note2: String = "$note$octave"
        val resId = resources.getIdentifier(note2, "raw", packageName)
        return resId
    }

    private fun getSecondOctave(octave: Int, note: String): Int {
        val secondOctave: Int = octave + 1
        val note2: String = "$note$secondOctave"
        val resId = resources.getIdentifier(note2, "raw", packageName)
        return resId
    }

    private fun playSound(buttonId: Int) {
        val soundId = soundMap[buttonId] ?: return
        val streamId = soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)

        if (isRecording) {
            val currentTime = System.currentTimeMillis()
            val timeBetweenNotes = currentTime - lastRecordedNoteTime
            recordedNotes.add(buttonId to timeBetweenNotes.toInt())
        }
        // Adjust the duration to make the sound play shorter (e.g., 300 milliseconds)
        val shorterDuration = 300

        // Use a Handler to stop the sound after the specified duration
        Handler().postDelayed({
            soundPool.stop(streamId)
        }, shorterDuration.toLong())
    }

    private fun octaveUp() {
        if (currentOctave < 5) {
            currentOctave++
            loadSounds()
        }
    }

    private fun octaveDown() {
        if (currentOctave > 1) {
            currentOctave--
            loadSounds()
        }
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
