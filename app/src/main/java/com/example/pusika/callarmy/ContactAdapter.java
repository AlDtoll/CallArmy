package com.example.pusika.callarmy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private LayoutInflater inflater;
    private int layout;
    private List<Contact> contacts;

    public ContactAdapter(@NonNull Context context, int resource, @NonNull List<Contact> contacts) {
        super(context, resource, contacts);
        this.contacts = contacts;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        TextView nameView = view.findViewById(R.id.nameOfContact);
        TextView phoneView = view.findViewById(R.id.phoneOfContact);
        TextView DMGAndHP = view.findViewById(R.id.DMGAndHP);
        TextView iconView = view.findViewById(R.id.iconOfContact);

        ImageView isTrap = view.findViewById(R.id.isTrap);
        ImageView isFight = view.findViewById(R.id.isFight);
        ImageView isBow = view.findViewById(R.id.isBow);
        ImageView isMagic = view.findViewById(R.id.isMagic);

        ImageView isGiant = view.findViewById(R.id.isGiant);
        ImageView isArmory = view.findViewById(R.id.isArmory);
        ImageView isUsual = view.findViewById(R.id.isUsual);
        ImageView isFly = view.findViewById(R.id.isFly);

        Contact contact = contacts.get(position);

        nameView.setText(contact.getName());
        phoneView.setText(contact.getPhone());
        DMGAndHP.setText(contact.getDMGAndHP());

        isTrap.setImageResource(contact.getIsTrap());
        isFight.setImageResource(contact.getIsFight());
        isBow.setImageResource(contact.getIsBow());
        isMagic.setImageResource(contact.getIsMagic());

        isGiant.setImageResource(contact.getIsGiant());
        isArmory.setImageResource(contact.getIsArmory());
        isUsual.setImageResource(contact.getIsUsual());
        isFly.setImageResource(contact.getIsFly());

        iconView.setText(contact.getIcon());

        return view;
    }
}
