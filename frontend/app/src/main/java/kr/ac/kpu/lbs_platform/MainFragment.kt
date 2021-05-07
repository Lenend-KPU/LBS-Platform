package kr.ac.kpu.lbs_platform

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import splitties.fragments.fragmentTransaction

class MainFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        GlobalScope.launch {
            while (MainActivity.instance == null) ;
            MainActivity.instance?.let {
                val bottomNav = it.binding.bottomNavigationView
                bottomNav.visibility = View.GONE
            }
        }
        val inflated = inflater.inflate(R.layout.fragment_main, container, false)
        val loginButton = inflated.findViewById<Button>(R.id.loginButton)
        val registerButton = inflated.findViewById<Button>(R.id.registerButton)

        loginButton.setOnClickListener {
            GlobalScope.launch {
                while (MainActivity.instance == null) ;
                MainActivity.instance?.fragmentTransaction {
                    replace(R.id.mainActivityfragment, FeedFragment())
                }
            }
        }
        registerButton.setOnClickListener {
            GlobalScope.launch {
                while (MainActivity.instance == null) ;
                MainActivity.instance?.fragmentTransaction {
                    replace(R.id.mainActivityfragment, FeedFragment())
                }
            }
        }

        return inflated
    }
}