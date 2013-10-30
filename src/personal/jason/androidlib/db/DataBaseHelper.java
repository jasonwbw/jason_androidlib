/**
 * 处理数据库操作，如果需要实例化子类请不要在程序中再使用本类。
 * 需要做数据库拷贝需要存放在asset文件夹下。
 * 注意：设计的数据库一定要保证java bean中字段与数据库中字段名称相同，
 *     且仅适用于id为主键的数据表，可以满足大多数需求，请考虑java bean继承自BaseBean
 *
 * @author vb.wbw
 *
 * @create 2010.12.14
 */

package personal.jason.androidlib.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

import personal.jason.androidlib.utils.JudgeInterface;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	// for copy db
	private static String MY_PACKAGE;
	private final static String DB_PATH = "/data/data/" + MY_PACKAGE
			+ "/databases/";
	private static Context context; // the main activity for get the db

	// for build instants
	private static String DATABASE_NAME = "**.db";
	private final static int DATABASE_VERSION = 1;

	private static String PRIMARY_KEY = "id";

	private SQLiteDatabase db;
	// -------------------------------------------------------
	// only one DataBaseHelper
	private static DataBaseHelper instance = null;

	private DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static void setContext(Context coupleContext) {
		context = coupleContext;
	}

	/**
	 * 获取DataBaseHelper帮助类
	 * @param myPackage 你的androidpackage
	 * @param dbName    数据库名称
	 * @param primaryKey 当仅有一个主键工具包提供了一些简洁的方法
	 * @return
	 */
	synchronized public static DataBaseHelper getInstance(
			String myPackage, String dbName, String primaryKey) {
		if (instance == null) {
			MY_PACKAGE = myPackage;
			DATABASE_NAME = dbName;
			PRIMARY_KEY = primaryKey;
			instance = new DataBaseHelper(context);
		}

		return instance;
	}

	// -------------------------------------------------------
	// copy db if is not exit

	/**
	 * copy check and copy db
	 */
	public void createDataBase() {
		// TODO Auto-generated method stub

		boolean dbExist = checkDataBase();

		if (dbExist) {

		} else {
			this.getReadableDatabase();

			try {
				copyDataBase();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d("person.jason.androidlib.db.DataBaseHelper",e.getMessage());
			}
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.
			Log.d("person.jason.androidlib.db.DataBaseHelper", "database does't exist yet.");
		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = context.getAssets().open(DATABASE_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DATABASE_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	// -----------------------------------------------------
	// db normal use
	// -----------------------------------------------------

	/**
	 * select the columns given. if columns is null means select *
	 * 
	 * @param tableName
	 * @param columns
	 * @param orderBy
	 * @return
	 */
	public Cursor select(String tableName, String[] columns, String orderBy) {
		setDBReadOnly(true);
		return db.query(tableName, columns, null, null, null, null, orderBy);
	}

	/**
	 * 带条件的搜索
	 * 
	 * 
	 * @param tableName
	 * @param selection
	 * @param condition
	 * @param orderBy
	 *            @return
	 */
	public Cursor fullSelect(String tableName, String selection,
			String condition, String orderBy) {
		setDBReadOnly(true);
		return db.query(tableName, null, condition, null, null, null, orderBy);
	}

	/**
	 * 查询符号条件记录个数
	 * 
	 * @param tableName
	 * @param selection
	 * @return
	 */
	public long getCount(String tableName, String selection) {
		setDBReadOnly(true);
		Cursor cursor = db.query(tableName, new String[] { "count(*)" },
				selection, null, null, null, null);
		if (cursor.moveToFirst()) {
			return cursor.getLong(0);
		} else {
			return 0;
		}
	}

	/**
	 * 根据ID和表名查找记录，需要使用PRIMARY_KEY
	 * 
	 * @param tableName
	 * @param Id
	 * @return
	 */
	public Cursor findById(String tableName, String Id) {
		setDBReadOnly(true);
		return db.query(tableName, null, PRIMARY_KEY + "=" + Id, null, null,
				null, null);
	}

	/**
	 * 保存或更新数据库中对象
	 * 
	 * @param baseBean
	 * @param needFatherAttribute 是否需要父类的字段（适合采用BaseBean或设计需要数据库中记录了父类字段）
	 * @return
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NumberFormatException
	 */
	public long saveOrUpdate(BaseBean baseBean, boolean needFatherAttribute)
			throws IllegalAccessException, NumberFormatException,
			SecurityException, IllegalArgumentException, NoSuchFieldException {
		Cursor cursor = findById(getTablename(baseBean), baseBean.getId());
		if (0 == cursor.getCount()) { // 判断数据库中是否已存在具有相同主键的记录
			return insert(baseBean, needFatherAttribute);
		}else {
			update(baseBean.getId(), baseBean, needFatherAttribute);
			return Long.parseLong(baseBean.getId());
		}
	}

	/**
	 * insert data
	 * 
	 * @param obj
	 * @param needFatherAttribute 是否需要父类的字段（适合采用BaseBean或设计需要数据库中记录了父类字段）
	 * @return the new id, id -1 something wrong
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public long insert(Object obj, boolean needFatherAttribute) throws IllegalArgumentException,
			IllegalAccessException {
		ContentValues cv = buildContentValues(obj,needFatherAttribute);

		setDBReadOnly(false);
		return db.insert(getTablename(obj), null, cv);
	}

	/**
	 * update db, 需要使用PRIMARY_KEY
	 * 
	 * @param id 主键
	 * @param obj
	 * @param needFatherAttribute 是否需要父类的字段（适合采用BaseBean或设计需要数据库中记录了父类字段）
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void update(String id, Object obj,boolean needFatherAttribute) throws IllegalArgumentException,
			IllegalAccessException {
		ContentValues cv = buildContentValues(obj,needFatherAttribute);

		setDBReadOnly(false);
		String where = PRIMARY_KEY + "=?";
		String[] wherevalues = { id };
		db.update(getTablename(obj), cv, where, wherevalues);
	}

	/**
	 * delete data
	 * 
	 * @param obj
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws NumberFormatException
	 */
	public void delete(Object obj) throws SecurityException,
			NoSuchFieldException, NumberFormatException,
			IllegalArgumentException, IllegalAccessException {
		Field field = obj.getClass().getSuperclass().getDeclaredField(
				PRIMARY_KEY);
		field.setAccessible(true);
		int id = Integer.parseInt(field.get(obj).toString());

		setDBReadOnly(false);
		String where = PRIMARY_KEY + "=?";
		String[] wherevalues = { "" + id };
		db.delete(getTablename(obj), where, wherevalues);
	}

	@Override
	public synchronized void close() {
		if (db != null)
			db.close();

		super.close();
	}

	// -----------------------------------------------------
	// tools
	// -----------------------------------------------------
	
	/**
	 * 设置androidSqliteDatabase的只读属性
	 * @param onlyReadable
	 */
	private void setDBReadOnly(boolean onlyReadable) {
		if (db != null) {
			if (db.isOpen()) {
				db.close();
			}
		}

		if (onlyReadable) {
			db = SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null,
					 SQLiteDatabase.OPEN_READONLY);
			//db = this.getReadableDatabase();
		} else {
			db = SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null,
					 SQLiteDatabase.OPEN_READWRITE);
			//db = this.getWritableDatabase();
		}

	}

	/**
	 * 根据TableName寻找表名。如果更改到一致可以使用mapping处理
	 * 
	 * @param obj
	 */
	private String getTablename(Object obj) {
		return obj.getClass().getSimpleName().toLowerCase();
	}

	/**
	 * 根据Object的所有字段构建ContentValues对数据库操作，仅构建非Collection属性
	 * @param obj
	 * @param needFatherAttribute 是否需要添加父类的字段
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private ContentValues buildContentValues(Object obj,boolean needFatherAttribute)
			throws IllegalArgumentException, IllegalAccessException {
		ContentValues cv = new ContentValues();

		Field[] fields = obj.getClass().getDeclaredFields();

		for (Field field : fields) {

			field.setAccessible(true);
			if (!JudgeInterface.isInterface(field.getType(),
					"java.util.Collection")) {
				if (null != field.get(obj)) {
					cv.put(field.getName(), field.get(obj).toString());
				}
			}
		}

		if(needFatherAttribute){
			fields = obj.getClass().getSuperclass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if (null != field.get(obj)) {
					cv.put(field.getName(), field.get(obj).toString());
				}
			}
		}

		return cv;
	}

	// -----------------------------------------------------
	// override method /do nothing
	// -----------------------------------------------------

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

}
