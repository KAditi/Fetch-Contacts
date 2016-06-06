/*
 * ContactDataBaseHandler class handles database related activities 
 * like Create Table, Insert and select queries
 *  
 * @author Aditi Kulkarni
 * 
 */
package com.project.fetch.contactdetails;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDataBaseHandler extends SQLiteOpenHelper{
	
	// Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "contactsManager";
 
    // Contacts table name
    private static final String TABLE_CONTACTS = "contactTable";
    
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_EMAIL = "email";
    
	public ContactDataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	/*
	 * (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 * @param SQLiteDatabase instance
	 * @return void
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + " (" +
                KEY_ID + " INTEGER, " +
				KEY_PH_NO + " TEXT, " +
				KEY_EMAIL + " TEXT, " +
                KEY_NAME + " TEXT);";
		
		
        db.execSQL(CREATE_CONTACTS_TABLE);
		
	}

	/*
	 * (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 * @param SQLiteDatabase instance This is first parameter
	 * @param int Second parameter is integer value
	 * @param int Third parameter is integer
	 * @return void
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		 
        // Create tables again
        onCreate(db);
	}
	
	/*
	 * Following method insert contact values to database
	 * @param ContactBean instance (ContactBean is wrapper class for contact details)
	 * @return void 
	 */
	public void addContact(ContactsBean contactBean)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
		values.put(KEY_ID, contactBean.id);
		values.put(KEY_NAME,contactBean.Name);
		values.put(KEY_PH_NO,contactBean.phoneNumber);		
		values.put(KEY_EMAIL,contactBean.Email);
		
		db.insert(TABLE_CONTACTS, null, values);
	    db.close();
	}
	
	/*
	 * Following method get all contacts from database and add those to list
	 * @return returns List of ContactBean objects
	 */
    public List<ContactsBean> getAllContacts() {
        List<ContactsBean> contactList = new ArrayList<ContactsBean>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // Following code loop through all rows and add those to list
        if (cursor.moveToFirst()) {
            do {
            	ContactsBean contact = new ContactsBean();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(3));
                contact.setPhoneNumber(cursor.getString(1));
                contact.setEmail(cursor.getString(2));
                // Adding contacts to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return contactList;
    }
	

}
