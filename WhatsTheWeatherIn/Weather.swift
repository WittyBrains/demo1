

import Foundation
import SwiftyJSON
import CoreLocation

typealias JSONDictionary = [String: AnyObject]
typealias WeatherForecast = (date: NSDate, imageID: String?, temp: Double?, description: String?)

class Weather {
	struct Constants {
		static let baseURL = "http://api.openweathermap.org/data/2.5/forecast?q="
		static let urlParams = "201301&mode=json&units=metric&cnt=7&APPID=ecc8d1543b57691313cbe29879e3285a"
		static let baseImageURL = "http://openweathermap.org/img/w/"
		static let imageExtension = ".png"
        static var manager: OneShotLocationManager?
        static var  locationcity:NSString = NSString()
        static var placenark:CLPlacemark!
        static var zip:NSString!
        static  var locValue:CLLocationCoordinate2D!
        static var locationManager: CLLocationManager!
	}
	
	var cityName:String?
	var forecast = [WeatherForecast]()
	
	var currentWeather:WeatherForecast? {
		if !forecast.isEmpty {
			return forecast[0]
		} else {
			return nil
		}
	}
	
	//TODO: Cash last request
	
	init(jsonObject: AnyObject) {
		let json = JSON(jsonObject)
       // print(json)
       
      
		self.cityName = json["city"]["name"].stringValue
		if let forecastArray = json["list"].array {
			for item in forecastArray {
				let itemForecast = (date: NSDate(timeIntervalSince1970: NSTimeInterval(item["dt"].intValue)),
					imageID: item["weather"][0]["icon"].string,
					temp: item["temp"]["night"].double,
					description: item["weather"][0]["description"].string)
				
				forecast.append(itemForecast)
			}
		}
	}
}
