package com.deliner.mosfauna.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.deliner.mosfauna.R
import com.deliner.mosfauna.utils.LoginManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BirdInfoActivity : UserActivity() {

    private var photoId = -1


    private lateinit var birdName: String


    private lateinit var birdImageView: ImageView
    private lateinit var birdNameView: TextView
    private lateinit var birdEnglishNameView: TextView
    private lateinit var birdInfoView: TextView
    private lateinit var actionButton: FloatingActionButton
    private lateinit var playSongButton: ImageView

    private val audioPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bird_info)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        birdName = intent.extras!!.getString("KEY_BIRD")!!
        photoId = intent.extras!!.getInt("KEY_PHOTO")

        birdImageView = findViewById(R.id.activity_bird_info_image)
        birdNameView = findViewById(R.id.activity_bird_info_bird_name)
        birdEnglishNameView = findViewById(R.id.activity_bird_info_bird_name_english)

        birdInfoView = findViewById(R.id.activity_bird_info_text)
        actionButton = findViewById(R.id.activity_bird_info_action_button)
        playSongButton = findViewById(R.id.activity_bird_info_play_song_button)



        birdNameView.text = birdName
//        birdEnglishNameView.text = getEnglishName(birdName)
        birdImageView.setImageResource(photoId)




    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}