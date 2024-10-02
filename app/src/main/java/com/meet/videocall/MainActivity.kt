package com.meet.videocall

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.meet.videocall.databinding.ActivityMainBinding
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions


class MainActivity : AppCompatActivity() {
 private lateinit var  binding:ActivityMainBinding
    private lateinit var clipboard: ClipboardManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        binding.generateMeetCodeBtn.setOnClickListener {
           val meetingCode = generateRandomMeetingCode(10)
            binding.generateMeetCode.text = meetingCode
            binding.copyClipboard.visibility = View.VISIBLE
            binding.generateMeetCode.setTextColor(Color.BLACK)
            binding.copyClipboard.isClickable = true
            binding.copyClipboard.setOnClickListener {
                copyToClipboard(meetingCode)
            }
        }
    }

    fun onButtonClick(view: View) {

        val text = binding.conferenceName.text

        if (text.isNotEmpty()) {
            // Creating a room using JitsiMeetConferenceOptions class
            val options = JitsiMeetConferenceOptions.Builder()
                .setRoom(text.toString())
                .setFeatureFlag("welcomepage.enabled", false)
                .setAudioMuted(true)
                .setVideoMuted(true)
                .build()
            JitsiMeetActivity.launch(this, options)
        }else{
            Toast.makeText(this, "Enter the room code", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateRandomMeetingCode(codeLen: Int): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..codeLen)
            .map { characters.random() }
            .joinToString("")
    }

    private fun copyToClipboard(text: String) {
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Meeting code copied!", Toast.LENGTH_SHORT).show()
    }
}