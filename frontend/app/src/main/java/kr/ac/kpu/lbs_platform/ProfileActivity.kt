package kr.ac.kpu.lbs_platform

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.kpu.lbs_platform.databinding.ActivityMainBinding
import kr.ac.kpu.lbs_platform.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        window.statusBarColor = getColor(R.color.status_bar_color)

        binding.profileActivityImageView.background = ShapeDrawable(OvalShape())
        binding.profileActivityImageView.clipToOutline = true

    }
}