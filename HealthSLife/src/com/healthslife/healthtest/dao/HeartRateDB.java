package com.healthslife.healthtest.dao;

import java.util.List;

import android.content.Context;

import com.healthslife.db.DataBaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class HeartRateDB {
	private DataBaseHelper databaseHelper = null;

	public HeartRateDB(Context mContext) {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(mContext,
					DataBaseHelper.class);
		}
	}

	public void add(HeartRateHisRecord aRecord) {
		RuntimeExceptionDao<HeartRateHisRecord, Integer> dao = databaseHelper
				.getRuntimeExceptionDao(HeartRateHisRecord.class);
		dao.create(aRecord);
	}

	public List<HeartRateHisRecord> queryAll() {
		RuntimeExceptionDao<HeartRateHisRecord, Integer> dao = databaseHelper
				.getRuntimeExceptionDao(HeartRateHisRecord.class);
		return dao.queryForAll();
	}

	public HeartRateHisRecord queryFirst() {
		RuntimeExceptionDao<HeartRateHisRecord, Integer> dao = databaseHelper
				.getRuntimeExceptionDao(HeartRateHisRecord.class);
		if (dao.queryForAll().size() == 0)
			return null;
		return dao.queryForAll().get(0);
	}

	protected void releaseDataHelper() {
		/*
		 * 释放资源
		 */
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}
}
