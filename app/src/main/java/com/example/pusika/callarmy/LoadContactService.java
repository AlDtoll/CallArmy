package com.example.pusika.callarmy;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadContactService extends Service {

    final String LOG_TAG = "myLogs";
    ExecutorService executorService;
    private List<Contact> contacts = new ArrayList();
    int counter = 0;

    public LoadContactService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        executorService = Executors.newFixedThreadPool(1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyRun mr = new MyRun(startId);
        executorService.execute(mr);
        return super.onStartCommand(intent, flags, startId);
    }

    class MyRun implements Runnable {

        int startId;

        MyRun(int startId) {
            this.startId = startId;
            Log.d(LOG_TAG, "MyRun#" + startId + " create");
        }

        @Override
        public void run() {
            Intent contactIntent = new Intent(MainActivity.BROADCAST_ACTION);
            Cursor cursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    final String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        // get the phone number
                        Cursor pCur = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id},
                                null);
                        if (pCur != null) {
                            while (pCur.moveToNext()) {
                                String phone = pCur.getString(
                                        pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Contact currentContact = createMonsterFromPerson(name, phone);
                                contacts.add(currentContact);
                            }
                            pCur.close();
                        }
                    }
                }
                cursor.close();
            }
            Log.d(LOG_TAG, "Сбор контактов закончен");
            contactIntent.putExtra(MainActivity.ARMY, (Serializable) contacts);
            //TODO лажа. нужно по другому как-то делать
            boolean s = true;
            while (s) {
                sendBroadcast(contactIntent);
            }
            stop();
        }

        void stop() {
            Log.d(LOG_TAG, "MyRun#" + startId + " end, stopSelfResult("
                    + startId + ") = " + stopSelfResult(startId));
        }
    }

    private String createIcon(String name) {
        String firstLetter = "B";
        String secondLetter = "B";
        int endIndex = 0;
        for (int i = 0; i < name.length(); i++) {
            if (Character.isUpperCase(name.charAt(i))) {
                endIndex = i + 1;
                firstLetter = name.substring(i, endIndex);
                break;
            } else {
                firstLetter = name.substring(0, 1);
            }
        }
        for (int i = endIndex; i < name.length(); i++) {
            if (Character.isUpperCase(name.charAt(i))) {
                secondLetter = name.substring(i, i + 1);
                break;
            } else {
                secondLetter = "";
            }
        }
        return firstLetter + secondLetter;
    }

    private Contact createMonsterFromPerson(String name, String phone) {
        String nameOfMonster;
        String price = phone.substring(phone.length() - 2);
        String DMGAndHP = "1/1";
        int isTrap = R.drawable.none;
        int isFight = R.drawable.none;
        int isBow = R.drawable.none;
        int isMagic = R.drawable.none;

        int isGiant = R.drawable.none;
        int isArmory = R.drawable.none;
        int isUsual = R.drawable.none;
        int isFly = R.drawable.none;
        if (checkName(name, "андрей", "дрон")) {
            nameOfMonster = "Мурлок";
            DMGAndHP = "5/7";
            isFight = R.drawable.isfight;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "максим", "макс", "мася")) {
            nameOfMonster = "Минотавр";
            DMGAndHP = "12/20";
            isFight = R.drawable.isfight;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Альберт")) {
            nameOfMonster = "Орк";
            DMGAndHP = "15/12";
            isFight = R.drawable.isfight;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Алексей", "леша", "леха", "алекс")) {
            nameOfMonster = "Зомби";
            DMGAndHP = "4/6";
            isFight = R.drawable.isfight;
            isArmory = R.drawable.isarmory;
        } else if (checkName(name, "Александр", "саша", "саня", "Александра")) {
            nameOfMonster = "Кентавр";
            DMGAndHP = "6/12";
            isBow = R.drawable.isbow;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Алиса")) {
            nameOfMonster = "Гарпия";
            DMGAndHP = "4/6";
            isFight = R.drawable.isfight;
            isFly = R.drawable.isfly;
        } else if (checkName(name, "Антон", "тоха")) {
            nameOfMonster = "Грифон";
            DMGAndHP = "6/12";
            isFight = R.drawable.isfight;
            isFly = R.drawable.isfly;
        } else if (checkName(name, "Владимир", "Влад", "Владик")) {
            nameOfMonster = "оборотень";
            DMGAndHP = "5/10";
            isTrap = R.drawable.istrap;
            isFight = R.drawable.isfight;
            isArmory = R.drawable.isarmory;
        } else if (checkName(name, "Алефтина")) {
            nameOfMonster = "ведьма";
            DMGAndHP = "5/10";
            isMagic = R.drawable.ismagic;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Артем", "Тема")) {
            nameOfMonster = "гоблин";
            DMGAndHP = "4/6";
            isFight = R.drawable.isfight;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Аркадий", "Аркаша")) {
            nameOfMonster = "гном";
            DMGAndHP = "5/15";
            isFight = R.drawable.isfight;
            isArmory = R.drawable.isarmory;
        } else if (checkName(name, "Никита", "ник", "кит")) {
            nameOfMonster = "болотный тролль";
            DMGAndHP = "15/30";
            isFight = R.drawable.isfight;
            isGiant = R.drawable.isgiant;
        } else if (checkName(name, "Артур", "артурчик")) {
            nameOfMonster = "единорог";
            DMGAndHP = "7/12";
            isFight = R.drawable.isfight;
            isMagic = R.drawable.ismagic;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Валера", "лера")) {
            nameOfMonster = "мантикора";
            DMGAndHP = "14/20";
            isFight = R.drawable.isfight;
            isGiant = R.drawable.isgiant;
            isFly = R.drawable.isfly;
        } else if (checkName(name, "Аристарх")) {
            nameOfMonster = "энт";
            DMGAndHP = "10/30";
            isTrap = R.drawable.istrap;
            isGiant = R.drawable.isgiant;
            isFight = R.drawable.isfight;
            isArmory = R.drawable.isarmory;
        } else if (checkName(name, "Екатерина", "катя", "катюша")) {
            nameOfMonster = "фея";
            DMGAndHP = "2/3";
            isMagic = R.drawable.ismagic;
            isFly = R.drawable.isfly;
        } else if (checkName(name, "Борис", "боря", "борька")) {
            nameOfMonster = "беорн";
            DMGAndHP = "20/10";
            isFight = R.drawable.isfight;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Анастасия", "настя", "настенька")) {
            nameOfMonster = "ночная эльфийка";
            DMGAndHP = "6/7";
            isBow = R.drawable.isbow;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Богдан", "бодя")) {
            nameOfMonster = "Голем";
            DMGAndHP = "5/10";
            isFight = R.drawable.isfight;
            isArmory = R.drawable.isarmory;
        } else if (checkName(name, "Вадим", "вадик")) {
            nameOfMonster = "горный тролль";
            DMGAndHP = "16/32";
            isFight = R.drawable.isfight;
            isGiant = R.drawable.isgiant;
        } else if (checkName(name, "Алла", "алина")) {
            nameOfMonster = "русалка";
            DMGAndHP = "4/12";
            isMagic = R.drawable.ismagic;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Марина", "маришка")) {
            nameOfMonster = "нага";
            DMGAndHP = "12/14";
            isFight = R.drawable.isfight;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Кристина", "кристя", "кристинка")) {
            nameOfMonster = "демоннеса";
            DMGAndHP = "6/7";
            isMagic = R.drawable.ismagic;
            isBow = R.drawable.isbow;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Маргарита", "рита")) {
            nameOfMonster = "суккуб";
            DMGAndHP = "5/10";
            isFight = R.drawable.isfight;
            isTrap = R.drawable.istrap;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Оксана")) {
            nameOfMonster = "кикимора";
            DMGAndHP = "6/7";
            isMagic = R.drawable.ismagic;
            isTrap = R.drawable.istrap;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Сергей", "серый")) {
            nameOfMonster = "василиск";
            DMGAndHP = "4/13";
            isFight = R.drawable.isfight;
            isTrap = R.drawable.istrap;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Анна", "аня")) {
            nameOfMonster = "виверна";
            DMGAndHP = "8/14";
            isFight = R.drawable.isfight;
            isFly = R.drawable.isfly;
        } else if (checkName(name, "Ксения", "ксюша")) {
            nameOfMonster = "высшая эльфийка";
            DMGAndHP = "6/7";
            isBow = R.drawable.isbow;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Наталья", "ната", "наташка")) {
            nameOfMonster = "гаргулья";
            DMGAndHP = "5/10";
            isFight = R.drawable.isfight;
            isArmory = R.drawable.isarmory;
            isFly = R.drawable.isfly;
        } else if (checkName(name, "Виктория", "вика", "викуся")) {
            nameOfMonster = "гидра";
            DMGAndHP = "20/40";
            isFight = R.drawable.isfight;
            isGiant = R.drawable.isgiant;
        } else if (checkName(name, "Татьяна", "танька", "таня")) {
            nameOfMonster = "ламия";
            DMGAndHP = "5/10";
            isTrap = R.drawable.istrap;
            isMagic = R.drawable.ismagic;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Галина", "галя")) {
            nameOfMonster = "джин";
            DMGAndHP = "5/10";
            isBow = R.drawable.isbow;
            isFight = R.drawable.isfight;
            isMagic = R.drawable.ismagic;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Томара", "тома", "тамара", "томка")) {
            nameOfMonster = "Имп";
            DMGAndHP = "1/3";
            isFight = R.drawable.isfight;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Владлена")) {
            nameOfMonster = "бес";
            DMGAndHP = "3/7";
            isFight = R.drawable.isfight;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Ирина", "ира", "ирка")) {
            nameOfMonster = "гуль";
            DMGAndHP = "6/7";
            isFight = R.drawable.isfight;
            isArmory = R.drawable.isarmory;
        } else if (checkName(name, "Евгения", "Евгений", "женя", "жека")) {
            nameOfMonster = "йотун";
            DMGAndHP = "20/45";
            isFight = R.drawable.isfight;
            isGiant = R.drawable.isgiant;
        } else if (checkName(name, "Игорь", "игорек")) {
            nameOfMonster = "волколак";
            DMGAndHP = "6/9";
            isFight = R.drawable.isfight;
            isTrap = R.drawable.istrap;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Елена", "лена", "ленок")) {
            nameOfMonster = "эльфийка крови";
            DMGAndHP = "6/7";
            isBow = R.drawable.isbow;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Надежда", "надя")) {
            nameOfMonster = "валькирия";
            DMGAndHP = "6/10";
            isFight = R.drawable.isfight;
            isFly = R.drawable.isfly;
        } else if (checkName(name, "Диана")) {
            nameOfMonster = "лесная эльфийка";
            DMGAndHP = "6/7";
            isBow = R.drawable.isbow;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Юра", "юрик")) {
            nameOfMonster = "огр";
            DMGAndHP = "11/33";
            isFight = R.drawable.isfight;
            isGiant = R.drawable.isgiant;
        } else if (checkName(name, "Зоя")) {
            nameOfMonster = "полурослик";
            DMGAndHP = "4/7";
            isTrap = R.drawable.istrap;
            isBow = R.drawable.isbow;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Пётр", "петя")) {
            nameOfMonster = "чёрный дракон";
            DMGAndHP = "21/40";
            isFight = R.drawable.isfight;
            isBow = R.drawable.isbow;
            isGiant = R.drawable.isgiant;
            isArmory = R.drawable.isarmory;
            isFly = R.drawable.isfly;
        } else if (checkName(name, "Лидия", "лида")) {
            nameOfMonster = "медуза горгона";
            DMGAndHP = "8/12";
            isBow = R.drawable.isbow;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Дмитрий", "дима", "димка")) {
            nameOfMonster = "вампир";
            DMGAndHP = "8/12";
            isTrap = R.drawable.istrap;
            isFight = R.drawable.isfight;
            isArmory = R.drawable.isarmory;
        } else if (checkName(name, "Егор", "егорка", "маньяк")) {
            nameOfMonster = "архилич";
            DMGAndHP = "10/15";
            isBow = R.drawable.isbow;
            isMagic = R.drawable.ismagic;
            isArmory = R.drawable.isarmory;
        } else if (checkName(name, "Данил", "сохатый", "даня")) {
            nameOfMonster = "высший дракон ";
            DMGAndHP = "18/30";
            isFight = R.drawable.isfight;
            isBow = R.drawable.isbow;
            isMagic = R.drawable.ismagic;
            isGiant = R.drawable.isgiant;
            isArmory = R.drawable.isarmory;
        } else if (checkName(name, "Константин", "костя")) {
            nameOfMonster = "демиург";
            DMGAndHP = "20/10";
            isMagic = R.drawable.ismagic;
            isUsual = R.drawable.isusual;
        } else if (checkName(name, "Михаил", "миша", "мишка", "мишачья")) {
            nameOfMonster = "архидемон";
            DMGAndHP = "15/30";
            isTrap = R.drawable.istrap;
            isFight = R.drawable.isfight;
            isMagic = R.drawable.ismagic;

            isGiant = R.drawable.isgiant;
            isFly = R.drawable.isfly;
        } else {
            nameOfMonster = "жижа";
        }
        counter++;
        return new Contact(capitalize(nameOfMonster), price, "Егоров\nдолг", DMGAndHP, isTrap, isBow, isFight, isMagic, isGiant, isArmory, isUsual, isFly);
    }

    private boolean checkName(String currentName, String... names) {
        for (String name : names) {
            if (currentName.toLowerCase().contains(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
