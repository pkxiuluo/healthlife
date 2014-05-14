package com.healthslife.healthtest.dao;

import java.util.List;

import android.content.Context;

import com.healthslife.db.DataBaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class ECGDB {
	private DataBaseHelper databaseHelper = null;

	public ECGDB(Context mContext) {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(mContext,
					DataBaseHelper.class);
		}
	}

	public void add(ECGAnalysisRecord aRecord) {
		RuntimeExceptionDao<ECGAnalysisRecord, Integer> dao = databaseHelper
				.getRuntimeExceptionDao(ECGAnalysisRecord.class);
		dao.create(aRecord);
	}

	public List<ECGAnalysisRecord> queryAll() {
		RuntimeExceptionDao<ECGAnalysisRecord, Integer> dao = databaseHelper
				.getRuntimeExceptionDao(ECGAnalysisRecord.class);
		return dao.queryForAll();
	}

	public ECGAnalysisRecord queryFirst() {
		RuntimeExceptionDao<ECGAnalysisRecord, Integer> dao = databaseHelper
				.getRuntimeExceptionDao(ECGAnalysisRecord.class);
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
