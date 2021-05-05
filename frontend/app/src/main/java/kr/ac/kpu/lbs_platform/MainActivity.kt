package kr.ac.kpu.lbs_platform

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kr.ac.kpu.lbs_platform.databinding.ActivityLoginBinding
import kr.ac.kpu.lbs_platform.databinding.ActivityMainBinding
import splitties.activities.start

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = Color.TRANSPARENT

        binding.loginButton.setOnClickListener {
            start<LoginActivity>()
        }
        binding.registerButton.setOnClickListener {
            start<RegisterActivity>()
        }


    }
}