package com.benpaoba.freerun;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DistanceInfoDao {
	private SQLiteOpenHelper mOpenHelper = null;
    private SQLiteDatabase db;

    public DistanceInfoDao(Context context) {
    	mOpenHelper = new DatabaseHelper(context);
    }

    public void insert(DistanceInfo mDistanceInfo) {
        if (mDistanceInfo == null) {
            return;
        }
        db = mOpenHelper.getWritableDatabase();
        String sql = "INSERT INTO milestone(distance,longitude,latitude, usedTime) VALUES('"+ 
        mDistanceInfo.getDistance() +
        "','"+ mDistanceInfo.getLongitude() +
        "','"+ mDistanceInfo.getLatitude() +
        "','"+ mDistanceInfo.getTime() +
        "')";
        db.execSQL(sql);
        db.close();
    }

    public int getMaxId() {
        db = mOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(id) as id from milestone",null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("id"));
        }
        return -1;
    }

    /**
     * ��������
     * @param orderDealInfo
     * @return
     */
    public synchronized int insertAndGet(DistanceInfo mDistanceInfo) {
        int result = -1;
        insert(mDistanceInfo);
        result = getMaxId();
        return result;
    }

    /**
     * ����id��ȡ
     * @param id
     * @return
     */
    public DistanceInfo getById(int id) {
        db = mOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from milestone WHERE id = ?",new String[] { String.valueOf(id) });
        DistanceInfo mDistanceInfo = null;
        if (cursor.moveToFirst()) {
            mDistanceInfo = new DistanceInfo();
            mDistanceInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            mDistanceInfo.setDistance(cursor.getFloat(cursor.getColumnIndex("distance")));
            mDistanceInfo.setLongitude(cursor.getFloat(cursor.getColumnIndex("longitude")));
            mDistanceInfo.setLatitude(cursor.getFloat(cursor.getColumnIndex("latitude")));
            mDistanceInfo.setTime(cursor.getInt(cursor.getColumnIndex("usedTime")));
        }
        cursor.close();
        db.close();
        return mDistanceInfo;
    }

    /**
     * ���¾���
     * @param orderDealInfo
     */
    public void updateDistance(DistanceInfo mDistanceInfo) {
        if (mDistanceInfo == null) {
            return;
        }
        db = mOpenHelper.getWritableDatabase();
        String sql = "update milestone set distance=" + mDistanceInfo.getDistance() +
        		",longitude = "+mDistanceInfo.getLongitude() + 
        		",latitude = "+mDistanceInfo.getLatitude() +
        		", usedTime = " + mDistanceInfo.getTime() + 
        		" where id = "+ mDistanceInfo.getId();
        db.execSQL(sql);
        db.close();
    }


    /**
     * ɾ����Ӧ��¼
     * @param id
     */
    public void delete(int id) {
        if (id == 0 || id < 0) {
            return;
        }
        db = mOpenHelper.getWritableDatabase();
        String sql = "delete from milestone where id = " + id;
        db.execSQL(sql);
        db.close();
    }
}