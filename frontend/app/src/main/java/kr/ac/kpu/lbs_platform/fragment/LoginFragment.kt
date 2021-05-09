package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
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
        RequestHelper.Builder(LoginRequest::class)
            .apply {
                this.currentFragment = this@LoginFragment
                this.destFragment = FeedFragment()
                this.urlParameter = "login/"
                this.params = params
                this.onSuccessCallback = {
                    val response = it
                    val userid = response.userid
                    User.userid = userid
                    checkUserProfileExists(this@LoginFragment)
                }
            }
            .build()
            .request()
    }

    companion object {
        fun checkUserProfileExists(fragment: Fragment) {
            val userid = User.userid
            RequestHelper.Builder(ProfilesRequest::class)
                .apply {
                    this.currentFragment = fragment
                    this.destFragment = FeedFragment()
                    this.urlParameter = "profiles?userid=${userid}"
                    this.method = com.android.volley.Request.Method.GET
                    this.params = params
                    this.onFailureCallback = {
                        toast(it.toString())
                        if (it.status == 401) {
                            goAddProfileFragment(fragment)
                        }
                    }
                    this.onSuccessCallback = {
                        val response = it
                        val result = response.result
                        result?.let {
                            if (it.isEmpty()) {
                                goAddProfileFragment(fragment)
                                return@let
                            }
                            getUserProfile(it.first())
                            goFeedFragment(fragment)
                        } ?: goAddProfileFragment(fragment)
                    }
                }
                .build()
                .request()
        }

        fun getUserProfile(profile: Profile) {
            kr.ac.kpu.lbs_platform.global.Profile.profile = profile
        }

        fun goFeedFragment(fragment: Fragment) {
            FragmentChanger.change(fragment, FeedFragment())
        }

        fun goAddProfileFragment(fragment: Fragment) {
            FragmentChanger.change(fragment, AddProfileFragment())
        }
    }

}