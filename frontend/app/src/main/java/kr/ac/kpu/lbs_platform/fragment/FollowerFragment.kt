package kr.ac.kpu.lbs_platform.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.adapter.FollowerAdapter
import kr.ac.kpu.lbs_platform.global.Profile
import kr.ac.kpu.lbs_platform.global.RequestHelper
import kr.ac.kpu.lbs_platform.poko.remote.FriendRequest

class FollowerFragment : Fragment(), Invalidatable {

    lateinit var followerRecyclerView: RecyclerView
    val selectedProfile = Profile.selectedProfile ?: Profile.profile


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflated = inflater.inflate(R.layout.fragment_follower, container, false)
        followerRecyclerView = inflated.findViewById(R.id.followerRecyclerView)
        followerRecyclerView.layoutManager = LinearLayoutManager(this.activity)
        getFriendsFromServer(followerRecyclerView)
        return inflated
    }

    fun getFriendsFromServer(recyclerView: RecyclerView) {
        Log.i(this::class.java.name, "getFriendsFromServer")
        val profileNumber = selectedProfile!!.pk
        RequestHelper.Builder(FriendRequest::class)
            .apply {
                this.currentFragment = this@FollowerFragment
                this.destFragment = null
                this.urlParameter = "profiles/$profileNumber/friends/"
                this.method = com.android.volley.Request.Method.GET
                this.onSuccessCallback = {
                    Log.i("FollowerFragment", it.toString())
                    recyclerView.adapter = FollowerAdapter(it, this@FollowerFragment)
                }
            }
            .build()
            .request()
    }

    override fun invalidateRecyclerView() {
        getFriendsFromServer(followerRecyclerView)
    }
}