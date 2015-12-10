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
    private static String DB_NAME = "My_DB.db"; //���ݿ�����
    private static int DB_VERSION = 2;   //�汾��
    private SQLiteDatabase db;  //���ݿ��������


    public MyDB(Context context) {

        //������Ĺ��캯��ʵ�ֹ����У�һ�����ȵ��ø����캯��
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();

    }
    //��SQLite���ݿ�����
    public SQLiteDatabase openConnection() {
        if (!db.isOpen()) {
            //��д��ʽ��ȡSQLiteDatabase
            db = getWritableDatabase();

        }
        return db;
    }

    //�ر�SQLite���ݿ����Ӳ���

    public void closeConnection() {
        try {
            if(db!=null &&db.isOpen())
                db.close();
        }catch(Exception e){
            e.printStackTrace();

        }
    }
    //������
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

    //��������
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

    //��������
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
    //ɾ������
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
    //��������
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

    //������ݱ��Ƿ����
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
        // TODO ÿ�γɹ������ݿ�����ȱ�ִ��
        super.onOpen(db);

    }
}
