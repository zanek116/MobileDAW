package com.example.mobiledaw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
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

    var trackId = 0

    val pianoActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_CANCELED) {
            Log.d("MainActivity", "Switch to Piano Activity Cancelled")
        } else {
            Log.d("MainActivity", "Switched to Piano activity")

        }
    }

    val drumpadActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
        if(result.resultCode == RESULT_CANCELED){
            Log.d("MainActivity","Switch to Drum Pad Activity Cancelled")
        }else{
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
                    override fun onItemSelected(parent: AdapterView<*>, view: View, num: Int, id: Long) {
                        Toast.makeText(this@MainActivity, "Selected instrument: " + instruments[num], Toast.LENGTH_SHORT).show()
                        if (instruments[num] == "Piano") {
                            startPiano()
                            finish()
                            Log.e("hereeeeee", instruments[num])
                        }
                        else if (instruments[num] == "Drum Pad") {
                            startDrumpad()
                            finish()
                            Log.e("hereeeeee", instruments[num])
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Toast.makeText(this@MainActivity, "Please select a valid instrument...", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val recordedNotes = intent.getSerializableExtra("recordedNotes") as? Array<Pair<Int, Int>>
        if (recordedNotes != null) {
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




    }

    private fun saveRecordedNotes(recordedNotes: Array<Pair<Int, Int>>) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = DAWDatabase.getDatabase(applicationContext).trackDao()
            var trackId : Int = dao.getNextId()
            if (trackId == null)
            {
                trackId = 0
            }
            else
            {
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


    private fun startPiano(){
        val pianoActivityIntent: Intent = Intent(this, PianoActivity::class.java)
        pianoActivityLauncher.launch(pianoActivityIntent)

    }
    private fun startDrumpad(){
        val drumpadActivityIntent: Intent = Intent(this, DrumpadActivity::class.java)
        drumpadActivityLauncher.launch(drumpadActivityIntent)


    }
}


