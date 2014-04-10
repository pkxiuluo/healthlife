package com.healthslife.run.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.healthslife.db.DataBaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

public class RunRecordDB {

	private DataBaseHelper helper;

	public RunRecordDB(Context context) {
		helper = OpenHelperManager.getHelper(context, DataBaseHelper.class);
	}

	public void add(RunRecord record) {
		RuntimeExceptionDao<RunRecord, Integer> dao = helper.getRuntimeExceptionDao(RunRecord.class);
		if (record != null) {
			dao.create(record);
		}
	}

	public List<RunRecord> queryByKind(int kind) {
		RuntimeExceptionDao<RunRecord, Integer> dao = helper.getRuntimeExceptionDao(RunRecord.class);
		QueryBuilder<RunRecord, Integer> qb = dao.queryBuilder();
		List<RunRecord> list = new ArrayList<RunRecord>();
		try {
			qb.where().eq("kind", kind);
			list.addAll(qb.query());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<RunRecord> queryByDate(Date date1, Date date2) {
		RuntimeExceptionDao<RunRecord, Integer> dao = helper.getRuntimeExceptionDao(RunRecord.class);
		QueryBuilder<RunRecord, Integer> qb = dao.queryBuilder();
		Date low;
		Date high;
		if (date1.before(date2)) {
			low = date1;
			high = date2;
		} else {
			low = date2;
			high = date1;
		}
		List<RunRecord> list = new ArrayList<RunRecord>();
		try {
			qb.where().between("createTime", low, high);
			list.addAll(qb.query());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<RunRecord> queryCompleted() {
		RuntimeExceptionDao<RunRecord, Integer> dao = helper.getRuntimeExceptionDao(RunRecord.class);
		QueryBuilder<RunRecord, Integer> qb = dao.queryBuilder();
		List<RunRecord> list = new ArrayList<RunRecord>();
		try {
			qb.where().ge("completeness", 1);
			list.addAll(qb.query());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<RunRecord> queryNotCompleted() {
		RuntimeExceptionDao<RunRecord, Integer> dao = helper.getRuntimeExceptionDao(RunRecord.class);
		QueryBuilder<RunRecord, Integer> qb = dao.queryBuilder();
		List<RunRecord> list = new ArrayList<RunRecord>();
		try {
			qb.where().lt("completeness", 1);
			list.addAll(qb.query());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
