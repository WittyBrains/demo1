//
//  WeatherAPI.swift
//  WhatsTheWeatherIn
//
//  Created by Pawan Jhurani on 22/04/16.
//  Copyright © 2016 wittybrains. All rights reserved.
//

import Foundation
import CoreLocation


typealias JSONDictionary = [String: AnyObject]
typealias WeatherForecast = (date: NSDate, imageID: String?, temp: Double?, description: String?)

class Weather{
   
    
	struct Constants {
         static var manager: OneShotLocationManager?
        static var  locationcity:NSString = NSString()
        static var placenark:CLPlacemark!
        static var text:String!
        static  var locValue:CLLocationCoordinate2D!
        static var locationManager: CLLocationManager!
    
	}

}
