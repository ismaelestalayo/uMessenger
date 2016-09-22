# uMessenger - Beta 1.3

:warning:

On Windows 10 Anniversary Update, Microsoft updated the command prompt, adding a lot of functionality, but taking out the ability to print colors. However they have implemented it back and it's right now on the Windows insider program only. I will update this program to make it compatible once they launch it for public. [Source](https://wpdev.uservoice.com/forums/266908-command-prompt-console-bash-on-ubuntu-on-windo/suggestions/6509361-provides-support-for-ansi-colors-like-in-bash) 

>"We’re excited to announce that we delivered 24-bit RGP true-color support in Windows Console in Windows 10 Insiders build #14931."

:warning:

Small Messenger-like application designed to run from the command line, and works on the same LAN only (unless using NAT on the server side with a Raspberry for example). **It's important to have userList.txt on the directory where the server is.**

If you wanna try it yourself you can either:

- Download the zip of the full repository and compile it yourself on your favourite IDE.

- Or download the zip file for being either a server or a client and run the corresponding .jar from the command line. Click on one of the rar files, and in the upper right corner, click on raw to download it.

 _Inside the rar there are .cmd files that executes `java -jar uMessenger_Client.jar` in a new command window (perfect for lazy people like myself)_.

<p align = "center">
  <img src="https://raw.githubusercontent.com/ismaelestalayo/uMessenger/master/Images/Client.png" width = "60%"/>
</p>

#Changelog:
####1.3:
- One of the lates beta version, as the app is quite stable
- Added a color for each username up to 6 users (then colors will be repeated, because of limits of the cmd).
- Fixed a problem with file sending.
- Fixed the color array.

####1.2:
- Notification to all users when a new user joins.
- Sending files between different networks should be working now.

####1.1:
- Changed most of the code from using `dos.sendUTF()` to `oos.sendObject()` to make easier understanding the code and adding more functionality in the future.
- Notification when a client dissconects closing the windows instead of `/fin`.
- Character º won't crash the app anymore.
- Sending a blank message won't crash anymore either.

####1.0:
- First beta version (previously Alpha).
- Sending files finally work! (On the same LAN atm).
- A lot of bugfixes.
- Added `/IPs` and `/users` commands.

#Future plans:
- [X] Usernames
- [ ] Passwords for usernames.
- [X] Ability to send files
- [X] Ability to send files between different LANs
- [ ] Save logs from the server
- [ ] More commands

#Suggest anything at:

 * Email: ismael.em@outlook.com
 * Twitter: [@isma_estalayo](https://twitter.com/isma_estalayo)

_NOTE:_
_I made the sending of files so that I could change the size of the segments I was sending, but the only size where the videos weren't corrupted was at the size of 1 byte. Even so, I've left the code there, so if you wanna try it yourself, on the FileSending class just change `con.sendArray(fileDumpedInArray, 1)`  to the size of segment you want, bear in mind that most files will be corrupted although I noticed photos not being corrupted but severely altered with a segment size greater than 500._
