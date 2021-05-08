package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.activity.MainActivity

class AddFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflated = inflater.inflate(R.layout.fragment_add, container, false)

        val buttonIds = listOf(R.id.generateDocumentButton, R.id.generatePlaceButton)
        val buttons = buttonIds.map { id -> inflated.findViewById<Button>(id) }
        val fragments = listOf(AddDocumentFragment(), AddPlaceFragment())

        for((button, fragment) in buttons.zip(fragments)) {
            button.setOnClickListener {
                MainActivity.instance?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.mainActivityfragment, fragment)
                    ?.commit()
            }
        }
        return inflated
    }
}