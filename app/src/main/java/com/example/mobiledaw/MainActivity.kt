package com.example.mobiledaw

import android.content.Intent
import android.content.res.TypedArray
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mobiledaw.Database.DAWDatabase
import com.example.mobiledaw.Database.TrackData
import com.example.mobiledaw.Instruments.DrumpadActivity
import com.example.mobiledaw.Instruments.PianoActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    var actualTrack1 = Array<Pair<Int, Int>>(100) { Pair(0, 0) }
    var actualTrack2 = Array<Pair<Int, Int>>(100) { Pair(0, 0) }
    var actualTrack3 = Array<Pair<Int, Int>>(100) { Pair(0, 0) }
    var actualTrack4 = Array<Pair<Int, Int>>(100) { Pair(0, 0) }


    var track1Index = 0
    var track2Index = 0
    var track3Index = 0
    var track4Index = 0
    private lateinit var soundPool: SoundPool
    private var soundMap: Map<Int, Int> = mutableMapOf()
    private var currentOctave: Int = 3 // Initial octave


    val pianoActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_CANCELED) {
                Log.d("MainActivity", "Switch to Piano Activity Cancelled")
            } else {
                Log.d("MainActivity", "Switched to Piano activity")

            }
        }

    val drumpadActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_CANCELED) {
                Log.d("MainActivity", "Switch to Drum Pad Activity Cancelled")
            } else {
                Log.d("MainActivity", "Switched to Drum Pad activity")

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val instruments = resources.getStringArray(R.array.Instruments)
        val spinner = findViewById<Spinner>(R.id.spinner)


        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, instruments
            )
            spinner.adapter = adapter
            spinner.setSelection(0)

            spinner.post {
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        num: Int,
                        id: Long
                    ) {
                        Toast.makeText(
                            this@MainActivity,
                            "Selected instrument: " + instruments[num],
                            Toast.LENGTH_SHORT
                        ).show()
                        if (instruments[num] == "Piano") {
                            startPiano()
                            finish()
                            Log.e("hereeeeee", instruments[num])
                        } else if (instruments[num] == "Drum Pad") {
                            startDrumpad()
                            finish()
                            Log.e("hereeeeee", instruments[num])
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Toast.makeText(
                            this@MainActivity,
                            "Please select a valid instrument...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        // Initialize SoundPool
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(7) // Number of simultaneous sounds
            .setAudioAttributes(audioAttributes)
            .build()

        val recordedNotes = intent.getSerializableExtra("recordedNotes") as? Array<Pair<Int, Int>>
        if (recordedNotes!=null){
            saveRecordedNotes(recordedNotes)
            // Display recorded notes in the ListView
            val listView = findViewById<ListView>(R.id.tracks)
            val adapter = ArrayAdapter<Pair<Int, Int>>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                recordedNotes
            )
            listView.adapter = adapter

        } else {
            Log.d("Recorded Notes", "No recordedNotes found in the Intent")
        }

        loadSounds()
        populateList()
        separateTracks()

        track1Index = 0
        track2Index = 0
        track3Index = 0
        track4Index = 0

        findViewById<ImageButton>(R.id.playButton).setOnClickListener {
            Log.d("track one contents", actualTrack1[0].toString())
            playTrack(actualTrack1,1)
            playTrack(actualTrack2,2)
            playTrack(actualTrack3,3)
            playTrack(actualTrack4,4)}

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
//        Log.e("loading sound ",  getFirstOctave(currentOctave, "c").toString())
//                soundPool.load(this, getFirstOctave(currentOctave, "d"), 1)
//        Log.e("loading sound ",  getFirstOctave(currentOctave, "d").toString())
//                soundPool.load(this, getFirstOctave(currentOctave, "e"), 1)
//                soundPool.load(this, getFirstOctave(currentOctave, "f"), 1)
//                soundPool.load(this, getFirstOctave(currentOctave, "g"), 1)
//                soundPool.load(this, getFirstOctave(currentOctave, "a"), 1)
//                soundPool.load(this, getFirstOctave(currentOctave, "b"), 1)
//                soundPool.load(this, getSecondOctave(currentOctave, "c"), 1)
//                soundPool.load(this, getSecondOctave(currentOctave, "d"), 1)
//                soundPool.load(this, getSecondOctave(currentOctave, "e"), 1)
//                soundPool.load(this, getSecondOctave(currentOctave, "f"), 1)
//                soundPool.load(this, getSecondOctave(currentOctave, "g"), 1)
//                soundPool.load(this, getSecondOctave(currentOctave, "a"), 1)
//                soundPool.load(this, getSecondOctave(currentOctave, "b"), 1)
//
//                soundPool.load(this, getFirstOctave(currentOctave, "db"), 1)
//                soundPool.load(this, getFirstOctave(currentOctave, "eb"), 1)
//        Log.e("loading sound ",  getFirstOctave(currentOctave, "eb").toString())
//                soundPool.load(this, getFirstOctave(currentOctave, "gb"), 1)
//                soundPool.load(this, getFirstOctave(currentOctave, "ab"), 1)
//                soundPool.load(this, getFirstOctave(currentOctave, "bb"), 1)
//                soundPool.load(this, getSecondOctave(currentOctave, "db"), 1)
//                soundPool.load(this, getSecondOctave(currentOctave, "eb"), 1)
//                soundPool.load(this, getSecondOctave(currentOctave, "gb"), 1)
//        Log.e("loading sound ",  getSecondOctave(currentOctave, "gb").toString())
//                soundPool.load(this, getSecondOctave(currentOctave, "ab"), 1)
//                soundPool.load(this, getSecondOctave(currentOctave, "bb"), 1)
//
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

    override fun onResume() {
        super.onResume()

        // Your function or code to be executed when the activity resumes
        populateList()
    }

    private fun populateList()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = DAWDatabase.getDatabase(applicationContext).trackDao()
            val noteList = dao.getTitles()

                val listView = findViewById<ListView>(R.id.tracks)

                // Convert List<Int> to List<String>
                val stringList = noteList.map { it.toString() }

                // Use ArrayAdapter<String> instead of ArrayAdapter<Int>
                val adapter = ArrayAdapter<String>(
                    this@MainActivity,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    stringList
                )

                listView.adapter = adapter
        }
    }







    private fun saveRecordedNotes(recordedNotes: Array<Pair<Int, Int>>) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = DAWDatabase.getDatabase(applicationContext).trackDao()
            var trackId: Int = dao.getNextId()
            if (trackId == null) {
                trackId = 0
            } else {
                trackId++
            }

            // Convert Pair<Int, Int> to RecordedNoteEntity and insert into the database
            recordedNotes.forEach { pair ->
                val first = pair.first
                val second = pair.second
                dao.insert(TrackData(first = first, second = second, track = trackId))
            }
        }
    }

    private fun separateTracks() {
        CoroutineScope(Dispatchers.IO).launch {

            val dao = DAWDatabase.getDatabase(applicationContext).trackDao()
            var noteList = dao.getAllData()
            var track1 = Array<Pair<Int, Int>>(100) { Pair(0, 0) }
            var track2 = Array<Pair<Int, Int>>(100) { Pair(0, 0) }
            var track3 = Array<Pair<Int, Int>>(100) { Pair(0, 0) }
            var track4 = Array<Pair<Int, Int>>(100) { Pair(0, 0) }

            var track1Index = 0
            var track2Index = 0
            var track3Index = 0
            var track4Index = 0

            for (note in noteList) {
                when (note.track) {
                    1 -> {

                        // Check if there is space in the array before adding a note
                        if (track1Index < track1.size) {
                            track1[track1Index++] = Pair(note.first, note.second)
                            Log.d("track1", "added to track1" + track1[track1Index-1].toString())
                        } else {
                            // Handle the case where the array is full
                        }
                    }

                    2 -> {

                        // Check if there is space in the array before adding a note
                        if (track2Index < track2.size) {
                            track2[track2Index++] = Pair(note.first, note.second)
                            Log.d("track2", "added to track2 at index" + track2Index + "value is " + track2[track2Index-1].toString())
                        } else {
                            // Handle the case where the array is full
                        }
                    }

                    3 -> {

                        // Check if there is space in the array before adding a note
                        if (track3Index < track3.size) {
                            track3[track3Index++] = Pair(note.first, note.second)

                        } else {
                            // Handle the case where the array is full
                        }
                    }

                    4 -> {

                        // Check if there is space in the array before adding a note
                        if (track4Index < track4.size) {
                            track4[track4Index++] = Pair(note.first, note.second)
                        } else {
                            // Handle the case where the array is full
                        }
                    }
                    // Handle other track values if needed
                }
            }

            // Now, track1, track2, track3, and track4 contain separated notes for each track
            // You can perform any further actions with these arrays
            // For example, if you want to play back each track separately:
            actualTrack1 = track1
            actualTrack2 = track2
            actualTrack3 = track3
            actualTrack4 = track4

        }
    }

    private fun playNextRecordedNote(track: Array<Pair<Int, Int>>, trackNumber: Int) {
        when (trackNumber) {
            1 -> {
            if (track1Index < track.size) {
                val (noteId, timeBetweenNotes) = track[track1Index]

                Handler().postDelayed({
                    playSound(noteId)
                    Log.e("track1sound", "reached track1 playback " + noteId.toString())
                    track1Index++
                    playNextRecordedNote(track, 1)
                }, timeBetweenNotes.toLong())
            }
        }
            2 -> {
                if (track2Index < track.size) {
                    val (noteId, timeBetweenNotes) = track[track2Index]

                    Handler().postDelayed({
                        playSound(noteId)
                        Log.e("track2sound", "reached track2 playback " + noteId.toString())
                        track2Index++
                        playNextRecordedNote(track, 2)
                    }, timeBetweenNotes.toLong())
                }
            }
            3 -> {
                if (track3Index < track.size) {
                    val (noteId, timeBetweenNotes) = track[track3Index]

                    Handler().postDelayed({
                        playSound(noteId)
                        Log.e("track3sound", "reached track3 playback " + noteId.toString())
                        track3Index++
                        playNextRecordedNote(track, 3)
                    }, timeBetweenNotes.toLong())
                }
            }
            4 -> {
                if (track4Index < track.size) {
                    val (noteId, timeBetweenNotes) = track[track4Index]

                    Handler().postDelayed({
                        playSound(noteId)
                        Log.e("track4sound", "reached track4 playback " + noteId.toString())
                        track4Index++
                        playNextRecordedNote(track, 4)
                    }, timeBetweenNotes.toLong())
                }
            }


        }

        //for actual playback
//        if (playbackIndex < recordedNotes.size) {
//            val (noteId, timeBetweenNotes) = recordedNotes[playbackIndex]
//
//            Handler().postDelayed({
//                playSound(noteId)
//                playbackIndex++
//                playNextRecordedNote()
//            }, timeBetweenNotes.toLong())
//        }
    }

    private fun playTrack(track: Array<Pair<Int, Int>>, trackNumber: Int) {

        track1Index = 0
        track2Index = 0
        track3Index = 0
        track4Index = 0


        when (trackNumber) {
            1 -> playNextRecordedNote(track, 1)
            2 -> playNextRecordedNote(track, 2)
            3 -> playNextRecordedNote(track, 3)
            4 -> playNextRecordedNote(track, 4)

        }

    }

    private fun playSound(buttonId: Int) {
        val soundId = soundMap[buttonId] ?: return
        Log.e("soundId", soundId.toString())
        val currentTime = System.currentTimeMillis()


        val streamId = soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)

        val shorterDuration = 300
        Handler().postDelayed({
            soundPool.stop(streamId)
        }, shorterDuration.toLong())
    }

    private fun startPiano(){
        val pianoActivityIntent: Intent = Intent(this, PianoActivity::class.java)
        pianoActivityLauncher.launch(pianoActivityIntent)

    }
    private fun startDrumpad(){
        val drumpadActivityIntent: Intent = Intent(this, DrumpadActivity::class.java)
        drumpadActivityLauncher.launch(drumpadActivityIntent)

    }
}


