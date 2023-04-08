package com.tunahan.kotlincatchthestewie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.tunahan.kotlincatchthekenny.R
import com.tunahan.kotlincatchthekenny.databinding.ActivityMainBinding
import kotlin.random.Random

//starta art arda basınca buglanıyor ona bir bakalım
// on finish birara çalışmadı resimleri gizlemedi -- tam çalışacağı zaman yeni resme tıklandığında öyle birşey oluyor ondan resmin aynı yerde tekrar çıkmamasını ayarlayalım

class MainActivity : AppCompatActivity() {
    var oldGold = 0
    var gold = 0
    var score = 0
    var imageArray = ArrayList<ImageView>()
    var handler = Handler()
    var runnable = Runnable{}
    private lateinit var sharedPreferences: SharedPreferences
    var highScoreData: Int? = null
    var goldData: Int? = null


    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.goldText.visibility = View.INVISIBLE
        binding.goldValue.visibility = View.INVISIBLE

        imageArray.add(binding.imageView)
        imageArray.add(binding.imageView1)
        imageArray.add(binding.imageView2)
        imageArray.add(binding.imageView3)
        imageArray.add(binding.imageView4)
        imageArray.add(binding.imageView5)
        imageArray.add(binding.imageView6)
        imageArray.add(binding.imageView7)
        imageArray.add(binding.imageView8)
        hideImages()
        sharedPreferences = this.getSharedPreferences("com.tunahan.kotlincatchthekenny",Context.MODE_PRIVATE)
        highScoreData =sharedPreferences.getInt("highScore",-1)
        if (highScoreData == -1){
            binding.highScoreText.text = "High Score: 0"
        }else{
            binding.highScoreText.text = "High Score: $highScoreData"
        }
    }
    fun showImages(){
        val random = Random
        var randomIndex = random.nextInt(9)
        imageArray[randomIndex].visibility = View.VISIBLE
    }
    fun hideImages(){
        for (image in imageArray){
            image.visibility = View.INVISIBLE
        }
    }

    fun startGame(view: View){
        showImages()
        score = 0
        object : CountDownTimer(15350,1000){
            override fun onTick(p0: Long) {
                binding.timeText.text = "Time: ${p0/1000}"
            }

            override fun onFinish() {
                handler.removeCallbacks(runnable)
                for (image in imageArray){
                    image.visibility = View.INVISIBLE
                }
                binding.timeText.text = "Time: 0"
                binding.button.visibility = View.VISIBLE
            }
        }.start()
        binding.button.visibility = View.INVISIBLE
    }

    fun increaseScore(view: View){
        score++
        binding.scoreText.text = "Score: $score"
        if(score%10 == 0){
            gold++
        }

        if (highScoreData == null){
            binding.highScoreText.text == "null"
        }else{
            if (score> highScoreData!!){
                sharedPreferences.edit().putInt("highScore",score).apply()
                highScoreData =sharedPreferences.getInt("highScore",-1)
                if (highScoreData == -1){
                    binding.highScoreText.text = "High Score: 0"
                }else{
                    binding.highScoreText.text = "High Score: $highScoreData"
                }
            }
        }

        hideImages()
        showImages()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflaterMenu =menuInflater
        inflaterMenu.inflate(R.menu.options_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.newGame){
            val intent = intent
            finish()
            startActivity(intent)

        }else if (item.itemId == R.id.resetHighScore){
            highScoreData = sharedPreferences.getInt("highScore",-1)
            if(highScoreData != -1){
                sharedPreferences.edit().remove("highScore").apply()
                binding.highScoreText.text = "High Score: 0"
            }
        }
        return super.onOptionsItemSelected(item)
    }
}


