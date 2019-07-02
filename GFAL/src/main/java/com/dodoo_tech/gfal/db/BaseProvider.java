package com.dodoo_tech.gfal.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

public abstract class BaseProvider extends ContentProvider{
    protected static final String TAG="BaseProvider";
    protected static final int BOOKS = 1;
    protected static final int BOOK = 2;
    private SQLiteOpenHelper dbHelper;

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		dbHelper = createDbHelper();
        return dbHelper != null;
	}

	protected abstract SQLiteOpenHelper createDbHelper();

	//查询
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder ,String TABLE_NAME,UriMatcher uriMatcher) {
		// TODO Auto-generated method stub
        Cursor cursor = null;

        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            if(db==null){
                return cursor;
            }
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(TABLE_NAME);
            switch (uriMatcher.match(uri)) {
                case BOOKS:
                    cursor = builder.query(db,projection, selection, selectionArgs, null, null,
                            sortOrder);
                    break;
                case BOOK:
                    long id = ContentUris.parseId(uri);
                    String where = BaseColumns._ID + "=" + id;
                    if (selection != null && !"".equals(selection)) {
                        where = selection + " and " + where;
                    }
                    cursor = builder.query(db, projection, where, selectionArgs, null,
                            null, sortOrder);
                    break;
                default:
                    throw new IllegalArgumentException("This is a unKnow Uri "
                            + uri.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
	}

	// 取得数据的类型,此方法会在系统进行URI的MIME过滤时被调用。
	public String getType(Uri uri,UriMatcher uriMatcher,String CONTENT_LIST,String CONTENT_ITEM) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)) {
        case BOOKS:
            return CONTENT_LIST;
        case BOOK:
            return CONTENT_ITEM;
        default:
            throw new IllegalArgumentException("This is a unKnow Uri"+ uri.toString());
		}
	}

	//增加
	public Uri insert(Uri uri, ContentValues values,UriMatcher uriMatcher,String TABLE_NAME) {
        // TODO Auto-generated method stub
        try {
            switch (uriMatcher.match(uri)) {
                case BOOKS:
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    if (db == null) {
                        return null;
                    }
                    //执行添加，返回行号，如果主健字段是自增长的，那么行号会等于主键id
                    long rowId = db.insert(TABLE_NAME, BaseColumns._ID, values);
                    Uri insertUri = Uri.withAppendedPath(uri, "/" + rowId);
                    //发出数据变化通知(表的数据发生变化)
                    getContext().getContentResolver().notifyChange(uri, null);
                    return insertUri;
                default:
                    //不能识别uri
                    throw new IllegalArgumentException("This is a unKnow Uri" + uri.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	//删除
	public int delete(Uri uri, String selection, String[] selectionArgs, UriMatcher uriMatcher,String TABLE_NAME) {
		// TODO Auto-generated method stub
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            if(db==null){
                return 0;
            }
            switch (uriMatcher.match(uri)) {
                case BOOKS:
                    return db.delete(TABLE_NAME, selection, selectionArgs);
                case BOOK:
                    long id = ContentUris.parseId(uri);
                    String where = BaseColumns._ID + "=" + id;
                    if (selection != null && !"".equals(selection)) {
                        where = selection + " and " + where;
                    }
                    return db.delete(TABLE_NAME, where, selectionArgs);
                default:
                    throw new IllegalArgumentException("This is a unKnow Uri"+ uri.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

	//更新
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs,UriMatcher uriMatcher,String TABLE_NAME) {
		// TODO Auto-generated method stub
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            if(db==null){
                return 0;
            }
            switch (uriMatcher.match(uri)) {
                case BOOKS:
                    return db.update(TABLE_NAME,values, selection, selectionArgs);
                case BOOK:
                    long id = ContentUris.parseId(uri);
                    String where = BaseColumns._ID + "=" + id;
                    if (selection != null && !"".equals(selection)) {
                        where = selection + " and " + where;
                    }
                    return db.update(TABLE_NAME,values, where, selectionArgs);
                default:
                    throw new IllegalArgumentException("This is a unKnow Uri"+ uri.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
