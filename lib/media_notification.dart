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
          int> BitmapImage, Color bgColor, Color titleColor, Color subtitleColor, Color iconColor, Icon previousIcon,
        String StatusBarIcon}) async {
    //switching the image from a URI to a byteArray for Android with offset and length;
    List<int> imagebytes;
    if (image != null) {
      File imgFile = File(image);
      if (imgFile != null) {
        imagebytes = imgFile.readAsBytesSync();
      }
    }



    final Map<String, dynamic> params = <String, dynamic>{
      'title': title,
      'author': author,
      'play': play,
      'image': imagebytes != null ? imagebytes : BitmapImage,
      'length': imagebytes != null ? imagebytes.length : BitmapImage != null
          ? BitmapImage.length
          : 0,
      'offset': 0,
      'bgColor': bgColor != null
          ? '#${bgColor.value.toRadixString(16)}'
          : '#${Colors.white.value.toRadixString(16)}',
      'titleColor': titleColor != null ? '#${titleColor.value.toRadixString(
          16)}' : '#${Colors.black.value.toRadixString(16)}',
      'subtitleColor': subtitleColor != null ? '#${subtitleColor.value
          .toRadixString(16)}' : '#838383',
      // this color is arbitrary and can be changed but it keeps a good look
      'iconColor': iconColor != null
          ? '#${iconColor.value.toRadixString(16)}'
          : '#${Colors.black.value.toRadixString(16)}',
      'iconId': StatusBarIcon != null ? StatusBarIcon : ""
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
    print(_listeners);
  }

  static Future setTitle(String title) async{
    final Map<String, dynamic> params = <String, dynamic>{
      'title': title,
    };
    await _channel.invokeMethod('setTitle', params);
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
