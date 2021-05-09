package kr.ac.kpu.lbs_platform.global

import android.util.Log
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import splitties.toast.toast
import java.nio.charset.Charset
import kotlin.reflect.KClass

class RequestHelper constructor(
    private val fn: () -> Unit
) {
    fun request() {
        fn()
    }
    companion object {
        val defaultBodyContentType = "application/x-www-form-urlencoded; charset=UTF-8"
    }
    class Builder<T : kr.ac.kpu.lbs_platform.poko.remote.Request>(var poko: KClass<T>) {
        var currentFragment: Fragment? = null
        var destFragment: Fragment? = null
        var urlParameter: String = ""
        var params: Map<String, String> = mapOf()
        var method: Int = Request.Method.POST
        var queue: RequestQueue? = null
            get() {
                if(queue == null) {
                    queue = Volley.newRequestQueue(currentFragment?.activity)
                }
                return queue
            }
        var bodyContentType: String = defaultBodyContentType
        var isAsync: Boolean = true
        var onFailureCallback: (responseObject: T) -> Unit = {
            val response = it
            currentFragment?.let {
                toast(response.comment)
            }
        }
        var onSuccessCallback: (responseObject: T) -> Unit = {
            destFragment?.let {
                val destFragment = it
                currentFragment?.let {
                    FragmentChanger.change(it, destFragment)
                }
            }
        }
        fun fn() {
            val fragmentName = currentFragment!!::class.java.name
            val req: StringRequest = object : StringRequest(
                method, "${ServerUrl.url}/$urlParameter",
                { response ->
                    Log.i("LoginFragment", response.toString())
                    val gson = Gson()
                    val responseObject = gson.fromJson(response, poko.java)
                    if (!responseObject.success) {
                        currentFragment?.toast(responseObject.toString())
                    } else {
                        onSuccessCallback(responseObject as T)
                    }
                }, { error ->
                    Log.i(fragmentName, error.toString())
                    error?.networkResponse?.data?.let {
                        val responseBody = String(it, Charset.defaultCharset())
                        val gson = Gson()
                        val request = gson.fromJson(responseBody, poko.java)
                        Log.i(fragmentName, request.toString())
                        onFailureCallback(request as T)
                    }
                }
            ) {
                override fun getBodyContentType(): String {
                    if (bodyContentType != defaultBodyContentType || method == Request.Method.POST) {
                        return bodyContentType
                    }
                    return ""
                }

                @Throws(AuthFailureError::class)
                public override fun getParams(): Map<String, String> {
                    return params
                }
            }
            queue?.add(req)
        }
        fun outer() {
            if (isAsync) {
                GlobalScope.launch {
                    fn()
                }
            } else {
                fn()
            }
        }
        fun build() : RequestHelper {
            return RequestHelper { outer() }
        }
    }
}