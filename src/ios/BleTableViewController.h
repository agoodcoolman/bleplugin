//
//  BleTableViewController.h
//  MyApp
//
//  Created by xyt on 2017/9/27.
//

#import <UIKit/UIKit.h>
@interface BleTableViewController : UITableViewController
typedef void(^Callable)(NSString *);
-(void)getCallableBlock:(Callable)block;
@end
