package com.barclays.capstone.main.constants;

/**
 * 
 * @author Aakash Gouri Shankar
 * @Description System Static Variables Declaration.
 * 
 */

public class SystemConstants {

	public static final String LOGIN = "/login";
	public static final String CHANGEPASSWORD = "/changePassword";
	public static final String CHECKCUSTOMERPAN = "/check-customer-pan/{customerId}/{cookieToken}";
	public static final String ADDNEWCUSTOMER = "/add-new-customer/{customerId}/{cookieToken}";
	public static final String ADDNEWACCOUNT = "/add-new-account/{customerId}/{cookieToken}";
	public static final String VIEWCUSTOMERDETAILS = "/viewDetails//{customerId}/{cookieToken}";
	public static final String DETELECUSTOMERDETAILS = "/delete/{customerId}/{cookieToken}";
	public static final String UPDATECUSTOMERDETAILS = "/update/{customerId}/{cookieToken}";
	public static final String DEPOSIT = "/deposit/{customerId}/{cookieToken}";
	public static final String WITHDRAW = "/withdrawal/{customerId}/{cookieToken}";
	public static final String TRANSFER = "/transfer/{customerId}/{cookieToken}";
	public static final String EXPORT = "/export/{customerId}/{cookieToken}";
	public static final String MINISTATEMENT = "/ministatement/{customerId}/{cookieToken}";
	public static final String LOGOUT = "/logout/{customerId}/{cookieToken}";
}
