package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.adapter.DocumentAdapter
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.global.ServerUrl
import kr.ac.kpu.lbs_platform.poko.remote.DocumentRequest
import kr.ac.kpu.lbs_platform.poko.remote.SearchRequest
import org.json.JSONObject
import splitties.toast.toast
import java.net.URLEncoder

class SearchFragment : Fragment(), Invalidatable {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var text = ""
    var bundle: Bundle? = null
    lateinit var searchView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bundle = savedInstanceState
        val inflated = inflater.inflate(R.layout.fragment_search, container, false)

        val editText = inflated.findViewById<EditText>(R.id.SearchEditText)
        val submitButton = inflated.findViewById<Button>(R.id.SearchSubmitButton)
        searchView = inflated.findViewById<RecyclerView>(R.id.SearchView)
        searchView.layoutManager = LinearLayoutManager(this.activity)

        submitButton.setOnClickListener {
            text = editText.text.toString()
            text = URLEncoder.encode(text, "UTF-8")
            if(text.isBlank()) {
                toast("내용이 비었습니다.")
                return@setOnClickListener
            }
            searchFromServer(searchView, bundle)
        }

        return inflated
    }

    override fun invalidateRecyclerView() {
        searchFromServer(searchView, bundle)
    }

    fun searchFromServer(searchView: RecyclerView, bundle: Bundle?) {
        val requestBody = "{\n" +
                " \"min_score\": 0.5,\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" +
                "        {\n" +
                "          \"match\": {\n" +
                "            \"fields.document_private\": true\n" +
                "         }\n" +
                "        },\n" +
                "        {\n" +
                "          \"match\": {\n" +
                "            \"places.fields.place_name\": {\n" +
                "              \"query\": \"$text\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}"
        Log.i("SearchFragment", requestBody)
        val jsonObj = JSONObject(requestBody)
        RequestHelper.Builder(SearchRequest::class)
            .apply {
                this.currentFragment = this@SearchFragment
                this.urlParameter = "feed/_search/"
                this.baseUrl = "${ServerUrl.url}:9200"
                this.method = Request.Method.POST
                this.jsonObj = jsonObj
                this.requestType = "json"
                this.bodyContentType = "application/json; charset=UTF-8"
                this.bypassFail = true
                this.onSuccessCallback = {
                    Log.i("SearchFragment", it.toString())
                    val documentRequest = DocumentRequest(true, 200, "", result = it.hits.hits.map { e -> e._source }.toTypedArray() )
                    Log.i("onSuccess", documentRequest.toString())
                    searchView.adapter = DocumentAdapter(documentRequest, bundle, this.activity!!, this@SearchFragment)
                }
            }
            .build()
            .request()
    }

}