package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.global.ServerUrl
import org.json.JSONObject
import splitties.toast.toast
import java.nio.charset.Charset


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
        RequestHelper.request(this, FeedFragment(),"login/", params)
    }
}