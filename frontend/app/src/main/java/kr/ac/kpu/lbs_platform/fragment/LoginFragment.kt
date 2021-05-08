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
import kr.ac.kpu.lbs_platform.global.ServerUrl
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
        val queue = Volley.newRequestQueue(activity)
        val params = mutableMapOf<String, String>()

        params["email"] = email
        params["password"] = password
        val req: StringRequest = object : StringRequest(
            Request.Method.POST, "${ServerUrl.url}/users/login/",
            { response ->
                Log.i("LoginFragment", response.toString())
                val gson = Gson()
                val request = gson.fromJson(response, kr.ac.kpu.lbs_platform.poko.remote.Request::class.java)
                if(!request.success) {
                    toast(request.toString())
                } else {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.mainActivityfragment, FeedFragment())
                        ?.commit()
                }
            }, {error -> Log.i("LoginFragment", error.toString()) }
        ) {
            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                return params
            }
        }
        queue.add(req)
    }
}