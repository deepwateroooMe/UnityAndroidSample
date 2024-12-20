package com.me.sample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {
    private final String TAG = "MainActivity";

    private String tokenStr = "这是MainActivity 里的tokenStr 数据";
    private Button btn;
    private TextView textView;
    private String unityDataStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        // 注册监听器: 
        btn.setOnClickListener(this::onClickStartGameBtn);
        handleIntent(getIntent()); 
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
        setIntent(intent);
    }

    int i = 0;
    // 按键点击进入UNITY
    private void onClickStartGameBtn(View view) {
        Log.d(TAG, "onClickStartGameBtn(): ");
        System.out.println("触发点击进入游戏的按钮事件");
        Intent intent = new Intent(this, UnityGameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // 同一个任务栈中，重新排序，旧活动重新置顶
        i = i+1; // 模拟点击进入Unity次数 每次点击+1
        intent.putExtra("TokenCode", tokenStr + "{" + i + "}"); // 安卓MainActivity 里点了【进入游戏】就显示 i=1
        // startActivity(intent);
        startActivityForResult(intent, 1); // requestCode >= 0即可
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult(): requestCode: " + requestCode);
        if (requestCode == 1) {
            // Bundle extras = data.getExtras(); // 退出Unity 会 crash, 因为 data=null 
            Bundle extras = null;
            if (data != null) // 有这步最简单的检测，就不会 crash. 可是还是会感觉笨安卓应用真脆弱。。动辄 crash 掉。。。
                extras = data.getExtras();
            if (extras != null) 
                unityDataStr = extras.getString("UnityData");
            textView.setText(unityDataStr);
            System.out.println(unityDataStr);
        }
    }
    // 当UnityActivityGame结束后返回
    // 这个是处理来自UnityGameActivity传过来的信息
    private void handleIntent(Intent intent) {
        Log.d(TAG, "handleIntent() ");
        if (intent == null || intent.getExtras() == null)  return ;
        if (intent.getExtras().containsKey("UnityData")) {
            unityDataStr = intent.getStringExtra("UnityData");
            textView.setText(unityDataStr);
        }
    }
}
