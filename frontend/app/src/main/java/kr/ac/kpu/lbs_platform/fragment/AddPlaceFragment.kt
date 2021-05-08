package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.databinding.FragmentAddPlaceBinding
import kr.ac.kpu.lbs_platform.global.RequestCode


class AddPlaceFragment : Fragment() {

    companion object {
        lateinit var instance: AddPlaceFragment
    }

    lateinit var currentPlace: Place
    var _inflated: View? = null
    val inflated get() = _inflated!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        instance = this
        _inflated = inflater.inflate(R.layout.fragment_add_place, container, false)

        val placeSearchButton = inflated.findViewById<Button>(R.id.placeSearchButton)
        placeSearchButton.setOnClickListener {
            val fields = listOf(Place.Field.ID, Place.Field.NAME)
            activity?.let {
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(it)
                activity?.startActivityForResult(intent, RequestCode.AUTOCOMPLETE_REQUEST_CODE)
            }
        }
        return inflated
    }

    fun setPlaceCallback(place: Place) {
        currentPlace = place
        renderPlace()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _inflated = null
    }


    fun renderPlace() {
        val placeTextView = inflated.findViewById<TextView>(R.id.placeTextView)
        placeTextView.text = currentPlace.name
    }
}