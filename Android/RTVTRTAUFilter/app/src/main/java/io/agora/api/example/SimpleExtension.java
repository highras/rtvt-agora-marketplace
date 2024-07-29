package io.agora.api.example;

import static io.agora.rtc2.Constants.CLIENT_ROLE_BROADCASTER;
import static io.agora.rtc2.Constants.REMOTE_AUDIO_STATE_STARTING;
import static io.agora.rtc2.Constants.RENDER_MODE_HIDDEN;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.agora.api.example.utils.CommonUtil;
import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.ExtensionInfo;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.video.VideoCanvas;

public class SimpleExtension extends AppCompatActivity implements View.OnClickListener, io.agora.rtc2.IMediaExtensionObserver {
    private static final String TAG = SimpleExtension.class.getSimpleName();
    public static final String EXTENSION_LIBRARY_RTVT_POST = "agora-iLiveData-filter-post";
    public static final String EXTENSION_LIBRARY_RTVT_PRE = "agora-iLiveData-filter-pre";
    public static final String EXTENSION_VENDOR_NAME_PRE = "iLiveDataPre";
    public static final String EXTENSION_VENDOR_NAME_POST = "iLiveDataPost";
    public static final String EXTENSION_RTVT_FILTER_POST = "RTVT_POST";
    public static final String EXTENSION_RTVT_FILTER_PRE = "RTVT_PRE";

    private FrameLayout local_view;
    private EditText et_channel;
    private Button join;
    private RtcEngine engine;
    private int myUid = 789;
    int remoteUid = 999;
    String joinchannel = "";
    private boolean joined = false;
    ListView rtvttestview;
    Context mycontext = this;
    protected Handler handler;
    ArrayAdapter srcadapter;
    ArrayList<String> srcarrayList = new ArrayList<>();

    private AlertDialog mAlertDialog;
    private String mAlertDialogMsg;

    String agora_app_id;
    String agora_access_token;
    long livedata_translate_pid;
    String livedata_translate_key;
    String livedata_translate_srclang;
    String livedata_translate_dstlang;


    void addlog(String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                srcarrayList.add(msg);
                srcadapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setContentView(R.layout.fragment_extension);
        join = findViewById(R.id.btn_join);

        findViewById(R.id.starttrans).setOnClickListener(this);
        findViewById(R.id.stoptrans).setOnClickListener(this);

        et_channel = findViewById(R.id.et_channel);
        findViewById(R.id.btn_join).setOnClickListener(this);
        local_view = findViewById(R.id.fl_local);
        rtvttestview = findViewById(R.id.rtvttest);
        srcadapter = new MyAdapter(this, android.R.layout.simple_list_item_1, srcarrayList);
        rtvttestview.setAdapter(srcadapter);

        agora_app_id = getString(R.string.agora_app_id);
        agora_access_token = getString(R.string.agora_access_token);
        String slivedata_translate_pid = getString(R.string.livedata_translate_pid);
        if (slivedata_translate_pid.isEmpty())
            livedata_translate_pid = 0;
        else
            livedata_translate_pid = Long.parseLong(slivedata_translate_pid);
        livedata_translate_key = getString(R.string.livedata_translate_key);

        livedata_translate_srclang = getString(R.string.livedata_translate_srclang);
        livedata_translate_dstlang = getString(R.string.livedata_translate_dstlang);

        try {
            RtcEngineConfig config = new RtcEngineConfig();
            /**
             * The context of Android Activity
             */
            config.mContext = this.getApplicationContext();
            /**
             * The App ID issued to you by Agora. See <a href="https://docs.agora.io/en/Agora%20Platform/token#get-an-app-id"> How to get the App ID</a>
             */
            config.mAppId = getString(R.string.agora_app_id);
            if (config.mAppId.isEmpty()){
                showAlert("Please configure the agora appid");
                return;
            }
            /** Sets the channel profile of the Agora RtcEngine.
             CHANNEL_PROFILE_COMMUNICATION(0): (Default) The Communication profile.
             Use this profile in one-on-one calls or group calls, where all users can talk freely.
             CHANNEL_PROFILE_LIVE_BROADCASTING(1): The Live-Broadcast profile. Users in a live-broadcast
             channel have a role as either broadcaster or audience. A broadcaster can both send and receive streams;
             an audience can only receive streams.*/
            config.mChannelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
            /**
             * IRtcEngineEventHandler is an abstract class providing default implementation.
             * The SDK uses this class to report to the app on SDK runtime events.
             */
            //Name of dynamic link library is provided by plug-in vendor,
            //e.g. libagora-bytedance.so whose EXTENSION_NAME should be "agora-bytedance"
            //and one or more plug-ins can be added
            int ret = 0;

            config.addExtension(EXTENSION_LIBRARY_RTVT_POST);
            config.addExtension(EXTENSION_LIBRARY_RTVT_PRE);

            config.mExtensionObserver = this;
            config.mEventHandler = iRtcEngineEventHandler;
            engine = RtcEngine.create(config);
            if (engine == null) {
                Log.e("sdktest", "engine is null");
                return;
            }

            if (!AndPermission.hasPermissions(this, Permission.Group.STORAGE, Permission.Group.MICROPHONE, Permission.Group.CAMERA)) {

                // Request permission
                AndPermission.with(this).runtime().permission(
                        Permission.Group.STORAGE,
                        Permission.Group.MICROPHONE
//                        Permission.Group.CAMERA
                ).onGranted(permissions ->
                {
                    int result = engine.enableExtension(EXTENSION_VENDOR_NAME_PRE, EXTENSION_RTVT_FILTER_PRE, true);
                    if (result <0){
                        showAlert("enableExtension error:" +ret + " " + EXTENSION_RTVT_FILTER_PRE );
                        return;
                    }

                    result = engine.enableExtension(EXTENSION_VENDOR_NAME_POST, EXTENSION_RTVT_FILTER_POST, true);
                    if (result <0){
                        showAlert("enableExtension error:" +ret + " " + EXTENSION_RTVT_FILTER_POST );
                        return;
                    }
/*                    engine.enableVideo();
                    TextureView textureView = new TextureView(this);
                    if(local_view.getChildCount() > 0)
                    {
                        local_view.removeAllViews();
                    }
                    // Add to the local container
                    local_view.addView(textureView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    // Setup local video to render your local camera preview
                    engine.setupLocalVideo(new VideoCanvas(textureView, RENDER_MODE_HIDDEN, 0));
                    engine.startPreview();*/
                }).start();
            }
            else{
                int result = engine.enableExtension(EXTENSION_VENDOR_NAME_PRE, EXTENSION_RTVT_FILTER_PRE, true);
                if (result <0){
                    showAlert("enableExtension error:" +ret + " " + EXTENSION_RTVT_FILTER_PRE );
                    return;
                }

                result = engine.enableExtension(EXTENSION_VENDOR_NAME_POST, EXTENSION_RTVT_FILTER_POST, true);
                if (result <0){
                    showAlert("enableExtension error:" +ret + " " + EXTENSION_RTVT_FILTER_POST );
                    return;
                }
/*                engine.enableVideo();
                TextureView textureView = new TextureView(this);
                if(local_view.getChildCount() > 0)
                {
                    local_view.removeAllViews();
                }
                local_view.addView(textureView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                engine.setupLocalVideo(new VideoCanvas(textureView, RENDER_MODE_HIDDEN, 0));
                engine.startPreview();*/
            }



//            initMediaPlayer();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.onBackPressed();
        }
    }



    protected void showAlert(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAlertDialog == null) {
                    mAlertDialog = new AlertDialog.Builder(mycontext).setTitle("Tips")
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                            .create();
                }
                if (!message.equals(mAlertDialogMsg)) {
                    mAlertDialogMsg = message;
                    mAlertDialog.setMessage(mAlertDialogMsg);
                    mAlertDialog.show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Class obj = null;
        if(v.getId() == R.id.starttrans){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("srcLang", livedata_translate_srclang);
                jsonObject.put("dstLang", livedata_translate_dstlang);
                jsonObject.put("asrResult", true);
                jsonObject.put("transResult", true);
                jsonObject.put("tempResult", true);
                jsonObject.put("appKey", livedata_translate_pid);
                jsonObject.put("appSecret", livedata_translate_key);
                jsonObject.put("userId", "peter-1234");

//                jsonObject.put("userId", "1234567");

/*                JSONArray array = new JSONArray();
                array.put("en");
                array.put("es");
                array.put("pt");
                jsonObject.put("srcAltLanguage", array);*/
//                jsonObject.put("srcAltLanguage", null);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            int ret  = engine.setExtensionProperty(EXTENSION_VENDOR_NAME_PRE, EXTENSION_RTVT_FILTER_PRE,"startAudioTranslation_pre", jsonObject.toString());
            if (ret < 0){
                showAlert("startAudioTranslation error ret:" + ret);
                return;
            }
            showShortToast("Start Translation");
        }else if(v.getId() == R.id.stoptrans){
            showShortToast("End Translation");
            engine.setExtensionProperty(EXTENSION_VENDOR_NAME_PRE, EXTENSION_RTVT_FILTER_PRE, "closeAudioTranslation_pre", "{}");
        }
        else if (v.getId() == R.id.btn_join) {
            if (engine == null){
                showAlert("Please configure the agora appid and key");
                return;
            }
            if (!joined) {
                CommonUtil.hideInputBoard(this, et_channel);
                // call when join button hit
                String channelId = et_channel.getText().toString();
                // Check permission
                if (AndPermission.hasPermissions(this, Permission.Group.STORAGE, Permission.Group.MICROPHONE)) {
                    joinChannel(channelId);
                    return;
                }
                // Request permission
                AndPermission.with(this).runtime().permission(
                        Permission.Group.STORAGE,
                        Permission.Group.MICROPHONE
                ).onGranted(permissions ->
                {
                    // Permissions Granted
                    joinChannel(channelId);
                }).start();
            } else {
                joined = false;
                /**After joining a channel, the user must call the leaveChannel method to end the
                 * call before joining another channel. This method returns 0 if the user leaves the
                 * channel and releases all resources related to the call. This method call is
                 * asynchronous, and the user has not exited the channel when the method call returns.
                 * Once the user leaves the channel, the SDK triggers the onLeaveChannel callback.
                 * A successful leaveChannel method call triggers the following callbacks:
                 *      1:The local client: onLeaveChannel.
                 *      2:The remote client: onUserOffline, if the user leaving the channel is in the
                 *          Communication channel, or is a BROADCASTER in the Live Broadcast profile.
                 * @returns 0: Success.
                 *          < 0: Failure.
                 * PS:
                 *      1:If you call the destroy method immediately after calling the leaveChannel
                 *          method, the leaveChannel process interrupts, and the SDK does not trigger
                 *          the onLeaveChannel callback.
                 *      2:If you call the leaveChannel method during CDN live streaming, the SDK
                 *          triggers the removeInjectStreamUrl method.*/
                engine.leaveChannel();
                join.setText(getString(R.string.join));
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        /**leaveChannel and Destroy the RtcEngine instance*/
        if (engine == null)
            return;
        if (engine != null) {
            engine.leaveChannel();
        }
        engine.enableExtension(EXTENSION_VENDOR_NAME_PRE, EXTENSION_RTVT_FILTER_PRE, false);
        // enable video filter before enable video
        engine.enableExtension(EXTENSION_VENDOR_NAME_POST, EXTENSION_RTVT_FILTER_POST, false);
        handler.post(RtcEngine::destroy);
        engine = null;
    }

    /**
     * @param channelId Specify the channel name that you want to join.
     *                  Users that input the same channel name join the same channel.
     */
    private void joinChannel(String channelId) {
        /**In the demo, the default is to enter as the anchor.*/
        engine.setClientRole(CLIENT_ROLE_BROADCASTER);
        /**Please configure accessToken in the string_config file.
         * A temporary token generated in Console. A temporary token is valid for 24 hours. For details, see
         *      https://docs.agora.io/en/Agora%20Platform/token?platform=All%20Platforms#get-a-temporary-token
         * A token generated at the server. This applies to scenarios with high-security requirements. For details, see
         *      https://docs.agora.io/en/cloud-recording/token_server_java?platform=Java*/
        String accessToken = getString(R.string.agora_access_token);
        if (TextUtils.equals(accessToken, "") || TextUtils.equals(accessToken, "<#YOUR ACCESS TOKEN#>")) {
            accessToken = null;
        }
        engine.enableAudioVolumeIndication(1000, 3, false);
        ChannelMediaOptions option = new ChannelMediaOptions();
        option.autoSubscribeAudio = true;
        option.autoSubscribeVideo = true;
        int res = engine.joinChannel(accessToken, channelId, (int)(System.currentTimeMillis()/1000), option);
        if (res != 0) {
            // Usually happens with invalid parameters
            // Error code description can be found at:
            // en: https://docs.agora.io/en/Voice/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_i_rtc_engine_event_handler_1_1_error_code.html
            // cn: https://docs.agora.io/cn/Voice/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_i_rtc_engine_event_handler_1_1_error_code.html
            showAlert(RtcEngine.getErrorDescription(Math.abs(res)));
            Log.e(TAG, RtcEngine.getErrorDescription(Math.abs(res)));
            return;
        }
        // Prevent repeated entry
        join.setEnabled(false);
    }

    void showShortToast(final String msg)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mycontext, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * IRtcEngineEventHandler is an abstract class providing default implementation.
     * The SDK uses this class to report to the app on SDK runtime events.
     */
    private final IRtcEngineEventHandler iRtcEngineEventHandler = new IRtcEngineEventHandler() {
        /**Occurs when a user leaves the channel.
         * @param stats With this callback, the application retrieves the channel information,
         *              such as the call duration and statistics.*/
        @Override
        public void onLeaveChannel(RtcStats stats) {
            super.onLeaveChannel(stats);
            Log.i(TAG, String.format("local user %d leaveChannel!", myUid));
            showShortToast(String.format("local user %d leaveChannel!", myUid));
        }

        /**Occurs when the local user joins a specified channel.
         * The channel name assignment is based on channelName specified in the joinChannel method.
         * If the uid is not specified when joinChannel is called, the server automatically assigns a uid.
         * @param channel Channel name
         * @param uid User ID
         * @param elapsed Time elapsed (ms) from the user calling joinChannel until this callback is triggered*/
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            Log.i(TAG, String.format("onJoinChannelSuccess channel %s uid %d", channel, uid));
            showShortToast(String.format("onJoinChannelSuccess channel %s uid %d", channel, uid));
            myUid = uid;
            joinchannel = channel;
            joined = true;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    join.setEnabled(true);
                    join.setText(getString(R.string.leave));
                }
            });
        }

        @Override
        public void onRemoteAudioStateChanged(int uid, int state, int reason, int elapsed) {
            super.onRemoteAudioStateChanged(uid, state, reason, elapsed);
            if (state == REMOTE_AUDIO_STATE_STARTING){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("srclang", "zh");
                    jsonObject.put("dstLang", "en");
                    jsonObject.put("asrResult", true);
                    jsonObject.put("transResult", true);
                    jsonObject.put("tempResult", true);
                    jsonObject.put("userId",  "translate-"+remoteUid);
                    jsonObject.put("appKey", String.valueOf(livedata_translate_pid));
                    jsonObject.put("appSecret", livedata_translate_key);
//                    JSONArray array = new JSONArray();
//                    array.put("en");
//                    array.put("es");
//                    array.put("pt");
//                    jsonObject.put("srcAltLanguage", array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ExtensionInfo extensionInfo = new ExtensionInfo();
                extensionInfo.localUid = myUid;
                extensionInfo.channelId = joinchannel;
                extensionInfo.remoteUid = remoteUid;

                int ret = engine.setExtensionProperty(EXTENSION_VENDOR_NAME_POST, EXTENSION_RTVT_FILTER_POST, extensionInfo,"startAudioTranslation_post", jsonObject.toString());
                if (ret != 0){
                    Log.e("sdktest","strart remote translation setExtensionProperty failed:" + ret);
                    return;
                }

                showShortToast("Start Translation");
                Log.i("sdktest", "startAudioTranslation ret:" + ret);
            }
        }
        /**Occurs when a remote user (Communication)/host (Live Broadcast) joins the channel.
         * @param uid ID of the user whose audio state changes.
         * @param elapsed Time delay (ms) from the local user calling joinChannel/setClientRole
         *                until this callback is triggered.*/
        @Override
        public void onUserJoined(int uid, int elapsed) {
            super.onUserJoined(uid, elapsed);
            remoteUid = uid;
//            Log.i(TAG, "onUserJoined->" + uid);
            Log.i("sdktest", "user joined!" + uid);


            showShortToast(String.format("user %d joined!", uid));//            Log.i(TAG, "onUserJoined->" + uid);
//            showToast(String.format("user %d joined!", uid));
//            /**Check if the context is correct*/
//            handler.post(() ->
//            {
//                if(remote_view.getChildCount() > 0){
//                    remote_view.removeAllViews();
//                }
//                /**Display remote video stream*/
//                TextureView textureView = null;
//                // Create render view by RtcEngine
//                textureView = new TextureView(mycontext);
//                // Add to the remote container
//                remote_view.addView(textureView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                // Setup remote video to render
//                engine.setupRemoteVideo(new VideoCanvas(textureView, RENDER_MODE_HIDDEN, uid));
//            });
        }

        /**Occurs when a remote user (Communication)/host (Live Broadcast) leaves the channel.
         * @param uid ID of the user whose audio state changes.
         * @param reason Reason why the user goes offline:
         *   USER_OFFLINE_QUIT(0): The user left the current channel.
         *   USER_OFFLINE_DROPPED(1): The SDK timed out and the user dropped offline because no data
         *              packet was received within a certain period of time. If a user quits the
         *               call and the message is not passed to the SDK (due to an unreliable channel),
         *               the SDK assumes the user dropped offline.
         *   USER_OFFLINE_BECOME_AUDIENCE(2): (Live broadcast only.) The client role switched from
         *               the host to the audience.*/
        @Override
        public void onUserOffline(int uid, int reason) {
            Log.i(TAG, String.format("user %d offline! reason:%d", uid, reason));
            showShortToast(String.format("user %d offline! reason:%d", uid, reason));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    /**Clear render view
                     Note: The video will stay at its last frame, to completely remove it you will need to
                     remove the SurfaceView from its parent*/
                    engine.setupRemoteVideo(new VideoCanvas(null, RENDER_MODE_HIDDEN, uid));
                }
            });
        }

        @Override
        public void onActiveSpeaker(int uid) {
            super.onActiveSpeaker(uid);
            Log.i(TAG, String.format("onActiveSpeaker:%d", uid));
        }
    };


    @Override
    public void onEvent(String vendor, String extension, String key, String value) {
        if (vendor.equals("iLiveData"))
            if (vendor.equals("iLiveData")) {
                try {
                    JSONObject jj = new JSONObject(value);
                    String result = jj.getString("result");
                    String startTs = jj.getString("startTs");
                    String endTs = jj.getString("endTs");
                    String recTs = jj.getString("recTs");
                    String msg =key +  " startTs:" + startTs + " endTs:"+ endTs + " recTs:"+ recTs + " result:" +result;
                    addlog(vendor + " " + extension+ " " + " " + key + " " + value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
    }


    @Override
    public void onStarted(String s, String s1) {

    }

    @Override
    public void onStopped(String s, String s1) {

    }

    @Override
    public void onError(String s, String s1, int i, String s2) {

    }
}
