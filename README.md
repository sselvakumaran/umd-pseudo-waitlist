# umd-pseudo-waitlist
Webscraper made to act as a waitlist for courses that don't have waitlists at the University of Maryland.
## Why?
For some reason, the University of Maryland has some courses that do not have waitlists. I made a program that will run in the background of your computer to continually scan for when a seat opens using BeautifulSoup webscraping.
It has a terminal window to show output in case of any errors.
## How to use
You will need Java on your computer. BeautifulSoup is included in the Java project folder and the executable JAR.
### Executable (easier)
1. Navigate to the executable folder.
2. Copy all contents to your computer.
3. Go to the Schedule of Classes (https://app.testudo.umd.edu/soc/) and modify your search criteria to what course / courses / sections you desire.
4. Hit Enter to show your current results (should be lots of **Open: 0**s) and copy the link of the site you are on (in full) to *links.txt*.
5. Repeat this for all courses you are looking for, each link should be seperated by a new line.
6. To run the executable, run *umd-pseudo-waitlist.command* for Mac and Linux users or *umd-pseudo-waitlist.cmd* for Windows users. (Note that I do not use Windows and do not know if the Windows .cmd file actually works, please let me know if it does or does not.)
7. You can put this folder (containing the .jar, .txt, .cmd, and .command) in your Startup folder or similar so that it runs as soon as possible. Make sure what is being run is the respective .cmd / .command file, you can delete the other one depending on your system.
### Java files (harder)
If you want to modify the code, the Java code is included in *src*. You can change the delay time, etc. Just make sure to recompile it to a executable .JAR to make your life easier instead of having to run it manually.
If there are any bugs please let me know! I wrote this mainly to serve me but since it is useful to others I thought I should share it and make it more robust. Enjoy!
