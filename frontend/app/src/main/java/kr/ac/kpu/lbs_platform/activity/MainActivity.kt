package kr.ac.kpu.lbs_platform.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.databinding.ActivityMainBinding
import kr.ac.kpu.lbs_platform.fragment.AddFragment
import kr.ac.kpu.lbs_platform.fragment.FeedFragment
import kr.ac.kpu.lbs_platform.fragment.ProfileFragment
import kr.ac.kpu.lbs_platform.fragment.SearchFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    companion object {
        var instance: MainActivity? = null
    }

    lateinit var binding: ActivityMainBinding
    
    var previousItemId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        instance = this
        val view = binding.root
        binding.bottomNavigationView.visibility = View.GONE
        binding.bottomNavigationView.selectedItemId = R.id.page_feed
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)
        window.statusBarColor = getColor(R.color.black)
        setContentView(view)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if(previousItemId == item.itemId) {
            return true
        }
        Log.i("MainActivity", item.itemId.toString())
        previousItemId = item.itemId
        when(item.itemId) {
            R.id.page_search -> {
                Log.i("MainActivity", "page_search")
                supportFragmentManager.beginTransaction().replace(R.id.mainActivityfragment, SearchFragment()).commit()
                return true
            }
            R.id.page_feed -> {
                Log.i("MainActivity", "page_feed")
                supportFragmentManager.beginTransaction().replace(R.id.mainActivityfragment, FeedFragment()).commit()
                return true
            }
            R.id.page_add -> {
                Log.i("MainActivity", "page_list")
                supportFragmentManager.beginTransaction().replace(R.id.mainActivityfragment, AddFragment()).commit()
                return true
            }
            R.id.page_profile -> {
                Log.i("MainActivity", "page_profile")
                supportFragmentManager.beginTransaction().replace(R.id.mainActivityfragment, ProfileFragment()).commit()
                return true
            }
            else -> {
                Log.i("MainActivity", "else")
                return true
            }
        }
    }
}