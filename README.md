# uMessenger - Alpha 0.5 version

Small Messenger-like application designed to run from the command line, and works on the same LAN only (unless using NAT on the server side with a Raspberry for example). **It's important to have userList.txt on the directory where the server is.**

If you wanna try it yourself you can either:

- Download the full zip file, and compile it yourself on your favourite IDE.

- Or download the two jar files and run it from the command line.
_I also uploaded two .cmd files that if placed in the same directory as the .jar files they will open in a new command window (perfect for lazy people like myself)_.

<p align = "center">
  <img src="https://raw.githubusercontent.com/ismaelestalayo/uMessenger/master/Images/Screenshot.png" width = "60%"/>
</p>

Right now my plans on what to implement are the following:

- ~~Usernames~~ with password
- ~~Ability to send files~~
- (More) Commands


Feel free to suggest anything at:

 * Email: ismael.em@outlook.com
 * Twitter: [@isma_estalayo](https://twitter.com/isma_estalayo)

__NOTE: The files are sent in segments of just 100 bytes, because I realized that some networks fragmented the TCP datagrams smaller than my first two aproximations of 2000 and 1000 bytes, so that the photos I was sending were received almost totally corrupted because of the Offset.__

__In the next updates I'll try to find the most efficient size of segments wihtout the need of segmentation on the TCP layer.__
