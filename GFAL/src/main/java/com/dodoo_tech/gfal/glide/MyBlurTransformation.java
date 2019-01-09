package com.dodoo_tech.gfal.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import java.security.MessageDigest;

/**
 * Created by HUI on 2017/8/19.
 */

public class MyBlurTransformation implements Transformation<Bitmap> {

	private static final String TAG = "MyBlurTransformation";
	private static int MAX_RADIUS = 25;
	private Context mContext;
	private BitmapPool mBitmapPool;
	private int mRadius;

	public MyBlurTransformation(Context context, BitmapPool pool) {
		this(context, pool, MAX_RADIUS);
	}

	public MyBlurTransformation(Context context, BitmapPool pool, int radius) {
		this.mContext = context;
		this.mBitmapPool = pool;
		this.mRadius = radius;
	}

	public MyBlurTransformation(Context context, int radius) {
		this(context, null, radius);
	}

	public Resource<Bitmap> blurBitmap(Resource<Bitmap> resource) {

		Bitmap source = (Bitmap)resource.get();
		int width = source.getWidth();
		int height = source.getHeight();
		Bitmap.Config config = source.getConfig() != null?source.getConfig(): Bitmap.Config.ARGB_8888;
		Bitmap bitmap = null;
		if (this.mBitmapPool != null) {
			bitmap = this.mBitmapPool.get(width, height, config);
		}
		if(bitmap == null) {
			bitmap = Bitmap.createBitmap(width, height, config);
		}

		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(source, 0.0F, 0.0F, (Paint)null);
		RenderScript rs = RenderScript.create(this.mContext);
		Allocation overlayAlloc = Allocation.createFromBitmap(rs, bitmap);
		ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
		blur.setInput(overlayAlloc);
		blur.setRadius((float)this.mRadius);
		blur.forEach(overlayAlloc);
		overlayAlloc.copyTo(bitmap);
		rs.destroy();
		return BitmapResource.obtain(bitmap, this.mBitmapPool);
	}

	public String getId() {
		return "BlurTransformation(radius=" + this.mRadius + ")";
	}

	@Override
	public Resource<Bitmap> transform(Context context, Resource<Bitmap> resource, int outWidth, int outHeight) {
		return blurBitmap(resource);
		//return null;
	}

	@Override
	public void updateDiskCacheKey(MessageDigest messageDigest) {

	}
}
