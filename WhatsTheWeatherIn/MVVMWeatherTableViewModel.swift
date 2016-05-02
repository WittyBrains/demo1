

import UIKit
import RxCocoa
import RxSwift
import SwiftyJSON
import CoreLocation
import Alamofire

extension NSDate {
	var dayString:String {
		let formatter = NSDateFormatter()
		formatter.setLocalizedDateFormatFromTemplate("d M")
		return formatter.stringFromDate(self)
	}
}

class MVVMWeatherTableViewModel {
		
	struct Constants {
    
		static let baseURL = "http://api.openweathermap.org/data/2.5/forecast/daily?q="
		static let urlExtension = "\(Weather.Constants.zip)&mode=json&units=metric&cnt=7&APPID=ecc8d1543b57691313cbe29879e3285a"
		static let baseImageURL = "http://openweathermap.org/img/w/"
		static let imageExtension = ".png"
	}
    
	var disposeBag = DisposeBag()

	//MARK: Model
	
	var weather: Weather? {
		didSet {
          
			if weather?.cityName != nil {
				updateModel()
			}
		}
	}
//    func latitude(){
//        Weather.Constants.manager = OneShotLocationManager()
//        
//        Weather.Constants.manager!.fetchWithCompletion {location, error in
//            
//            // fetch location or an error
//            //            if let loc = location {
//            Weather.Constants.locValue = location!.coordinate
//            //self.retrieveWeatherForecast()
//            let geoCoder = CLGeocoder()
//            let location = CLLocation(latitude: Weather.Constants.locValue.latitude, longitude:Weather.Constants.locValue.longitude)
//            geoCoder.reverseGeocodeLocation(location)
//            {
//                (placemarks, error) -> Void in
//                
//                let placeArray = placemarks as [CLPlacemark]!
//                //Weather.Constants.placeMark
//                // Place details
//                Weather.Constants.placenark = placeArray?[0]
//                
//                // City
//                if let city = Weather.Constants.placenark.addressDictionary?["City"] as? NSString
//                {
//                    //self.cityname.text = city as String
//                    print(city)
//                }
//                if let zip = Weather.Constants.placenark.addressDictionary!["ZIP"] as? NSString {
//                    Weather.Constants.zip = zip as String
//                }
//                
//                // Country
//                if let country = Weather.Constants.placenark.addressDictionary!["Country"] as? NSString {
//                    print(country)
//                }
//                
//                // }
//            }
//            
//            // destroy the object immediately to save memory
//            Weather.Constants.manager = nil
//        }
//        
//        
//        
//    }
    

	//MARK: UI
	
	var cityName = PublishSubject<String?>()
	var degrees = PublishSubject<String?>()
	var weatherDescription = PublishSubject<String?>()
	private var forecast:[WeatherForecast]?
	var weatherImage = PublishSubject<UIImage?>()
	var backgroundImage = PublishSubject<UIImage?>()
	var tableViewData = PublishSubject<[(String, [WeatherForecast])]>()
    var errorAlertController = PublishSubject<UIAlertController>()
	
	func updateModel() {
		cityName.on(.Next(weather?.cityName))
		if let temp = weather?.currentWeather?.temp {
			degrees.on(.Next(String(temp)))
		}
		weatherDescription.on(.Next(weather?.currentWeather?.description))
		if let id = weather?.currentWeather?.imageID {
			setWeatherImageForImageID(id)
			setBackgroundImageForImageID(id)
		}
		forecast = weather?.forecast
		if forecast != nil {
			sendTableViewData()
		}
	}
	
	func setWeatherImageForImageID(imageID: String) {
		dispatch_async(dispatch_get_global_queue(QOS_CLASS_USER_INITIATED, 0)) { () -> Void in
			if let url = NSURL(string: Constants.baseImageURL + imageID + Constants.imageExtension) {
				if let data = NSData(contentsOfURL: url) {
					dispatch_async(dispatch_get_main_queue()) { () -> Void in
						self.weatherImage.on(.Next(UIImage(data: data)))
					}
				}
			}
		}
	}
	
	//TODO:
	func setBackgroundImageForImageID(imageID: String) {
	}
	
	
	
	//Parses the forecast data into an array of (date, forecasts for that day) tuple
	func sendTableViewData() {
		if let currentForecast = forecast {
				
			var forecasts = [[WeatherForecast]]()
			var days = [String]()
			days.append(NSDate(timeIntervalSinceNow: 0).dayString)
			var tempForecasts = [WeatherForecast]()
			for forecast in currentForecast {
				if days.contains(forecast.date.dayString) {
					tempForecasts.append(forecast)
				} else {
					days.append(forecast.date.dayString)
					forecasts.append(tempForecasts)
					tempForecasts.removeAll()
					tempForecasts.append(forecast)
				}
			}
			tableViewData.on(.Next(Array(zip(days, forecasts))))
		}
	}
	
	
	
	//MARK: Weather fetching
	
	var searchText:String? {
		didSet {
			if let text = searchText {
				let urlString = Constants.baseURL + text.stringByReplacingOccurrencesOfString(" ", withString: "%20") + Constants.urlExtension
				getWeatherForRequest(urlString)
			}
		}
	}
	
	func getWeatherForRequest(urlString: String) {
        Alamofire.request(Method.GET, urlString).rx_responseJSON()
		.observeOn(MainScheduler.sharedInstance)
		.subscribe(
			onNext: { json in
				let jsonForValidation = JSON(json)
				if let error = jsonForValidation["message"].string {
					print("got error \(error)")
					self.postError("Error", message: error)
					return
				}
				self.weather = Weather(jsonObject: json)

				},
			onError: { error in
				print("Got error")
				let gotError = error as NSError
				
				print(gotError.domain)
				self.postError("\(gotError.code)", message: gotError.domain)
			})
		.addDisposableTo(disposeBag)
	}
	
	func postError(title: String, message: String) {
        errorAlertController.on(.Next(UIAlertController(title: title, message: message, preferredStyle: .Alert)))
	}

}