package org.storm.tags.general.utilities.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class TextTag extends BodyTagSupport
{
	private static final long serialVersionUID = -5794820627577972483L;
	private String               _key = null, _value = null, _basename = null, _language = null, _country = null;
	private String				  bundleName = "bundleVar";
    @SuppressWarnings("unused")
	private ResourceBundle       _bundle = null;
	
	public void setKey(String theKey)
    {
        _key = theKey;
    }
    
	public void setBasename(String theBasename)
	{
      	_basename = theBasename;
    }
    
	public void setLanguage(String theLanguage)
	{
      	_language = theLanguage;
    }
    
    public void setCountry(String theCountry)
	{
      	_country = theCountry;
    }

    public void setBundlename(String tempParam)
	{
		if (tempParam == null || tempParam.equals("null")) bundleName = "bundleVar";
		else bundleName = tempParam.trim().toLowerCase();
	}

	private ResourceBundle getBundle() throws JspException
    {
    	ResourceBundle bundle = null;
        if (_basename != null)
        {
        	if (_country == null) _country = "";
        	if (_language == null) _language = (String)pageContext.getServletContext().getInitParameter("defaultLanguage");
        	Locale _locale;
        	if (_language != null) _locale = new Locale(_language, _country);
			else _locale = new Locale("es", _country);
			
			bundle = ResourceBundle.getBundle(_basename, _locale);
        }	
		
		if (bundle == null)
		{
			//System.out.println("Using bundle from bundleVar");
			@SuppressWarnings("unused")
			ServletRequest sr = pageContext.getRequest();
			bundle = (ResourceBundle)find(bundleName);

			if (bundle == null)
			{
				System.out.println("Using defaultbundle");
				Locale locale = new Locale("es","");
				bundle = ResourceBundle.getBundle("iilabels", locale);
			}
		}
		
		return bundle;
    }

    public Object find(String name)
    {
		Object ret = pageContext.getAttribute(name, PageContext.PAGE_SCOPE);
		if (ret == null)
		{
	    	ret = pageContext.getAttribute(name, PageContext.REQUEST_SCOPE);
	    	if (ret == null)
	    	{
				if (pageContext.getSession() != null)
				{
		    		// check session only if a session is present
		    		ret = pageContext.getAttribute(name, PageContext.SESSION_SCOPE);
				}
				if (ret == null)
				{
		    		ret = pageContext.getAttribute(name, PageContext.APPLICATION_SCOPE);
		    		if (ret == null)
		    		{
						ret = pageContext.getServletContext().getInitParameter(name);
		    		}
				}
	    	}
		}

		return ret;
    }

	public int doStartTag() throws JspException
    {
       // Reset value for resource bundle key
       _value = null;

       // ensure we have a key
        if (_key == null )
        {
            throw new JspTagException("Text tag requires a key attribute.");
        }

        ResourceBundle bundle = this.getBundle();
        if ( bundle == null )
        {
            throw new JspTagException("Text tag: no bundle available for use.");
        }

        // see if the bundle has a value, if not, we default to the tag contents
        try
        {
            _value = bundle.getString(_key);
        }
        catch (java.util.MissingResourceException e)
        {
            ServletContext sc = pageContext.getServletContext();
            sc.log("Text tag, value not found for key:" + _key);
        }

        return EVAL_BODY_AGAIN;
    }

    public final int doEndTag() throws JspException
    {
        try {
            if (_value == null && bodyContent != null)
            {
            	// if the value is null, use the body content
                _value = bodyContent.getString();
                bodyContent.clear();
            }
        	
        	if (_value == null)
        	{
        		_value = _key;
        		System.out.println("Text tag: Value not found for key: '" + _key + "' and there is no default value. Using key instead.");
        	}
        	
			// print the value to the JspWriter
			this.pageContext.getOut().print(_value);
			
        }
        catch (java.io.IOException e)
        {
            throw new JspTagException("Text tag IO Error: " + e.getMessage());
        }

		 _key = null;
		 _value = null;
		 _basename = null;
		 _language = null;
		 _country = null;
		 bundleName = "bundleVar";

        return EVAL_PAGE;
    }


}



        /*
        if (bundle == null) {
			
			
			
			
			//Locale locale = new Locale((String)sr.getAttribute("locale"),"");
			
			//bundle = ResourceBundle.getBundle((String)sr.getAttribute("basename"), locale);
        	
            if (_bundleRef != null ) {
                bundle = (ResourceBundle)pageContext.getAttribute(_bundleRef);
                if ( bundle == null ) {
                    throw new JspTagException("i18n:message tag, could not find bundleRef=" + _bundleRef);
                }
            } 
            else {
                SetBundleTag bundleTag = (SetBundleTag)this.findAncestorWithClass(this,SetBundleTag.class);
                if (bundleTag != null) {
                    return bundleTag.getBundle();
                }
                bundle = ResourceHelper.getBundle(pageContext);
            }
        
        }
        */

