package kr.ac.kpu.lbs_platform.global

import android.util.Log
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kr.ac.kpu.lbs_platform.R
import splitties.toast.toast
import java.nio.charset.Charset

object RequestHelper {
    fun request (
        currentFragment: Fragment,
        destFragment: Fragment,
        urlParameter: String = "",
        params: Map<String, String> = mapOf(),
        method: Int = Request.Method.POST,
        poko: Class<out kr.ac.kpu.lbs_platform.poko.remote.Request> = kr.ac.kpu.lbs_platform.poko.remote.Request::class.java,
        fragmentName: String = poko.name,
        queue: RequestQueue = Volley.newRequestQueue(currentFragment.activity)
    ) {
        val req: StringRequest = object : StringRequest(
            method, "${ServerUrl.url}/$urlParameter",
            { response ->
                Log.i("LoginFragment", response.toString())
                val gson = Gson()
                val request = gson.fromJson(response, poko)
                if(!request.success) {
                    currentFragment.toast(request.toString())
                } else {
                    currentFragment.activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.mainActivityfragment, destFragment)
                        ?.commit()
                }
            }, { error ->
                Log.i(fragmentName, error.toString())
                val responseBody = String(error.networkResponse.data, Charset.defaultCharset())
                val gson = Gson()
                val request = gson.fromJson(responseBody, poko)
                Log.i(fragmentName, request.toString())
            }
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