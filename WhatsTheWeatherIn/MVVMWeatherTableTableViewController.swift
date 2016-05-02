

import UIKit
import Foundation
import RxCocoa
import RxSwift
import Alamofire
import CoreLocation


class MVVMWeatherTableViewController: UITableViewController, UIAlertViewDelegate, CLLocationManagerDelegate {
    var cityname:String = String()
	var boundToViewModel = false
	let locationManager = CLLocationManager()
	func bindSourceToLabel(source: PublishSubject<String?>, label: UILabel) {
			source
				.subscribeNext { text in
						dispatch_async(dispatch_get_main_queue(), { () -> Void in
							label.text = text
						})
				}
				.addDisposableTo(disposeBag)
	}
	
	
	
	//MARK: Outlets
	
	let disposeBag = DisposeBag()
	
	@IBOutlet weak var cityTextField: UITextField!
	
	@IBOutlet weak var cityNameLabel: UILabel!
	@IBOutlet weak var cityDegreesLabel: UILabel!
	@IBOutlet weak var weatherMessageLabel: UILabel!
	@IBOutlet weak var weatherImageOutlet: UIImageView!
	@IBOutlet weak var backgroundImageOutlet: UIImageView!
		
    var alertController: UIAlertController? {
        didSet {
            if let alertController = alertController {
                alertController.addAction(UIAlertAction(title: "Ok", style: .Default, handler: nil))
                presentViewController(alertController, animated: true, completion: nil)
            }
        }
    }
	
	//table view header (current weather display)
	@IBOutlet weak var weatherView: UIView! {
		didSet {
         //   weatherView.bounds.size = "10"
			//weatherView.bounds.size = ((UIScreen.mainScreen().bounds.size) / 4 )
		}
	}
	


	
	//MARK: Lifecycle
	
	var viewModel = MVVMWeatherTableViewModel()
	
	override func viewDidLoad() {
		super.viewDidLoad()
        self.locationManager.requestAlwaysAuthorization()
        
        // For use in foreground
        self.locationManager.requestWhenInUseAuthorization()
        
        if CLLocationManager.locationServicesEnabled() {
            locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
            locationManager.startUpdatingLocation()
        }

		cityTextField.rx_text
			.debounce(0.3, MainScheduler.sharedInstance)
			
			.subscribeNext { searchText in
				self.viewModel.searchText = searchText
			}
			.addDisposableTo(disposeBag)
		
		
		bindSourceToLabel(viewModel.cityName, label: cityNameLabel)
		bindSourceToLabel(viewModel.degrees, label: cityDegreesLabel)
		bindSourceToLabel(viewModel.weatherDescription, label: weatherMessageLabel)
		
		viewModel.weatherImage.subscribeNext { image in
			self.weatherImageOutlet.image = image
		}
		.addDisposableTo(disposeBag)
		
		viewModel.tableViewData.subscribeNext { data in
			self.tableViewData = data
			self.tableView.reloadData()
		}
		.addDisposableTo(disposeBag)
		
		viewModel.backgroundImage.subscribeNext { image in
			dispatch_async(dispatch_get_main_queue(), { () -> Void in
				self.backgroundImageOutlet.image = image
			})
		}
		.addDisposableTo(disposeBag)
		
        viewModel.errorAlertController.subscribeNext { alertController in
            dispatch_async(dispatch_get_main_queue(), { () -> Void in
                self.alertController = alertController
            })
        }
        .addDisposableTo(disposeBag)
		
	}
    func locationManager(manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        Weather.Constants.locValue = manager.location!.coordinate
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
                                //self.cityname.text = city as String
                                print(city)
                            }
                            if let zip = Weather.Constants.placenark.addressDictionary!["ZIP"] as? NSString {
                                Weather.Constants.zip = zip
                                print(Weather.Constants.zip )
                            }
        
                            // Country
                            if let country = Weather.Constants.placenark.addressDictionary!["Country"] as? NSString {
                                print(country)
                            }
                            
                        }

       // print("locations = \(locValue.latitude) \(locValue.longitude)")
    }
   
	//MARK: Table view data source
	
	var tableViewData:[(String, [WeatherForecast])]?
	
	override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
		return tableViewData == nil ? 0	: tableViewData!.count
	}
	
	override func tableView(tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
		return tableViewData?[section].0
	}
	
	override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
		return tableViewData == nil ? 0 : tableViewData![section].1.count
	}
	
	override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> ForecastTableViewCell {
		let cell = tableView.dequeueReusableCellWithIdentifier("forecastCell", forIndexPath: indexPath) as? ForecastTableViewCell
		
		cell!.forecast = tableViewData == nil ? nil : tableViewData![indexPath.section].1[indexPath.row]
		return cell!
	}
}
