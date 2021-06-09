package kr.ac.kpu.lbs_platform.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.global.RequestCode
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.global.User
import kr.ac.kpu.lbs_platform.poko.remote.Request
import splitties.toast.toast


open class AddProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        lateinit var instance: AddProfileFragment
    }

    lateinit var addProfileImageView: ImageView
    var imageBitMap: Bitmap? = null
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

        imageUploadButton.setOnClickListener {
            requireActivity().startActivityForResult(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                ),
                RequestCode.IMAGE_UPLOAD_REQUEST_CODE
            )
        }

        privateRadioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            buttonView.isChecked = !isChecked
        }
        submitButton.setOnClickListener {
            val nameEditText = inflated.findViewById<EditText>(R.id.addProfileNameEditText)
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

    protected open fun submitRequest(params: Map<String, String>) {
        RequestHelper.Builder(Request::class)
            .apply {
                this.currentFragment = this@AddProfileFragment
                this.destFragment = FeedFragment()
                this.urlParameter = "profiles/"
                this.params = params
                this.onSuccessCallback = {
                    LoginFragment.checkUserProfileExists(this@AddProfileFragment )
                }
            }
            .build()
            .request()
    }

}