package com.example.pusika.callarmy;

import java.io.Serializable;

public class Contact implements Serializable {

    private String name;
    private String phone;
    private String icon;
    private String DMGAndHP;

    private int isTrap;
    private int isBow;
    private int isFight;
    private int isMagic;

    private int isGiant;
    private int isArmory;
    private int isUsual;
    private int isFly;

    public Contact(String name, String phone, String icon, String DMGAndHP, int isTrap, int isBow, int isFight, int isMagic, int isGiant, int isArmory, int isUsual, int isFly) {
        this.name = name;
        this.phone = phone;
        this.icon = icon;
        this.DMGAndHP = DMGAndHP;
        this.isTrap = isTrap;
        this.isBow = isBow;
        this.isFight = isFight;
        this.isMagic = isMagic;
        this.isGiant = isGiant;
        this.isArmory = isArmory;
        this.isUsual = isUsual;
        this.isFly = isFly;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDMGAndHP() {
        return DMGAndHP;
    }

    public void setDMGAndHP(String DMGAndHP) {
        this.DMGAndHP = DMGAndHP;
    }

    public int getIsTrap() {
        return isTrap;
    }

    public void setIsTrap(int isTrap) {
        this.isTrap = isTrap;
    }

    public int getIsBow() {
        return isBow;
    }

    public void setIsBow(int isBow) {
        this.isBow = isBow;
    }

    public int getIsFight() {
        return isFight;
    }

    public void setIsFight(int isFight) {
        this.isFight = isFight;
    }

    public int getIsMagic() {
        return isMagic;
    }

    public void setIsMagic(int isMagic) {
        this.isMagic = isMagic;
    }

    public int getIsGiant() {
        return isGiant;
    }

    public void setIsGiant(int isGiant) {
        this.isGiant = isGiant;
    }

    public int getIsArmory() {
        return isArmory;
    }

    public void setIsArmory(int isArmory) {
        this.isArmory = isArmory;
    }

    public int getIsUsual() {
        return isUsual;
    }

    public void setIsUsual(int isUsual) {
        this.isUsual = isUsual;
    }

    public int getIsFly() {
        return isFly;
    }

    public void setIsFly(int isFly) {
        this.isFly = isFly;
    }
}
