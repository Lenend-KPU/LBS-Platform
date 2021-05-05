package kr.ac.kpu.lbs_platform

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import kr.ac.kpu.lbs_platform.databinding.ActivityFeedBinding
import kr.ac.kpu.lbs_platform.databinding.ActivityRegisterBinding

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white))
    }
}