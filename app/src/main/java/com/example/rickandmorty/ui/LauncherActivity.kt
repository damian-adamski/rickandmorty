package com.example.rickandmorty.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.*
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.rickandmorty.R

import java.util.*
import kotlin.concurrent.schedule

class LauncherActivity : AppCompatActivity() {

    /** Launcher screen */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Fade(Fade.IN)
            exitTransition = Fade(Fade.OUT)
        }
        setContentView(R.layout.activity_launcher)


        Timer("Launcher", false)
            .schedule(1500) {
                runOnUiThread {
                    val intent = Intent(this@LauncherActivity, MainActivity::class.java)
                    startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(this@LauncherActivity)
                            .toBundle()
                    )
                    this@LauncherActivity.finish()
                }
            }
    }
}