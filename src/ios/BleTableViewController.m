//
//  BleTableViewController.m
//  MyApp
//
//  Created by xyt on 2017/9/27.
//

#import "BleTableViewController.h"
#import "BLEManager.h"
#import "PrintService.h"
@interface BleTableViewController ()<UITextFieldDelegate,BLEManagerDelegate>
@property(strong, atomic) NSMutableArray *dateArray;
@property(strong, atomic) UIRefreshControl * refreshController;



@property(nonatomic, strong)Callable callable;

@end



@implementation BleTableViewController



- (void)viewDidLoad {
    [super viewDidLoad];
    self.refreshController = [[UIRefreshControl alloc] init];
    self.refreshController.attributedTitle = [[NSAttributedString alloc] initWithString:@"loading"];
    [super setRefreshControl:self.refreshController];
    self.dateArray = [[NSMutableArray alloc] init];
    [self.tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:@"reuseIdentifier"];
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    [BLEMANAGER setDelegate:self];
    
    
}


-(void)viewDidAppear:(BOOL)animated {
    
    [BLEMANAGER setDelegate:self];
    [BLEMANAGER scanForPeripherals:nil];
    
    
}

-(void)viewDidDisappear:(BOOL)animated{
    [BLEMANAGER stopScan];
    [BLEMANAGER setDelegate:nil];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
#warning Incomplete implementation, return the number of sections
    return [self.dateArray count];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
#warning Incomplete implementation, return the number of rows
    return 1;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"reuseIdentifier" forIndexPath:indexPath];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"reuseIdentifier"];
        
    }
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(20, 10, 200, 30)];
    label.tag = 101;
    CBPeripheral *p = [self.dateArray objectAtIndex:indexPath.row];
    [label setText:p.name];
    [cell.contentView addSubview:label];
    
    return cell;
}



#pragma mark - Table view delegate

// In a xib-based application, navigation from a table can be handled in -tableView:didSelectRowAtIndexPath:
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    // Navigation logic may go here, for example:
    // Create the next view controller.
    CBPeripheral *peripheral = (CBPeripheral *)[_dateArray objectAtIndex:indexPath.row];
    // Pass the selected object to the new view controller.
    [BLEMANAGER stopScan];
    [BLEMANAGER stopScan];
    
    [BLEMANAGER connect:peripheral];
    

    // Push the view controller.
}

#pragma mark 蓝牙状态改变
- (void)didUpdateState:(CBCentralManagerState) state{
    NSLog(@"[BLEDevicesViewController]BLEManager state changed to %ld", (long)state);
}

#pragma mark 发现设备
- (void)didPeripheralFound:(nonnull CBPeripheral *)peripheral advertisementData:(nullable BLEAdvertisementData *)advertisementData RSSI:(nullable NSNumber *)RSSI{
    NSLog (@"[BLEDevicesViewController]Device found:%@", peripheral.name);
    if ([self.dateArray indexOfObject:peripheral] == LONG_MAX && peripheral.name != nil) {
        
        [self.dateArray addObject:peripheral];
        
        [[self tableView] reloadData];
    } else {
        
    }
    
}

#pragma mark 连接成功
- (void)didConnectPeripheral:(nonnull CBPeripheral *)peripheral{
    NSLog(@" connect sucess");
    [BLEMANAGER discoverServices:peripheral];
    [self dismissViewControllerAnimated:YES completion:^{
        _callable(peripheral.name);
    }];
    

}



#pragma mark 连接失败
- (void)didFailToConnectPeripheral:(nonnull CBPeripheral *)peripheral error:(nullable NSError *)error{
    NSLog(@"connect faild");
    
}

#pragma mark 连接断开
- (void)didDisconnectPeripheral:(nonnull CBPeripheral *)peripheral error:(nullable NSError *)error{
    NSLog(@"disconnect device");
}

#pragma mark 发现服务
- (void)didServicesFound:(nonnull CBPeripheral *)peripheral services:(nullable NSArray<CBService *> *)services{
    NSLog(@"service found");

    for(CBService* service in peripheral.services) {
        if ([service.UUID isEqual:[CBUUID UUIDWithString:SERVICE_UUID_IVT_DATA_TRANSMISSION]]
            || [service.UUID isEqual:[CBUUID UUIDWithString:@"49535343-FE7D-4AE5-8FA9-9FAFD205E455"]]) {
//            BLEGATTService* bleGattService = [[BLEGATTService alloc] init];
//            [bleGattService start:service.UUID.UUIDString on:peripheral];
//            [BLEMANAGER setDelegate:nil];
//
            [[PrintService sharePrintService]startConnectBlue:peripheral UUID:service.UUID.UUIDString];
            
        }
    }
}


-(void)getCallableBlock:(Callable)block {
    _callable = block;
}

@end
