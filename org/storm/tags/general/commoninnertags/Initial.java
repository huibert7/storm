package org.storm.tags.general.commoninnertags;

import javax.servlet.jsp.JspTagException;

import org.storm.tags.StormTag;

public class Initial extends StormTag
{
	private static final long serialVersionUID = -6044657082311036681L;

	public int doStartTag() throws JspTagException
	{
		StormTag parentTag;

		try
		{
	      parentTag = (StormTag)getParent();
	    }
	    catch (Exception ex)
	    {
			throw new JspTagException("Initial Tag: nesting error");
	    }

		if (!parentTag.supportsInitial())
		{
			throw new JspTagException("Initial Tag: nesting error");
		}

	    return (parentTag.doInitial());
	}
}
