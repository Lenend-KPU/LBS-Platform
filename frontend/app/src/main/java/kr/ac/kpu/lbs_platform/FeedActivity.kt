package kr.ac.kpu.lbs_platform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.kpu.lbs_platform.databinding.ActivityFeedBinding
import kr.ac.kpu.lbs_platform.databinding.ActivityRegisterBinding

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
    }
}