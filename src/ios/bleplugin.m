/********* bleplugin.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>

@interface bleplugin : CDVPlugin {
  // Member variables go here.
}

- (void)sacn:(CDVInvokedUrlCommand*)command;
- (void)close:(CDVInvokedUrlCommand*)command;
- (void)isEnabled:(CDVInvokedUrlCommand*)command;
- (void)doPrint:(CDVInvokedUrlCommand*)command;
- (void)printText:(CDVInvokedUrlCommand*)command;
- (void)printBarcode:(CDVInvokedUrlCommand*)command;
- (void)printQRcode:(CDVInvokedUrlCommand*)command;
- (void)startPage:(CDVInvokedUrlCommand*)command;
- (void)bluetoothName:(CDVInvokedUrlCommand*)command;
- (void)gotoMarklabel:(CDVInvokedUrlCommand*)command;
- (void)multilineText:(CDVInvokedUrlCommand*)command;
- (void)drawRect:(CDVInvokedUrlCommand*)command;
- (void)drawLine:(CDVInvokedUrlCommand*)command;
@end

@implementation bleplugin
- (void)sacn:(CDVInvokedUrlCommand*)command {
  CDVPluginResult *pluginResult = nil;
  NSString *echo = [command.arguments objectAtIndex:0];
  
}
- (void)coolMethod:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* echo = [command.arguments objectAtIndex:0];

    if (echo != nil && [echo length] > 0) {
      UIAlertController* controller =  [UIAlertController alertControllerWithTitle:@"cao" message:nil preferredStyle:UIAlertControllerStyleAlert];
  UIAlertAction *cancle = [UIAlertAction actionWithTitle:@"cancle" style:UIAlertActionStyleCancel handler:^(UIAlertAction *action){

  }];
  UIAlertAction *confirm = [UIAlertAction actionWithTitle:@"confirm" style:UIAlertViewStyleDefault handler:^(UIAlertAction *action){

  }];
  [controller addAction:cancle];
  [controller addAction:confirm];

  [[self viewController]presentViewController:controller animated:YES completion:^{

  }];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
