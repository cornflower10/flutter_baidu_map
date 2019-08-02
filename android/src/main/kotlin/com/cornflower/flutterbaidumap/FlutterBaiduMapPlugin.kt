package com.cornflower.flutterbaidumap

import android.content.Intent
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

class FlutterBaiduMapPlugin: MethodCallHandler,EventChannel.StreamHandler {
  var  sink : EventChannel.EventSink? = null
  lateinit var registrar: Registrar
  override fun onListen(p0: Any?, p1: EventChannel.EventSink?) {
   //To change body of created functions use File | Settings | File Templates.
    sink = p1
  }

  override fun onCancel(p0: Any?) {
  //To change body of created functions use File | Settings | File Templates.
  }

  companion object {
    lateinit var flutterBaiduMapPlugin :FlutterBaiduMapPlugin
    @JvmStatic
    fun registerWith(registrar: Registrar) {
       flutterBaiduMapPlugin = FlutterBaiduMapPlugin()
      flutterBaiduMapPlugin.registrar = registrar
      val channel = MethodChannel(registrar.messenger(), "BaiduSdkPlugin")
      var eventChannel = EventChannel(registrar.view(),"_eventChannel_location")
      eventChannel.setStreamHandler(flutterBaiduMapPlugin)
      channel.setMethodCallHandler(flutterBaiduMapPlugin)
//      send(address)
    }

     fun instance(): FlutterBaiduMapPlugin {
      return flutterBaiduMapPlugin

    }


    private fun startLocation(registrar: Registrar) {
      var intent = Intent(registrar.activeContext(), MapActivity::class.java)
      registrar.activeContext().startActivity(intent)
    }



  }

  fun sendLocation(address:String?){
    this.sink?.success(address)
  }
  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method .equals("startLocation") ) {
      startLocation(registrar)
      result.success("success")
    }
    else if (call.method .equals("finish") ) {
      registrar.activity().finish()
      result.success("success")
    }else {
      result.notImplemented()
    }
  }
}
