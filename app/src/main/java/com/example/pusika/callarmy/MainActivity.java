package com.example.pusika.callarmy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final String TROOP = "TROOP";
    static final String ARMY = "ARMY";
    static final String TREASURE = "TREASURE";

    public final static String BROADCAST_ACTION = "com.example.pusika.callarmy";
    public final static String SAVED_TEXT = "saved_text";

    private static final int REQUEST_ACCESS_TYPE = 1;

    private static final int REQUEST_CODE_READ_CONTACTS = 1;
    private static boolean READ_CONTACTS_GRANTED = false;

    ListView troopList;
    private ArrayList troop = new ArrayList();
    TextView treasureTextView;
    TextView sizeOfTroopTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        troopList = findViewById(R.id.troopList);
        treasureTextView = findViewById(R.id.treasure);
        sizeOfTroopTextView = findViewById(R.id.sizeOfTroop);

        // Создаем Intent для вызова сервиса,
        Intent loadContactServiceIntent = new Intent(this, LoadContactService.class);
        // стартуем сервис
        int hasReadContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        // если устройство до API 23, устанавливаем разрешение
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
            READ_CONTACTS_GRANTED = true;
        } else {
            // вызываем диалоговое окно для установки разрешений
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }
        // если разрешение установлено, загружаем контакты
        if (READ_CONTACTS_GRANTED) {
            startService(loadContactServiceIntent);
        }
    }

    public void callArmy(View view) {
        final Context context = getApplicationContext();
        Intent contactIntent = new Intent(context, ContactActivity.class);
        startActivityForResult(contactIntent, REQUEST_ACCESS_TYPE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    READ_CONTACTS_GRANTED = true;
                }
        }
        if (READ_CONTACTS_GRANTED) {
            Intent loadContactServiceIntent = new Intent(this, LoadContactService.class);
            startService(loadContactServiceIntent);
        } else {
            Toast.makeText(this, "Требуется установить разрешения", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ACCESS_TYPE) {
            if (resultCode == RESULT_OK) {
                int treasure = data.getIntExtra(TREASURE, 0);
                treasureTextView.setText("Наличные средства: " + String.valueOf(treasure));
                troop = (ArrayList) data.getSerializableExtra(MainActivity.TROOP);
                sizeOfTroopTextView.setText("Существ в отряде: " + String.valueOf(troop.size()));
                // создаем адаптер
                ContactAdapter contactAdapter = new ContactAdapter(this, R.layout.contact, troop);
//        // устанавливаем адаптер
                troopList.setAdapter(contactAdapter);

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent loadContactServiceIntent = new Intent(this, LoadContactService.class);
        stopService(loadContactServiceIntent);
    }
}
