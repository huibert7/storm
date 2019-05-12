package org.storm.tags.general.commoninnertags;

import javax.servlet.jsp.JspTagException;

import org.storm.tags.StormTag;

public class Success extends StormTag
{
	private static final long serialVersionUID = -3211077086106474778L;

	public void setName(String theName)
	{
	}
	
	public void setType(String theType)
	{
	}
	
	public int doStartTag() throws JspTagException
	{
		StormTag parentTag;

	    try
	    {
	      parentTag = (StormTag)getParent();
	    }
	    catch (Exception ex)
		{
			throw new JspTagException("Success Tag: nesting error");
		}

		if (!parentTag.supportsSuccess())
		{
			throw new JspTagException("Success Tag: nesting error");
		}

		if (parentTag.error)
		{
			return (SKIP_BODY);
		}

	    return (parentTag.doSuccess());
	}
}
