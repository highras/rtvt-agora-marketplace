

## 声网 SDK 的 API 参考

本节提供声网 SDK 中与使用插件相关的 API 参考。

### Java

- `RtcEngineConfig` 类的 [addExtension](https://docs.agora.io/cn/video-call-4.x-beta/API%20Reference/java_ng/API/rtc_api_data_type.html#api_addextension)
- `RtcEngine` 类的 [enableExtension](https://docs.agora.io/cn/video-call-4.x-beta/API%20Reference/java_ng/API/class_irtcengine.html#api_enableextension)
- `RtcEngine` 类的 [setExtensionProperty](https://docs.agora.io/cn/video-call-4.x-beta/API%20Reference/java_ng/API/class_irtcengine.html#api_setextensionproperty)
- `IMediaExtensionObserver` 类的 [onEvent](https://docs.agora.io/cn/video-call-4.x-beta/API%20Reference/java_ng/API/class_imediaextensionobserver.html#callback_onextensionevent)

### Objective-C

- `AgoraRtcEngineKit` 类的 [enableExtensionWithVendor](https://docs.agora.io/cn/video-call-4.x-beta/API%20Reference/ios_ng/API/class_irtcengine.html#api_enableextension)
- `AgoraRtcEngineKit` 类的 [setExtensionPropertyWithVendor](https://docs.agora.io/cn/video-call-4.x-beta/API%20Reference/ios_ng/API/class_irtcengine.html#api_setextensionproperty)
- `AgoraMediaFilterEventDelegate` 类的 [onEvent](https://docs.agora.io/cn/video-call-4.x-beta/API%20Reference/ios_ng/API/class_imediaextensionobserver.html#callback_onextensionevent)

## 插件的 key 概览 <a name="key-value"></a>

在声网 SDK 中调用插件相关 API 时，需要传入指定的 key 和 value。本节介绍云上曲率实时语音识别&翻译（多语种）插件支持的所有 key。

### 方法 key

调用声网 SDK 的 `setExtensionProperty`/`setExtensionPropertyWithVendor` 方法时，支持传入以下 key：

----------------------------------------
| setExtensionProperty/setExtensionPropertyWithVendor 方法的 key| 描述 |
| ------------------------------------ | -------- |
| [startAudioTranslation](#startAudioTranslation) | 开始识别+翻译 |
| [closeAudioTranslation](#closeAudioTranslation) | 结束识别+翻译 |
----------------------------------------


### 回调 key

声网 SDK 的 `onEvent` 回调可能包括以下 key：
----------------------------------------
|onEvent 回调的 key| 描述 |
| ---------------------------- | -------------- |
| [recognizeResult](#recognizeResult) | 识别结果 |
| [translateResult](#translateResult) | 翻译结果 |
| [start](#start)        | 插件启动错误信息 |
----------------------------------------

##  方法 key 的 value 说明

### startAudioTranslation
----------------------------------------
value 包含以下参数：
| value 参数| 描述 |
| ----------------- | ----------------------- |
| `appKey`       | app标识 |
| `appSecret`    | app加密秘钥 |
| `srcLanguage`  | 源语言 |
| `destLanguage` | 目标语言 |
----------------------------------------


### closeAudioTranslation
----------------------------------------
value 包含以下参数：
| value 参数| 描述 |
| ----------------- | ----------------------- |
| `end` | 结束 |
----------------------------------------


##  回调 key 的 value 说明

### recognizeResult

value 包含以下参数：

| value 参数 | 描述|
| ----------- | ---------------------- |
| `recognizeResult` | 语音识别结果 |


### translateResult

value 包含以下参数：

| value 参数 | 描述|
| ----------- | ---------------------- |
| `translateResult` | 语音翻译结果 |

### start

value 包含以下参数：

| value 参数 | 描述|
| ----------- | ---------------------- |
| `start` | 启动插件错误信息 对应方法startAudioTranslation |
