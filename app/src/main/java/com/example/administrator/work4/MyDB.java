package com.example.administrator.work4;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/10/28.
 */
public class MyDB  extends SQLiteOpenHelper{
    private static String DB_NAME = "My_DB.db"; //数据库名称
    private static int DB_VERSION = 2;   //版本号
    private SQLiteDatabase db;  //数据库操作对象


    public MyDB(Context context) {

        //在子类的构造函数实现过程中，一般首先调用父类额构造函数
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();

    }
    //打开SQLite数据库连接
    public SQLiteDatabase openConnection() {
        if (!db.isOpen()) {
            //读写方式获取SQLiteDatabase
            db = getWritableDatabase();

        }
        return db;
    }

    //关闭SQLite数据库连接操作

    public void closeConnection() {
        try {
            if(db!=null &&db.isOpen())
                db.close();
        }catch(Exception e){
            e.printStackTrace();

        }
    }
    //创建表
    public boolean createTable(String createTableSql){
        try{
            openConnection();
            db.execSQL(createTableSql);

        }catch(SQLException e){
            e.printStackTrace();
            return false;

        }finally{
            closeConnection();
        }
        return true;
    }

    //保存数据
    public boolean save(String tableName,ContentValues values){
        try {
            openConnection();
            db.insert(tableName,null,values);
        }catch (SQLException ex){
            ex.printStackTrace();
            return false;

        }finally {
            closeConnection();
        }
        return true;

    }

    //更新数据
    public boolean update(String table,ContentValues values,String whereClause,String [] whereArgs){
        try {
            openConnection();
            db.update(table, values, whereClause, whereArgs);
        }catch (SQLException ex){
            ex.printStackTrace();
            return false;
        }finally {
            closeConnection();
        }
        return true;

    }
    //删除数据
    public boolean delete(String table,String deleteSql,String obj[]){
        try{
            openConnection();
            db.delete(table, deleteSql, obj);
        }catch (SQLException ex){
            ex.printStackTrace();
            return false;
        }finally {
            closeConnection();
        }
        return true;

    }
    //查找数据
    public Cursor find(String findSql,String obj[]){
        try {
            openConnection();
            Cursor cursor = db.rawQuery(findSql,obj);
            return cursor;
        }catch (SQLException ex) {

            ex.printStackTrace();
            return null;
        }
    }

    //检查数据表是否存在
    public boolean isTableExits(String tableName){
        try{
            openConnection();
            String str = "select count(*) xcount from "+tableName;
            db.rawQuery(str,null).close();

        }catch(Exception e){
            return false ;
        }
        return true;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    @Override
    public void onOpen(SQLiteDatabase db){
        // TODO 每次成功打开数据库后首先被执行
        super.onOpen(db);

    }
}
