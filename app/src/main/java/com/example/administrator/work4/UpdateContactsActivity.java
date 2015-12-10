package com.example.administrator.work4;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateContactsActivity extends Activity {
    private EditText nameET,mobileET,qqET,danweiET,addressET;    //UI�ؼ�����
    private User user;                                          // ��ϵ�˶���
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameET = (EditText) findViewById(R.id.name);
        danweiET = (EditText) findViewById(R.id.danwei);
        mobileET = (EditText) findViewById(R.id.mobile);
        qqET = (EditText) findViewById(R.id.qq);
        addressET = (EditText) findViewById(R.id.address);
        Bundle localBundle = getIntent().getExtras();
        int id = localBundle.getInt("user_ID");
        ContactsTable ct = new ContactsTable(this);
        user = ct.getUserByID(id);
        nameET.setText(user.getName());
        danweiET.setText(user.getDanwei());
        mobileET.setText(user.getMobile());
        qqET.setText(user.getQq());
        addressET.setText(user.getAddress());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_update_contacts, menu);
        menu.add(Menu.NONE, 1, Menu.NONE, "����");
        menu.add(Menu.NONE,2,Menu.NONE,"����");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case 1:    //����
                if(!nameET.getText().toString().equals(""))
                {
                    user.setName(nameET.getText().toString());
                    user.setDanwei(danweiET.getText().toString());
                    user.setMobile(mobileET.getText().toString());
                    user.setQq(qqET.getText().toString());
                    user.setAddress(addressET.getText().toString());
                    ContactsTable ct = new ContactsTable(UpdateContactsActivity.this);

                    //�޸����ݿ���ϵ����Ϣ
                    if (ct.updateUser(user)){
                        Toast.makeText(UpdateContactsActivity.this,"�޸ĳɹ���",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(UpdateContactsActivity.this,"�޸�ʧ�ܣ�",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(UpdateContactsActivity.this,"���ݲ���Ϊ�գ�",Toast.LENGTH_LONG).show();
                }
                break;
            case 2:   //����
                finish();
                break;
            default:
                break;
        }
        /*//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }
}
