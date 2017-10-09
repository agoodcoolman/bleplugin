

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface Printer : NSObject


-(NSData *)printContent:(NSString *) str;


-(NSData *)printerBeep;

-(NSData *)printerWake;


-(NSData *)printStr:(NSString *) str;


-(NSData *)printStrLine:(NSString *) str;

-(NSData *)printLF;


-(NSData *)printCR;


-(NSData *)setFontDouble:(int32_t) op;

-(NSData *)setFontBold:(int32_t) op;


-(NSData *) feedToBlack;


-(NSData *) setUnderLine:(int32_t) op;


-(NSData *) feed:(int32_t) n;


-(NSData *) setLineSpace:(int32_t) distance;


-(NSData *) setAbsPosition:(int32_t) abspos;


-(NSData *) setRelPosition:(int32_t) relpos;


-(NSData *) setSnapMode:(int32_t) snapMode;

-(NSData *) setRotation:(int32_t) op;


-(NSData *) setLeftMargin:(int32_t) dot;


-(NSData *) setCharSpace:(int32_t) dot;


-(NSData *) chooseFont:(int32_t) op;


-(NSData *) fontSizeHeight:(int32_t) height andWidth:(int32_t) width;


-(NSData *) feedRow:(int32_t) row;


-(NSData *) printPicInflashIndex:(int32_t) index andMode:(int32_t) mode;

-(NSData *) drawBarCode:(NSString *) str andBarType:(int32_t)barType andHeightDot:(int32_t)heightDot
        andHRI_location:(int32_t)HRI_location;


-(NSData *)printQRcode:(int32_t) Ver andLevel:(int32_t)Level andStr:(NSString *) str;


-(NSData *) printBitmap:(NSString *)bmpName Mode:(int32_t) mode;


-(NSData *) createPageX:(int32_t) x andY:(int32_t) y;


-(NSData *) printPageHorizontal:(int32_t)horizontal andSkip:(int32_t) skip;


-(NSData *) Page_drawLineW:(int32_t) w andSx:(int32_t)sx andSy:(int32_t) sy anEx:(int) ex andEy:(int32_t) ey;


-(NSData *)Page_setTextBox:(int32_t)x andY:(int32_t)y Width:(int32_t)width Height:(int32_t)height andStr:(NSString *)str Fontsize:(int32_t)fontsize Rotate:(int32_t)rotate Bold:(int32_t)bold Underline:(Boolean)underline Reverse:(int32_t)reverse;


-(NSData *) Page_Area_Reverse:(int32_t)startx startY:(int32_t)starty endX:(int32_t)endx endY:(int32_t)endy Reverse:(Boolean)reverse;



-(NSData *)Page_setTextX:(int32_t)x andY:(int32_t)y andStr:(NSString *)str Fontsize:(int32_t)fontsize Rotate:(int32_t)rotate Bold:(int32_t)bold Underline:(Boolean)underline Reverse:(Boolean)reverse;


-(NSData *) Page_drawLineSx:(int32_t)sx andSy:(int32_t) sy andEx:(int32_t) ex andEy:(int32_t) ey;


-(NSData *)Page_drawBarX:(int32_t)x andY:(int32_t)y andStr:(NSString *)str BarcodeType:(int32_t)barcodetype Rotate:(int32_t)rotate BarWidth:(int32_t)barWidth BarHeight:(int32_t)barHeight;


-(NSData *)Page_printQrCodeX:(int32_t)x andY:(int32_t)y Rotate:(int32_t) rotate andVer:(int32_t)Ver Lel:(int32_t)lel andStr:(NSString *)Text;


-(NSData *)Page_printPDF417X:(int32_t)x andY:(int32_t)y Rotate:(int32_t)rotate Width:(int32_t)width andStr:(NSString *)Text;

-(NSData *) Page_printBitmapX:(int32_t)x andY:(int32_t)y Bitmap:(NSString *)bmpName;
@end
