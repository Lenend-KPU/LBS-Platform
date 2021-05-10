package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import com.android.volley.Response
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.global.FragmentChanger
import kr.ac.kpu.lbs_platform.global.Profile
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.global.User
import kr.ac.kpu.lbs_platform.poko.remote.ProfilesRequest
import kr.ac.kpu.lbs_platform.poko.remote.Request
import splitties.toast.toast

class ProfileEditFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflated = inflater.inflate(R.layout.fragment_add_profile, container, false)

        val privateRadioButton = inflated.findViewById<RadioButton>(R.id.addProfilePrivateRadioButton)
        val submitButton = inflated.findViewById<Button>(R.id.addProfileSubmitButton)

        val nameEditText = inflated.findViewById<EditText>(R.id.addProfileNameEditText)
        nameEditText.setText(Profile.profile!!.fields.profile_name)

        privateRadioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            buttonView.isChecked = !isChecked
        }
        submitButton.setOnClickListener {
            val nameEditText = inflated.findViewById<EditText>(R.id.addProfileNameEditText)
            val name = nameEditText.text.toString()
            if(name == "") {
                toast("비어있는 필드가 있습니다.")
                return@setOnClickListener
            }
            val private = privateRadioButton.isChecked
            val photo = 0
            val userid = User.userid

            val params = mutableMapOf<String, String>()
            params["userid"] = userid.toString()
            params["name"] = name
            params["photo"] = photo.toString()
            params["private"] = private.toString()
            RequestHelper.Builder(ProfilesRequest::class)
                .apply {
                    this.currentFragment = this@ProfileEditFragment
                    this.method = com.android.volley.Request.Method.PUT
                    this.urlParameter = "profiles/${Profile.profile!!.pk}/"
                    this.params = params
                    this.onSuccessCallback = {
                        Log.i(this::class.java.name, it.toString())
                        Profile.profile!!.fields.profile_name = name
                        Profile.profile!!.fields.profile_private = private
                        FragmentChanger.change(this@ProfileEditFragment, FeedFragment())
                    }
                }
                .build()
                .request()
        }
        return inflated
    }

}