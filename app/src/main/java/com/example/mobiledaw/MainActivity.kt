package com.example.mobiledaw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mobiledaw.Instruments.DrumpadActivity
import com.example.mobiledaw.Instruments.PianoActivity

class MainActivity : AppCompatActivity() {

    val pianoActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
        if(result.resultCode == RESULT_CANCELED){
            Log.d("MainActivity","Switch to Piano Activity Cancelled")
        }else{
            Log.d("MainActivity", "Switched to Piano activity")

        }
    }

    val drumpadActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
        if(result.resultCode == RESULT_CANCELED){
            Log.d("MainActivity","Switch to Drumpad Activity Cancelled")
        }else{
            Log.d("MainActivity", "Switched to Drumpad activity")

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //onClick functionality for inserting new GeoPhoto
        findViewById<ImageButton>(R.id.recordButton).setOnClickListener {
            startPiano()
            finish()
        }

        val instruments = resources.getStringArray(R.array.Instruments)
        val spinner = findViewById<Spinner>(R.id.spinner)

        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, instruments
            )
            spinner.adapter = adapter

            spinner.post {
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, num: Int, id: Long) {
                        Toast.makeText(this@MainActivity, "Selected instrument: " + " " + instruments[num], Toast.LENGTH_SHORT).show()

                        // Call the appropriate method based on the selected instrument
                        when (instruments[num]) {
                            "Piano" -> startPiano()
                            "Drumpad" -> startDrumpad()
                            // Add other instrument cases as needed
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Toast.makeText(this@MainActivity, "Please select a valid instrument...", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun startPiano(){
        val pianoActivityIntent: Intent = Intent(this,PianoActivity::class.java)
        pianoActivityLauncher.launch(pianoActivityIntent)

    }
    private fun startDrumpad(){
        val drumpadActivityIntent: Intent = Intent(this, DrumpadActivity::class.java)
        drumpadActivityLauncher.launch(drumpadActivityIntent)

    }


}


