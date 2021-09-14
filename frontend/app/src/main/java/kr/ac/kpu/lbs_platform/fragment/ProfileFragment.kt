package kr.ac.kpu.lbs_platform.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.activity.MainActivity
import kr.ac.kpu.lbs_platform.global.FragmentChanger
import kr.ac.kpu.lbs_platform.global.Profile
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.poko.remote.FriendRequest
import kr.ac.kpu.lbs_platform.poko.remote.Request
import splitties.toast.toast

class ProfileFragment(val selectedProfile: kr.ac.kpu.lbs_platform.poko.remote.Profile) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var profileFollowButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val inflated = inflater.inflate(R.layout.fragment_profile, container, false)
        profileFollowButton = inflated.findViewById(R.id.profileFollowButton)
        val profileTextView = inflated.findViewById<TextView>(R.id.profileTextView)
        val profileActivityImageView = inflated.findViewById<ImageView>(R.id.profileActivityImageView)

        selectedProfile.let {
            Glide.with(this).load(it.fields.profile_photo).fitCenter().override(Target.SIZE_ORIGINAL).into(profileActivityImageView)
        }

        profileTextView.text = selectedProfile.let {
            return@let it.fields.profile_name
        }


        profileFollowButton.let {
            if(Profile.profile != selectedProfile) {
                it.visibility = View.VISIBLE
                followingUserAction(selectedProfile!!.pk,
                    {
                        profileFollowButton.setText("unfollow")
                    }, {
                        profileFollowButton.setText("follow")
                    }
                )
                it.setOnClickListener {
                    followingUserAction(selectedProfile!!.pk,
                        {
                            unfollowUser(selectedProfile!!.pk)
                        }, {
                            followUser(selectedProfile!!.pk)
                        }
                    )
                }
            } else {
                it.visibility = View.GONE
            }
        }


        val follwingCountTextView = inflated.findViewById<TextView>(R.id.followingCountTextView)
        val followersCountTextView = inflated.findViewById<TextView>(R.id.followersCountTextView)

        follwingCountTextView.text = selectedProfile?.let {
            return@let it.fields.profile_following.toString()
        }
        followersCountTextView.text = selectedProfile?.let {
            return@let it.fields.profile_follower.toString()
        }

        val followersLayout = inflated.findViewById<LinearLayout>(R.id.followersLayout)
        val followingLayout = inflated.findViewById<LinearLayout>(R.id.followingLayout)

        followersLayout.setOnClickListener {
            FragmentChanger.change(this, FollowerFragment(selectedProfile))
        }

        followingLayout.setOnClickListener {
            FragmentChanger.change(this, FollowingFragment(selectedProfile))
        }


        val routesRadioButton = inflated.findViewById<RadioButton>(R.id.routesRadioButton)
        val placesRadioButton = inflated.findViewById<RadioButton>(R.id.placesRadioButton)

        routesRadioButton.setOnClickListener {
            routesRadioButton.isChecked = true
            placesRadioButton.isChecked = false
            FragmentChanger.change(this, DocumentFragment(selectedProfile), R.id.profileFragment)
        }
        placesRadioButton.setOnClickListener {
            placesRadioButton.isChecked = true
            routesRadioButton.isChecked = false
            FragmentChanger.change(this, PlaceFragment(selectedProfile), R.id.profileFragment)
        }
        routesRadioButton.performClick()

        // Inflate the layout for this fragment
        return inflated
    }

    fun followingUserAction(profilePk: Int, ifExistsCallback: () -> Unit, ifNotExistsCallback: () -> Unit) {
        val myProfilePk = Profile.profile!!.pk
        RequestHelper.Builder(FriendRequest::class)
            .apply {
                this.destFragment = null
                this.urlParameter = "profiles/$myProfilePk/friends/"
                this.method = com.android.volley.Request.Method.GET
                this.onSuccessCallback = {
                    Log.i("followingUserAction", it.result.followings.toList().toString())
                    if(it.result.followings.any { it.fields.friend_profile.pk == profilePk }) {
                        ifExistsCallback()
                    } else {
                        ifNotExistsCallback()
                    }
                }
            }
            .build()
            .request()
    }

    fun unfollowUser(profilePk: Int) {
        val myProfilePk = Profile.profile!!.pk
        RequestHelper.Builder(Request::class)
            .apply {
                this.destFragment = null
                this.urlParameter = "profiles/$myProfilePk/friends/$profilePk/"
                this.method = com.android.volley.Request.Method.DELETE
                this.onSuccessCallback = {
                    profileFollowButton.setText("follow")
                    toast("언팔로우 성공")
                }
            }
            .build()
            .request()
    }

    fun followUser(profilePk: Int) {
        val myProfilePk = Profile.profile!!.pk
        RequestHelper.Builder(Request::class)
            .apply {
                this.destFragment = null
                this.urlParameter = "profiles/$myProfilePk/friends/$profilePk/"
                this.method = com.android.volley.Request.Method.POST
                this.onSuccessCallback = {
                    profileFollowButton.setText("unfollow")
                    toast("팔로우 성공")
                }
            }
            .build()
            .request()
    }
}