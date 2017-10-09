//
//  PrintService.h
//  MyApp
//
//  Created by xyt on 2017/9/29.
//

#ifndef PrintService_h
#define PrintService_h
#import <Foundation/Foundation.h>
#import "BLEManager.h"
#import "BLEGATTService.h"
#import "Printer.h"
@interface PrintService:NSObject <BLEGATTServiceDelegate>

- (void)close;
- (BOOL)isEnabled;
- (void)doPrint;
- (void)printText:(int)x y:(int) y text:(NSString *) text fontName:(NSString*)fontName testSize:(int) textSize bold:(BOOL)bold rotate:(BOOL) rotate;
- (void)printBarcode:(int)x y:(int)y text:(NSString *) text type:(NSString* ) type width:(int) width heigth:(int) heigth rotate:(BOOL)rotate ;
- (void)printQRcode:(int)x y:(int)y text:(NSString*) text size:(int)size;
- (void)createPage:(int)pageWidth height:(int) height;
- (NSString *)bluetoothName;
- (void)gotoMarklabel;
- (void)drawLine:(int)x0 y0:(int) y0 x1:(int) x1 y1:(int) y1 lineWidth:(int) width;
-(void)startConnectBlue:(CBPeripheral *)peripheral UUID:(NSString *)UUID ;
-(void)drawRectLeft:(int)left Top:(int)top rigth:(int)right bottom:(int)bottom width:(int)width;
+(instancetype)sharePrintService;

@end

#endif /* PrintService_h */
