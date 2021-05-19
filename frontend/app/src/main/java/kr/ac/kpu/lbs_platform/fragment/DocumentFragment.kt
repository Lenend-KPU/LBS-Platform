package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.activity.MainActivity
import kr.ac.kpu.lbs_platform.adapter.DocumentAdapter
import kr.ac.kpu.lbs_platform.global.Profile
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.poko.remote.DocumentRequest
import kr.ac.kpu.lbs_platform.poko.remote.Request

class DocumentFragment : Fragment(), invalidatable {

    lateinit var documentRecyclerView: RecyclerView
    var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bundle = savedInstanceState
        val inflated = inflater.inflate(R.layout.fragment_document, container, false)
        documentRecyclerView = inflated.findViewById<RecyclerView>(R.id.documentRecyclerView)
        documentRecyclerView.layoutManager = LinearLayoutManager(this.activity)
        getDocumentsFromServer(documentRecyclerView, savedInstanceState)
        // Inflate the layout for this fragment
        return inflated
    }

    fun getDocumentsFromServer(recyclerView: RecyclerView, savedInstanceState: Bundle?) {
        Log.i(this::class.java.name, "getDocumentsFromServer")
        val profileNumber = Profile.profile!!.pk
        RequestHelper.Builder(DocumentRequest::class)
            .apply {
                this.currentFragment = this@DocumentFragment
                this.destFragment = null
                this.urlParameter = "profiles/$profileNumber/documents/"
                this.method = com.android.volley.Request.Method.GET
                this.onSuccessCallback = {
                    Log.i("SAVES", it.result.map { it.saves }.toString())
                    recyclerView.adapter = DocumentAdapter(it, savedInstanceState, MainActivity.instance!!, this@DocumentFragment)
                }
            }
            .build()
            .request()
    }

    override fun invalidateRecyclerView() {
        getDocumentsFromServer(documentRecyclerView, bundle)
    }
}