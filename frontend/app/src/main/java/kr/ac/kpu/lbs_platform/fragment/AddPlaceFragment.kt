package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.activity.MainActivity


class AddPlaceFragment : Fragment() {
    private val AUTOCOMPLETE_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflated = inflater.inflate(R.layout.fragment_add_place, container, false)

        val placeSearchEditText = inflated.findViewById<EditText>(R.id.placeSearchEditText)
        placeSearchEditText.setOnClickListener {
            val fields = listOf(Place.Field.ID, Place.Field.NAME)
            MainActivity.instance?.let {
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(it)
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
            }
        }
        return inflated
    }
}