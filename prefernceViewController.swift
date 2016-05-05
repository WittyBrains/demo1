//
//  prefernceViewController.swift
//  WhatsTheWeatherIn
//
//  Created by Pawan Jhurani on 05/05/16.
//  Copyright © 2016 marinbenc. All rights reserved.
//

import UIKit

class prefernceViewController: UITableViewController {
  var viewModel = MVVMWeatherTableViewModel()
//    @IBAction func buttonClicked(sender: UIButton) {
    @IBOutlet var mySwitch: UISwitch!
    @IBAction func buttonClicked(sender: UISwitch) {
        if mySwitch.on {
             Weather.Constants.unit = "metric"
             self.viewModel.unit = "metric"
                        print("Switch is on")
                        mySwitch.setOn(false, animated:true)
                    } else {
                             Weather.Constants.unit = "imperial"
                       self.viewModel.unit = "imperial"

                        mySwitch.setOn(true, animated:true)
                    }

        
    }
   

    func switchIsChanged(mySwitch: UISwitch) {
        if mySwitch.on {
            Weather.Constants.unit = "metric"
            self.viewModel.unit = "metric"
            //print("Switch is on")
        } else {
            Weather.Constants.unit = "imperial"
            self.viewModel.unit = "imperial"
        }
    }


    override func viewDidLoad() {
        super.viewDidLoad()
        mySwitch.addTarget(self, action: Selector("switchIsChanged:"), forControlEvents: UIControlEvents.ValueChanged)
//         mySwitch.addTarget(self, action: #selector(prefernceViewController.switchIsChanged(_:)), forControlEvents: UIControlEvents.ValueChanged)

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }

//    @IBAction func indexChanged(sender: AnyObject) {
//        switch segmentcontrol.selectedSegmentIndex
//        {
//        case 0:
//            Weather.Constants.unit = "metric"
//          
//        self.viewModel.unit = "metric"
//           print(Weather.Constants.unit);
//        case 1:
//             Weather.Constants.unit = "imperial"
//             self.segmentcontrol.selectedSegmentIndex = 1
//            self.viewModel.unit = "imperial"
//            print("First selected");
//        default:
//            break; 
//        }
//    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

//    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
//        // #warning Incomplete implementation, return the number of sections
//        return 0
//    }
//
//    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
//        // #warning Incomplete implementation, return the number of rows
//        return 0
//    }

    /*
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("reuseIdentifier", forIndexPath: indexPath)

        // Configure the cell...

        return cell
    }
    */

    /*
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == .Delete {
            // Delete the row from the data source
            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
        } else if editingStyle == .Insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
