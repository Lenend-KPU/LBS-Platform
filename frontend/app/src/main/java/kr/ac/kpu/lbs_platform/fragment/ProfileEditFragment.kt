package kr.ac.kpu.lbs_platform.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.global.*
import kr.ac.kpu.lbs_platform.poko.remote.ProfilesRequest
import kr.ac.kpu.lbs_platform.poko.remote.Request
import splitties.toast.toast
import splitties.views.imageBitmap


class ProfileEditFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        lateinit var instance: ProfileEditFragment
    }

    lateinit var addProfileImageView: ImageView
    var imageUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        instance = this
        val inflated = inflater.inflate(R.layout.fragment_add_profile, container, false)

        addProfileImageView = inflated.findViewById(R.id.addProfileImageView)
        val privateRadioButton = inflated.findViewById<RadioButton>(R.id.addProfilePrivateRadioButton)
        val submitButton = inflated.findViewById<Button>(R.id.addProfileSubmitButton)
        val imageUploadButton = inflated.findViewById<Button>(R.id.addProfileUploadImageButton)
        val nameEditText = inflated.findViewById<EditText>(R.id.addProfileNameEditText)

        submitButton.text = "프로필 수정"

        privateRadioButton.isChecked = Profile.profile!!.fields.profile_private
        nameEditText.setText(Profile.profile!!.fields.profile_name)
        imageUrl = Profile.profile!!.fields.profile_photo
        Glide.with(this).load(imageUrl).fitCenter().override(Target.SIZE_ORIGINAL).into(addProfileImageView)

        imageUploadButton.setOnClickListener {
            requireActivity().startActivityForResult(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                ),
                RequestCode.IMAGE_EDIT_REQUEST_CODE
            )
        }

        privateRadioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            buttonView.isChecked = !isChecked
        }
        submitButton.setOnClickListener {
            val name = nameEditText.text.toString()
            if(name == "" || imageUrl == "") {
                toast("비어있는 필드가 있습니다.")
                return@setOnClickListener
            }
            val private = privateRadioButton.isChecked
            val userid = User.userid

            val params = mutableMapOf<String, String>()
            params["userid"] = userid.toString()
            params["name"] = name
            params["photo"] = imageUrl
            params["private"] = private.toString()
            submitRequest(params)
        }
        return inflated
    }

    fun submitRequest(params: Map<String, String>) {
        RequestHelper.Builder(ProfilesRequest::class)
            .apply {
                this.currentFragment = this@ProfileEditFragment
                this.method = com.android.volley.Request.Method.PUT
                this.urlParameter = "profiles/${Profile.profile!!.pk}/"
                this.params = params
                this.onSuccessCallback = {
                    Log.i(this::class.java.name, it.toString())
                    Profile.profile!!.fields.profile_photo = params["photo"].toString()
                    Profile.profile!!.fields.profile_name = params["name"].toString()
                    Profile.profile!!.fields.profile_private = params["private"].toBoolean()
                    FragmentChanger.change(this@ProfileEditFragment, FeedFragment())
                }
            }
            .build()
            .request()
    }

}