package org.storm.filter;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class UploadRequestWrapper extends HttpServletRequestWrapper
{
	Hashtable<String, String>	parameters = new Hashtable<String, String>();

	public UploadRequestWrapper(HttpServletRequest request)
	{
		super(request);
	}
	
	public void setParameter(String name,String value)
	{
		parameters.put(name,value);
	}
	
	public Enumeration<String> getParameterNames()
	{
		return parameters.keys();
	}

	public String getParameter(String key)
	{
		if (parameters.get(key) != null)
			return (String)parameters.get(key);
		else return null;
	}

	public String[] getParameterValues(String key)
	{
		if (parameters.get(key) != null)
			return new String[] {(String)parameters.get(key)};
		else return null;
	}
}