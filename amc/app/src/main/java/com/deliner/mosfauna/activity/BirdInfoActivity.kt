package com.deliner.mosfauna.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.deliner.mosfauna.R
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

    private var infoPlayer: MediaPlayer? = null
    private var songPlayer: MediaPlayer? = null

    private var songIsPlaying = false
    private var infoIsPlaying = false

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
        birdEnglishNameView.text = getEnglishName(birdName)
        birdImageView.setImageResource(photoId)
        birdInfoView.text = getBirdInfo(birdName)


        playSongButton.setOnClickListener {
            if (songIsPlaying) {
                songPlayer!!.stop()
                songIsPlaying = false
                songPlayer = null
                playSongButton.setImageResource(R.drawable.ic_baseline_play)
            } else {
                playSongButton.setImageResource(R.drawable.ic_pause)
                songPlayer = MediaPlayer.create(applicationContext, getSongRaw(birdName))
                songPlayer!!.setOnCompletionListener {
                    songPlayer!!.stop()
                    songIsPlaying = false
                    songPlayer = null
                    playSongButton.setImageResource(R.drawable.ic_baseline_play)
                }
                songPlayer!!.start()
                songIsPlaying = true
            }
        }

        actionButton.setOnClickListener {
            if (infoIsPlaying) {
                infoPlayer!!.stop()
                infoIsPlaying = false
                infoPlayer = null
                actionButton.setImageResource(R.drawable.ic_play)
            } else {
                actionButton.setImageResource(R.drawable.ic_pause_action)
                infoPlayer = MediaPlayer.create(applicationContext, getInfoRaw(birdName))
                infoPlayer!!.setOnCompletionListener {
                    infoPlayer!!.stop()
                    infoIsPlaying = false
                    infoPlayer = null
                    playSongButton.setImageResource(R.drawable.ic_play)
                }
                infoPlayer!!.start()
                infoIsPlaying = true
            }
        }
    }

    private fun getSongRaw(name: String): Int {
        return when (name) {
            "Обыкновенный гоголь" -> R.raw.gogol
            "Ястреб-перепелятник" -> R.raw.perepel
            "Ястреб-тетеревятник" -> R.raw.teterev
            "Серая неясыть" -> R.raw.neyasyt
            else -> R.raw.gogol
        }
    }

    private fun getInfoRaw(name: String): Int {
        return when (name) {
            "Обыкновенный гоголь" -> R.raw.info_gogol
            "Ястреб-перепелятник" -> R.raw.info_perepel
            "Ястреб-тетеревятник" -> R.raw.info_teterev
            "Серая неясыть" -> R.raw.info_neyas
            else -> R.raw.info_gogol
        }
    }

    private fun getEnglishName(name: String): String {
        return when (name) {
            "Обыкновенный гоголь" -> getString(R.string.gog_english)
            "Ястреб-перепелятник" -> getString(R.string.peperel_english)
            "Ястреб-тетеревятник" -> getString(R.string.teterev_englis)
            "Серая неясыть" -> getString(R.string.neyas_english)
            else -> ""
        }
    }

    private fun getBirdInfo(name: String): String {
        return when (name) {
            "Обыкновенный гоголь" -> getString(R.string.gog_info)
            "Ястреб-перепелятник" -> getString(R.string.peperel_info)
            "Ястреб-тетеревятник" -> getString(R.string.teterev_info)
            "Серая неясыть" -> getString(R.string.neyas_info)
            else -> ""
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}