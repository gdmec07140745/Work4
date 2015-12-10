package com.example.administrator.work4;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyContactsActivity extends Activity {

    private ListView lv;               //结果列表
    private BaseAdapter lvAdapter;     //ListView 列表适配器
    private User users [];             //通讯录用户
    private int selectItem = 0;        //当前选择
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts);
        lv = (ListView) findViewById(R.id.listView);
        loadContacts();
    }

    //加载联系人列表
    public  void loadContacts(){
        ContactsTable ct = new ContactsTable(this);
        users = ct.getAllUser();
        lvAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return users.length;
            }

            @Override
            public Object getItem(int i) {
                return users[i];
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    TextView tv = new TextView(MyContactsActivity.this);
                    tv.setTextSize(22);
                    view = tv;


                }
                String mobile = users[i].getMobile() == null?"":users[i].getMobile();
                TextView tv = (TextView) view;
                tv.setText(users[i].getName() + "==============" + mobile);
                if (i == selectItem) {
                    view.setBackgroundColor(Color.YELLOW);

                } else {
                    view.setBackgroundColor(0);
                }
                return view;
            }
        };
        lv.setAdapter(lvAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectItem = i;
                lvAdapter.notifyDataSetChanged();

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_my_contacts, menu);
        menu.add(0,1,1,"添加");
        menu.add(0,2,2,"编辑");
        menu.add(0,3,3,"查看信息");
        menu.add(0,4,4,"删除");
        menu.add(0,5,5,"查询");
        menu.add(0, 6, 6, "导入到手机电话簿");
        menu.add(0, 7, 7, "退出");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case 1: //添加
                Intent intent = new Intent(MyContactsActivity.this, AddContactsActivity.class);
                startActivity(intent);
                break;
            case 2: //编辑
                if (users[selectItem].getId_DB() > 0) {
                    intent = new Intent(MyContactsActivity.this, UpdateContactsActivity.class);
                    intent.putExtra("user_ID", users[selectItem].getId_DB());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "无结果记录，无法操作！", Toast.LENGTH_LONG).show();
                }
                break;
            case 3: //查看信息
                if (users[selectItem].getId_DB() > 0) {
                    intent = new Intent(MyContactsActivity.this, ContactsMessageActivity.class);
                    intent.putExtra("user_ID", users[selectItem].getId_DB());
                    startActivity(intent);

                } else {
                    Toast.makeText(this, "无结果记录,无法操作！", Toast.LENGTH_LONG).show();
                }
                break;
            case 4: //删除
                if (users[selectItem].getId_DB() > 0) {
                    delete();
                } else {
                    Toast.makeText(this, "无结果记录,无操作！", Toast.LENGTH_LONG).show();
                }
                break;

            case 5:   //查询
                new FindDialog(MyContactsActivity.this).show();
                break;
            case 6:   //导入到手机电话薄
                if (users[selectItem].getId_DB() > 0) {
                    importPhone(users[selectItem].getName(), users[selectItem].getMobile());
                    Toast.makeText(this, "已经成功导入'" + users[selectItem].getName() + "'到手机电话簿！", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "无结果记录,无法操作！", Toast.LENGTH_LONG).show();
                }
                break;
            case 7: //退出
                finish();
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public void onResume(){
        super.onResume();
        ContactsTable ct =new ContactsTable(this);   //重新加载数据
        users = ct.getAllUser();
        lvAdapter.notifyDataSetChanged();            //刷新列表
    }
    public void delete(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("系统信息");
        alert.setMessage("是否要删除联系人？");
        alert.setPositiveButton("是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichbutton) {
                        ContactsTable ct = new ContactsTable(MyContactsActivity.this);
                        //删除联系人信息
                        if (ct.deleteByUser(users[selectItem])) {
                            //重新获取数据
                            users = ct.getAllUser();
                            //刷新列表
                            lvAdapter.notifyDataSetChanged();
                            selectItem = 0;
                            Toast.makeText(MyContactsActivity.this, "删除成功！", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MyContactsActivity.this, "删除失败！", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        alert.setNegativeButton("否",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        alert.show();
    }



    //导入到手机电话簿
    public void importPhone(String name,String phone){
        //系统通讯录 ContentProvider的URI
        Uri phoneURL = ContactsContract.Data.CONTENT_URI;
        ContentValues values = new ContentValues();
        //首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
        Uri rawContactUri =this.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI,values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        //往data表插入姓名数据
        values.clear();
        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Contacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        this.getContentResolver().insert(phoneURL, values);
        //往data表插入电话数据
        values.clear();
        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Contacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,phone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        this.getContentResolver().insert(phoneURL,values);






    }
}
