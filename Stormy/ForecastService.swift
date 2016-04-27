//
//  ForecastService.swift
//  Stormy
//
//  Created by Pawan Jhurani on 22/04/16.
//  Copyright © 2016 wittybrains. All rights reserved.
//
import Foundation

struct ForecastService {
    
    let forecastAPIKey: String
    let forecastBaseURL: NSURL?
    
    init(APIKey: String) {
        forecastAPIKey = APIKey
        forecastBaseURL = NSURL(string: "https://api.forecast.io/forecast/\(forecastAPIKey)/")
    }
    
    func getForecast(lat: Double, lon: Double, completion: (Forecast? -> Void)) {
        
        if let forecastURL = NSURL(string: "\(lat),\(lon)", relativeToURL: forecastBaseURL) {
            let networkOperation = NetworkOperation(url: forecastURL)
            
            networkOperation.downloadJSONFromURL {
                (let JSONDictionary) in
                let forecast = Forecast(weatherDictionary: JSONDictionary)
                completion(forecast)
                print(forecast)
            }
        } else {
            print("Could not construct a valid URL")
        }
    }
    
    
    
}