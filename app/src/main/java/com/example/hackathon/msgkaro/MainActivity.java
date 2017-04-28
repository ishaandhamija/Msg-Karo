package com.example.hackathon.msgkaro;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button send;
    EditText et, msgEt;
    ImageView pickContact;
    int c;
    static final int PICK_CONTACT=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send = (Button) findViewById(R.id.send);
        et = (EditText) findViewById(R.id.et);
        msgEt = (EditText) findViewById(R.id.msgEt);
        pickContact = (ImageView) findViewById(R.id.pickContact);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c = 0;
                for (int i=0;i<10;i++){
                    if (msgEt.getText().toString() == null) {
                        sendMessage(et.getText().toString(), "Mathur _|_");
                    }
                    else{
                            sendMessage(et.getText().toString(), msgEt.getText().toString());
                    }
                }
            }
        });

        pickContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickContact();
            }
        });

    }

    public boolean sendMessage(String phno, String msg) {
        try {
            if (phno == null) {
                return false;
            } else {
                SmsManager smsmanager = SmsManager.getDefault();
                smsmanager.sendTextMessage(phno, null, msg, null, null);
                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, "MessgAct Ecxp", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void pickContact(){

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);

    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                                    null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
                            et.setText(cNumber);
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

                    }
                }
                break;
        }
    }

}
