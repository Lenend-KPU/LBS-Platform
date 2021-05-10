package kr.ac.kpu.lbs_platform.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.activity.SelectPlaceActivity
import kr.ac.kpu.lbs_platform.adapter.DocumentAdapter
import kr.ac.kpu.lbs_platform.global.RequestCode
import splitties.toast.toast

class AddDocumentFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        var places = mutableListOf<Int>()
        lateinit var instance: AddDocumentFragment
    }

    lateinit var inflated: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        instance = this
        places = mutableListOf<Int>()
        inflated = inflater.inflate(R.layout.fragment_add_document, container, false)

        val addPlaceButton = inflated.findViewById<Button>(R.id.addPlaceButton)

        addPlaceButton.setOnClickListener {
            val intent = Intent(this.activity, SelectPlaceActivity::class.java)
            this.activity?.startActivityForResult(intent, RequestCode.SELECT_PLACE_REQUEST_CODE)
        }

        val submitDocumentButton = inflated.findViewById<Button>(R.id.submitDocumentButton)
        val documentNameEditText = inflated.findViewById<EditText>(R.id.documentNameEditText)
        val contentEditText = inflated.findViewById<EditText>(R.id.contentEditText)
        submitDocumentButton.setOnClickListener {
            val title = documentNameEditText.text.toString()
            val content = contentEditText.text.toString()
            if(title == "" || content == "") {
                toast("비어있는 필드가 있습니다.")
                return@setOnClickListener
            }
        }
        val addDocumentRecyclerView = inflated.findViewById<RecyclerView>(R.id.addDocumentRecyclerView)
        addDocumentRecyclerView.layoutManager = LinearLayoutManager(this.activity)
        addDocumentRecyclerView.adapter = DocumentAdapter(places)
        // Inflate the layout for this fragment
        return inflated
    }

    fun notifyDataHasChanged() {
        val addDocumentRecyclerView = inflated.findViewById<RecyclerView>(R.id.addDocumentRecyclerView)
        addDocumentRecyclerView.adapter = DocumentAdapter(places)
    }
}