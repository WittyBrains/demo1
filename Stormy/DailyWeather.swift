//
//  DailyWeather.swift
//  Stormy
//
//  Created by Pawan Jhurani on 22/04/16.
//  Copyright © 2016 wittybrains. All rights reserved.
//

import UIKit
import Foundation

struct DailyWeather {

    let maxTemperature:Int?
    let minTemperature:Int?
    let humidity:Int?
    let precipChange:Int?
    let summary:String?
    var icon:UIImage? = UIImage(named: "default.png")
    var largeIcon:UIImage? = UIImage(named: "default_large.png")
    var sunriseTime:String?
    var sunsetTime:String?
    var day:String?
    let dateFormatter = NSDateFormatter()
    
    init(dailyWeatherDict:[String:AnyObject]){
        maxTemperature = dailyWeatherDict["temperatureMax"] as? Int
        minTemperature = dailyWeatherDict["temperatureMin"] as? Int
        
        if let humidityFloat = dailyWeatherDict["humidity"] as? Double {
            humidity = Int(humidityFloat * 100)
        } else {
            humidity = nil
        }
        
        if let precipChanceFloat = dailyWeatherDict["precipProbability"] as? Double {
            precipChange = Int(precipChanceFloat * 100)
        } else {
            precipChange = nil
        }
        
        summary = dailyWeatherDict["summary"] as? String
        
        if let iconString = dailyWeatherDict["icon"] as? String,
            let iconEnum = Icon(rawValue: iconString) {
                icon = iconEnum.toImage().regularIcon
                largeIcon = iconEnum.toImage().largeIcon
                //(icon, largeIcon) = iconEnum.toImage()
        }
        
        if let sunriseDate = dailyWeatherDict["sunriseTime"] as? Double {
            sunriseTime = timeStringFromUnixTime(sunriseDate)
        }
        
        if let sunsetDate = dailyWeatherDict["sunsetTime"] as? Double {
            sunsetTime = timeStringFromUnixTime(sunsetDate)
        }
        
        if let time = dailyWeatherDict["time"] as? Double {
            day = dayStringFromTime(time)
        }
    }
    
    func timeStringFromUnixTime(unixTime:Double)->String{
        let date = NSDate(timeIntervalSince1970: unixTime)
        
        dateFormatter.dateFormat = "hh:mm a"
        return dateFormatter.stringFromDate(date)
    }
    
    func dayStringFromTime(time:Double)->String{
         let date = NSDate(timeIntervalSince1970: time)
        dateFormatter.locale = NSLocale(localeIdentifier: NSLocale.currentLocale().localeIdentifier)
        dateFormatter.dateFormat = "EEEE"
        
        return dateFormatter.stringFromDate(date)
    }
}
