package com.example.administrator.work4;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;

        import java.util.Vector;

/**
 * Created by Administrator on 2015/11/2.
 */
public class ContactsTable {
    private final static String TABLENAME = "contactsTable"; //表名
    private MyDB db;  //定义SQLiteOpenHelper 数据库管理对象

    public ContactsTable(Context context) {
        db = new MyDB(context);
        if(!db.isTableExits(TABLENAME)){
            String createTableSql = "CREATE TABLE IF NOT EXISTS "+
                    TABLENAME + "(id_DB integer primary key AUTOINCREMENT,"+
                    User.NAME+ " VARCHAR,"+
                    User.DANWEI+" VARCHAR,"+
                    User.MOBILE+" VARCHAR,"+
                    User.QQ + " VARCHAR,"+
                    User.ADDRESS+" VARCHAR)";
            db.createTable(createTableSql);


        }


    }
    //添加数据到联系人表
    public boolean addData(User user){
        ContentValues values = new ContentValues();
        values.put(User.NAME,user.getName());
        values.put(User.DANWEI,user.getDanwei());
        values.put(User.MOBILE,user.getMobile());
        values.put(User.QQ,user.getQq());
        values.put(User.ADDRESS,user.getAddress());
        return db.save(TABLENAME,values);

    }

    //获取联系人表全部记录
    public User [] getAllUser(){
        Vector<User> v = new Vector<User>();
        Cursor cursor = null;
        try {
            cursor = db.find("select * from " + TABLENAME,null);
            while (cursor.moveToNext()) {
                User temp = new User();
                temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
                temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
                temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
                temp.setMobile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
                temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
                temp.setAddress(cursor.getString(cursor.getColumnIndex(User.ADDRESS)));
                v.add(temp);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(cursor != null){
                cursor.close();

            }
            db.closeConnection();
        }
        if (v.size()>0){
            return v.toArray(new User[]{});
        }else {
            User [] users = new User[1];
            User user = new User();
            user.setName("无结果");
            users[0] = user;
            return  users;
        }

    }

    //根据联系人的 ID 来获取联系人
    public User getUserByID(int id){
        Cursor cursor = null;
        try{
            cursor = db.find("select * from "+TABLENAME +" where id_DB=?",new String []{id+""});
            User temp = new User();
            cursor.moveToNext();
            temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
            temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
            temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
            temp.setMobile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
            temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
            temp.setAddress(cursor.getString(cursor.getColumnIndex(User.ADDRESS)));

            return temp;

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
            db.closeConnection();
        }
        return null;

    }

    //更新联系人信息
    public boolean updateUser(User user){
        ContentValues values = new ContentValues();
        values.put(User.NAME,user.getName());
        values.put(User.MOBILE,user.getMobile());
        values.put(User.QQ,user.getQq());
        values.put(User.DANWEI,user.getDanwei());
        values.put(User.ADDRESS,user.getAddress());
        return db.update(TABLENAME,values,"id_db=?",new String [] {user.getId_DB()+""});

    }
    //根据特定条件查询联系人信息
    public User[] findUserByKey(String key){
        Vector<User> v =new Vector<User>();
        Cursor cursor = null;
        try{
            cursor = db.find("select * from "+TABLENAME+" where "
                    +User.NAME+" like '%"+key+"%' "+" or "+User.MOBILE+" like '%"+key+"%' "
                    +" or "+User.DANWEI+" like '%"+key+"%' "
                    +" or "+User.QQ+" like '%"+key+"%' "
                    +" or "+User.ADDRESS+" like '%"+key+"%' ",null);
            while (cursor.moveToNext()){
                User temp = new User();
                temp.setId_DB(cursor.getInt(
                        cursor.getColumnIndex("id_DB")));
                temp.setName(cursor.getString(
                        cursor.getColumnIndex(User.NAME)));
                temp.setMobile(cursor.getString(
                        cursor.getColumnIndex(User.MOBILE)));
                temp.setDanwei(cursor.getString(
                        cursor.getColumnIndex(User.DANWEI)));
                temp.setQq(cursor.getString(
                        cursor.getColumnIndex(User.QQ)));
                temp.setAddress(cursor.getString(cursor.getColumnIndex(User.ADDRESS)));
                v.add(temp);

            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {

            if(cursor != null){
                cursor.close();
            }
            db.closeConnection();
        }
        if(v.size() > 0){
            return v.toArray(new User[] {});

        }else {

            User [] users = new User[1];
            User user = new User();
            user.setName("无结果");
            users[0]=user;
            return users;
        }

    }
    public boolean deleteByUser(User user){
        return db.delete(TABLENAME,"id_DB=?",new String []{user.getId_DB()+""});

    }

}
