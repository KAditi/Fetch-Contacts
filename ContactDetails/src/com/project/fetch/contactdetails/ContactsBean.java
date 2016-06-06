package com.project.fetch.contactdetails;

public class ContactsBean {
	
	public int id;
	public String phoneNumber;
	public String Name;
	public String Email;
	public ContactsBean(){	}
	public ContactsBean(int id, String phone_number,String name, String Email)
	{
		this.id = id;
		this.phoneNumber = phone_number;
		this.Name = name;
		this.Email = Email;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	

}
