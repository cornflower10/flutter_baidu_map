package com.cornflower.flutterbaidumap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.geocode.*
import com.baidu.mapapi.search.poi.*
import com.chad.library.adapter.base.BaseQuickAdapter
import io.flutter.app.FlutterActivity
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : FlutterActivity(), BaiduMap.OnMapStatusChangeListener, OnGetGeoCoderResultListener {


    lateinit var location: LocationClient
    lateinit var listLocationAdapter: ListLocationAdapter
    var address: String? = null
    var geoCoder: GeoCoder? = null
    lateinit var bdMap: BaiduMap
    var isFirstLoc: Boolean = true
    var list: List<Location> = ArrayList()
    var city:String?=null
    var latLng:LatLng?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        bdMap = bmapView.map
        bdMap.isMyLocationEnabled = true
        bdMap.setMyLocationConfiguration(MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,
                true, null))
        setMapListenter()
        location = LocationClient(this)
        location.registerLocationListener(MyLocationListener())
        geoCoder = GeoCoder.newInstance()
        geoCoder?.setOnGetGeoCodeResultListener(this)

        //通过LocationClientOption设置LocationClient相关参数
        val option = LocationClientOption()
        option.isOpenGps = true // 打开gps
        option.setCoorType("bd09ll") // 设置坐标类型
        option.setScanSpan(5000)
        option.setIsNeedLocationPoiList(true)
        option.setIsNeedLocationDescribe(true)
        option.setIsNeedAddress(true)
        location.locOption = option
        location.start()


        bt_ok.setOnClickListener {
            FlutterBaiduMapPlugin.instance().sendLocation(address)
            finish()
        }

        tv_search.setOnClickListener {
            if (!TextUtils.isEmpty(city)){
                var intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("city",city)
                intent.putExtra("latLng",latLng)
                startActivityForResult(intent, 99)
            }

        }
        var lastIndex = -1
        listLocationAdapter = ListLocationAdapter(list)
        listLocationAdapter.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                if (lastIndex != position) {
                    updateLocation(list.get(position).poi.location)
                    this@MapActivity.address = location?.let { list.get(position).poi.name }
                }
            }

        })
        rv.adapter = listLocationAdapter;
        rv.layoutManager = LinearLayoutManager(this)
    }

    private fun geoCoder(ll: LatLng) {
        // 发起反地理编码请求(经纬度->地址信息)
        var reverseGeoCodeOption = ReverseGeoCodeOption()
// 设置反地理编码位置坐标
        reverseGeoCodeOption.location(ll)
        geoCoder?.reverseGeoCode(reverseGeoCodeOption)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 99) {
                var location = data?.getParcelableExtra<Location>("location")
                location?.poi?.let { updateLocation(location.poi.location)
                    geoCoder(location.poi.location)

                    address =location.poi.name
                }

//                geoCoder?.geocode(GeoCodeOption().address(location?.address).city(location?.city))

            }
        }
    }

    override fun onGetGeoCodeResult(p0: GeoCodeResult?) {
//        p0?.location?.let { updateLocation(it) }


    }

    override fun onGetReverseGeoCodeResult(p0: ReverseGeoCodeResult?) {
        var listLocation = p0?.poiList
        var mutableList = this@MapActivity.list.toMutableList()
        mutableList.clear()
        for ((index, value) in listLocation?.withIndex()!!) {
            var location = Location()
            if (index == 0) location.isChoose = true else location.isChoose = false
            location.poi = value
            mutableList.add(location)

        }
        this@MapActivity.listLocationAdapter.replaceData(mutableList)
    }

    override fun onResume() {
        super.onResume()
        bmapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        bmapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        location.stop()
        bmapView.map.isMyLocationEnabled = false
        bmapView.onDestroy()
    }

    fun setMapListenter() {
        bdMap.setOnMapStatusChangeListener(this)
    }

    override fun onMapStatusChangeStart(p0: MapStatus?) {
    }

    override fun onMapStatusChangeStart(p0: MapStatus?, p1: Int) {
    }

    override fun onMapStatusChange(p0: MapStatus?) {
    }

    override fun onMapStatusChangeFinish(p0: MapStatus?) {
        //To change body of created functions use File | Settings | File Templates.
//        p0?.target?.let { updateLocation(it) }

        p0?.target?.let { geoCoder(it) }
    }

    inner class MyLocationListener : BDAbstractLocationListener(), OnGetPoiSearchResultListener {
        override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {
        }

        override fun onGetPoiResult(p0: PoiResult?) {

            var listLocation = p0?.allPoi
            var mutableList = this@MapActivity.list.toMutableList()
            for (value in listLocation!!) {
                var location = Location()
                location.isChoose = false
                location.poi = value
                mutableList.add(location)

            }
            this@MapActivity.listLocationAdapter.replaceData(mutableList)
        }

        override fun onGetPoiDetailResult(p0: PoiDetailResult?) {
        }

        override fun onGetPoiDetailResult(p0: PoiDetailSearchResult?) {
        }

        override fun onReceiveLocation(location: BDLocation?) {
            this@MapActivity.address = location?.let { location.addrStr }
            this@MapActivity.city = location?.city
            val locData = location?.radius?.let {
                MyLocationData.Builder()
                        .accuracy(it)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection()).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build()
            }
            this@MapActivity.bdMap.setMyLocationData(locData)
            if (this@MapActivity.isFirstLoc) {
//                location?.locationDescribe
                this@MapActivity.isFirstLoc = false

                val ll = LatLng(location!!.getLatitude(),
                        location.getLongitude())
                updateLocation(ll)
                this@MapActivity.geoCoder(ll)
//                nearbyPoiSearch(ll, 600, 0, location.locationDescribe)

            }
        }


    }

    private fun updateLocation(ll: LatLng) {
        val u = MapStatusUpdateFactory.newLatLngZoom(ll, 17f)
        bdMap.animateMapStatus(u)
        latLng = ll
    }


}
