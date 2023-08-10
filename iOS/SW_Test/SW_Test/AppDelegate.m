//
//  AppDelegate.m
//  RTAT_Test
//
//  Created by zsl on 2022/8/1.
//

#import "AppDelegate.h"
#import "ViewController.h"
@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
    [self.window setRootViewController:[[ViewController alloc]init]];
    [self.window makeKeyAndVisible];
    
    
    return YES;
}





@end
