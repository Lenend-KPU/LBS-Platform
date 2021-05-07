package kr.ac.kpu.lbs_platform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.kpu.lbs_platform.databinding.ActivityMainBinding
import splitties.fragments.fragmentTransaction

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        fragmentTransaction {
            this.add(R.id.mainActivityfragment, MainFragment())
        }

    }
}