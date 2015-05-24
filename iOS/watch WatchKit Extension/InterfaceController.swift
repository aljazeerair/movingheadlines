//
//  InterfaceController.swift
//  watch WatchKit Extension
//
//  Created by Morad Rayyan on 3/13/15.
//  Copyright (c) 2015 Tamatm. All rights reserved.
//

import WatchKit
import Foundation

class InterfaceController: WKInterfaceController {
   
    @IBOutlet var storyImage: WKInterfaceImage!
    
    
    var sampleHeadline = "http://www.aljazeera.com/news/2015/04/rouhani-iran-turkey-agree-stop-yemen-war-150407192559290.html"
    override func awakeWithContext(context: AnyObject?) {
        super.awakeWithContext(context)
       
        var url = NSBundle.mainBundle().URLForResource("giphy", withExtension: "gif")
        
        var imageData = NSData(contentsOfURL: url!)
        
        // Returns an animated UIImage
        storyImage.setImage(UIImage.animatedImageWithData(imageData!))
        
        storyImage.startAnimating()
    }
    
    override func willActivate() {
        // This method is called when watch view controller is about to be visible to user
        super.willActivate()
    }
    
    override func didDeactivate() {
        // This method is called when watch view controller is no longer visible
        super.didDeactivate()
    }
    
    @IBAction func openOnPhone() {
        var dictionary = NSDictionary(objects: [sampleHeadline], forKeys: ["url"])
        WKInterfaceController.openParentApplication(dictionary as [NSObject : AnyObject], reply: { (replyInfo, error) -> Void in
            var dictionary = replyInfo as NSDictionary
            let successString: AnyObject = dictionary.objectForKey("success")!
            
            println(successString)
        })
    }
    
   
}
