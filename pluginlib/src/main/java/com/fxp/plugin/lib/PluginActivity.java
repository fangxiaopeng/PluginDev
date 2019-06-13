package com.fxp.plugin.lib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Title:       PluginActivity
 * <p>
 * Package:     com.fxp.plugin.lib
 * <p>
 * Author:      fxp
 * <p>
 * Create at:   2019-06-12 16:12
 * <p>
 * Description: 插件Apk中Activity继承此类，方可插件化调起
 * 如果是插件Apk内部调起，则走默认生命周期；如果是外部插件化调起，则不走默认生命周期；
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
public class PluginActivity extends AppCompatActivity implements IPlugin {

    private int launchFrom = LAUNCH_FROM_INTERNAL;

    /**
     * 作为插件的上下文使用
     */
    protected Activity proxyActivity;

    /**
     * attach context
     *
     * @param proxyActivity
     */
    @Override
    public void attach(Activity proxyActivity) {
        this.proxyActivity = proxyActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            launchFrom = savedInstanceState.getInt("launch_from");
        }
        if (launchFrom == LAUNCH_FROM_INTERNAL){
            super.onCreate(savedInstanceState);
            proxyActivity = this;
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (launchFrom == LAUNCH_FROM_INTERNAL){
            super.setContentView(layoutResID);
        } else {
            proxyActivity.setContentView(layoutResID);
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if (launchFrom == LAUNCH_FROM_INTERNAL){
            return super.findViewById(id);
        } else {
            return proxyActivity.findViewById(id);
        }
    }

    @Override
    public void onStart() {
        if (launchFrom == LAUNCH_FROM_INTERNAL){
            super.onStart();
        }
    }

    @Override
    public void onRestart() {
        if (launchFrom == LAUNCH_FROM_INTERNAL){
            super.onRestart();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (launchFrom == LAUNCH_FROM_INTERNAL){
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        if (launchFrom == LAUNCH_FROM_INTERNAL){
            super.onResume();
        }
    }

    @Override
    public void onPause() {
        if (launchFrom == LAUNCH_FROM_INTERNAL){
            super.onPause();
        }
    }

    @Override
    public void onStop() {
        if (launchFrom == LAUNCH_FROM_INTERNAL){
            super.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (launchFrom == LAUNCH_FROM_INTERNAL){
            super.onDestroy();
        }
    }
}
