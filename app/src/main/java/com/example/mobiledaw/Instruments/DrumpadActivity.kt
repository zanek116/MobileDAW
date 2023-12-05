package com.example.mobiledaw.Instruments
import android.content.Intent
import android.media.AudioAttributes
import android.widget.Button
import android.media.SoundPool
import android.os.Handler
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mobiledaw.MainActivity
import com.example.mobiledaw.R

class DrumpadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drumpad)

        findViewById<Button>(R.id.drumpadHome).setOnClickListener { returnHome() }


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

