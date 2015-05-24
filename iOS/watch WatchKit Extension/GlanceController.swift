//
//  GlanceController.swift
//  watch WatchKit Extension
//
//  Created by Morad Rayyan on 3/13/15.
//  Copyright (c) 2015 Tamatm. All rights reserved.
//

import WatchKit
import Foundation


class GlanceController: WKInterfaceController {

    @IBOutlet var storyImage: WKInterfaceImage!
    override func awakeWithContext(context: AnyObject?) {
        super.awakeWithContext(context)
        
        // Configure interface objects here.
        var url = NSBundle.mainBundle().URLForResource("giphy", withExtension: "gif")
        var imageData = NSData(contentsOfURL: url!)
        
        // Returns an animated UIImage
        storyImage.setImage(UIImage.animatedImageWithData(imageData!))
        storyImage.startAnimating()
        
        
        /*let userInfo = [
            "few": "something",
            "what": "new"
        ]
        
        /*
        Lister uses a specific user activity name registered in the Info.plist and defined as a constant to
        separate this action from the built-in UIDocument handoff support.
        */
        
        self.updateUserActivity("123", userInfo: nil, webpageURL: url)*/
    }

    override func willActivate() {
        // This method is called when watch view controller is about to be visible to user
        super.willActivate()
    }

    override func didDeactivate() {
        // This method is called when watch view controller is no longer visible
        super.didDeactivate()
    }

}
