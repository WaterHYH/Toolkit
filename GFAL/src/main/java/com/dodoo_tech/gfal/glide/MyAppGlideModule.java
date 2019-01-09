package com.dodoo_tech.gfal.glide;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * 必须写上@GlideModule注释，否则glide会找不到
 * AppGlideModule implementations must always be annotated with @GlideModule. If the annotation is not present, the module will not be discovered and you will see a warning in your logs with the Glide log tag that indicates that the module couldn’t be found.
 * Created by HUI on 2017/6/29.
 */

@GlideModule
public class MyAppGlideModule extends AppGlideModule {

}
