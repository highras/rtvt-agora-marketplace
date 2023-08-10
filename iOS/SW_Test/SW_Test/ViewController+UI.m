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
        make.left.equalTo(self.view).offset(200);
        make.right.equalTo(self.view).offset(-100);
    }];
    
    [self.view addSubview:self.startRtvtButton];
    [self.startRtvtButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.equalTo(self.addRoomButton);
        make.top.equalTo(self.addRoomButton.mas_bottom).offset(20);
    }];

    [self.view addSubview:self.startRtauButton];
    [self.startRtauButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.equalTo(self.startRtvtButton);
        make.top.equalTo(self.startRtvtButton.mas_bottom).offset(20);
    }];

    self.translatedResultArray = [NSMutableArray array];
    self.recognizedResultArray = [NSMutableArray array];
    
    [self.view addSubview:self.translatedTableView];
    [self.translatedTableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.startRtauButton.mas_bottom).offset(50);
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



@end
