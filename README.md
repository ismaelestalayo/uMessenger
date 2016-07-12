# uMessenger - Beta 1.0

Small Messenger-like application designed to run from the command line, and works on the same LAN only (unless using NAT on the server side with a Raspberry for example). **It's important to have userList.txt on the directory where the server is.**

If you wanna try it yourself you can either:

- Download the zip of the full repository and compile it yourself on your favourite IDE.

- Or download the zip file for being either a server or a client and run the corresponding .jar from the command line.

 _Inside the rar there are .cmd files that executes `java -jar UMessengerClient.jar´ in a new command window (perfect for lazy people like myself)_.

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

_NOTE:_
_I made the sending of files so that I could change the size of the segments I was sending, but the only size where the videos weren't corrupted was at the size of 1 byte. Even so, I've left the code there, so if you wanna try it yourself, on the FileSending class just change  con.sendArray(fileDumpedInArray, 1)  to the size of segment you want, bear in mind that most files will be corrupted although I noticed photos not being corrupted but severely altered with a segment size greater than 500._
