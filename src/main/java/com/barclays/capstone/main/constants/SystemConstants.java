package com.barclays.capstone.main.constants;

public class SystemConstants {

	  public static final String LOGIN = "/login";
	  public static final String CHANGEPASSWORD = "/changePassword";
	  public static final String CHECKCUSTOMERPAN = "/check-customer-pan/{customerId}/{cookieToken}";
	  public static final String ADDNEWCUSTOMER = "/add-new-customer/{customerId}/{cookieToken}";
	  public static final String ADDNEWACCOUNT = "/add-new-account/{customerId}/{cookieToken}";
	  public static final String VIEWCUSTOMERDETAILS= "/viewDetails/{customerid}";
	  public static final String DETELECUSTOMERDETAILS= "/delete/{customerid}";
	  public static final String UPDATECUSTOMERDETAILS= "/update/{customerid}";
	  public static final String DEPOSIT= "/deposit/{customerId}/{cookieToken}";
	  public static final String WITHDRAW= "/withdrawal/{customerId}/{cookieToken}";
	  public static final String TRANSFER= "/transfer/{customerId}/{cookieToken}";
	  
}
