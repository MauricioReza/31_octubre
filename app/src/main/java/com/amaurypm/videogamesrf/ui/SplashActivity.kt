package com.amaurypm.videogamesrf.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.amaurypm.videogamesrf.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Configura un temporizador para el SplashScreen (por ejemplo, 3 segundos)
        val splashDuration = 300 // en milisegundos

        Handler().postDelayed({
            // Después de la duración del SplashScreen, inicia la actividad principal
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)

            // Cierra la actividad actual
            finish()
        }, splashDuration.toLong())
    }
}
