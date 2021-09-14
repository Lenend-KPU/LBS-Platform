package kr.ac.kpu.lbs_platform.activity

import android.Manifest
import android.R.attr
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.NetworkResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.MapsInitializer
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.databinding.ActivityMainBinding
import kr.ac.kpu.lbs_platform.fragment.*
import kr.ac.kpu.lbs_platform.fragment.AddProfileFragment.Companion.instance
import kr.ac.kpu.lbs_platform.global.Profile
import kr.ac.kpu.lbs_platform.global.RequestCode
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.global.VolleyMultipartRequest
import kr.ac.kpu.lbs_platform.poko.remote.Place
import kr.ac.kpu.lbs_platform.poko.remote.Request
import splitties.toast.toast
import java.util.*


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, InvalidatableBottomNavigationManager {
    companion object {
        var instance: MainActivity? = null
        var previousItemId: Int = 0
    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.place_api_key), Locale.KOREA)
        }
        instance = this
        val view = binding.root
        binding.bottomNavigationView.visibility = View.GONE
        binding.bottomNavigationView.selectedItemId = R.id.page_feed
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)
        window.statusBarColor = getColor(R.color.black)
        setContentView(view)
        grantPermissions()

        MapsInitializer.initialize(this)
    }

    fun grantPermissions() {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: List<String?>) {
                Toast.makeText(
                    this@MainActivity,
                    "Permission Denied\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE)
            .check()
    }

    override fun invalidateBottomNavigationRoute() {
        previousItemId = -1
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if(
            previousItemId == item.itemId
        ) {
            return true
        }
        Log.i("MainActivity", item.itemId.toString())
        previousItemId = item.itemId
        when(item.itemId) {
            R.id.page_feed -> {
                Log.i("MainActivity", "page_feed")
                supportFragmentManager.beginTransaction().replace(R.id.mainActivityfragment, FeedFragment()).commit()
                return true
            }
            R.id.page_search -> {
                Log.i("MainActivity", "page_search")
                supportFragmentManager.beginTransaction().replace(R.id.mainActivityfragment, SearchFragment()).commit()
                return true
            }
            R.id.page_add -> {
                Log.i("MainActivity", "page_add")
                supportFragmentManager.beginTransaction().replace(R.id.mainActivityfragment, AddFragment()).commit()
                return true
            }
            R.id.page_profile -> {
                Log.i("MainActivity", "page_profile")
                supportFragmentManager.beginTransaction().replace(R.id.mainActivityfragment, ProfileFragment(Profile.profile!!)).commit()
                return true
            }
            else -> {
                Log.i("MainActivity", "else")
                return true
            }
        }
    }

    @SuppressLint("LongLogTag")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("ActivityResult: requestCode", requestCode.toString())
        Log.i("ActivityResult: resultCode", resultCode.toString())
        Log.i("ActivityResult: data", data.toString())
        if(resultCode != RESULT_OK) {
            return
        }
        when(requestCode) {
            RequestCode.AUTOCOMPLETE_REQUEST_CODE -> {
                data?.let {
                    val place = Autocomplete.getPlaceFromIntent(it)
                    AddPlaceFragment.instance.setPlaceCallback(place)
                }
            }
            RequestCode.SELECT_PLACE_REQUEST_CODE -> {
                data?.let {
                    val place = it.getSerializableExtra("place") as Place
                    Log.i(this::class.java.name, place.pk.toString())
                    if(AddDocumentFragment.places.contains(place)) {
                        toast("중복된 place입니다.")
                        return
                    }
                    AddDocumentFragment.places.add(place)
                    AddDocumentFragment.instance.notifyDataHasChanged()
                }
            }
            RequestCode.IMAGE_UPLOAD_REQUEST_CODE -> {
                data?.let {
                    it.data ?: return
                    val stream = contentResolver.openInputStream(Uri.parse(it.dataString))
                    val bitmap = BitmapFactory.decodeStream(stream)
                    postImage(bitmap, AddProfileFragment.instance) {
                        val imageUrl = String(it.data)
                        AddProfileFragment.instance.imageUrl = imageUrl
                        toast(imageUrl)
                        Glide.with(this).load(imageUrl).fitCenter().override(Target.SIZE_ORIGINAL).into(AddProfileFragment.instance.addProfileImageView)
                        Log.i("IMAGE_UPLOAD_REQUEST_CODE", imageUrl)
                    }
                }
            }
            RequestCode.IMAGE_EDIT_REQUEST_CODE -> {
                data?.let {
                    it.data ?: return
                    val stream = contentResolver.openInputStream(Uri.parse(it.dataString))
                    val bitmap = BitmapFactory.decodeStream(stream)
                    postImage(bitmap, ProfileEditFragment.instance) {
                        val imageUrl = String(it.data)
                        ProfileEditFragment.instance.imageUrl = imageUrl
                        toast(imageUrl)
                        Glide.with(this).load(imageUrl).fitCenter().override(Target.SIZE_ORIGINAL).into(ProfileEditFragment.instance.addProfileImageView)
                        Log.i("IMAGE_EDIT_REQUEST_CODE", imageUrl)
                    }
                }
            }
            RequestCode.PLACE_IMAGE_UPLOAD_REQUEST_CODE -> {
                data?.let {
                    it.data ?: return
                    val stream = contentResolver.openInputStream(Uri.parse(it.dataString))
                    val bitmap = BitmapFactory.decodeStream(stream)
                    postImage(bitmap, AddPlaceFragment.instance) {
                        val imageUrl = String(it.data)
                        AddPlaceFragment.instance.imageUrl = imageUrl
                        toast(imageUrl)
                        Glide.with(this).load(imageUrl).fitCenter().override(Target.SIZE_ORIGINAL).into(AddPlaceFragment.instance.addPlaceImageView)
                        Log.i("PLACE_IMAGE_UPLOAD_REQUEST_CODE", imageUrl)
                    }
                }
            }
            else -> {
                return
            }
        }
    }

    fun postImage(bitmap: Bitmap, instance: Fragment, callback: (NetworkResponse) -> Unit) {
        RequestHelper.Builder(Request::class)
            .apply {
                this.currentFragment = instance
                this.destFragment = null
                this.urlParameter = "s3_upload/"
                this.requestType = "form"
                this.method = com.android.volley.Request.Method.POST
                this.formObj = VolleyMultipartRequest.bitmapToByteArray(bitmap)
                this.bypassFail = true
                this.rawOnSuccessCallback = callback
            }
            .build()
            .request()
    }
}