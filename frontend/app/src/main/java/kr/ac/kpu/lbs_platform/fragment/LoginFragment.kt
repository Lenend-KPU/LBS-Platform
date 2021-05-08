package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.global.FragmentChanger
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.global.User
import kr.ac.kpu.lbs_platform.poko.remote.LoginRequest
import kr.ac.kpu.lbs_platform.poko.remote.Profile
import kr.ac.kpu.lbs_platform.poko.remote.ProfilesRequest
import kr.ac.kpu.lbs_platform.poko.remote.Request
import splitties.toast.toast


class LoginFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val inflated = inflater.inflate(R.layout.fragment_login, container, false)
        val registerButton = inflated.findViewById<Button>(R.id.loginSubmitButton)
        registerButton.setOnClickListener {
            val editTextIds = listOf(R.id.loginEmailEditText, R.id.loginPasswordEditText)
            val editTexts = editTextIds.map {
                inflated.findViewById<EditText>(it)
            }
            val texts = editTexts.map {
                it.text.toString()
            }
            if(texts.any { it == "" }) {
                toast("입력되지 않은 필드가 있습니다.")
                return@setOnClickListener
            }
            val (email, password) = texts
            loginUserToServer(email, password)
        }
        // Inflate the layout for this fragment
        return inflated
    }

    fun loginUserToServer(email: String, password: String) {
        val params = mutableMapOf<String, String>()
        params["email"] = email
        params["password"] = password
        RequestHelper.request(this, FeedFragment(),"login/", params,
            poko=LoginRequest::class.java) {
            val response = it as LoginRequest
            val userid = response.userid
            User.userid = userid
            checkUserProfileExists()
        }
    }

    fun checkUserProfileExists() {
        val params = mutableMapOf<String, String>()
        params["userid"] = User.userid!!.toString()
        RequestHelper.request(this,
            FeedFragment(),
            "profiles/",
            params = params,
            method = com.android.volley.Request.Method.GET,
            poko = ProfilesRequest::class.java) {
            val response = it as ProfilesRequest
            val result = response.result
            if(result.isEmpty()) {
                goAddProfileFragment()
            } else {
                getUserProfile(result)
                goFeedFragment()
            }
        }
    }

    fun getUserProfile(profiles: Array<Profile>) {
        kr.ac.kpu.lbs_platform.global.Profile.profile = profiles.first()
    }

    fun goFeedFragment() {
        FragmentChanger.change(this, FeedFragment())
    }

    fun goAddProfileFragment() {
        FragmentChanger.change(this, AddProfileFragment())
    }
}