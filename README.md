# Travel_Journal
Author: Yihui Wang
## Description
In the current rapid pace of life, long-form travel logs have been gradually eliminated and replaced by more streamlined records. In this travel log app, users only need a picture, a sentence and a location to quickly record the journey. In addition, users can search and collect popular scenic spots across the country in this app, and plan travel routes.
## Function
This travel log app has five modules, which are log viewing, log publishing, national tourist attraction search, tourist attraction collection, and photo album. <br/>
### The log viewing module:
1. Log view based on map: View the log on the map according to the location information of the log;
2. Log view based on timeline: view the log according to the time sequence of the log;
### The log publishing module:
1. Upload picture: You can use the camera to take a photo or select a photo from the local to upload to the log;
2. Picture editing: Edit the uploaded pictures, such as cropping, rotating, and graffiti;
3. Add text: you can add a paragraph of description text in the log;
4. Select location: You can obtain location information based on the current location or manually add location information;
5. Save log: save the current log and display it in the log viewing module;
### The national tourist attractions search module:
1. Query scenic spots: you can query the location, introduction, opening hours and other information of national tourist attractions;
2. Route navigation: call Baidu map to navigate from the current location to the target scenic spot;
3. Attractions collection: You can add scenic spots to the collection;<br/>
### The tourist attraction collection module:
1. View favorites: You can view the location, introduction, opening hours and other information of the favorite scenic spots;
2. Delete favorites: you can remove the favorite scenic spots from the favorites;
### The photo album module:
1. View log pictures: View log pictures in album mode;
2. Data statistics: display the total number of photos, the total number of location information, and the total number of favorites;
## Project highlights
### Call Baidu map api
Baidu map api is called on multiple interfaces, and this api is used to realize real-time positioning, map display, coordinate coding and other functions;
### Call LY.com open api
Call the open api of Tongcheng.com to realize the scenic spot search function through network request;
### Parse Json data through Gson
Parse the json data returned by LY.com's open api through the Gson method;
### Multi-threaded operation
Use multi-threaded operations to implement database operations, network requests and other operations;
### Use the Camera class to write a custom camera
Take a picture by using the Camera class to write a custom camera instead of opening the system camera;
### Call a third-party open source library to achieve image editing
Realize simple image editing functions by calling third-party open source libraries
### Call the system album to select local pictures
Select a picture from the local album by calling the system album
### Bitmap
Realize image editing, storage and other functions through Bitmap
### picasso, universalimageloader, glide
Choose to use picasso, universalimageloader, glide to load pictures according to different purposes
## User Interface
### Photo Map
![](https://github.com/jameswyh/Travel_Journal_App/blob/master/Travel_UI_pic/Picture1.png)
### Phtot Timeline
![](https://github.com/jameswyh/Travel_Journal_App/blob/master/Travel_UI_pic/Picture2.png)
### Attractions Search
![](https://github.com/jameswyh/Travel_Journal_App/blob/master/Travel_UI_pic/Picture3.png)
### Search Result
![](https://github.com/jameswyh/Travel_Journal_App/blob/master/Travel_UI_pic/Picture4.png)
### Result Detail
![](https://github.com/jameswyh/Travel_Journal_App/blob/master/Travel_UI_pic/Picture5.png)
![](https://github.com/jameswyh/Travel_Journal_App/blob/master/Travel_UI_pic/Picture6.png)
### Liked Attractions
![](https://github.com/jameswyh/Travel_Journal_App/blob/master/Travel_UI_pic/Picture7.png)
### Photo Album
![](https://github.com/jameswyh/Travel_Journal_App/blob/master/Travel_UI_pic/Picture8.png)
### Send a log
![](https://github.com/jameswyh/Travel_Journal_App/blob/master/Travel_UI_pic/post.png)



