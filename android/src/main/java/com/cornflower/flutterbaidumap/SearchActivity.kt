package com.cornflower.flutterbaidumap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.poi.*
import com.baidu.mapapi.search.sug.SuggestionSearch
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : AppCompatActivity(), OnGetPoiSearchResultListener {

    lateinit var listLocationAdapter: SearchAdapter
    var list: List<Location> = ArrayList()
    lateinit var search :SuggestionSearch
    var city:String?=null
    var ll:LatLng?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_search)
        }catch (e:Exception){
          e.printStackTrace()
        }

        listLocationAdapter = SearchAdapter(list)
        rv.adapter = listLocationAdapter
        rv.layoutManager = LinearLayoutManager(this)
        city = intent.getStringExtra("city")
        ll = intent.getParcelableExtra("latLng")
        listLocationAdapter.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

             var location = list.toMutableList().get(position)

              setResult(Activity.RESULT_OK, Intent().putExtra("location",location))
                finish()
            }



        })
        tv_cancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        search = SuggestionSearch.newInstance()
//        search .setOnGetSuggestionResultListener {
//            listLocationAdapter.replaceData(it.allSuggestions)
//        }


        et_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {

            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {

                 if (actionId == EditorInfo.IME_ACTION_SEND||actionId == EditorInfo.IME_ACTION_SEARCH  ||  event?.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                      var text = relaceBalnk(et_search.text.toString())
                     if(!TextUtils.isEmpty(text)){
//                        var option =  SuggestionSearchOption().citylimit(false).keyword(text).city(city).location(ll)
//                         search.requestSuggestion(option)
                         ll?.let{
                             nearbyPoiSearch(ll!!,10000000,0,text)

                         }
                     }

                     return true

                } else  return false

            }

        })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString("city",city)
        outState?.putParcelable("latLng",ll)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        ll = savedInstanceState?.getParcelable("latLng")
        city =  savedInstanceState?.getString("city")
    }
    /**
     * 去除空格
     *
     * @param str
     * @return
     */
    fun relaceBalnk(str: String): String {
        return if (TextUtils.isEmpty(str)) "" else str.replace(" ".toRegex(), "")

    }
    override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGetPoiResult(p0: PoiResult?) {
        var listLocation = p0?.allPoi
        var mutableList = list.toMutableList()
        if (listLocation!=null){
            for (value in listLocation!!) {
                var location = Location()
                location.isChoose = false
                location.poi = value
                mutableList.add(location)

            }
            list = mutableList
            listLocationAdapter?.replaceData(list)
        }else{
            mutableList?.clear()
            listLocationAdapter?.notifyDataSetChanged()
            Toast.makeText(this,"没有数据",Toast.LENGTH_SHORT).show()
        }
      //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGetPoiDetailResult(p0: PoiDetailResult?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGetPoiDetailResult(p0: PoiDetailSearchResult?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    /**
     * 周边poi检索示例
     */
    fun nearbyPoiSearch(ll: LatLng, radius: Int, loadIndex: Int, key: String) {
        //创建poi检索实例
        var poiSearch = PoiSearch.newInstance()
        //创建poi监听者
        //设置poi监听者该方法要先于检索方法searchNearby(PoiNearbySearchOption)前调用，否则会在某些场景出现拿不到回调结果的情况
        poiSearch.setOnGetPoiSearchResultListener(this)
        //设置请求参数
        var nearbySearchOption = PoiNearbySearchOption()
                .keyword(key)//检索关键字
                .location(ll)//检索位置
                .pageNum(loadIndex)//分页编号，默认是0页
                .pageCapacity(10)//设置每页容量，默认10条
                .radius(radius).radiusLimit(false)//附近检索半径
        poiSearch.searchNearby(nearbySearchOption)
        //释放检索对象
//        poiSearch.destroy()
    }



}
