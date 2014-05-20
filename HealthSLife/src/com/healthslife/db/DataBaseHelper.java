package com.healthslife.db;

import java.sql.SQLException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;

import com.healthslife.healthtest.dao.ECGAnalysisRecord;
import com.healthslife.healthtest.dao.HeartRateHisRecord;
import com.healthslife.run.dao.RunRecord;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String DB_NAME = "healthlife.db";
	private static final int DB_VSERSION = 2;
	private Dao<HeartRateHisRecord, Integer> heartRateDao = null;
	private Context mContext;

	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VSERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTable(connectionSource, RunRecord.class);
			TableUtils.createTable(connectionSource, HeartRateHisRecord.class);
			TableUtils.createTable(connectionSource, ECGAnalysisRecord.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		try {
			TableUtils.createTable(connectionSource, ECGAnalysisRecord.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		super.close();
		heartRateDao = null;
	}

	public Dao<HeartRateHisRecord, Integer> getHelloDataDao()
			throws SQLException {
		if (heartRateDao == null) {
			heartRateDao = getDao(HeartRateHisRecord.class);
		}
		return heartRateDao;
	}

}
