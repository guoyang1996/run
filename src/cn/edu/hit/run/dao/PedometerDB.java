package cn.edu.hit.run.dao;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hit.run.domain.Step;
import cn.edu.hit.run.domain.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 对数据库pedometer里的各个表进行增删改查
 */
public class PedometerDB {



	public static final String DB_NAME = "rundb";// 数据库名称

	public static final int VERSION = 1;// 数据版本

	private static PedometerDB pedometerDB;

	private SQLiteDatabase db;



	/**
	 * 将PedometerDB的构造方法设置为私有方法，在别的类里不能通过new来创建这个对象
	 * 
	 * @param context
	 */
	private PedometerDB(Context context) {
		PedometerOpenHelper pHelper = new PedometerOpenHelper(context, DB_NAME,
				null, VERSION);
		db = pHelper.getWritableDatabase();
	}

	/**
	 * 使用单例模式创建数据库
	 */
	public synchronized static PedometerDB getInstance(Context context) {
		if (pedometerDB == null) {
			pedometerDB = new PedometerDB(context);
		}
		return pedometerDB;
	}

	/**
	 * 增加user表里的数据
	 * 
	 * @param user
	 */
	public void saveUser(User user) {
		if (user != null) {
			ContentValues values = new ContentValues();
			values.put("objectId", user.getObjectId());
			values.put("name", user.getName());
			values.put("gender", user.getGender());
			values.put("sensitivity", user.getSensitivity());
			values.put("step_length", user.getStep_length());
			values.put("today_step", user.getToday_step());
			values.put("goal", user.getGoal());
			db.insert("user", null, values);
		}
	}

	/**
	 * 根据user的id删除user表里的数据
	 * 
	 * @param user
	 */
	public void deleteUser(User user) {
		if (user != null) {
			db.delete("user", "objectId = ?",
					new String[] { user.getObjectId() });
		}
	}

	/**
	 * 升级user表里的数据
	 * 
	 * @param user
	 */
	public void updateUser(User user) {
		if (user != null) {
			ContentValues values = new ContentValues();
			values.put("objectId", user.getObjectId());
			values.put("name", user.getName());
			values.put("gender", user.getGender());
			values.put("sensitivity", user.getSensitivity());
			values.put("step_length", user.getStep_length());
			values.put("today_step", user.getToday_step());
			values.put("goal", user.getGoal());
			Log.i("update", ""+user.getGoal());
			db.update("user", values, "objectId = ?",
					new String[] { user.getObjectId() });
		}
	}

	/**
	 * 升级user表里的数据
	 * 
	 * @param user
	 */
	public void changeObjectId(User user) {
		if (user != null) {
			ContentValues values = new ContentValues();
			values.put("objectId", user.getObjectId());
			db.update("user", values, null, null);
		}
	}

	/**
	 * 增加step表里的数据
	 * 
	 * @param step
	 */
	public void saveStep(Step step) {
		if (step != null) {
			ContentValues values = new ContentValues();
			values.put("number", step.getNumber());
			values.put("date", step.getDate());
			values.put("userId", step.getUserId());
			values.put("weight", step.getWeight());
			db.insert("step", null, values);
		}
	}

	/**
	 * 升级step表里的数据
	 * 
	 * @param step
	 */
	public void updateStep(Step step) {
		if (step != null) {
			ContentValues values = new ContentValues();
			values.put("number", step.getNumber());
			values.put("date", step.getDate());
			values.put("userId", step.getUserId());
			values.put("weight", step.getWeight());
			db.update("step", values, "userId = ? and date = ?", new String[] {
					step.getUserId(), step.getDate() });
		}
	}

	/**
	 * 升级step表里的数据
	 * 
	 * @param step
	 */
	public void changeuserId(Step step) {
		if (step != null) {
			ContentValues values = new ContentValues();
			// values.put("number", step.getNumber());
			// values.put("date", step.getDate());
			values.put("userId", step.getUserId());
			db.update("step", values, null, null);
		}
	}


	/**
	 * 根据user表的userid和date来取数据
	 * 
	 * @param userId
	 * @param date
	 * @return
	 */
	public Step loadSteps(String userId, String date) {
		Step step = null;
		Cursor cursor = db.query("step", null, "userId = ? and date = ?",
				new String[] { userId, date }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				step = new Step();
				step.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
				step.setDate(cursor.getString(cursor.getColumnIndex("date")));
				step.setWeight(cursor.getInt(cursor.getColumnIndex("weight")));
				step.setUserId(userId);
			} while (cursor.moveToNext());

		} else {
			Log.i("tag", "step is null!");
		}
		return step;
	}

	
	/**
	 * 取出user中所有的数据，按照步数的降序取出
	 * 
	 * @return
	 */
	public List<User> loadListUsers() {
		List<User> list = null;
		Cursor cursor = db.rawQuery(
				"select * from user  order by today_step desc", null);
		if (cursor.moveToFirst()) {
			list = new ArrayList<User>();
			do {
				User user = new User();
				user.setObjectId(cursor.getString(cursor
						.getColumnIndex("objectId")));
				user.setName(cursor.getString(cursor.getColumnIndex("name")));
				user.setGender(cursor.getString(cursor.getColumnIndex("gender")));
				user.setSensitivity(cursor.getInt(cursor
						.getColumnIndex("sensitivity")));
				user.setStep_length(cursor.getInt(cursor
						.getColumnIndex("step_length")));
				user.setToday_step(cursor.getInt(cursor
						.getColumnIndex("today_step")));
				list.add(user);
			} while (cursor.moveToNext());
		}
		return list;

	}

	/**
	 * 更具date取出所有的step数据
	 * 
	 * @param date
	 * @return
	 */
	public List<Step> loadListSteps() {
		List<Step> list = new ArrayList<Step>();

		Cursor cursor = db.rawQuery("select * from step",
				null);
		if (cursor.moveToFirst()) {
			do {
				Step step = new Step();
				step.setId(cursor.getInt(cursor.getColumnIndex("id")));
				step.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
				step.setDate(cursor.getString(cursor.getColumnIndex("date")));
				step.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
				step.setWeight(cursor.getInt(cursor.getColumnIndex("weight")));
				list.add(step);
			} while (cursor.moveToNext());

		}

		return list;
	}

	/**
	 * 根据id取出user数据
	 * 
	 * @param id
	 * @return
	 */
	public User loadUser(String objectId) {
		User user = null;
		if(objectId==null)
		{
			return user;
		}
		Cursor cursor = db.query("user", null, "objectId = ?",
				new String[] { objectId }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				user = new User();
				user.setName(cursor.getString(cursor.getColumnIndex("name")));
				user.setGender(cursor.getString(cursor.getColumnIndex("gender")));
				user.setObjectId(objectId);
				user.setSensitivity(cursor.getInt(cursor
						.getColumnIndex("sensitivity")));
				user.setStep_length(cursor.getInt(cursor
						.getColumnIndex("step_length")));
				user.setToday_step(cursor.getInt(cursor
						.getColumnIndex("today_step")));
				user.setGoal(cursor.getInt(cursor
						.getColumnIndex("goal")));
				Log.i("loadUser", "User is  not null!");
			} while (cursor.moveToNext());
		} else {
			Log.i("tag", "User is null!");
		}
		return user;
	}

	/**
	 * 取出第一个用户，也就是用此app的用户
	 * 
	 * @param id
	 * @return
	 */
	public User loadFirstUser() {
		User user = null;
		Cursor cursor = db.query("user", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			user = new User();
			user.setName(cursor.getString(cursor.getColumnIndex("name")));
			user.setGender(cursor.getString(cursor.getColumnIndex("gender")));
			user.setObjectId(cursor.getString(cursor.getColumnIndex("objectId")));
			user.setSensitivity(cursor.getInt(cursor
					.getColumnIndex("sensitivity")));
			user.setStep_length(cursor.getInt(cursor
					.getColumnIndex("step_length")));
			user.setToday_step(cursor.getInt(cursor
					.getColumnIndex("today_step")));
		} else {
			Log.i("tag", "User is null!");
		}
		return user;
	}
	public Step loadFirstStep() {
		
		Step step = null;
		Cursor cursor = db.query("step", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			step = new Step();
			step.setId(cursor.getInt(cursor.getColumnIndex("id")));
			step.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
			step.setDate(cursor.getString(cursor.getColumnIndex("date")));
			step.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
			step.setWeight(cursor.getInt(cursor.getColumnIndex("weight")));
		} else {
			Log.i("tag", "User is null!");
		}
		return step;
	}
}
