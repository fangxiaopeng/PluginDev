package com.fxp.plugin.lib;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Title:       ProxyActivity
 * <p>
 * Package:     com.fxp.plugin.lib
 * <p>
 * Author:      fxp
 * <p>
 * Create at:   2019-06-12 15:45
 * <p>
 * Description: 插件Apk目标Activity代理类
 * 原理：启动插件Apk的ProxyActivity，传入目标Activity类名路径，然后在ProxyActivity中以反射的方式启动目标activity。
 * 在ProxyActivity的生命周期中同步执行目标activity的生命周期
 * 并管理目标activity的生命周期
 * （插件Apk中Activity不走生命周期，所以需要在代理类中管理）
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
public class ProxyActivity extends AppCompatActivity {

    private String className;

    private int launchFrom;

    private PluginApk pluginApk;

    private IPlugin plugin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        className = getIntent().getStringExtra("className");
        launchFrom = getIntent().getIntExtra("launch_from", IPlugin.LAUNCH_FROM_EXTERNAL);

        pluginApk = PluginManager.getInstance().getPluginApk();

        launchActivity();
    }

    private void launchActivity(){
        if (pluginApk == null){
            throw new RuntimeException("pluginApk is null");
        }
        try {
            // 实例化目标activity
            Class<?> clazz = pluginApk.dexClassLoader.loadClass(className);
            Object obj = clazz.newInstance();
            if (obj instanceof IPlugin){
                plugin = (IPlugin)obj;

                // 赋予插件Apk上下文
                plugin.attach(this);

                // 生命周期管理
                Bundle bundle = new Bundle();
                bundle.putInt("launchFrom", launchFrom);
                plugin.onCreate(bundle);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        plugin.onStart();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        plugin.onRestart();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        plugin.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        plugin.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        plugin.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        plugin.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        plugin.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Resources getResources() {
        return pluginApk != null ? pluginApk.resources : super.getResources();
    }

    @Override
    public ClassLoader getClassLoader() {
        return pluginApk != null ? pluginApk.dexClassLoader : super.getClassLoader();
    }

    @Override
    public AssetManager getAssets() {
        return pluginApk != null ? pluginApk.assetManager : super.getAssets();
    }

}
