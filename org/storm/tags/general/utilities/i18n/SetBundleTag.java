package org.storm.tags.general.utilities.i18n;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.jsp.PageContext;

import org.storm.tags.StormTag;

public class SetBundleTag extends StormTag
{
	private static final long serialVersionUID = 5866290654513509378L;
	String	basename, _language = null, _country = null, bundleName = "bundleVar";
	boolean reset = false;
	int scope = PageContext.REQUEST_SCOPE;

	public void setBasename(String theBasename)
	{
		if (theBasename == null || theBasename.equals("null"))
		{
			error = true;
			errorMsg = "Missing 'basename' parameter";
			errorCode = 9999;
		}
		else
		{
        	this.basename = theBasename;
		}
    }
    
	public void setLanguage(String theLanguage)
	{
      	_language = theLanguage;
    }
    
    public void setCountry(String theCountry)
	{
      	_country = theCountry;
    }

    public void setScope(String tempParam)
	{
		if (tempParam == null || tempParam.equals("null")) scope = PageContext.REQUEST_SCOPE;
		else
		{
			String temp = tempParam.trim().toLowerCase();
			if (temp.equals("session"))
			{
				if (pageContext.getSession() != null)
					scope = PageContext.SESSION_SCOPE;
				else
					scope = PageContext.APPLICATION_SCOPE;
			}		
			else if (temp.equals("application")) scope = PageContext.APPLICATION_SCOPE;
			else if (temp.equals("page")) scope = PageContext.PAGE_SCOPE;
			else if (temp.equals("request")) scope = PageContext.REQUEST_SCOPE;
		}
	}

    public void setBundlename(String tempParam)
	{
		if (tempParam == null || tempParam.equals("null")) bundleName = "bundleVar";
		else bundleName = tempParam.trim().toLowerCase();
	}


    public void setReset(String tempParam)
	{
		if (tempParam == null || tempParam.equals("null"))
		{
			reset = false;
		}
		else
		{
			if (tempParam.trim().toLowerCase().equals("true")) reset = true;
			else reset = false;
		}
	}

	public void resetCache()
	{
		try
		{
			//Class klass = ResourceBundle.getBundle("resources.framework").getClass().getSuperclass();
			//Class klass = theBundle.getClass().getSuperclass();
			//Class klass = ResourceBundle.getBundle(basename).getClass().getSuperclass();
			@SuppressWarnings("rawtypes")
			Class klass = ResourceBundle.getBundle(basename).getClass().getSuperclass();
			Field field = klass.getDeclaredField("cacheList");
			field.setAccessible(true);
			sun.misc.SoftCache cache = (sun.misc.SoftCache)field.get(null);
			cache.clear();
			field.setAccessible(false); //to prevent any access by anything else 
			System.out.println("Resetting bundle cache: " + basename);
		}
		catch (Exception ex)
		{
			System.out.println("Error resetting bundle cache." + ex.getMessage());
		}
	}

    public int doEndTag()
    {
		if (!error)
		{
			if (_country == null) _country = "";
			
			if (_language == null) _language = (String)pageContext.getServletContext().getInitParameter("defaultLanguage");

			//System.out.println("SetBundle: " + basename + "_" + _language + "_" + _country);
			
			Locale locale;
			if (_language != null) locale = new Locale(_language, _country);
			else locale = new Locale("es", _country);
			
			if (reset) resetCache();
			
			ResourceBundle bundleVar = ResourceBundle.getBundle(basename, locale);
			
			pageContext.setAttribute(bundleName, bundleVar, scope);
		} 
		
		_language = null;
		_country = null;
		reset = false;
		scope = PageContext.REQUEST_SCOPE;
		
		return EVAL_PAGE;
    }
}
