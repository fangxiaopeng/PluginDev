package com.fxp.plugin.lib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Title:       PluginManager
 * <p>
 * Package:     com.fxp.plugin.lib
 * <p>
 * Author:      fxp
 * <p>
 * Create at:   2019-06-12 14:59
 * <p>
 * Description:
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
public class PluginManager {

    private Context context;

    private PluginApk pluginApk;

    private static PluginManager pluginManager = new PluginManager();

    public static PluginManager getInstance() {
        return pluginManager;
    }

    private PluginManager() {}

    public void init(Context context){
        this.context = context.getApplicationContext();
    }

    public void loadApk(String path){
        if (context == null){
            throw new RuntimeException("context cannot be null");
        }
        PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        if (packageInfo != null){
            // 创建访问插件Apk的DexClassLoader对象
            DexClassLoader dexClassLoader = createDexClassLoader(path);
            // 创建访问插件Apk的AssetManager对象
            AssetManager assetManager = createAssetManager(path);
            // 创建访问插件Apk的Resources对象
            Resources resources = createResources(assetManager);
            pluginApk = new PluginApk(packageInfo, dexClassLoader, resources);
        }
    }

    public PluginApk getPluginApk(){
        return pluginApk;
    }

    /**
     * @Description: 创建访问插件Apk的DexClassLoader对象
     *
     * @Author:  fxp
     * @Date:    2019-06-12   15:40
     * @param    path
     * @return   dalvik.system.DexClassLoader
     * @exception/throws
     */
    private DexClassLoader createDexClassLoader(String path){
        File file = context.getDir("odex", Context.MODE_PRIVATE);

        return new DexClassLoader(path, file.getAbsolutePath(), null, context.getClassLoader());
    }

    /**
     * @Description: 创建访问插件Apk的AssetManager对象
     * AssetManager对象不能直接new，需要通过反射获取
     *
     * @Author:  fxp
     * @Date:    2019-06-12   15:31
     * @param    path
     * @return   android.content.res.AssetManager
     * @exception/throws
     */
    private AssetManager createAssetManager(String path){
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            method.invoke(assetManager, path);

            return assetManager;
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @Description: 创建访问插件Apk的Resources对象
     *
     * @Author:  fxp
     * @Date:    2019-06-12   15:30
     * @param    assetManager
     * @return   android.content.res.Resources
     * @exception/throws
     */
    private Resources createResources(AssetManager assetManager){
        Resources res = context.getResources();

        return new Resources(assetManager, res.getDisplayMetrics(), res.getConfiguration());
    }
}
