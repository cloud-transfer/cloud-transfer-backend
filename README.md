This project take url from user,and upload file from that url to Google Drive.

Currently This project is hosted to https://savetodrive-dhavalmehta.rhcloud.com

Steps to upload File to google drive.

Step 1: Take OAuth. Go to https://savetodrive-dhavalmehta.rhcloud.com/OAuth2/OAuthRedirectServlet to allow access via oauth


Step 2: Pass url of file as query parameter to https://savetodrive-dhavalmehta.rhcloud.com/TestingServlet.
        in query paramter key will be "url" and value will be url of file which you want to upload.
        
        
For example I want to upload vlc media player to google drive. Url to vlc media player is: 
http://mirror.de.leaseweb.net/videolan/vlc/2.2.6/win32/vlc-2.2.6-win32.exe. So call to servlet will be https://savetodrive-dhavalmehta.rhcloud.com/TestingServlet?url=http://mirror.de.leaseweb.net/videolan/vlc/2.2.6/win32/vlc-2.2.6-win32.exe
          
You can provide name via filename query parameter. let say i want to save file as "vlc.exe" then call will be 

https://savetodrive-dhavalmehta.rhcloud.com/TestingServlet?url=http://mirror.de.leaseweb.net/videolan/vlc/2.2.6/win32/vlc-2.2.6-win32.exe&filename=vlc.exe
          
          
