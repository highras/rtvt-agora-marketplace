package io.agora.api.example;

import static io.agora.rtc2.Constants.CLIENT_ROLE_BROADCASTER;
import static io.agora.rtc2.Constants.RENDER_MODE_HIDDEN;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SeekBar;
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
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.video.VideoCanvas;

public class SimpleExtension extends AppCompatActivity implements View.OnClickListener, io.agora.rtc2.IMediaExtensionObserver {
    private static final String TAG = SimpleExtension.class.getSimpleName();
    public static final String EXTENSION_NAME = "agora-iLiveData-filter"; // Name of target link library used in CMakeLists.txt
    public static final String EXTENSION_VENDOR_NAME = "iLiveData"; // Provider name used for registering in agora-bytedance.cpp
    public static final String EXTENSION_VIDEO_FILTER_WATERMARK = "RTAU"; // Video filter name defined in LiveDataExtensionProvider.h
    public static final String EXTENSION_AUDIO_FILTER_VOLUME = "RTVT"; // Audio filter name defined in LiveDataExtensionProvider.h
    private FrameLayout local_view;
    private EditText et_channel;
    private Button join;
    private RtcEngine engine;
    private int myUid;
    private boolean joined = false;
    private SeekBar record;
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
    long livedata_audit_pid;
    String livedata_audit_key;
    String livedata_callbackUrl;
    String livedata_translate_srclang;
    String livedata_translate_dstlang;
    String livedata_audit_lang;


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

        findViewById(R.id.startaudit).setOnClickListener(this);
        findViewById(R.id.closeAudit).setOnClickListener(this);
        findViewById(R.id.starttrans).setOnClickListener(this);
        findViewById(R.id.stoptrans).setOnClickListener(this);

        et_channel = findViewById(R.id.et_channel);
        findViewById(R.id.btn_join).setOnClickListener(this);
        record = findViewById(R.id.recordingVol);
        record.setEnabled(false);
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

        String slivedata_audit_pid = getString(R.string.livedata_audit_pid);
        if (slivedata_audit_pid.isEmpty())
            livedata_audit_pid = 0;
        else
            livedata_audit_pid = Long.parseLong(slivedata_audit_pid);
        livedata_audit_key = getString(R.string.livedata_audit_key);
        livedata_callbackUrl = getString(R.string.livedata_callbackUrl);

        livedata_translate_srclang = getString(R.string.livedata_translate_srclang);
        livedata_translate_dstlang = getString(R.string.livedata_translate_dstlang);
        livedata_audit_lang = getString(R.string.livedata_audit_lang);

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
            config.addExtension(EXTENSION_NAME);
            config.mExtensionObserver = this;
            config.mEventHandler = iRtcEngineEventHandler;
            engine = RtcEngine.create(config);
            /**
             * Enable/Disable extension.
             *
             * @param id id for extension, e.g. agora.beauty.
             * @param enable enable or disable.
             * - true: enable.
             * - false: disable.
             *
             * @return
             * - 0: Success.
             * - < 0: Failure.
             */
            int ret = engine.enableExtension(EXTENSION_VENDOR_NAME, EXTENSION_AUDIO_FILTER_VOLUME, true);
            // enable video filter before enable video

            Log.i("sdktest", "ret is " + ret);
            ret = engine.enableExtension(EXTENSION_VENDOR_NAME, EXTENSION_VIDEO_FILTER_WATERMARK, true);
            // enable video filter before enable video

            Log.i("sdktest", "ret is " + ret);

            if (!AndPermission.hasPermissions(this, Permission.Group.STORAGE, Permission.Group.MICROPHONE, Permission.Group.CAMERA)) {

                // Request permission
                AndPermission.with(this).runtime().permission(
                        Permission.Group.STORAGE,
                        Permission.Group.MICROPHONE,
                        Permission.Group.CAMERA
                ).onGranted(permissions ->
                {
                    engine.enableVideo();
                    TextureView textureView = new TextureView(this);
                    if(local_view.getChildCount() > 0)
                    {
                        local_view.removeAllViews();
                    }
                    // Add to the local container
                    local_view.addView(textureView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    // Setup local video to render your local camera preview
                    engine.setupLocalVideo(new VideoCanvas(textureView, RENDER_MODE_HIDDEN, 0));
                    engine.startPreview();
                }).start();
            }
            else{
                engine.enableVideo();
                TextureView textureView = new TextureView(this);
                if(local_view.getChildCount() > 0)
                {
                    local_view.removeAllViews();
                }
                local_view.addView(textureView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                engine.setupLocalVideo(new VideoCanvas(textureView, RENDER_MODE_HIDDEN, 0));
                engine.startPreview();
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
        if (v.getId() == R.id.starttrans) {
            JSONObject jsonObject = new JSONObject();
            try {
//                    Log.i("sdktest", "java token is " + ApiSecurityExample.genToken(80001000,"qwerty"));
                String spid  = getString(R.string.livedata_translate_pid);
                if (spid.isEmpty()){
                    showAlert("Please configure the project ID for real-time translation");
                    return;
                }
                long pid = Long.parseLong(spid);

                String skey  = getString(R.string.livedata_translate_key);
                if (skey.isEmpty()){
                    showAlert("Please configure the key for real-time translation");
                    return;
                }

                jsonObject.put("srclang", livedata_translate_srclang);
                jsonObject.put("dstLang", livedata_translate_dstlang);

                jsonObject.put("asrResult", true);
                jsonObject.put("asrTempResult", false);
                jsonObject.put("transResult", true);
                jsonObject.put("appKey", livedata_translate_pid);
                jsonObject.put("appSecret", livedata_translate_key);
                jsonObject.put("userId", "1234567");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            int ret  = engine.setExtensionProperty(EXTENSION_VENDOR_NAME, EXTENSION_AUDIO_FILTER_VOLUME, "startAudioTranslation", jsonObject.toString());
            if (ret < 0){
                showAlert("startAudioTranslation error ret:" + ret);
                return;
            }
            Toast.makeText(this, "Start Translation", Toast.LENGTH_SHORT).show();
        }
        else if (v.getId() == R.id.stoptrans){
            Toast.makeText(this, "Stop Translation", Toast.LENGTH_SHORT).show();
            engine.setExtensionProperty(EXTENSION_VENDOR_NAME, EXTENSION_AUDIO_FILTER_VOLUME, "closeAudioTranslation", "{}");
        }
        else if (v.getId() == R.id.startaudit){

            String spid  = getString(R.string.livedata_audit_pid);
            if (spid.isEmpty()){
                showAlert("Please configure the project ID for audit");
                return;
            }
            long pid = Long.parseLong(spid);

            if (livedata_audit_key.isEmpty()){
                showAlert("Please configure the key for audit");
                return;
            }

            JSONObject jsonObject = new JSONObject();
            try {
                ArrayList<String> attrs = new ArrayList<String>(){{add("1");add("2");}};
                jsonObject.put("streamId", String.valueOf(System.currentTimeMillis()));
                jsonObject.put("callbackUrl", livedata_callbackUrl);
                jsonObject.put("audioLang", livedata_audit_lang);
                jsonObject.put("appKey", livedata_audit_pid);
                jsonObject.put("appSecret", livedata_audit_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            int ret = engine.setExtensionProperty(EXTENSION_VENDOR_NAME, EXTENSION_VIDEO_FILTER_WATERMARK, "startAudit", jsonObject.toString());
            if (ret != 0 ){
                showAlert("setExtensionProperty startAudit error " + ret);
                return;
            }
            Toast.makeText(this, "Start Audit", Toast.LENGTH_SHORT).show();
            Log.i("sdktest","Start Audit " + ret);
        }
        else if (v.getId() == R.id.closeAudit){
            Toast.makeText(this, "End Audit", Toast.LENGTH_SHORT).show();
            int ret = engine.setExtensionProperty(EXTENSION_VENDOR_NAME, EXTENSION_VIDEO_FILTER_WATERMARK, "closeAudit", "{}");
            Log.i("sdktest","setExtensionProperty closeAudit " + ret);
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
                record.setEnabled(false);
                record.setProgress(0);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        /**leaveChannel and Destroy the RtcEngine instance*/
        if (engine != null) {
            engine.leaveChannel();
        }
        engine.enableExtension(EXTENSION_VENDOR_NAME, EXTENSION_AUDIO_FILTER_VOLUME, false);
        // enable video filter before enable video
        engine.enableExtension(EXTENSION_VENDOR_NAME, EXTENSION_VIDEO_FILTER_WATERMARK, false);
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


    void showToast(final String msg)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mycontext, msg, Toast.LENGTH_LONG).show();
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
            showToast(String.format("local user %d leaveChannel!", myUid));
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
            showToast(String.format("onJoinChannelSuccess channel %s uid %d", channel, uid));
            myUid = uid;
            joined = true;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    join.setEnabled(true);
                    join.setText(getString(R.string.leave));
                    record.setEnabled(true);
                    record.setProgress(100);
                }
            });
        }

        /**Occurs when a remote user (Communication)/host (Live Broadcast) joins the channel.
         * @param uid ID of the user whose audio state changes.
         * @param elapsed Time delay (ms) from the local user calling joinChannel/setClientRole
         *                until this callback is triggered.*/
        @Override
        public void onUserJoined(int uid, int elapsed) {
            super.onUserJoined(uid, elapsed);
//            Log.i(TAG, "onUserJoined->" + uid);
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
            showToast(String.format("user %d offline! reason:%d", uid, reason));
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
            addlog(key + " " + value);
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
