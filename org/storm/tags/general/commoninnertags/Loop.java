package org.storm.tags.general.commoninnertags;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;

import org.storm.tags.StormTag;

public class Loop extends StormTag
{
	private static final long serialVersionUID = 3425274705434662046L;
	int		loopAgain = SKIP_BODY;

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
			throw new JspTagException("Loop Tag: nesting error");
		}

		if (!parentTag.supportsLoop())
		{
			throw new JspTagException("Loop Tag: nesting error");
		}

		if (parentTag.error)
		{
			return (SKIP_BODY);
		}

	    return (parentTag.doLoop());
	}

	public int doAfterBody()
	{
		BodyContent body = getBodyContent();
		JspWriter writer = body.getEnclosingWriter();

		try
		{
			writer.print(body.getString());
			getBodyContent().clear();
		}
		catch(Exception ex) {}

		return (((StormTag)(getParent())).doLoop());
	}
}
