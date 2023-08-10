//
//  ViewController.h
//  SW_Test
//
//  Created by zsl on 2022/8/31.
//

#import <AgoraRtcKit/AgoraRtcKit.h>
#import <iLiveData_Agora/iLiveData-Agora.h>
#import <UIKit/UIKit.h>
#import "MBProgressHUD.h"
#import "Masonry.h"

#define YS_Color_alpha(value, a) [UIColor colorWithRed:((float)((value & 0xFF0000) >> 16))/255.0  \
                                             green:((float)((value & 0xFF00) >> 8))/255.0     \
                                              blue:((float)(value & 0xFF))/255.0              \
                                             alpha:(a)/1.0]

@interface ViewController : UIViewController<UITableViewDelegate,UITableViewDataSource>
@property(nonatomic,assign)int64_t authTime;
@property(nonatomic,strong)NSString * authToken;
@property(nonatomic,strong)NSMutableArray * translatedResultArray;
@property(nonatomic,strong)NSMutableArray * recognizedResultArray;

@property(nonatomic,strong)UIButton * startRtvtButton;
@property(nonatomic,strong)UIButton * startRtauButton;
@property(nonatomic,strong)UIButton * addRoomButton;
@property(nonatomic,strong)UITableView * translatedTableView;
@property(nonatomic,strong)UITableView * recognizedTableView;


@end

