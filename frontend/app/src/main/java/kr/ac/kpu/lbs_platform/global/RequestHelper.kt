package kr.ac.kpu.lbs_platform.global

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kr.ac.kpu.lbs_platform.activity.MainActivity
import org.json.JSONObject
import splitties.toast.toast
import java.nio.charset.Charset
import kotlin.reflect.KClass

class RequestHelper private constructor(
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
        var context: Context? = null
        var activity: Activity? = null
        var destFragment: Fragment? = null
        var urlParameter: String = ""
        var params: Map<String, String> = mapOf()
        var jsonObj: JSONObject? = null
        var method: Int = Request.Method.POST
        var queue: RequestQueue? = null
        var bodyContentType: String = defaultBodyContentType
        var isAsync: Boolean = true
        var baseUrl: String = ServerUrl.url
        var bypassFail: Boolean = false
        var requestType: String = "string"
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
            val fragmentName = currentFragment?.let {
                it::class.java.name
            } ?: "RequestHelper"
            if(context == null && activity == null && currentFragment != null) {
                activity = currentFragment?.activity
            }
            if (queue == null) {
                queue = Volley.newRequestQueue(MainActivity.instance!!.applicationContext!!)
            }

            val onResponse = { response: String ->
                Log.i("RequestHelper", response.toString())
                val gson = Gson()
                val responseObject = gson.fromJson(response, poko.java)
                if (!responseObject.success && !bypassFail) {
                    currentFragment?.toast(responseObject.toString())
                } else {
                    onSuccessCallback(responseObject as T)
                }
            }
            val onError = { error: VolleyError? ->
                Log.e(fragmentName, error.toString())
                error?.networkResponse?.data?.let {
                    val responseBody = String(it, Charset.defaultCharset())
                    Log.e(fragmentName, responseBody)
                    val gson = Gson()
                    val request = gson.fromJson(responseBody, poko.java)
                    Log.e(fragmentName, request.toString())
                    onFailureCallback(request as T)
                }
            }

            when(requestType) {
                "string" -> {
                    val req = object : StringRequest(
                        method, "${baseUrl}/$urlParameter",
                        { e -> onResponse(e)},
                        { e -> onError(e)}
                    ) {
                        override fun getBodyContentType(): String {
                            if (this@Builder.bodyContentType == defaultBodyContentType && method == Request.Method.GET) {
                                return ""
                            }
                            return this@Builder.bodyContentType
                        }

                        @Throws(AuthFailureError::class)
                        public override fun getParams(): Map<String, String> {
                            return this@Builder.params
                        }
                    }
                    queue?.add(req)
                }
                "json" -> {
                    val req = object : JsonObjectRequest(
                        method, "${baseUrl}/$urlParameter",
                        jsonObj,
                        { e -> onResponse(e.toString())},
                        { e -> onError(e)}
                    ) {}
                    queue?.add(req)
                }
                else -> {
                    return
                }
            }

        }

        fun build(): RequestHelper {
            return RequestHelper {
                if (isAsync) {
                    GlobalScope.launch {
                        fn()
                    }
                } else {
                    fn()
                }
            }
        }
    }
}