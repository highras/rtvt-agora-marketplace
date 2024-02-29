//
//  ViewController+UI.m
//  SW_Test
//
//  Created by zsl on 2022/9/2.
//

#import "ViewController+UI.h"

@implementation ViewController (UI)
-(void)setUpUI{
    
    [self.view addSubview:self.addRoomButton];
    [self.addRoomButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.view).offset(60);
        make.centerX.equalTo(self.view);
    }];
    
    
    [self.view addSubview:self.leaveRoomButton];
    [self.leaveRoomButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.view);
        make.top.equalTo(self.addRoomButton.mas_bottom).offset(20);
    }];

    [self.view addSubview:self.startRtvtButton];
    [self.startRtvtButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.view);
        make.top.equalTo(self.leaveRoomButton.mas_bottom).offset(20);
    }];
    
    [self.view addSubview:self.closeRtvtButton];
    [self.closeRtvtButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.view);
        make.top.equalTo(self.startRtvtButton.mas_bottom).offset(20);
    }];
    

    self.translatedResultArray = [NSMutableArray array];
    self.recognizedResultArray = [NSMutableArray array];
    
    [self.view addSubview:self.translatedTableView];
    [self.translatedTableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.closeRtvtButton.mas_bottom).offset(50);
        make.left.equalTo(self.view).offset(30);
        make.right.equalTo(self.view).offset(-30);
        make.height.equalTo(@160);
    }];
    
    [self.view addSubview:self.recognizedTableView];
    [self.recognizedTableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.translatedTableView.mas_bottom).offset(10);
        make.left.equalTo(self.view).offset(30);
        make.right.equalTo(self.view).offset(-30);
        make.height.equalTo(@(160));
    }];

   
    
    
}
-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (tableView == self.translatedTableView) {
        UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:@"cellId"];
        if (cell == nil) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cellId"];
            cell.backgroundColor = [UIColor blackColor];
            cell.textLabel.textColor = [UIColor whiteColor];
            cell.textLabel.font = [UIFont systemFontOfSize:12];
            cell.textLabel.numberOfLines = 0;
        }
        
        cell.textLabel.text = [self.translatedResultArray objectAtIndex:indexPath.row];
        
        return cell;
        
    }else{
        
        UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:@"cellId"];
        if (cell == nil) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cellId"];
            cell.backgroundColor = [UIColor blackColor];
            cell.textLabel.textColor = [UIColor whiteColor];
            cell.textLabel.font = [UIFont systemFontOfSize:12];
            cell.textLabel.numberOfLines = 0;
        }
        
        cell.textLabel.text = [self.recognizedResultArray objectAtIndex:indexPath.row];
        
        return cell;
    }
    
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    if (tableView == self.translatedTableView) {
        
        return self.translatedResultArray.count;
        
    }else{
        
        return self.recognizedResultArray.count;
    }
}

- (void)showHudMessage:(NSString*)message hideTime:(int)hideTime{
   
    dispatch_async(dispatch_get_main_queue(), ^{
        
        [MBProgressHUD hideHUDForView:self.view animated:YES];
        
        MBProgressHUD * hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        //hud.backgroundView.style = MBProgressHUDBackgroundStyleSolidColor;
        hud.bezelView.style = MBProgressHUDBackgroundStyleSolidColor;
        hud.bezelView.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.8];
        hud.mode = MBProgressHUDModeText;
        hud.label.text = message;
        hud.label.textColor = [UIColor whiteColor];
        hud.label.numberOfLines = 0;
        hud.removeFromSuperViewOnHide = YES;
        [hud hideAnimated:YES afterDelay:hideTime];
        
    });
    
}


-(void)showLoadHud{
    
    dispatch_async(dispatch_get_main_queue(), ^{
        
        [MBProgressHUD hideHUDForView:self.view animated:YES];
        
        MBProgressHUD * hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        hud.removeFromSuperViewOnHide = true;
        hud.bezelView.style = MBProgressHUDBackgroundStyleSolidColor;
        hud.bezelView.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.8];
        hud.contentColor = [UIColor whiteColor];
        hud.label.textColor = [UIColor whiteColor];
        
    });
    
}

@end
