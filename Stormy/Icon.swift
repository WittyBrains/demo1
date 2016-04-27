//
//  Icon.swift
//  Stormy
//
//  Created by Pawan Jhurani on 22/04/16.
//  Copyright © 2016 wittybrains. All rights reserved.
//

import Foundation
import UIKit

enum Icon: String {
    case ClearDay = "clear-day"
    case ClearNight = "clear-night"
    case Rain = "rain"
    case Snow = "snow"
    case Sleet = "sleet"
    case Wind = "wind"
    case Fog = "fog"
    case Cloudy = "cloudy"
    case PartlyCloudyDay = "partly-cloudy-day"
    case PartlyCloudyNight = "partly-cloudy-night"
    
    func toImage() -> (regularIcon:UIImage!, largeIcon:UIImage!) {
        var imageName: String
        
        switch self {
        case .ClearDay:
            imageName = "clear-day"
        case .ClearNight:
            imageName = "clear-night"
        case .Rain:
            imageName = "rain"
        case .Snow:
            imageName = "snow"
        case .Sleet:
            imageName = "sleet"
        case .Wind:
            imageName = "wind"
        case .Fog:
            imageName = "fog"
        case .Cloudy:
            imageName = "cloudy"
        case .PartlyCloudyDay:
            imageName = "cloudy-day"
        case .PartlyCloudyNight:
            imageName = "cloudy-night"
        }
        let regurlarIcon = UIImage(named: "\(imageName).png")
        let largeIcon = UIImage(named: "\(imageName)_large.png")
        return (regurlarIcon, largeIcon)
    }
}