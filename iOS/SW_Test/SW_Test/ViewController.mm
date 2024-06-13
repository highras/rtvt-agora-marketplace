//
//  ViewController.m
//  AgoraRtcKitTest
//
//  Created by zsl on 2022/8/31.
//

#import "ViewController.h"
#import "ViewController+UI.h"

@interface ViewController ()<AgoraRtcEngineDelegate,AgoraMediaFilterEventDelegate>

@property(nonatomic,strong)AgoraRtcEngineKit * kit;
@property(nonatomic,strong)AgoraRtcEngineConfig * config;
@property(nonatomic,strong)NSString * agoraToken;

@property(nonatomic,assign)NSUInteger  agora_remoteUid;
@property(nonatomic,assign)NSUInteger  agora_myUid;

@property(nonatomic,strong)NSString * agora_appId;
@property(nonatomic,strong)NSString * agora_Token;
@property(nonatomic,strong)NSString * agora_RoomId;

@property(nonatomic,strong)NSString * appKeyRTVT;
@property(nonatomic,strong)NSString * appSecretRTVT;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    
    self.view.backgroundColor = [UIColor whiteColor];
    
    dispatch_async(dispatch_get_main_queue(), ^{
        
        
        self.agora_appId = @"";
        self.agora_RoomId = @"";
        self.agora_myUid = arc4random() % 999999999;
        
        self.appKeyRTVT = @"";
        self.appSecretRTVT = @"";
        
        
        if (self.agora_appId == nil || self.appSecretRTVT == nil ) {
            return;
        }
        
        [self setUpUI];
        self.agoraToken = self.agora_Token;
        self.config = [[AgoraRtcEngineConfig alloc] init];
        self.config.appId = self.agora_appId;
        self.config.areaCode = AgoraAreaCodeTypeGlobal;
        self.config.channelProfile = AgoraChannelProfileLiveBroadcasting;
        self.config.audioScenario = AgoraAudioScenarioDefault;
        self.config.eventDelegate = self;
        
        
        self.kit = [AgoraRtcEngineKit sharedEngineWithConfig:self.config delegate:self];
        
        NSLog(@"[self.kit setClientRole:AgoraClientRoleBroadcaster]  %d",[self.kit setClientRole:AgoraClientRoleBroadcaster]);
        NSLog(@"[self.kit enableAudio];  %d",[self.kit enableAudio]);
        
    
        NSLog(@"self.kit enableExtensionWithVendor: post %d",[self.kit enableExtensionWithVendor:[iLiveDataSimpleFilterManager_post companyName] extension:[iLiveDataSimpleFilterManager_post rtvt_post_plugName] enabled:YES]);
        NSLog(@"self.kit enableExtensionWithVendor:  pre%d",[self.kit enableExtensionWithVendor:[iLiveDataSimpleFilterManager_pre companyName] extension:[iLiveDataSimpleFilterManager_pre rtvt_pre_plugName] enabled:YES]);
        

        NSLog(@"[self.kit setAudioProfile:AgoraAudioProfileDefault]  %d",[self.kit setAudioProfile:AgoraAudioProfileDefault]);
        NSLog(@"[self.kit setDefaultAudioRouteToSpeakerphone:YES];   %d",[self.kit setDefaultAudioRouteToSpeakerphone:YES]);
        
        
    });
    
}
- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine remoteAudioStateChangedOfUid:(NSUInteger)uid state:(AgoraAudioRemoteState)state reason:(AgoraAudioRemoteReason)reason elapsed:(NSInteger)elapsed{
    
    
    //The post-processing plug-in needs to be opened here and someone in the room needs to open it
    if (state == AgoraAudioRemoteStateStarting) {
        [self showHudMessage:[NSString stringWithFormat:@"user %lu enter room  auto start translate",(unsigned long)uid] hideTime:1];
        self.agora_remoteUid = uid;
        
        NSLog(@"remoteAudioStateChangedOfUid --- AgoraAudioRemoteStateStarting");
        [self _endRtvtButtonClick];
        [self _startRtvtButtonClick];
    }
    
}



//enter room start rtvt
-(void)_startRtvtButtonClick{
    
    NSDictionary * translateDic = @{@"appKey":self.appKeyRTVT,
                                    @"appSecret":self.appSecretRTVT,
                                    @"srcLanguage":@"zh",
                                    @"destLanguage":@"en",
                                    @"srcAltLanguage":@[],
                                    @"tempResult":@(NO),
                                    
                                    
                                    // @"asrResult":@(YES),      Recognition result switch  The default YES is not passed
                                    // @"transResult":@(YES),    Translation result switch  The default YES is not passed
                                    // @"tempResult":@(NO),  Recognition + Translation tmp result switch  The default NO is not passed
                                    
    };
    
    
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:translateDic options:NSJSONWritingPrettyPrinted error:nil];
    NSString * jsonStr = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    
    //startAudioTranslation_post   !when the room is opened with only one person will fail
    //You need to focus on the method
    //(void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine remoteAudioStateChangedOfUid:(NSUInteger)uid state:(AgoraAudioRemoteState)state reason:(AgoraAudioRemoteReason)reason elapsed:(NSInteger)elapsed
    
    int start_audio_translate_result = [self _setProperty:@"startAudioTranslation_post" value:jsonStr type:0 isPost:YES];
    int start_audio_translate_result2 = [self _setProperty:@"startAudioTranslation_pre" value:jsonStr type:0 isPost:NO];
    
    if (start_audio_translate_result == 0 && start_audio_translate_result2 == 0) {
        
        [self showHudMessage:@"start success" hideTime:1];
        
    }else{
        
        [self showHudMessage:[NSString stringWithFormat:@"start fail %d",start_audio_translate_result] hideTime:1];
        
    }
        
}


-(void)_endRtvtButtonClick{
    
    int end_audio_translate_result = [self _setProperty:@"closeAudioTranslation_post" value:@"end" type:0 isPost:YES];
    int end_audio_translate_result2 = [self _setProperty:@"closeAudioTranslation_pre" value:@"end" type:0 isPost:NO];
    
    if (end_audio_translate_result == 0 && end_audio_translate_result2 ==0) {
        
        
        [self showHudMessage:@"close success" hideTime:1];
        
    }else{
        
        
        [self showHudMessage:[NSString stringWithFormat:@"close fail %d",end_audio_translate_result] hideTime:1];
        
    }
    
    
}
-(void)_startAddRoomButtonClick{
    
    
    [self showLoadHud];
    int result = [self.kit joinChannelByToken:self.agoraToken
                                    channelId:self.agora_RoomId
                                         info:nil
                                          uid:self.agora_myUid
                                  joinSuccess:^(NSString * _Nonnull channel, NSUInteger uid, NSInteger elapsed) {
        
       

    }];
    
    if (result == 0) {
        
        [self showHudMessage:@"enter room success" hideTime:1];
        
        
        
        
    }else{
        
        [self showHudMessage:@"enter room fail" hideTime:1];
        
        
        
    }
    
    
}

-(void)_leaveRoomButtonClick{
    
    [self showLoadHud];
    BOOL result = [self.kit leaveChannel:^(AgoraChannelStats * _Nonnull stat) {
            
    }];
    if (result == 0) {
        
        
        [self showHudMessage:@"leave room success" hideTime:1];
        
    }else{
        
        [self showHudMessage:@"leave room fail" hideTime:1];
        
    }
}
//0 rtvt 1rtau
-(int)_setProperty:(NSString*)key value:(NSString*)value type:(int)type  isPost:(BOOL)isPost{
    
    
    AgoraExtensionInfo * info = [[AgoraExtensionInfo alloc] init];
    info.remoteUid = self.agora_remoteUid;
    info.channelId = self.agora_RoomId;
    info.localUid = self.agora_myUid;
    
    int result;
    if (isPost) {
        result = [self.kit setExtensionPropertyWithVendor:[iLiveDataSimpleFilterManager_post companyName]
                                                     extension:[iLiveDataSimpleFilterManager_post rtvt_post_plugName]
                                                 extensionInfo:info
                                                           key:key
                                                         value:value];
        NSLog(@"post %d",result);
    }else{
        result = [self.kit setExtensionPropertyWithVendor:[iLiveDataSimpleFilterManager_pre companyName]
                                                     extension:[iLiveDataSimpleFilterManager_pre rtvt_pre_plugName]
                                                 extensionInfo:info
                                                           key:key
                                                         value:value];
        NSLog(@"pre %d",result);
    }
    
    
    
    
    return result;
    
    
    
}
-(void)onEvent:(NSString *)provider extension:(NSString *)extension key:(NSString *)key value:(NSString *)value{
    
//    NSLog(@"onEvent  %@   %@  %@   %@",provider,extension,key,value);
    
    
    NSError * error2;
    NSData *jsonData = [value dataUsingEncoding:NSUTF8StringEncoding];
     
    NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData
                                                        options:NSJSONReadingMutableContainers error:&error2];
    if(error2 == nil){
//         NSLog(@"onEvent ----- %@ ----- %@ ----- %@ ",key,extension,dic);
    }
    
    
    dispatch_async(dispatch_get_main_queue(), ^{
        
        if ([key isEqualToString:@"recognizeResult"]) {
            
            [self.recognizedResultArray addObject:[NSString stringWithFormat:@"%@-%@",extension,[dic objectForKey:@"result"]]];
            [self.recognizedTableView reloadData];
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:self.recognizedResultArray.count - 1 inSection:0];
            [self.recognizedTableView scrollToRowAtIndexPath:indexPath atScrollPosition:UITableViewScrollPositionBottom animated:YES];
            
            
        }else if ([key isEqualToString:@"translateResult"]){
            
            [self.translatedResultArray addObject:[NSString stringWithFormat:@"%@-%@",extension,[dic objectForKey:@"result"]]];
            [self.translatedTableView reloadData];
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:self.translatedResultArray.count - 1 inSection:0];
            [self.translatedTableView scrollToRowAtIndexPath:indexPath atScrollPosition:UITableViewScrollPositionBottom animated:YES];
            
        }
        
    });
    
    
}













#pragma mark ui

-(UIButton*)startRtvtButton{
    if (_startRtvtButton == nil) {
        _startRtvtButton = [UIButton buttonWithType:UIButtonTypeCustom];
        _startRtvtButton.titleLabel.font = [UIFont systemFontOfSize:13];
        [_startRtvtButton setTitle:@"start translate" forState:UIControlStateNormal];
        [_startRtvtButton addTarget:self action:@selector(_startRtvtButtonClick) forControlEvents:UIControlEventTouchUpInside];
        _startRtvtButton.backgroundColor = YS_Color_alpha(0x1b9fff,1);
        [_startRtvtButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    }
    return _startRtvtButton;
}
-(UIButton*)closeRtvtButton{
    if (_closeRtvtButton == nil) {
        _closeRtvtButton = [UIButton buttonWithType:UIButtonTypeCustom];
        _closeRtvtButton.titleLabel.font = [UIFont systemFontOfSize:13];
        [_closeRtvtButton setTitle:@"close translate" forState:UIControlStateNormal];
        [_closeRtvtButton addTarget:self action:@selector(_endRtvtButtonClick) forControlEvents:UIControlEventTouchUpInside];
        _closeRtvtButton.backgroundColor = YS_Color_alpha(0x1b9fff,1);
        [_closeRtvtButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    }
    return _closeRtvtButton;
}

-(UIButton*)leaveRoomButton{
    if (_leaveRoomButton == nil) {
        _leaveRoomButton = [UIButton buttonWithType:UIButtonTypeCustom];
        _leaveRoomButton.titleLabel.font = [UIFont systemFontOfSize:13];
        [_leaveRoomButton setTitle:@"leave room" forState:UIControlStateNormal];
        [_leaveRoomButton addTarget:self action:@selector(_leaveRoomButtonClick) forControlEvents:UIControlEventTouchUpInside];
        _leaveRoomButton.backgroundColor = YS_Color_alpha(0x1b9fff,1);
        [_leaveRoomButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    }
    return _leaveRoomButton;
}

-(UIButton*)addRoomButton{
    if (_addRoomButton == nil) {
        _addRoomButton = [UIButton buttonWithType:UIButtonTypeCustom];
        _addRoomButton.titleLabel.font = [UIFont systemFontOfSize:13];
        [_addRoomButton setTitle:@"enter room" forState:UIControlStateNormal];
        [_addRoomButton addTarget:self action:@selector(_startAddRoomButtonClick) forControlEvents:UIControlEventTouchUpInside];
        _addRoomButton.backgroundColor = YS_Color_alpha(0x1b9fff,1);
        [_addRoomButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    }
    return _addRoomButton;
}


-(UITableView*)translatedTableView{
    if (_translatedTableView == nil) {
        _translatedTableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        _translatedTableView.backgroundColor = [UIColor blackColor];
        _translatedTableView.delegate = self;
        _translatedTableView.dataSource = self;
        _translatedTableView.rowHeight = UITableViewAutomaticDimension;
    }
    return _translatedTableView;
}
-(UITableView*)recognizedTableView{
    if (_recognizedTableView == nil) {
        _recognizedTableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        _recognizedTableView.backgroundColor = [UIColor blackColor];
        _recognizedTableView.delegate = self;
        _recognizedTableView.dataSource = self;
        _recognizedTableView.rowHeight = UITableViewAutomaticDimension;
    }
    return _recognizedTableView;
}
@end

