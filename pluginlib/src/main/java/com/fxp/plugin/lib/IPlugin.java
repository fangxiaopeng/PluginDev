package com.fxp.plugin.lib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Title:       IPlugin
 * <p>
 * Package:     com.fxp.plugin.lib
 * <p>
 * Author:      fxp
 * <p>
 * Create at:   2019-06-12 14:42
 * <p>
 * Description: ProxyActivity 生命周期接口
 *  用于管理插件Apk中Activity 生命周期
 * <p>
 * <p>
 * Modification History:
 * <p>
 * Date       Author       Version      Description
 * -----------------------------------------------------------------
 * 2019-06-12    fxp       1.0         First Created
 * <p>
 * Github:  https://github.com/fangxiaopeng
 */
public interface IPlugin {

    /**
     * 内部跳转
     */
    int LAUNCH_FROM_INTERNAL = 0;

    /**
     * 外部跳转
     */
    int LAUNCH_FROM_EXTERNAL = 1;

    /**
     * attach context
     * @param proxyActivity
     */
    void attach(Activity proxyActivity);

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onRestart();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

}
