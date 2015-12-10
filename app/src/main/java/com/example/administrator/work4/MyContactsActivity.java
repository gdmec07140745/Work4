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

    private ListView lv;               //����б�
    private BaseAdapter lvAdapter;     //ListView �б�������
    private User users [];             //ͨѶ¼�û�
    private int selectItem = 0;        //��ǰѡ��
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts);
        lv = (ListView) findViewById(R.id.listView);
        loadContacts();
    }

    //������ϵ���б�
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
        menu.add(0,1,1,"���");
        menu.add(0,2,2,"�༭");
        menu.add(0,3,3,"�鿴��Ϣ");
        menu.add(0,4,4,"ɾ��");
        menu.add(0,5,5,"��ѯ");
        menu.add(0, 6, 6, "���뵽�ֻ��绰��");
        menu.add(0, 7, 7, "�˳�");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case 1: //���
                Intent intent = new Intent(MyContactsActivity.this, AddContactsActivity.class);
                startActivity(intent);
                break;
            case 2: //�༭
                if (users[selectItem].getId_DB() > 0) {
                    intent = new Intent(MyContactsActivity.this, UpdateContactsActivity.class);
                    intent.putExtra("user_ID", users[selectItem].getId_DB());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "�޽����¼���޷�������", Toast.LENGTH_LONG).show();
                }
                break;
            case 3: //�鿴��Ϣ
                if (users[selectItem].getId_DB() > 0) {
                    intent = new Intent(MyContactsActivity.this, ContactsMessageActivity.class);
                    intent.putExtra("user_ID", users[selectItem].getId_DB());
                    startActivity(intent);

                } else {
                    Toast.makeText(this, "�޽����¼,�޷�������", Toast.LENGTH_LONG).show();
                }
                break;
            case 4: //ɾ��
                if (users[selectItem].getId_DB() > 0) {
                    delete();
                } else {
                    Toast.makeText(this, "�޽����¼,�޲�����", Toast.LENGTH_LONG).show();
                }
                break;

            case 5:   //��ѯ
                new FindDialog(MyContactsActivity.this).show();
                break;
            case 6:   //���뵽�ֻ��绰��
                if (users[selectItem].getId_DB() > 0) {
                    importPhone(users[selectItem].getName(), users[selectItem].getMobile());
                    Toast.makeText(this, "�Ѿ��ɹ�����'" + users[selectItem].getName() + "'���ֻ��绰����", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "�޽����¼,�޷�������", Toast.LENGTH_LONG).show();
                }
                break;
            case 7: //�˳�
                finish();
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public void onResume(){
        super.onResume();
        ContactsTable ct =new ContactsTable(this);   //���¼�������
        users = ct.getAllUser();
        lvAdapter.notifyDataSetChanged();            //ˢ���б�
    }
    public void delete(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("ϵͳ��Ϣ");
        alert.setMessage("�Ƿ�Ҫɾ����ϵ�ˣ�");
        alert.setPositiveButton("��",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichbutton) {
                        ContactsTable ct = new ContactsTable(MyContactsActivity.this);
                        //ɾ����ϵ����Ϣ
                        if (ct.deleteByUser(users[selectItem])) {
                            //���»�ȡ����
                            users = ct.getAllUser();
                            //ˢ���б�
                            lvAdapter.notifyDataSetChanged();
                            selectItem = 0;
                            Toast.makeText(MyContactsActivity.this, "ɾ���ɹ���", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MyContactsActivity.this, "ɾ��ʧ�ܣ�", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        alert.setNegativeButton("��",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        alert.show();
    }



    //���뵽�ֻ��绰��
    public void importPhone(String name,String phone){
        //ϵͳͨѶ¼ ContentProvider��URI
        Uri phoneURL = ContactsContract.Data.CONTENT_URI;
        ContentValues values = new ContentValues();
        //������RawContacts.CONTENT_URIִ��һ����ֵ���룬Ŀ���ǻ�ȡϵͳ���ص�rawContactId
        Uri rawContactUri =this.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI,values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        //��data�������������
        values.clear();
        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Contacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        this.getContentResolver().insert(phoneURL, values);
        //��data�����绰����
        values.clear();
        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Contacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,phone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        this.getContentResolver().insert(phoneURL,values);






    }
}
