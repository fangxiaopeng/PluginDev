package com.fxp.plugin.lib;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

/**
 * Title:       PluginApk
 * <p>
 * Package:     com.fxp.plugin.lib
 * <p>
 * Author:      fxp
 * <p>
 * Create at:   2019-06-12 14:56
 * <p>
 * Description: 提供获取插件Apk中资源的对象
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
public class PluginApk {

    public PackageInfo packageInfo;

    /**
     * 访问插件Apk的DexClassLoader对象
     */
    public DexClassLoader dexClassLoader;

    /**
     * 访问插件Apk的Resources对象
     */
    public Resources resources;

    /**
     * 访问插件Apk的AssetManager对象
     */
    public AssetManager assetManager;

    public PluginApk(PackageInfo packageInfo, DexClassLoader dexClassLoader, Resources resources){
        this.packageInfo = packageInfo;
        this.dexClassLoader = dexClassLoader;
        this.resources = resources;
        this.assetManager = (resources != null) ? resources.getAssets() : null;
    }
}
