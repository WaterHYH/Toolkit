package com.dodoo_tech.gfal.glide;

import com.bumptech.glide.load.Key;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * Created by HUI on 2017/7/12.
 */

public class StringSignature implements Key {
	private final String signature;

	public StringSignature(String signature) {
		if(signature == null) {
			throw new NullPointerException("Signature cannot be null!");
		} else {
			this.signature = signature;
		}
	}

	@Override
	public void updateDiskCacheKey(MessageDigest messageDigest){
		try {
			messageDigest.update(this.signature.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public boolean equals(Object o) {
		if(this == o) {
			return true;
		} else if(o != null && this.getClass() == o.getClass()) {
			StringSignature that = (StringSignature)o;
			return this.signature.equals(that.signature);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return this.signature.hashCode();
	}



	public String toString() {
		return "StringSignature{signature=\'" + this.signature + '\'' + '}';
	}
}
