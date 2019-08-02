import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_baidu_map/flutter_baidu_map.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp>  implements ReceiveCallBack{
  String _platformVersion = 'Unknown';
  String _address = '';

  @override
  void initState() {
    super.initState();

  }

  // Platform messages are asynchronous, so we initialize in an async method.
//  Future<void> initPlatformState() async {
//    String platformVersion;
//    // Platform messages may fail, so we use a try/catch PlatformException.
//    try {
//      platformVersion = await FlutterBaiduMap.platformVersion;
//    } on PlatformException {
//      platformVersion = 'Failed to get platform version.';
//    }
//
//    // If the widget was removed from the tree while the asynchronous platform
//    // message was in flight, we want to discard the reply rather than calling
//    // setState to update our non-existent appearance.
//    if (!mounted) return;
//
//    setState(() {
//      _platformVersion = platformVersion;
//    });
//  }

  @override
  Widget build(BuildContext context) {

    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: InkWell(child: Text('Running on: $_address\n'),onTap: _initPlatformState,),
        ),
      ),
    );
  }
  FlutterBaiduMap flutterPlugin = null;
  void _initPlatformState() {
     flutterPlugin = new FlutterBaiduMap(this);
    FlutterBaiduMap.startLocation();
    flutterPlugin.initReceiveLocation();
  }

  @override
  onEnvent(Object o) {
    setState(() {
      _address = o.toString();
    });
    print(o.toString());
//    FlutterBaiduMap.finishLocation();
  }

  @override
  onError(Object o) {
  }
}
