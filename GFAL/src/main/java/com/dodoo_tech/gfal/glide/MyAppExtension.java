package com.dodoo_tech.gfal.glide;

import android.content.Context;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;

import static com.bumptech.glide.request.RequestOptions.decodeTypeOf;

/**
 * 必须写上@GlideExtension注解，否则自定义的方法会被忽略
 * 要有一个空的构造方法，并且是私有的
 * GlideExtension应该是final的，方法应该是static的
 * Extensions use annotated static methods to add new options, modifying existing options, or add additional types.
 * The annotation must be present on any classes that extend Glide’s API. If the annotation is not present, annotations on methods will be ignored.
 * should have a private and empty constructor.
 * Classes annotated with GlideExtension should also be final and contain only static methods.
 * GlideExtension annotated classes can define two types of extension methods:
 * 1.GlideOption - Adds a custom option to RequestOptions.
 * 2.GlideType - Adds support for a new resource type (GIFs, SVG etc).
 * <p>
 * Created by HUI on 2017/6/29.
 */
@GlideExtension
public final class MyAppExtension {

    private static final RequestOptions DECODE_TYPE_GIF = decodeTypeOf(GifDrawable.class).lock();

    /**
     * 必须定义空的私有构造方法
     */
    private MyAppExtension() {

    }

	/*@GlideType(GifDrawable.class)
    public static void asGif(RequestBuilder<GifDrawable> requestBuilder){
		requestBuilder
				.transition(new DrawableTransitionOptions())
				.apply(DECODE_TYPE_GIF);
	}*/

    @GlideOption
    public static void circle(RequestOptions options) {
        options.transform(new MyCircleTransform());
    }

    @GlideOption
    public static void blurBitmap(RequestOptions options, Context context, int radius) {
        options.transform(new MyBlurTransformation(context, radius));
        options.apply(options);
    }

}
