package kr.ac.kpu.lbs_platform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.kpu.lbs_platform.databinding.ActivityLoginBinding
import kr.ac.kpu.lbs_platform.databinding.ActivityMainBinding
import splitties.activities.start

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        // 로그인 로직 TODO

        start<FeedActivity>()
    }
}