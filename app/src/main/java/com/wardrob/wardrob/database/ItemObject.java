package com.wardrob.wardrob.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "item_table")
public class ItemObject
{
    @PrimaryKey(autoGenerate = true)
        private int uid;
    @ColumnInfo(name = "user_name")
        private String userName;
    @ColumnInfo(name = "item_name")
        private String itemName;
    @ColumnInfo(name = "item_image_name")
        private String itemImageName;
    @ColumnInfo(name = "item_main_category")
    private String itemMainCategory;
    @ColumnInfo(name = "item_category")
        private String itemCategory;
    @ColumnInfo(name = "item_relations")
        private String itemRelations;
    @ColumnInfo(name = "item_color")
        private Integer itemColor;
    @ColumnInfo(name = "item_purchase_date")
        private String itemPurchaseData;
    @ColumnInfo(name = "item_price")
        private String itemPrice;
    @ColumnInfo(name = "item_warms")
        private float itemWarms;
    @ColumnInfo(name = "item_favor")
        private float itemFavor;
    @ColumnInfo(name = "item_is_visible")
        private boolean itemVisibility;
    @ColumnInfo(name = "item_washing_1")
        private String itemWashing1;
    @ColumnInfo(name = "item_washing_2")
        private String itemWashing2;
    @ColumnInfo(name = "item_washing_3")
        private String itemWashing3;
    @ColumnInfo(name = "item_washing_4")
        private String itemWashing4;
    @ColumnInfo(name = "item_washing_5")
        private String itemWashing5;
//=============================================================
    public void setUid(int id)
    { uid = id;}
    public int getUid()
    { return uid;}
//=============================================================
    public void setUserName(String name)
    { userName = name;}
    public String getUserName()
    { return userName;}
//=============================================================
    public void setItemName(String item)
    { itemName = item;}
    public String getItemName()
    { return itemName;}
//=============================================================
    public void setItemImageName(String item)
    {  itemImageName = item;}
    public String getItemImageName()
    { return itemImageName;}
//=============================================================
    public void setItemMainCategory(String item)
    {  itemMainCategory = item;}
    public String getItemMainCategory()
    { return itemMainCategory;}
//=============================================================
    public void setItemCategory(String item)
    {  itemCategory = item;}
    public String getItemCategory()
    { return itemCategory;}
//=============================================================
    public void setItemRelations(String item)
    {  itemRelations = item;}
    public String getItemRelations()
    { return itemRelations;}
//=============================================================
    public void setItemColor(Integer item)
    {  itemColor = item;}
    public Integer getItemColor()
    { return itemColor;}
//=============================================================
    public void setItemPurchaseData(String item)
    { itemPurchaseData = item;}
    public String getItemPurchaseData()
    { return itemPurchaseData;}
//=============================================================
    public void setItemPrice(String item)
    {  itemPrice = item;}
    public String getItemPrice()
    { return itemPrice;}
//=============================================================
    public void setItemWarms(float item)
    {  itemWarms = item;}
    public float getItemWarms()
    { return itemWarms;}
//=============================================================
    public void setItemFavor(float item)
    { itemFavor = item;}
    public float getItemFavor()
    { return itemFavor;}
//=============================================================
    public void setItemVisibility(boolean item)
    {  itemVisibility = item;}
    public boolean getItemVisibility()
    { return itemVisibility;}
//=============================================================
    public void setItemWashing1(String item)
    { itemWashing1  = item;}
    public String getItemWashing1()
    { return itemWashing1;}
//=============================================================
    public void setItemWashing2(String item)
    {  itemWashing2 = item;}
    public String getItemWashing2()
    { return itemWashing2;}
//=============================================================
    public void setItemWashing3(String item)
    { itemWashing3 = item;}
    public String getItemWashing3()
    { return itemWashing4;}
//=============================================================
    public void setItemWashing4(String item)
    { itemWashing4 = item;}
    public String getItemWashing4()
    { return itemWashing4;}
//=============================================================
    public void setItemWashing5(String item)
    { itemWashing5 = item;}
    public String getItemWashing5()
    { return itemWashing5;}

    public int getItemId() {
        return uid;
    }
//=============================================================
}
