package com.me.sample;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

// 进入到这个类就进入了unity游戏画面
public class UnityGameActivity extends UnityPlayerActivity {
    private static final String TAG = "UnityGameActivity";
    private String tokenStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() ");
        GetTokenFromMainActivity();
    }

    // 进入UnityGameActivity就调用sendMessageToUnity给Unity Text组件赋值
    private void GetTokenFromMainActivity(){
        Log.d(TAG, "GetTokenFromMainActivity() ");
        Bundle bundle = getIntent().getExtras(); // 启动这个当前 UnityGameActivity 活动的 Intent
        if (bundle != null)
            tokenStr= bundle.getString("TokenCode");
        sendMessageToUnity(tokenStr);
    }
    // 发送给Unity的数据 Android-》Unity
    private void sendMessageToUnity(String str) {
        Log.d(TAG, "sendMessageToUnity(): str: " + str);
        Log.i(TAG, "Android传给unity的信息: " + str);
        // 其中GameDataMgr是unity生成的gameobject，脚本要挂在上面。AndroidToUnity是unity里实现的方法。str是传过去的值。
        UnityPlayer.UnitySendMessage("UnityGameDataMgr", "sendMessageToUnity", str);
    }
    // 返回MainActivity并将unityData数据传递给MainActivity显示( Unity-》Android)
    // unity 退出应用之前将Unity的数据传递给MainActivity
    // isFinish为false 直接返回MainActivity;当为true,结束此活动返回MainActivity
    public void showMainActivity(boolean isFinish, String unityDataStr) {
        Log.d(TAG, "showMainActivity(): isFinish: " + isFinish);
        Log.d(TAG, "showMainActivity(): unityDataStr: " + unityDataStr);
        if (!isFinish) { // 主要标记：启动新活动后，是否结束当前这个旧活动
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("UnityData", unityDataStr);
            startActivity(intent);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("UnityData", unityDataStr);
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            setResult(1,intent);
            intent.putExtras(bundle);
            finish(); // 结束当前活动
        }
    }
}
