package cn.edu.hit.run.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PedometerOpenHelper extends SQLiteOpenHelper{
	
	/**
	 * 创建step表
	 */
	public static final String CREATE_STEP = "create table step("
			+ "id integer primary key autoincrement,"
			+ "number text,"
			+ "weight integer,"
			+ "date integer,"
			+ "userId text)";
	/**
	 * 创建user表
	 */
	public static final String CREATE_USER = "create table user("
			+ "objectId text ,"
			+ "name text,"
			+ "gender text,"
			+ "step_length integer,"
			+ "sensitivity integer,"
			+ "goal integer,"
			+ "today_step integer)";
	
	/**
	 * 带参数的PedometerOpenHelper构造函数
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public PedometerOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	/**
	 * 创建数据库
	 */
	public void onCreate(SQLiteDatabase db) {
	
		db.execSQL(CREATE_STEP);
		db.execSQL(CREATE_USER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}

	

}
