package com.example.administrator.work4;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ContactsMessageActivity extends Activity {
    private TextView nameTV,mobileTV,danweiTV,qqTV,addressTV;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_message);
        nameTV = (TextView) findViewById(R.id.name);
        danweiTV = (TextView) findViewById(R.id.danwei);
        mobileTV = (TextView) findViewById(R.id.mobile);
        qqTV = (TextView) findViewById(R.id.qq);
        addressTV = (TextView) findViewById(R.id.address);
        Bundle localBundle = getIntent().getExtras();
        int id=localBundle.getInt("user_ID");
        ContactsTable ct =new ContactsTable(this);
        user = ct.getUserByID(id);
        nameTV.setText("姓名："+user.getName());
        danweiTV.setText("单位："+user.getDanwei());
        mobileTV.setText("电话："+user.getMobile());
        qqTV.setText("QQ:"+user.getQq());
        addressTV.setText("地址："+user.getAddress());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts_message, menu);
        menu.add(Menu.NONE, 1, Menu.NONE, "返回");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case 1: //返回
                finish();
                break;
            default:
                break;
        }
       /* //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
