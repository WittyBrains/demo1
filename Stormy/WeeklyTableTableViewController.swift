//
//  WeeklyTableTableViewController.swift
//  Stormy
//
//  Created by Pawan Jhurani on 22/04/16.
//  Copyright © 2016 wittybrains. All rights reserved.
//

import UIKit
import CoreLocation
class WeeklyTableTableViewController: UITableViewController, CLLocationManagerDelegate {

    @IBOutlet weak var currentTemperatureLabel: UILabel!
    @IBOutlet weak var currentWeatherIcon: UIImageView!
    @IBOutlet weak var currentPrecipitationLabel: UILabel!
    @IBOutlet weak var currentTemperatureRangeLabel: UILabel!
    
    @IBOutlet var cityname: UILabel!
    let dateFormatter = NSDateFormatter()
    var current1 = [String]()
 
    
    // API key
    private let forecastAPIKey = "5eba174091e0725e8b0ffa3e758de11d"
    
    var weeklyWeather:[DailyWeather] = []
    //let coordinate: AnyObject!
    override func viewDidLoad() {
        super.viewDidLoad()
              latitude()
        getdate()
        configureView()
       
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func getdate(){
        let date = NSDate()
        let calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)
        let dateComponent = NSDateComponents()
        //var dateStr          = "2015-03-09"
        let formatter  = NSDateFormatter()
        formatter.dateFormat = "YYYY-MM-dd"
        dateFormatter.dateStyle = NSDateFormatterStyle.ShortStyle
        
        // var currDate  = formatter.stringFromDate(date)
        for i in 0...7
        {
            dateComponent.day = i
            let newDate = calendar?.dateByAddingComponents(dateComponent, toDate: date, options:NSCalendarOptions())
            print( newDate)
            //            var convertedDate = [""]
            let convertedDate = dateFormatter.stringFromDate(newDate!)
            current1.append(convertedDate)
            
            
        }

        
    }
    func latitude(){
        Weather.Constants.manager = OneShotLocationManager()
        Weather.Constants.manager!.fetchWithCompletion {location, error in
            
            // fetch location or an error
            if let loc = location {
                Weather.Constants.locValue = location!.coordinate
                self.retrieveWeatherForecast()
                let geoCoder = CLGeocoder()
                let location = CLLocation(latitude: Weather.Constants.locValue.latitude, longitude:Weather.Constants.locValue.longitude)
                geoCoder.reverseGeocodeLocation(location)
                    {
                        (placemarks, error) -> Void in
                        
                        let placeArray = placemarks as [CLPlacemark]!
                        //Weather.Constants.placeMark
                        // Place details
                        Weather.Constants.placenark = placeArray?[0]
            
                        // City
                        if let city = Weather.Constants.placenark.addressDictionary?["City"] as? NSString
                        {
                            self.cityname.text = city as String
                            print(city)
                        }
                                      }
            } else if let err = error {
                
                print(err.localizedDescription)
            }
            
            // destroy the object immediately to save memory
            Weather.Constants.manager = nil
        }
        
        
        
    }

    func configureView(){
        //set background tableview
        tableView.backgroundView = BackgroundView()
        
        tableView.rowHeight = 64
        
        //change the font size - nav bar
        if let navBarFont = UIFont(name: "HelveticaNeue-Thin", size: 20.0) {
            let navBarAttributesDictionary: [String: AnyObject]? = [
                NSForegroundColorAttributeName: UIColor.whiteColor(),
                NSFontAttributeName: navBarFont
            ]
            navigationController?.navigationBar.titleTextAttributes = navBarAttributesDictionary
        }
        
        refreshControl?.layer.zPosition = (tableView.backgroundView?.layer.zPosition)! + 1
        refreshControl?.tintColor = UIColor.whiteColor()
    }
    
    @IBAction func refreshWeather() {
        retrieveWeatherForecast()
        refreshControl?.endRefreshing()
    }
    
    // MARK: NAVIGATION
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "showDaily"{
            if let indexPath = tableView.indexPathForSelectedRow{
                let dailyWeather = weeklyWeather[indexPath.row]
                (segue.destinationViewController as! ViewController).dailyWeather = dailyWeather
            }
        }
    }

    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // number of section
        return 1
    }
    
    override func tableView(tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return "Forecast"
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // return number of rows in the section
        return weeklyWeather.count
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("WeatherCell") as! DailyWeatherTableViewCell
        
        let dailyWeather = weeklyWeather[indexPath.row]
        let maxtTemp = dailyWeather.maxTemperature
        if maxtTemp != nil{
            cell.temperatureLabel.text = "\(maxtTemp!)º"
        }
        
        cell.icon.image = dailyWeather.icon
        cell.dayLabel.text = dailyWeather.day
       //print(current1[indexPath.row])
        cell.date.text = current1[indexPath.row]
        return cell
    }
    
    // MARK: delegate method
    override func tableView(tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        view.tintColor = UIColor(red: 170/255.0, green: 131/255.0, blue: 224/255.0, alpha: 1.0)
        if let header = view as? UITableViewHeaderFooterView{
            header.textLabel?.font = UIFont(name: "HelveticaNeue-Thin", size: 14.0)
            header.textLabel?.textColor = UIColor.whiteColor()
        }
    }
    
    override func tableView(tableView: UITableView, didHighlightRowAtIndexPath indexPath: NSIndexPath) {
        let cell = tableView.cellForRowAtIndexPath(indexPath)
        cell?.contentView.backgroundColor = UIColor(red: 165/255.0, green: 142/255.0, blue: 203/255.0, alpha: 1.0)
        let highlightView = UIView()
        highlightView.backgroundColor = UIColor(red: 165/255.0, green: 142/255.0, blue: 203/255.0, alpha: 1.0)
        cell?.selectedBackgroundView = highlightView
    }
    
    // MARK: fetching data
    func retrieveWeatherForecast() {
        let forecastService = ForecastService(APIKey: forecastAPIKey)
        forecastService.getForecast(Weather.Constants.locValue.latitude,lon: Weather.Constants.locValue.longitude) {
            (let forecast) in
            
            if let weatherForecast = forecast,
                let currentWeather = weatherForecast.currentWeather{
                
                dispatch_async(dispatch_get_main_queue()) {
                    
                    if let temperature = currentWeather.temperature {
                        self.currentTemperatureLabel?.text = "\(temperature)º"
                    }
                    
                    if let precipitation = currentWeather.precipProbability {
                        self.currentPrecipitationLabel?.text = "Rain: \(precipitation)%"
                    }
                    
                    if let icon = currentWeather.icon {
                        self.currentWeatherIcon?.image = icon
                    }
                    
                    self.weeklyWeather = weatherForecast.weekly
                    
                    if let highTemp = self.weeklyWeather.first?.maxTemperature,
                        let lowTemp = self.weeklyWeather.first?.minTemperature{
                            self.currentTemperatureRangeLabel?.text = "↑\(highTemp)º ↓\(lowTemp)º"
                    }
                    
                    self.tableView.reloadData()
                    
                }
                
            }
        }
    }

    

}
