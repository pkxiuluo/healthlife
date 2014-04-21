package com.healthslife.run.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.healthslife.db.DataBaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

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
	public void delete(int id){
		RuntimeExceptionDao<RunRecord, Integer> dao = helper.getRuntimeExceptionDao(RunRecord.class);
		dao.deleteById(id);
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

	public List<RunRecord> query(int date, int pattern, int completeness) throws SQLException {
		RuntimeExceptionDao<RunRecord, Integer> dao = helper.getRuntimeExceptionDao(RunRecord.class);
	
		QueryBuilder<RunRecord, Integer> qb = dao.queryBuilder();
		Where<RunRecord, Integer> where = qb.where();
		if (date != 0) {
			where = getWhereByDate(qb.where(), date);
			if (pattern != 0 || completeness != 0) {
				where = where.and();
			}
		}
		if (pattern != 0) {
			where = getWhereByPattern(where, pattern);
			if (completeness != 0) {
				where = where.and();
			}
		}
		if (completeness != 0) {
			where = getWhereByCompleteness(where, completeness);
		}
		if (date != 0 || pattern != 0 || completeness != 0) {
			return where.query();
		} else {
			return 	dao.queryForAll();
		}
		
	}

	/**
	 * 
	 * @param where
	 * @param date
	 *            0: 所有，1: 最近一个月 ， 2 :最近一周
	 * @return
	 * @throws SQLException
	 */
	private Where<RunRecord, Integer> getWhereByDate(Where<RunRecord, Integer> where, int date) throws SQLException {
		Calendar calendar;
		switch (date) {
		case 1:
			calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -1);
			where.gt("createTime", calendar.getTime());
			break;
		case 2:
			calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -7);
			where.gt("createTime", calendar.getTime());
			break;
		default:
			break;
		}
		return where;
	}

	/**
	 * 
	 * @param where
	 * @param pattern
	 *            0: 所有，1: 自由跑 ， 2 :距离跑，3.地点跑
	 * @return
	 * @throws SQLException
	 */
	private Where<RunRecord, Integer> getWhereByPattern(Where<RunRecord, Integer> where, int pattern)
			throws SQLException {
		switch (pattern) {
		case 1:
			where.eq("kind", RunSetting.NORMAL);
			break;
		case 2:
			where.eq("kind", RunSetting.DISTANCE);
			break;
		case 3:
			where.eq("kind", RunSetting.DESTINATION);
			break;
		default:
			break;
		}
		return where;
	}

	/**
	 * 
	 * @param where
	 * @param completeness
	 *            0: 所有，1: 完成 ， 2 :未完成
	 * @return
	 * @throws SQLException
	 */
	private Where<RunRecord, Integer> getWhereByCompleteness(Where<RunRecord, Integer> where, int completeness)
			throws SQLException {
		switch (completeness) {
		case 1:
			where.ge("completeness", 1);
			break;
		case 2:
			where.lt("completeness", 1);
			break;
		default:
			break;
		}
		return where;
	}
}
