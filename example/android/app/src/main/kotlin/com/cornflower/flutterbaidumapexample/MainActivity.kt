package com.cornflower.flutterbaidumapexample

import android.os.Bundle
import io.flutter.app.FlutterActivity
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GeneratedPluginRegistrant.registerWith(this)
//    FlutterBaiduMapPlugin.registerWith(this.registrarFor("BaiduSdkPlugin"))
//    startActivity(Intent(this,MapActivity::class.java))
  }
}
