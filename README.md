<!--# 使用云上曲率实时语音识别&翻译插件-->
# LiveData RTVT
### [中文文档](README-zh.md) <br />
<!--本文介绍如何在你的项目中集成和使用云上曲率实时语音识别&翻译插件（以下简称曲率识别及翻译插件），包括Android和iOS平台。-->

This guide is provided by LiveData. Agora is planning a documentation upgrade program for all extensions on the marketplace. Please stay tuned.

LiveData RTVT extension allows you to embed real-time voice transcription and translation into your mobile application providing automated speech recognition(ASR) without any upfront data training requirements.You can find the integration examples below.


<!--## 技术原理

曲率识别及翻译插件是对云上曲率[实时语音识别](https://docs.ilivedata.com/asr/overview/introduction/)和[实时翻译](https://docs.ilivedata.com/alt/overview/introduction/)核心 API 的封装。通过调用[声网视频 SDK v4.2.0 Beta](https://docs.agora.io/cn/video-call-4.x-beta/product_video_ng?platform=Android) 的 [setExtensionProperty](https://docs.agora.io/cn/video-call-4.x-beta/API%20Reference/java_ng/API/class_irtcengine.html#api_setextensionproperty) 或 [setExtensionPropertyWithVendor](https://docs.agora.io/cn/video-call-4.x-beta/API%20Reference/ios_ng/API/class_irtcengine.html#api_setextensionproperty)方法，传入指定的 `key` 和 `value` 参数，你可以快速集成云上曲率的实时语音识别和翻译的能力。支持的 key 和 value 详见[插件的 key-value 列表]（./模板-插件接口说明v1.md/#方法 key 的 value 说明）。
-->

## Understand the tech

The LiveData RTVT extension encapsulates the core APIs of the LiveData RTVT SDK. By calling the [setExtensionProperty](https://api-ref.agora.io/en/video-sdk/android/4.x/API/class_irtcengine.html#api_irtcengine_setextensionproperty) or [setExtensionPropertyWithVendor](https://api-ref.agora.io/en/video-sdk/ios/4.x/API/class_irtcengine.html#api_irtcengine_setextensionproperty) method of the [Agora Video SDK v4.x]() and passing in the corresponding `key` and `value`, you can quickly integrate capabilities of LiveData RTVT. For details, see the key-value overview.

<!--## 前提条件

- Android 开发环境需满足以下要求：
  - Android Studio 4.1 以上版本。
  - 运行 Android 5.0 或以上版本的真机（非模拟器）。
- iOS 开发环境需满足以下要求：
  - Xcode 9.0 或以上版本。
  - 运行 iOS 9.0 或以上版本的真机（非模拟器）。
-->
## Prerequisites
The development environment has to meet the following requirements:
- Android
  - Android Studio 4.1 or later
  - A physical device (not an emulator) running Android 5.0 or later
- iOS
  - Xcode 9.0 or later.
  - A physical device (not an emulator) running iOS 9.0 or later.


<!--## 准备工作-->
## Project Setup

The LiveData RTVT extension works together with the Video SDK v4.x. Refer to the following doc to integrate the SDK and start a basic voice call:

- SDK quickstart
  - [Android](https://docs.agora.io/en/voice-calling/get-started/get-started-sdk?platform=android)
  - [iOS](https://docs.agora.io/en/voice-calling/get-started/get-started-sdk?platform=ios)

<!--### 使用声网 SDK 实现视频通话

曲率识别及翻译插件需要与[声网视频 SDK v4.2.0 Beta](https://docs.agora.io/cn/video-call-4.x-beta/product_video_ng?platform=Android) 搭配使用。参考以下文档集成视频 SDK v4.2.0 Beta 并实现基础的视频通话：
- [实现视频通话（Android）](https://docs.agora.io/cn/video-call-4.x-beta/start_call_android_ng?platform=Android#%E5%BB%BA%E7%AB%8B%E9%A1%B9%E7%9B%AE)
- [实现视频通话（iOS）](https://docs.agora.io/cn/video-call-4.x-beta/start_call_ios_ng%20?platform=iOS#%E5%88%9B%E5%BB%BA%E9%A1%B9%E7%9B%AE)



### 购买和激活插件

在声网控制台[购买和激活](https://docs.agora.io/cn/extension_customer/get_extension?platform=All%20Platforms)曲率识别及翻译插件，保存好获取到的 `appKey` 和 `appSecret`，后续初始化插件时需要用到。--->

To receive a `appKey` and a `appSecret` from LiveData
- please buy and activate the extension project on [Agora Console](https://console.agora.io/), then click View in the Secret column.
- or contact us via Agora.

<!--### 集成插件-->

## Integrate the extension

<!--参考如下步骤在你的项目中集成曲率识别及翻译插件：-->

<!--**Android**


1. 在[声网云市场下载](https://docs.agora.io/cn/extension_customer/downloads?platform=All%20Platforms)页面下载曲率识别及翻译插件的 Android 插件包。解压后，将所有 `.aar` 文件保存到项目文件夹的  `/app/libs`  路径。
2. 打开 `app/build.gradle` 文件，在 `dependencies` 中添加如下行：

   ```java
   implementation fileTree(dir: "libs", include: ["*.jar", "*.aar"])
   ```
-->
### Android

1. [Download]() the Android package of LiveData RTVT from the Extensions Marketplace.
2. Unzip the package, and save all `.aar` files to the `/app/libs` path of your project folder.
3. In the `app/build.gradle` file, add the following line in `dependencies`:

```java
implementation fileTree(dir: "libs", include: ["*.jar", "*.aar"])
```

<!--**iOS**


1. 在[声网云市场下载](https://docs.agora.io/cn/extension_customer/downloads?platform=All%20Platforms)页面下载曲率识别及翻译插件的 iOS 插件包。解压后，将所有 `.framework` 库文件保存到你的项目文件夹下。
2. 在 Xcode 中[添加动态库](https://help.apple.com/xcode/mac/current/#/dev51a648b07)，确保 **Embed** 属性设置为 **Embed & Sign**。

以如下项目结构为例，你可以把库文件保存到 `<ProjectName>` 路径下。

```shell
.
├── <ProjectName>
├── <ProjectName>.xcodeproj
```
-->
### iOS

1. [Download]() the iOS package of LiveData RTVT from the Extensions Marketplace.
2. Unzip the package, and save all `.framework` files to the `<ProjectName> `path.
3. Ensure that you select **Embed & Sign** instead of **Embed**.

You can save `.framework` files under your project folder, as follows:

```shell
.
├── <ProjectName>
├── <ProjectName>.xcodeproj
```



<!--## 调用流程

本节介绍插件相关接口的调用流程。接口的参数解释详见[接口说明]()。-->

## Call Sequence
This section describes the call sequence you implement to use LiveData RTVT features in your app.

<!--### 1. 启用插件

**Android**
初始化声网 `AgoraRtcEngine` 时，需要先调用`addExtension`加载插件，再调用 `enableExtension` 启用插件。

```java
    RtcEngineConfig config = new RtcEngineConfig();
    config.addExtension("agora-iLiveData-filter");
    engine = RtcEngine.create(config);
    engine.enableExtension("iLiveData", "RTVT", true);
```

**iOS**
初始化声网 `AgoraRtcEngine` 时，调用 `enableExtensionWithVendor` 启用插件。

```objective-c
   AgoraRtcEngineConfig *config = [AgoraRtcEngineConfig new];

   // 设置 config.eventDelegate = self 接受翻译识别结果
   // 开启RTVT插件
   [_agoraKit enableExtensionWithVendor:[iLiveDataSimpleFilterManager companyName]
                             extension:[iLiveDataSimpleFilterManager plugName]
                             enabled:YES]；
```-->
### 1. Enable the extension

#### Android
When you initialize `AgoraRtcEngine` :
- first call `addExtension` to load the extension
- then call `enableExtension` to enable the extension

```java
RtcEngineConfig config = new RtcEngineConfig();
config.addExtension("agora-iLiveData-filter");
engine = RtcEngine.create(config);
engine.enableExtension("iLiveData", "RTVT", true);
```


#### iOS
When you initialize `AgoraRtcEngine`, call `enableExtensionWithVendor` to enable the extension.

```objective-c
AgoraRtcEngineConfig *config = [AgoraRtcEngineConfig new];

// set config.eventDelegate = self to accept the `onEvent` callback
// enable RTVT extension
[_agoraKit enableExtensionWithVendor:[iLiveDataSimpleFilterManager companyName]
                          extension:[iLiveDataSimpleFilterManager plugName]
                            enabled:YES]；
```


<!--### 2. 使用插件

**Android**
调用`setExtensionProperty` 指定 key 为 `startAudioTranslation` 在value中以json格式传入`appkey` `appsecret`等参数

```java
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("appKey", "appKey");
    jsonObject.put("appSecret", "appSecrect");
    jsonObject.put("srclang", "zh");
    jsonObject.put("dstLang", "en");

    engine.setExtensionProperty(EXTENSION_VENDOR_NAME, EXTENSION_AUDIO_FILTER_VOLUME, "startAudioTranslation", jsonObject.toString());
```

**iOS**
调用`setExtensionPropertyWithVendor`，指定 key 为 `startAudioTranslation` 并在 value 中传入 `appKey` 和 `appSecret` 等参数。

```objective-c
   NSDictionary * translateDic = @{
                                   @"appKey":@"appKey",
                                   @"appSecret":@"appSecret",
                                   @"srcLanguage":@"zh",
                                   @"destLanguage":@"en"
                                  };

     NSData * translateDicJsonData = [NSJSONSerialization dataWithJSONObject:translateDic options:NSJSONWritingPrettyPrinted error:nil];
     NSString * translateDicJsonString = [[NSString alloc] initWithData:translateDicJsonData encoding:NSUTF8StringEncoding];

     [_agoraKit setExtensionPropertyWithVendor:[iLiveDataSimpleFilterManager companyName]
                                     extension:[iLiveDataSimpleFilterManager rtvt_plugName])
                                           key:@"startAudioTranslation"
                                         value:translateDicJsonString];
```
-->

### 2. Start using the extension

#### Android

When you are ready to start using RTVT, call `setExtensionProperty` and pass in the corresponding keys and values:
- set key as`startAudioTranslation`
- set value as `appkey`, `appsecret`, `srclang`, `dstLang` in JSON

```java
JSONObject jsonObject = new JSONObject();
jsonObject.put("appKey", "appKey");
jsonObject.put("appSecret", "appSecrect");
jsonObject.put("srclang", "zh");
jsonObject.put("dstLang", "en");

engine.setExtensionProperty(EXTENSION_VENDOR_NAME, EXTENSION_AUDIO_FILTER_VOLUME, "startAudioTranslation", jsonObject.toString());
```

#### iOS

When you are ready to start using RTVT, call `setExtensionPropertyWithVendor` and pass in the corresponding keys and values:
- set key as`startAudioTranslation`
- set value as `appkey`, `appsecret`,`srclang` `dstLang` in JSON

```objective-c
NSDictionary * translateDic = @{
                                @"appKey":@"appKey",
                                @"appSecret":@"appSecret",
                                @"srcLanguage":@"zh",
                                @"destLanguage":@"en"
                              };

NSData * translateDicJsonData = [NSJSONSerialization dataWithJSONObject:translateDic options:NSJSONWritingPrettyPrinted error:nil];
NSString * translateDicJsonString = [[NSString alloc] initWithData:translateDicJsonData encoding:NSUTF8StringEncoding];

[_agoraKit setExtensionPropertyWithVendor:[iLiveDataSimpleFilterManager companyName]
                                extension:[iLiveDataSimpleFilterManager rtvt_plugName])
                                      key:@"startAudioTranslation"
                                    value:translateDicJsonString];
```



<!--### 3. 结束使用插件

**Android**
调用 `setExtensionProperty`方法并指定 key 为 `closeAudioTranslation` 来结束曲率识别和翻译插件的使用。

```java
    engine.setExtensionProperty(EXTENSION_VENDOR_NAME, EXTENSION_AUDIO_FILTER_VOLUME, "closeAudioTranslation", "{}");
```


**iOS**
调用 `setExtensionPropertyWithVendor`方法并指定 key 为 `closeAudioTranslation` 来结束曲率识别和翻译插件的使用。

```objective-c
   [_agoraKit setExtensionPropertyWithVendor:[iLiveDataSimpleFilterManager companyName]
                                    extension:[iLiveDataSimpleFilterManager rtvt_plugName]
                                          key:"closeAudioTranslation"
                                        value:"end"];
```
-->

### 3. Stop using the extension

#### Android

When stop using RTVT, call `setExtensionProperty` and pass in the corresponding keys and values:
- set key as`closeAudioTranslation`
- set value as `end`

```java
engine.setExtensionProperty(EXTENSION_VENDOR_NAME, EXTENSION_AUDIO_FILTER_VOLUME, "closeAudioTranslation", "end");
```

#### iOS

When stop using RTVT, call `setExtensionPropertyWithVendor` and pass in the corresponding keys and values:
- set key as`closeAudioTranslation`
- set value as `end`

```objective-c
[_agoraKit setExtensionPropertyWithVendor:[iLiveDataSimpleFilterManager companyName]
                                extension:[iLiveDataSimpleFilterManager rtvt_plugName]
                                      key:"closeAudioTranslation"
                                    value:"end"];
```


<!--### 4. 识别和翻译结果回调

**Android**
初始化成功后，曲率识别及翻译插件会通过 `onEvent` 回调返回识别结果。识别结果的含义详见 `onEvent` 回调。

```java
@Override
public void onEvent(String vendor, String extension, String key, String value) {
    key: "recognizeResult"识别结果标识  "translateResult"翻译结果标识
      value: 对应key分别为 识别结果 和 翻译结果
}
```

**iOS**
初始化成功后，曲率识别及翻译插件会通过 `onEvent` 回调返回识别结果。识别结果的含义详见 onEvent 回调。


```objective-c
-(void)onEvent:(NSString *)provider extension:(NSString *)extension key:(NSString *)key value:(NSString *)value{

       provider:"iLiveData"
      extension:"RTVT"
            key: "recognizeResult"识别结果标识  "translateResult"翻译结果标识
          value: 对应key分别为 识别结果 和 翻译结果

}
```
-->

### 4. Result callback

LiveData RTVT extension provides result of voice transcription and translation, you can receive all return of results or either of them, using `onEvent` callback of the Agora SDK.

#### Android

After enable LiveData RTVT extension， you can receive the results of voice transcription and translation via `onEvent` callback. The description of `onEvent` callback keys can be seen below.

```java
@Override
public void onEvent(String vendor, String extension, String key, String value) {
        key: "recognizeResult"  "translateResult"
      value:
}
```
#### iOS
After enable LiveData RTVT extension， you can receive the results of voice transcription and translation via `onEvent` callback. The description of `onEvent` callback keys can be seen below.

```objective-c
-(void)onEvent:(NSString *)provider extension:(NSString *)extension key:(NSString *)key value:(NSString *)value{

       provider:"iLiveData"
      extension:"RTVT"
            key: "recognizeResult"  "translateResult"
          value:
}
```

<!--
## 示例项目

| 平台    | 语言        | 示例项目                                                     |
| :------ | :---------- | :----------------------------------------------------------- |
| Android | Java        | [项目示例](https://github.com/highras/rtvt-agora-marketplace) |
| iOS     | Objective-C | [项目示例](https://github.com/highras/rtvt-agora-marketplace) |

### 运行步骤

**Android**

1. 克隆仓库：
  ```shell
	git clone （//TODO: 仓库链接）
  ```
2. 从[声网云市场下载](https://docs.agora.io/cn/extension_customer/downloads?platform=All%20Platforms)页面下载曲率识别及翻译插件的 Android 插件包。解压后，将所有 `.aar` 文件保存到 `（TODO:具体路径） ` 。
3. 在 Android Studio 中打开示例项目 `（TODO: 工程文件的路径）`。
4. 将项目与 Gradle 文件同步。
5. 打开 `（TODO: 文件的具体路径）`，进行如下修改：
	- 将 `<YOUR_APP_ID>` 替换为你的 App ID。获取 App ID 请参考[开始使用 Agora 平台](https://docs.agora.io/cn/Agora%20Platform/get_appid_token?platform=All%20Platforms)。
	- 将 `<YOUR_APP_KEY>` 和 `<YOUR_APP_SECRET>` 分别替换为你的 `appKey` 和 `appSecret`。获取方式详见[购买和激活插件](https://docs.agora.io/cn/extension_customer/get_extension?platform=All%20Platforms)。

  ```java
  // TODO: 替换成你的插件对应的代码
  public interface Config {
       String mAppId = "<YOUR_APP_ID>";
       String mAppKey = "<YOUR_APP_KEY>";
       String mAppSecret = "<YOUR_APP_SECRET>";
  }
  ```
4. 连接一台 Android 真机（非模拟器），运行项目。


**iOS**
1. 从[声网云市场下载](https://docs.agora.io/cn/extension_customer/downloads?platform=All%20Platforms)页面下载曲率识别及翻译插件的 iOS 插件包。解压后，将所有 `.framwork` 库文件保存到 `（TODO: 具体路径）` 。
2. 将 iLiveData_Agora.framework 拖入项目。
3. 项目设置 在TARGETS->Build Settings->Other Linker Flags （选中ALL视图）中添加-ObjC，字母O和C大写，符号“-”请勿忽略。
4. 静态库中采用Objective-C++实现，因此需要您保证您工程中至少有一个.mm后缀的源文件( 您可以将任意一个.m后缀的文件改名为.mm )。
5. 打开 `（TODO: 文件的具体路径）`，进行如下修改：
	- 将 `<YOUR_APP_ID>` 替换为你的 App ID。获取 App ID 请参考[开始使用 Agora 平台](https://docs.agora.io/cn/Agora%20Platform/get_appid_token?platform=All%20Platforms)。
	- 将 `<YOUR_APP_KEY>` 和 `<YOUR_APP_SECRET>` 分别替换为你的 `appKey` 和 `appSecret`。获取方式详见[购买和激活插件](https://docs.agora.io/cn/extension_customer/get_extension?platform=All%20Platforms)。
5. 执行 初始化 + 启用插件 步骤
6. 连接一台 iOS 真机（非模拟器），运行项目。




### 预期效果

运行成功后，示例项目会安装到你的 Android 或 iOS 设备上。

1. 启动 app，你可以在界面上看到 `disableExtension` 和 `Start ASR` 按钮
2. 点击 `Start RTVT` 开始语音识别。
3. 点击 `End ASR` 结束语音识别。
-->

## Sample Project

The complete sample code and project is provided on GitHub:
| Platform    | Language    | Sample Project   |
| :------ | :---------- | :--------------|
| Android | Java        | [Sample Project](https://github.com/highras/rtvt-agora-marketplace/blob/master/Android/RTVTRTAUFilter/app/src/main/java/io/agora/api/example/MainActivity.java) |
| iOS     | Objective-C | [Sample Project](https://github.com/highras/rtvt-agora-marketplace/tree/master/iOS/SW_Test/SW_Test.xcworkspace) |

### Run the project

#### Android

1. git clone:
```shell
git clone https://github.com/highras/rtvt-agora-marketplace.git
```
2. Open the Sample project in Android Studio.
3. Gradle sync with the project.
4. Open `app/src/main/res/values/string_configs.xml` file:
    - change `agora_app_id` and `agora_access_token` to your own Agora project information.
    - change `livedata_translate_pid` and `livedata_translate_key` to your own LiveData RTVT extension information。
5. Using a physical Android device (not an emulator) to run the project.

#### iOS

1. git clone:
```shell
git clone https://github.com/highras/rtvt-agora-marketplace.git
```
2. Open the project in Xcode. Access to the `iOS/SW_Test` path, and run the CocoaPods command as below:
```shell
pod install
```
3. Open the sample project `SW_Test.xcworkspace` in Xcode.
4. Open `iOS/SW_Test/SW_Test/ViewController.mm` file:
    - Fill in you own project information `appId` and `Token` from Agora console.
    - Fill in your own LiveData RTVT extension information `appKeyRTVT` and `appSecretRTVT` from Agora console。
5. Using a physical iOS device (not an emulator) to run the project.

### Result

After running the project successfully, LiveData RTVT sample project will be installed on you Android or iOS device.

1. Start the sample, fill in the channel number in the input box and click `Join`. The button `Join` will be change to `Leave` after join the channel successfully.
2. Click the button `start translation`, speak to the device microphone, and you will see the real-time transcription and translation on the screen.
3. Click the button `stop translation` to stop transcription and translation.
4. Click the button `stop`to stop running LiveData RTVT sample project.


## API reference

This section lists the APIs related to using extensions with the Agora SDK.

### Android

- [addExtension](https://api-ref.agora.io/en/video-sdk/android/4.x/API/rtc_api_data_type.html#api_irtcengine_addextension) in the `RtcEngineConfig` class
- [enableExtension](https://api-ref.agora.io/en/video-sdk/android/4.x/API/class_irtcengine.html#api_irtcengine_enableextension) in the `RtcEngine` class
- [setExtensionProperty](https://api-ref.agora.io/en/video-sdk/android/4.x/API/class_irtcengine.html#api_irtcengine_setextensionproperty) in the `RtcEngine` class
- [getExtensionProperty](https://api-ref.agora.io/en/video-sdk/android/4.x/API/class_irtcengine.html#api_irtcengine_getextensionproperty) in the `RtcEngine` class
- [onEvent](https://api-ref.agora.io/en/video-sdk/android/4.x/API/class_imediaextensionobserver.html#callback_irtcengineeventhandler_onextensionevent) in the `IMediaExtensionObserver` class


### iOS

- [enableExtensionWithVendor](https://api-ref.agora.io/en/video-sdk/ios/4.x/API/class_irtcengine.html#api_irtcengine_enableextension) in the `AgoraRtcEngineKit` class
- [setExtensionPropertyWithVendor](https://api-ref.agora.io/en/video-sdk/ios/4.x/API/class_irtcengine.html#api_irtcengine_setextensionproperty) in the `AgoraRtcEngineKit` class
- [getExtensionPropertyWithVendor](https://api-ref.agora.io/en/video-sdk/ios/4.x/API/class_irtcengine.html#api_irtcengine_getextensionproperty) in the `AgoraRtcEngineKit` class
- [onEvent](https://api-ref.agora.io/en/video-sdk/ios/4.x/API/class_imediaextensionobserver.html#callback_irtcengineeventhandler_onextensionevent) in the `AgoraMediaFilterEventDelegate` class

### Key description
To implement the LiveData RTVT extension in app, you need to pass in the corresponding key-value pair。

#### `setExtensionProperty` or `setExtensionPropertyWithVendor`
When calling `setExtensionProperty` or `setExtensionPropertyWithVendor`, you can pass keys:

| Key    | Description  |
| ------ | ---------- |
| [startAudioTranslation]() | start real-time voice transcription and translation   |
| [closeAudioTranslation]()     | stop real-time voice transcription and translation |

#### `onEvent`
The Agora SDK `onEvent` contain keys:

| Key    | Description  |
| ------ | ---------- |
| [recognizeResult]() | result of real-time voice transcription   |
| [translateResult]()    | result of real-time voice translation |
| [start]()   | error message when calling `startAudioTranslation`  |

### Key-value description

#### startAudioTranslation
| Value    | Description  |
| ------ | ---------- |
| appKey | buy and activate the extension project on Agora Console, then click View in the Secret column   |
| appSecret    | buy and activate the extension project on Agora Console, then click View in the Secret column. |
| srcLanguage   | source language，local language    |
| destLanguage   | destination language，translate language   |

#### closeAudioTranslation
| Value    | Description  |
| ------ | ---------- |
| end | stop real-time voice transcription and translation  |

#### recognizeResult
| Value    | Description  |
| ------ | ---------- |
| recognizeResult | result of real-time voice transcription  |

#### translateResult
| Value    | Description  |
| ------ | ---------- |
| translateResult | result of real-time voice translation  |

#### start
| Value    | Description  |
| ------ | ---------- |
| start | error message when calling `startAudioTranslation`  |
