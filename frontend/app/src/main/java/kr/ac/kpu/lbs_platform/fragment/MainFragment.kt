package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kr.ac.kpu.lbs_platform.activity.MainActivity
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.global.FragmentChanger

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
            FragmentChanger.change(this, LoginFragment())
        }
        registerButton.setOnClickListener {
            FragmentChanger.change(this, RegisterFragment())
        }

        return inflated
    }


}