# AuctionApp
An auction app using sockets for my distributed programming university module

To Ensure this solution builds you will need to take a couple steps. 

0. Go into the modify options dropdown and press add new configuration in the client solution. 

1. Ensure that the java SDK is set to Java 16 and the Module is set to ExamsSoftwareUni. See screenshot for all the correct configuration as related to my machine

![alt text](https://github.com/RussianHamster6/AuctionApp/blob/main/image.png)

2. to Ensure that it builds properly make sure your Run/Debug configurations have been configured with the following VMSetting Equivalents:
--module-path
C:\Users\declan.rhodes\Desktop\javafx-sdk-11.0.2\lib 
--add-modules
javafx.controls,javafx.fxml

where your module-path is the location of the javafx sdk lib file on your machine. 

3. Ensure that the appropriate packages are still present in the File > project structure > project settings > libraries in the server solution
There should be the following added as direct jars: ![image](https://user-images.githubusercontent.com/33499319/156947193-8e277adc-a1d6-4d11-ba5b-dca8f4388cdf.png)
and then junit:junit:4.13 that can be found in Maven.

If you need to find these JAR files they can be found and downloaded at https://jar-download.com/artifacts/org.mockito I used Mockito version 4.3.1

The default users are User with a password of pass and secondUser with a password of pass2

