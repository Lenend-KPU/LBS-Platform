package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.global.Profile
import kr.ac.kpu.lbs_platform.global.RequestCode
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.poko.remote.Request
import splitties.toast.toast


class AddPlaceFragment : Fragment() {

    companion object {
        lateinit var instance: AddPlaceFragment
    }

    var currentPlace: Place? = null
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
            val fields = listOf(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME)
            activity?.let {
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(it)
                activity?.startActivityForResult(intent, RequestCode.AUTOCOMPLETE_REQUEST_CODE)
            }
        }
        val placeSubmitButton = inflated.findViewById<Button>(R.id.submitPlaceButton)
        val rateEditText = inflated.findViewById<EditText>(R.id.placeContentEditText)
        placeSubmitButton.setOnClickListener {
            val rate = rateEditText.text.toString()
            if(currentPlace == null || rate == "") {
                toast("비어있는 필드가 있습니다.")
                return@setOnClickListener
            }
            currentPlace?.let {
                postPlaceToserver(it, rate)
            }
        }
        return inflated
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _inflated = null
    }

    fun postPlaceToserver(place: Place, content: String) {
        val params = mutableMapOf<String, String>()
        params["name"] = place.name.toString()
        params["rate"] = content
        params["photo"] = "0"
        params["private"] = "true"
        params["latitude"] = place.latLng?.let {
            return@let it.latitude.toString()
        } ?: "0"
        params["longitude"] = place.latLng?.let {
            return@let it.longitude.toString()
        } ?: "0"
        val profileNumber = Profile.profile?.pk.toString()
        RequestHelper.Builder(Request::class)
            .apply {
                this.currentFragment = this@AddPlaceFragment
                this.destFragment = PlaceFragment()
                this.urlParameter = "profiles/$profileNumber/places/"
                this.params = params
            }
            .build()
            .request()
    }


    fun setPlaceCallback(place: Place) {
        currentPlace = place
        renderPlace()
    }


    fun renderPlace() {
        val placeTextView = inflated.findViewById<TextView>(R.id.placeTextView)
        placeTextView.text = currentPlace?.name
    }
}