using UnityEngine;
using System;
using UnityEngine.UI;

public class GameDataMgr : MonoBehaviour {
    private const string TAG = "Unity GameDataMgr";

    public Button Btn_UnityExit; // 结束：Unity 游戏端活动
    public Button Btn_ShowMainActivityQuit;
    public Button Btn_ShowMainActivityUnload;
    public Text Txt_FormAndroidMessage;
    
    string userData = "用户信息(UseData):Me, 爱亲爱的表哥，活宝妹一定要嫁的亲爱的表哥！！！要嫁给表哥！！";

    private void Awake() {
        DontDestroyOnLoad(this);
        Debug.Log(TAG + " Awake()");
    }

    private void Start() {
        Debug.Log(TAG + " Start()");
        Btn_UnityExit.onClick.AddListener(OnQuitUnity); // 【TODO：】先测这个：crash 掉了。。
        // 点击ShowMainActivityUnload按钮调用
        Btn_ShowMainActivityUnload.onClick.AddListener(() => {
            Debug.Log(TAG + " Btn_ShowMainActivityUnload()");
            CallAndroidMethod("showMainActivity", false, ("UnloadSend:" + userData));
        });
        // 点击ShowMainActivityQuit按钮调用
        Btn_ShowMainActivityQuit.onClick.AddListener(() => {
            Debug.Log(TAG + " Btn_ShowMainActivityQuit()");
            CallAndroidMethod("showMainActivity", true, ("QuitSend:" + userData));
        });
    }

    // 调用UnityGameActivity的方法：这就是最常用的实现方法 
    // <param name="methodName">Android方法名称</param>
    // <param name="isFinish">方法参数1:是否结束UnityGameActivity</param>
    // <param name="data">方法参数2:具体数据</param>
    private void CallAndroidMethod(string methodName, bool isFinish, string data) {
#if UNITY_ANDROID
        try {
            AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
            AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
            jo.Call(methodName, isFinish, data);
        } catch (Exception e) {
            Debug.LogError(e.Message);
        }
#endif
    }

    // Android调用方法 在安卓UnityGameActivity OnCreate调用
    public void sendMessageToUnity(string str) {
        Debug.Log("Unity获取Android MainActivity发送过来的Token信息: " + str);
        Txt_FormAndroidMessage.text = "来自Android MainActivity信息：" + str;
    }

#region Unity程序原生方法
    void OnQuitUnity() { // 【TODO：】通过测试，搞明白：先前，2 年前出现过的，Unity 与安卓活动切换时，游戏进程？被杀，黑屏问题
        // for basic editor mode testing
        Debug.Log(TAG + " OnQuitUnity()");
        // 这里，调用了会 crash: 明明是 com.me.sample 【同一个、唯一一个进程】
        Application.Quit(); // 【TODO：】去确认，这里好像是Unity 游戏进程 UnityPlayerActivity 里结结束自己 finish() 会结束【游戏进程？】然后安卓端就会收到 null-intent
    }
#endregion
}
