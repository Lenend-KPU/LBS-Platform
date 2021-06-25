package kr.ac.kpu.lbs_platform.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.activity.MainActivity
import kr.ac.kpu.lbs_platform.global.FragmentChanger
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
    var imageUrl = ""
    lateinit var addPlaceImageView: ImageView

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

        addPlaceImageView = inflated.findViewById(R.id.addPlaceImageView)
        val addImageButton = inflated.findViewById<Button>(R.id.addPlaceAddImageButton)
        val placeSubmitButton = inflated.findViewById<Button>(R.id.submitPlaceButton)
        val ratingBar = inflated.findViewById<RatingBar>(R.id.addPlaceRatingBar)

        addImageButton.setOnClickListener {
            requireActivity().startActivityForResult(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                ),
                RequestCode.PLACE_IMAGE_UPLOAD_REQUEST_CODE
            )
        }

        placeSubmitButton.setOnClickListener {
            val rate = ratingBar.rating.toInt().toString()
            if(currentPlace == null || rate == "") {
                toast("비어있는 필드가 있습니다.")
                return@setOnClickListener
            }
            currentPlace?.let {
                postPlaceToserver(it, rate)
            }
        }

        ratingBar.setOnRatingBarChangeListener { _, rating, _ -> ratingBar.rating = rating }

        return inflated
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _inflated = null
    }

    fun postPlaceToserver(place: Place, rate: String) {
        val params = mutableMapOf<String, String>()
        params["name"] = place.name.toString()
        params["rate"] = rate
        params["photo"] = imageUrl
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
                this.destFragment = null
                this.urlParameter = "profiles/$profileNumber/places/"
                this.params = params
                this.onSuccessCallback = {
                    MainActivity.instance!!.let {
                        val bottomNavView = it.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                        val menu = bottomNavView.menu
                        menu.forEach {
                            it.isChecked = false
                        }
                        menu.findItem(R.id.page_profile).isChecked = true
                        FragmentChanger.change(this@AddPlaceFragment, ProfileFragment(Profile.profile!!))
                    }
                }
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