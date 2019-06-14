# 插件化开发实践
    基于动脑课程整理、拓展


#### 插件化是什么
1. 先安装Apk再启动 | No
2. 无需安装直接启动 | Yes

#### App资源加载方式
**宿主App**

- dex文件                --> PathClassLoader
- 资源文件（图片、xml等）   --> Resources

**插件App**

- dex文件                --> DexClassLoader
- 资源文件（图片、xml等）   --> Resources

#### 插件化需要解决的问题
1. 代码加载  -->  创建DexClassLoader对象加载插件dex文件
2. 资源加载  -->  创建Resources对象加载资源文件（图片、xml等）
3. 生命周期  -->  创建代理Activity管理插件Apk activity生命周期

#### 关于Android类加载机制
1. Android虚拟机只能识别dex文件；
2. Android把所有class文件进行合并，优化，然后生成一个最终的class.dex,目的是把不同class文件重复的东西只需保留一份,如果我们的Android应用不进行分dex处理,最后一个应用的apk只会有一个dex文件。
3. Android中ClassLoader分为系统ClassLoader和自定义ClassLoader。系统ClassLoader包括3种：
    - BootClassLoader，Android系统启动时预加载系统类，BootClassLoader是ClassLoader的一个内部类。
    - PathClassLoader，可以加载已经安装的Apk，也就是/data/app/package 下的apk文件，也可以加载/vendor/lib, /system/lib下的nativeLibrary。
    - DexClassLoader，可以加载一个未安装的apk文件，支持加载直接或间接包含dex文件的文件，如APK、DEX和JAR，可以从SD卡进行加载。
4. 详情可参考：
   - [《Android类加载机制》](https://www.cnblogs.com/NeilZhang/p/8467721.html)
   - [《类加载机制系列1——深入理解Java中的类加载器》](https://www.jianshu.com/p/2026b01fb965 )
   - [《类加载机制系列2——深入理解Android中的类加载器》](https://www.jianshu.com/p/7193600024e7)
   - [《类加载机制系列3——MultiDex原理解析》](https://www.jianshu.com/p/07200780dc83)

#### 关于应用程序访问资源文件（图片、xml文件）原理
1. Apk安装后，会被重命名为base.apk，存放到 data/app/包名-XXX/base.apk目录。
2. AssetManager对象调用addAssetPath(resDir)方法，将data/app/包名-XXX/base.apk添加到资源管理器，生成Resources对象。
3. 通过context的抽象方法getResources()，获取到此Resources对象，然后通过此Resources对象访问应用程序资源。

#### 关于Activity生命周期
1. Instrumentation对象调用newActivity()方法，使用类加载器初始化一个Activity对象。
2. Activity的生命周期由Instrumentation对象管理。
3. Activity对象创建完成后，通过createBaseContextForActivity(r, activity)创建Context对象，然后创建Window对象。调用Activity对象的attach方法，将上下文对象、window等附加到Activity上后，activity才具备上下文。
4. 详情可参考：
   - [《Android Instrumentation源码分析（附Activity启动流程）》](https://blog.csdn.net/ahence/article/details/54959235 )
5. 通过DexClassLoader对象加载的Activity不具备生命周期，也不具备上下文。

#### 插件化实践思路
1. 宿主App和插件App都依赖pluginlib；
2. 插件Apk中Activity继承PluginActivity；
3. 将插件Apk下载到本地文件夹；
4. 创建DexClassLoader对象加载其dex文件，创建Resources对象加载其资源文件（图片、xml等）；
5. 启动插件Apk的ProxyActivity，传入目标Activity类名路径，然后在ProxyActivity中以反射的方式启动目标activity；
6. 在ProxyActivity生命周期中同步执行目标activity的生命周期，实现管理目标activity的生命周期；
