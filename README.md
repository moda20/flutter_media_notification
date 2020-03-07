# media_notification

Media notification for flutter, on android (IOs Not implemented)


#### This plugin is used in the music player app [TuneIn](https://github.com/moda20/flutter-tunein).
#### please star this repository or [TuneIn](https://github.com/moda20/flutter-tunein), i am open to comments and possible features.

## Usage


### Main functions
You can call the show method to show the media controls like follow :


#### Show Function
```dart
    ByteData dibd = await rootBundle.load("images/image.png");
    List<int> defaultImageBytes = dibd.buffer.asUint8List();

try {
      await MediaNotification.show(
        title: title, // the title of the track
        author: author, // a subtitle usually the artist
        image:"/storage/emulated/0/Pictures/Reddit/c4c7164.jpg", // an image, Must be a URI
        BitmapImage: defaultImageBytes, // an image in bytes form, can get over any permission problems 
        bgColor: Colors.deepPurple, // the background Color of the notification panel
        iconColor: Colors.blue, // The control icons colors
        subtitleColor: Colors.deepOrange, // The subtitle color
        titleColor: Colors.orange // the title color
      );
    } on PlatformException {

    }
```
#### Hide Function
You can call the hide method to hide the Media controls like follow :

```dart
MediaNotification.hide();
```
### Extra functions and listeners

You can use the next functions and listeners n order to further polish the experience of using the plugin
 
#### Extra Functions 

`MediaNotification.setTo(false);` must take a boolean argument, will set the play/pause button icon accordingly :
    1- True means currently playing, will set the icon to *Pause* icon
    2- False means currently on pause or stop ( no distinction between the two ), will set the icon to *Play* icon

`MediaNotification.togglePlayPause();` takes no arguments, will automatically toggle the play/pause button icon

`MediaNotification.setSubtitle(SubtitleString);` must take a String argument will change the subtitle on the notification to the given string

`MediaNotification.setTitle(TitleString);` must take a String argument will change the title on the notification to the given string

#### Extra Listeners

The following listeners can be used to listen on the different control buttons when tapped

```dart
MediaNotification.setListener('play', () {
      print("playing shoud slart playin"); // will be triggered when the Play arrow button is tapped
    });

    MediaNotification.setListener('pause', () {
      print("playback should pause"); // will be triggered when the Pause Bars button is tapped
    });

    MediaNotification.setListener('next', () {
      print("playback should go to next track"); // will be triggered when the next button is tapped
    });

    MediaNotification.setListener('prev', () {
       print("playback should go to previous track"); // will be triggered when the previous button is tapped
    });

    MediaNotification.setListener('select', () {
      print("selected Notification"); // will be triggered when the notification is selected (tapped) from the notification area
    });

```  
 

## Screenshots

|       Notification Area                                    |        Lock Screen                                   |        Lock Screen                                   |
| ----------------------------------------- | ----------------------------------------- | ----------------------------------------- |
| <img src="screenshots/scrs.png" width="250"> | <img src="screenshots/scrs2.png" width="250"> | <img src="screenshots/scrs3.png" width="250"> |

## Getting Started With the development

For help getting started with Flutter, view our online
[documentation](https://flutter.io/).

For help on editing plugin code, view the [documentation](https://flutter.io/platform-plugins/#edit-code).


## Support me

You can support me by:

⭐️ this repo if you like it.

Buy me a cup of coffee ☕️:

*NOT there yet*


## Contact me

*email:* kadhem03@gmail.com

Thank you in advance 👍