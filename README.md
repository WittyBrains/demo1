# Weather-Forecast-Application1

This App is used for weather forecating.

1) I have created a MainActivity that contains two tabs on ActionBar 
    - HomeInfoTab : It has diaplyed current weather including cityname,image,temperature,change city button.
    - WeekInfoTab : It has displayed weather of week including current day. It has contained listView.
    
2) I have a adapter folder that contains CustomListViewAdapter class. This class is used to create view for listview.

3) I have a bean folder that contains UserInfo,Weather Class for maintain data.

4) I have a utilities folder that contains DownloadImageTask,WeatherWebService class. Both have used to take data from server. 

    - My WeatherWebService is a AsyncTask that request to the url and fetch the JSON data as a response.
      parseTodayWeather(), parseWeekWeather() these methods basically used to parsing data and store infomation into UserInfo,Weather class.
    -  DownloadImageTask is also a AsyncTask that request to the url and return bitmap image as a response.
