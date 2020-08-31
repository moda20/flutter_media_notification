import 'dart:async';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:meta/meta.dart';

class MediaNotification {
  static const MethodChannel _channel = const MethodChannel('com.moda.twenty/media_notification');
  static const MethodChannel _channel_back = const MethodChannel('com.moda.twenty/media_notification_back');
  static Map<String, Function> _listeners = new Map();
  
  static Future<dynamic> _myUtilsHandler(MethodCall methodCall) async {
    // Вызываем слушателя события
    _listeners.forEach((event, callback) {
      if (methodCall.method == event) {
        callback();
        return true;
      }
    });
  }

  /**
   *  params @BitmapImage a list of bytes representing an image, has less priority when @image is present
   */
  static Future show(
      {@required title, @required author, play = true, String image = "", List<
          int> BitmapImage, Color bgColor, Color titleColor, Color subtitleColor, Color iconColor, Color bigLayoutIconColor ,Icon previousIcon,
        String StatusBarIcon, String bgImage, List<
          int> bgBitmapImage, Color bgImageBackgroundColor, String timeStamp}) async {
    //switching the image from a URI to a byteArray for Android with offset and length;
    List<int> imagebytes;
    List<int> bgImagebytes;
    if (image != null) {
      File imgFile = File(image);
      if (imgFile != null) {
        imagebytes = imgFile.readAsBytesSync();
      }
    }
    if(bgImage!=null){
      File bgImgFile = File(bgImage);
      if (bgImgFile != null) {
        bgImagebytes = bgImgFile.readAsBytesSync();
      }
    }
    if(bigLayoutIconColor==null){
      bigLayoutIconColor= iconColor;
    }



    final Map<String, dynamic> params = <String, dynamic>{
      'title': title,
      'author': author,
      'play': play,
      'image': imagebytes != null ? imagebytes : BitmapImage,
      'bgImage': bgImagebytes != null ? bgImagebytes : bgBitmapImage,
      'length': imagebytes != null ? imagebytes.length : BitmapImage != null
          ? BitmapImage.length
          : 0,
      'offset': 0,
      'bgLength': bgImagebytes != null ? bgImagebytes.length : bgBitmapImage != null
          ? bgBitmapImage.length
          : 0,
      'bgOffset': 0,
      'bgColor': bgColor != null
          ? '#${bgColor.value.toRadixString(16)}'
          : '#${Colors.white.value.toRadixString(16)}',
      'bgImageBackgroundColor': bgImageBackgroundColor != null
          ? '#${bgImageBackgroundColor.value.toRadixString(16)}'
          : '#${Colors.white.value.toRadixString(16)}',
      'titleColor': titleColor != null ? '#${titleColor.value.toRadixString(
          16)}' : '#${Colors.black.value.toRadixString(16)}',
      'subtitleColor': subtitleColor != null ? '#${subtitleColor.value
          .toRadixString(16)}' : '#838383',
      // this color is arbitrary and can be changed but it keeps a good look
      'iconColor': iconColor != null
          ? '#${iconColor.value.toRadixString(16)}'
          : '#${Colors.black.value.toRadixString(16)}',
      'bigLayoutIconColor': bigLayoutIconColor != null
          ? '#${bigLayoutIconColor.value.toRadixString(16)}'
          : '#${Colors.black.value.toRadixString(16)}',
      'iconId': StatusBarIcon != null ? StatusBarIcon : "",
      'timestamp': timeStamp != null ? timeStamp : "--/--"
    };
    await _channel.invokeMethod('show', params);

    _channel_back.setMethodCallHandler(_myUtilsHandler);
  }

  static Future hide() async {
    await _channel.invokeMethod('hide');
  }

  static Future togglePlayPause() async {
    await _channel.invokeMethod('togglePlayPause');
  }

  static setListener(String event, Function callback) {
    _listeners.addAll({event: callback});
  }

  static Future setTitle(String title) async{
    final Map<String, dynamic> params = <String, dynamic>{
      'title': title,
    };
    await _channel.invokeMethod('setTitle', params);
  }

  static Future setTimestamp(String timeStamp) async{
    final Map<String, dynamic> params = <String, dynamic>{
      'timestamp': timeStamp,
    };
    await _channel.invokeMethod('setTimestamp', params);
  }

  static Future setStatusIcon(String iconName) async {
    final Map<String, dynamic> params = <String, dynamic>{
      'icon': iconName,
    };
    await _channel.invokeMethod('setIcon', params);
  }

  static Future setSubtitle(String subtitle) async{
    final Map<String, dynamic> params = <String, dynamic>{
      'subtitle': subtitle,
    };
    await _channel.invokeMethod('setSubtitle', params);
  }

  static Future setTo(bool play) async{
    final Map<String, dynamic> params = <String, dynamic>{
      'play': play,
    };
    await _channel.invokeMethod('setToPlayPause', params);
  }
}
