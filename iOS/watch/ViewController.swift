//
//  ViewController.swift
//  watch
//
//  Created by Morad Rayyan on 3/13/15.
//  Copyright (c) 2015 Tamatm. All rights reserved.
//

import UIKit
import Parse

class ViewController: UIViewController {
    
    @IBOutlet weak var randomButton: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func pushRandomHeadline(sender: UIButton) {
       
        randomButton.enabled = false
        // get data from Parse
       var getHeadlines = PFQuery(className: "Headline")
        getHeadlines.findObjectsInBackgroundWithBlock { (objects:[AnyObject]?, error:NSError?) -> Void in
         
            if (error == nil){
                self.randomButton.enabled = true
                
                if let objects = objects as? [PFObject] {
                    let random = self.getRandomInt(UInt32(0), max : UInt32(objects.count-1))
                    // TODO: Get GifName + TargetLink
                    // Write the values to Firebase
                    
                }
                else {
                    println("Error: \(error) \(error?.userInfo)")
                }
            }
        }
        
    }
    
    func getRandomInt(min:UInt32, max:UInt32)-> UInt32{
        let range = (max - min + 1)
        let diceRoll = arc4random_uniform(range)
        
        return diceRoll;
    }
}

