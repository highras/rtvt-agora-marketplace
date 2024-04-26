//
//  SimpleFilterManager.h
//
//  Created by xianing on 2021/7/27.
//  Copyright © 2021 Agora Corp. All rights reserved.
//



#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface iLiveDataSimpleFilterManager_post : NSObject

+ (instancetype)sharedInstance;

+ (NSString * __nonnull)companyName;
+ (NSString * __nonnull)rtvt_post_plugName;
+ (NSString * __nonnull)rtau_post_plugName;

+ (NSString*)getVersion;

@end

NS_ASSUME_NONNULL_END
