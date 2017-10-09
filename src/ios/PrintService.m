//
//  PrintService.m
//  MyApp
//
//  Created by xyt on 2017/9/29.
//  it's work for print
//




#import "PrintService.h"

@implementation PrintService

NSMutableData* _date;
Printer * _print;

BLEGATTService * _bleGattService;
NSString* _deviceName;
BOOL _hasStart ;
#pragma mark - BLEGATTServiceDelegate

static id _instance = nil;

+(instancetype)allocWithZone:(struct _NSZone *)zone{
    //只进行一次
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _instance = [super allocWithZone:zone];
    });
    return _instance;
}



+(instancetype)sharePrintService {
    return [[self alloc]init] ;
}

//copy在底层 会调用copyWithZone:
- (id)copyWithZone:(NSZone *)zone{
    return  _instance;
}
+ (id)copyWithZone:(struct _NSZone *)zone{
    return  _instance;
}
+ (id)mutableCopyWithZone:(struct _NSZone *)zone{
    return _instance;
}
- (id)mutableCopyWithZone:(NSZone *)zone{
    return _instance;
}


-(instancetype) init {
    
    // 只进行一次
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _instance = [super init];
    });
    _bleGattService = [[BLEGATTService alloc] init];
    [_bleGattService setDelegate:self];
    _hasStart = NO;
    
    _date = [[NSMutableData alloc] init];
    _print = [[Printer alloc]init];
    
    return _instance;
}




-(void)startConnectBlue:(CBPeripheral *)peripheral UUID:(NSString *)UUID ;{
    [_bleGattService start:UUID on:peripheral];
    _deviceName = [peripheral name];
}



#pragma mark - BLEGATTServiceDelegate

#pragma mark 启动服务的结果,调用start之后会产生此事件
- (void)bleGattService:(nonnull BLEGATTService *)bleGattService didStart:(BOOL)result{
    if (result) {
        // 开始打印
        // 可以开始打印了
        
        _hasStart = YES;
        
    }
}

#pragma mark 是否可发送，以及可发送的字节数
- (void)bleGattService:(nonnull BLEGATTService *)bleGattService didFlowControl:(int)credit withMtu:(int)mtu {
    
}

#pragma mark 数据接收
- (void)bleGattService:(nonnull BLEGATTService *)bleGattService didDataReceived:(nonnull NSData *)revData{
    
}

- (void)close {
    [_bleGattService stop];
}
- (BOOL)isEnabled{
    if (_deviceName == nil) {
        return NO;
    } else{
        return YES;
    }
    
}
- (void)doPrint{
    int location = 0,size = 100;
    int length = _date.length/size ;
    for (int i = 0; i < length; i ++) {
        NSData * temp_data = [_date subdataWithRange:NSMakeRange(location, size)];
        location += size;
        [_bleGattService write:temp_data withResponse:false];
    }
    NSData * temp_data = [_date subdataWithRange:NSMakeRange(location, _date.length - location)];
    [_bleGattService write:temp_data withResponse:false];
    
}
- (void)printText:(int)x y:(int) y text:(NSString *) text fontName:(NSString*)fontName testSize:(int) textSize bold:(BOOL)bold rotate:(BOOL) rotate {
    [_date appendData:[_print Page_setTextX:x andY:y andStr:text Fontsize:textSize Rotate:0 Bold:bold Underline:NO Reverse:NO]];
}
- (void)printBarcode:(int)x y:(int)y text:(NSString *) text type:(NSString* ) type width:(int) width heigth:(int) heigth rotate:(BOOL)rotate {
    [_date appendData:[_print Page_drawBarX:x andY:y andStr:text BarcodeType:2 Rotate:0 BarWidth:width BarHeight:heigth]];
}
- (void)printQRcode:(int)x y:(int)y text:(NSString*) text size:(int)size{
    [_date appendData:[_print Page_printQrCodeX:x andY:y Rotate:0 andVer:4 Lel:1 andStr: text]];
}
- (void)createPage:(int)pageWidth height:(int) pageHeight{
    Byte arrayOfByte[] = {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
    [_date appendBytes:arrayOfByte length:10];
    [_date appendData:[_print createPageX:pageWidth andY:pageHeight]];
}
- (NSString *)bluetoothName{
    
    return _deviceName;
}

- (void)gotoMarklabel{
    [_date appendData:[_print printPageHorizontal:0 andSkip:0]];
}

- (void)drawLine:(int)x0 y0:(int) y0 x1:(int) x1 y1:(int) y1 lineWidth:(int) width{
    [_date appendData:[_print Page_drawLineW:width andSx:x0 andSy:y0 anEx:x1 andEy:y1]];
}
-(void)drawRectLeft:(int)left Top:(int)top rigth:(int)right bottom:(int)bottom width:(int)width{
    [self drawLine:left y0:top x1:right y1:top lineWidth:width];
    [self drawLine:left y0:top x1:left y1:bottom lineWidth:width];
    [self drawLine:right y0:top x1:right y1:bottom lineWidth:width];
    [self drawLine:left y0:bottom x1:right y1:bottom lineWidth:width];
    
}


@end



