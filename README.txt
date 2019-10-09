STEPS TO RUN THE APP:

1. The app has been created using android studio, so the project can be directly imported in android studio
2. Once import click on RUN -> Run 'app'. The app would start on emulator or a usb device if connected.
3. The videos are located in videos/
4. The apk is located in apk/

UPLOAD FUNCTIONALITY:

1. We have hosted a PHP + Apache server on a virtual machine in Google Cloud Platform. Since its on GCP the server url is accessible from any network.
2. The app already includes the our server URL so building the app and clicking on the upload will directly upload it to our server and no changes are required.
3. Our server url is http://34.66.8.146/video/ . This can be opened in the browser to view the uploaded videos. The uploaded videos are located in files/G10/<ASUID>/accept/<video_name>.mp4 and the same can be viewed in the browser.