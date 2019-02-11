package com.example.pusika.callarmy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_CONTACTS = 1;
    private static boolean READ_CONTACTS_GRANTED = false;
    boolean isFirstPost = true;

    int money = 0;
    int cost = 0;

    ListView contactList;
    private ArrayList contacts = new ArrayList();
    private ArrayList army = new ArrayList();
    BroadcastReceiver broadcastReceiver;
    TextView moneyTextView;
    TextView costOfArmyTextView;
    TextView sizeOfArmyTextView;
    ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        setTitle("Найм армии");

        moneyTextView = findViewById(R.id.money);
        costOfArmyTextView = findViewById(R.id.costOfArmy);
        sizeOfArmyTextView = findViewById(R.id.sizeOfArmy);

        contactList = findViewById(R.id.contactList);
        broadcastReceiver = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                if (isFirstPost) {
                    contacts = (ArrayList) intent.getSerializableExtra(MainActivity.ARMY);
                    getContact();
                    isFirstPost = false;
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(MainActivity.BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(broadcastReceiver, intentFilter);
        getContact();


        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // получаем выбранный пункт
                Contact selectedContact = (Contact) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Был нанят " + selectedContact.getName(),
                        Toast.LENGTH_SHORT).show();

                army.add(selectedContact);
                money = money - Integer.parseInt(selectedContact.getPhone());
                moneyTextView.setText("Наличные средства: " + String.valueOf(money));
                cost = cost - Integer.parseInt(selectedContact.getPhone());
                costOfArmyTextView.setText("Стоимость армии: " + String.valueOf(cost));
                contacts.remove(selectedContact);
                sizeOfArmyTextView.setText("В лагере: " + String.valueOf(contacts.size()));
                contactAdapter.notifyDataSetChanged();
            }
        };
        contactList.setOnItemClickListener(itemListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getContact() {
        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = (Contact) contacts.get(i);
            if (contact.getName().equals("Жижа")) {
                if (contact.getPhone().matches("\\d+")) {
                    money = money + 10;
                }
                contacts.remove(i);
                i--;
            } else {
                if (contact.getPhone().matches("\\d+")) {
                    cost = cost + Integer.parseInt(contact.getPhone());
                }
            }
        }
        // создаем адаптер
        contactAdapter = new ContactAdapter(this, R.layout.contact, contacts);
//        // устанавливаем адаптер
        contactList.setAdapter(contactAdapter);
        moneyTextView.setText("Наличные средства: " + String.valueOf(money));
        costOfArmyTextView.setText("Стоимость армии: " + String.valueOf(cost));
        sizeOfArmyTextView.setText("В лагере: " + String.valueOf(contacts.size()));
    }

    public void leaveCamp(View view) {
        Intent data = new Intent();
        data.putExtra(MainActivity.TROOP, (Serializable) army);
        data.putExtra(MainActivity.TREASURE, (Serializable) money);
        setResult(RESULT_OK, data);
        finish();
    }
}
