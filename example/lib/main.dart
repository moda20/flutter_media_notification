import 'dart:io';
import 'dart:math';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:media_notification/media_notification.dart';
import 'package:simple_permissions/simple_permissions.dart';


void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await SimplePermissions.requestPermission(Permission.ReadExternalStorage);
  runApp(new MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String status = 'hidden';

  @override
  void initState() {
    super.initState();

    MediaNotification.setListener('pause', () {
      setState(() => status = 'pause');
    });

    MediaNotification.setListener('play', () {
      setState(() => status = 'play');

    });
    
    MediaNotification.setListener('next', () {
      MediaNotification.setTitle("${DateTime.now().millisecondsSinceEpoch} XXX");
    });

    MediaNotification.setListener('prev', () {
      MediaNotification.setSubtitle("${DateTime.now().millisecondsSinceEpoch} XXX");
    });

    MediaNotification.setListener('select', () {
      MediaNotification.setTitle("${DateTime.now().millisecondsSinceEpoch} XXX");
    });
  }

  Future<void> hide() async {
    try {
      await MediaNotification.hide();
      setState(() => status = 'hidden');
  } on PlatformException {

    }
  }

  Future<void> changeStatusIcon() async {
    String secondIcon = "drawable/baseline_play_arrow_black_48";
    try {
      await MediaNotification.setStatusIcon(secondIcon);
      setState(() => status = 'hidden');
    } on PlatformException {

    }
  }

  Future<void> show(title, author) async {
    try {
      await MediaNotification.show(
          title: title,
          author: author,
          image: "/storage/emulated/0/Pictures/Reddit/c4c7164.jpg",
          bgColor: Colors.deepPurple,
          iconColor: Colors.blue,
          subtitleColor: Colors.deepOrange,
          titleColor: Colors.orange,
          StatusBarIcon: "drawable/baseline_pause_black_48"
      );
      setState(() => status = 'play');
    } on PlatformException {

    }
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: const Text('Plugin example app'),
        ),
        body: new Center(
          child: Container(
            height: 250.0,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: <Widget>[
                FlatButton(
                  child: Text('Show notification'),
                  onPressed: () => show('Title', 'Song author'),
                ),
                FlatButton(
                  child: Text('Update notification'),
                  onPressed: () => show('New title', 'New song author'),
                ),
                FlatButton(
                  child: Text('Hide notification'),
                  onPressed: hide,
                ),
                FlatButton(
                  child: Text('Change Status Icon'),
                  onPressed: changeStatusIcon,
                ),
                Text('Status: ' + status)
              ],
            ),
          )
        ),
      ),
    );
  }
}
