package com.ftu.sm.model;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: FPT - IS</p>
 *
 * @author HoaiPN
 * @version 1.0
 */
public class SaleCustomer
{
	private String customerType = "";
	private String customerName = "";
	private String address = "";
	private String taxCode = "";
	private String company = "";

	public SaleCustomer()
	{
	}

	public SaleCustomer(
		String customerType,
		String customerName,
		String address,
		String taxCode,
		String company)
	{
		this.customerType = customerType;
		this.customerName = customerName;
		this.address = address;
		this.taxCode = taxCode;
		this.company = company;
	}

	public void setCustomerType(String customerType)
	{
		this.customerType = customerType;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public void setTaxCode(String taxCode)
	{
		this.taxCode = taxCode;
	}

	public void setCompany(String company)
	{
		this.company = company;
	}

	public String getCustomerType()
	{
		return customerType;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public String getAddress()
	{
		return address;
	}

	public String getTaxCode()
	{
		return taxCode;
	}

	public String getCompany()
	{
		return company;
	}

}
