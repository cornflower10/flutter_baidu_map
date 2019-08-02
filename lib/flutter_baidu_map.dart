import 'dart:async';

import 'package:flutter/services.dart';

class FlutterBaiduMap {
  final ReceiveCallBack _receiveCallBack;
  static const MethodChannel _channel =
  const MethodChannel('BaiduSdkPlugin');
  EventChannel _eventChannel =
  const EventChannel('_eventChannel_location');

  FlutterBaiduMap(this._receiveCallBack);

//  static Future<String> get platformVersion async {
//    final String version = await _channel.invokeMethod('getPlatformVersion');
//    return version;
//  }
  static Future<void>  startLocation() async {
   String result = await _channel.invokeMethod('startLocation');
    print("startLocation:$result");
  }


  //  }
  static Future<void>  finishLocation() async {
    String finish = await _channel.invokeMethod('finish');
    print("finish:$finish");
  }

  initReceiveLocation(){
    _eventChannel.receiveBroadcastStream().listen(_onEnvent,onError: _onError);
  }

  void _onEnvent(Object o) {
    print("_onEnvent:$o");
    _receiveCallBack.onEnvent(o);
  }

  void _onError(Object o) {
    _receiveCallBack.onError(o);
  }
}

abstract class ReceiveCallBack{
  void onEnvent(Object o);
  void onError(Object o);
}
