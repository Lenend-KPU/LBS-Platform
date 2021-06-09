package kr.ac.kpu.lbs_platform.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kr.ac.kpu.lbs_platform.R
import kr.ac.kpu.lbs_platform.adapter.PlaceAdapter
import kr.ac.kpu.lbs_platform.adapter.SelectPlaceAdapter
import kr.ac.kpu.lbs_platform.databinding.ActivityMainBinding
import kr.ac.kpu.lbs_platform.databinding.ActivitySelectPlaceBinding
import kr.ac.kpu.lbs_platform.fragment.PlaceFragment
import kr.ac.kpu.lbs_platform.poko.remote.Place

class SelectPlaceActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectPlaceBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectPlaceBinding.inflate(layoutInflater)

        val layoutManager = LinearLayoutManager(this)

        val selectPlaceRecyclerView = binding.selectPlaceRecyclerView
        selectPlaceRecyclerView.layoutManager = layoutManager

        GlobalScope.launch {
            PlaceFragment.getPlacesFromServer(
                currentFragment = null,
                currentActivity = this@SelectPlaceActivity
            ) { placeRequest ->
                selectPlaceRecyclerView.adapter = SelectPlaceAdapter(placeRequest.result!!, this@SelectPlaceActivity) {
                    val itemPosition: Int = selectPlaceRecyclerView.getChildLayoutPosition(it)
                    val item: Place = placeRequest.result[itemPosition]
                    val intent = Intent()
                    intent.putExtra("pk", item.pk)
                    setResult(RESULT_OK, intent)
                    finish();
                }
            }
        }
        val view = binding.root
        setContentView(view)
    }
}