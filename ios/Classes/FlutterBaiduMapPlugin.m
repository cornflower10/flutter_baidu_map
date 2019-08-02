#import "FlutterBaiduMapPlugin.h"
#import <flutter_baidu_map/flutter_baidu_map-Swift.h>

@implementation FlutterBaiduMapPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterBaiduMapPlugin registerWithRegistrar:registrar];
}
@end
