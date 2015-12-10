package com.example.administrator.work4;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddContactsActivity extends Activity {


    private EditText nameET,mobileET,qqET,danweiET,addressET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameET = (EditText) findViewById(R.id.name);
        danweiET = (EditText) findViewById(R.id.danwei);
        mobileET = (EditText) findViewById(R.id.mobile);
        qqET = (EditText) findViewById(R.id.qq);
        addressET = (EditText) findViewById(R.id.address);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_contacts,menu);
        menu.add(0,1,1, "保存");
        menu.add(0, 2, 2, "返回");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case 1:  //保存
                if(!nameET.getText().toString().equals("")){
                    User user = new User();
                    user.setName(nameET.getText().toString());
                    user.setDanwei(danweiET.getText().toString());
                    user.setMobile(mobileET.getText().toString());
                    user.setQq(qqET.getText().toString());
                    user.setAddress(addressET.getText().toString());
                    ContactsTable ct = new ContactsTable(this);
                    if(ct.addData(user)){
                        Toast.makeText(this,"添加成功！",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(this,"添加失败！",Toast.LENGTH_LONG).show();

                    }
                }else {
                    Toast.makeText(this,"请先输入姓名！",Toast.LENGTH_LONG).show();
                }
                break;
            case 2: //返回
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
