package com.example.mobiledaw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mobiledaw.PianoActivity.PianoActivity

class MainActivity : AppCompatActivity() {


    val pianoActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
        if(result.resultCode == RESULT_CANCELED){
            Log.d("MainActivity","Switch to Piano Activity Cancelled")
        }else{
            Log.d("MainActivity", "Switched to Piano activity")

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

    }

    private fun startPiano(){
        val pianoActivityIntent: Intent = Intent(this,PianoActivity::class.java)
        pianoActivityLauncher.launch(pianoActivityIntent)

    }
}