//
//  DetailViewController.swift
//  WhatsTheWeatherIn
//
//  Created by Pawan Jhurani on 05/05/16.
//  Copyright © 2016 marinbenc. All rights reserved.
//

import UIKit

class DetailViewController: UIViewController {
    @IBOutlet var time: UILabel!

    @IBOutlet var desc: UILabel!
    @IBOutlet var temp: UILabel!
    @IBOutlet var weatherde: UIImageView!
    var id: Int!

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
   
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        //
        //if(segue.identifier == "DetailView") {
        //let indexPath = NSIndexPath(forRow: Weather.Constants.dailyWeather.count-1, inSection: 0)
        if segue.identifier == "DetailView" {
            if let cell = sender as? UITableViewCell {
                let indexPath = MVVMWeatherTableViewController().tableView.indexPathForCell(cell)
                if let index = indexPath?.row {
                    Weather.Constants.dailyWeather = MVVMWeatherTableViewController().tableViewData == nil ? nil : MVVMWeatherTableViewController().tableViewData![indexPath!.section].1[index] as! AnyObject
                    print(Weather.Constants.dailyWeather)
                }
            }
        }
        
        
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
