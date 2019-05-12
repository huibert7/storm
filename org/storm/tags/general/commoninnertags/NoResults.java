package org.storm.tags.general.commoninnertags;

import javax.servlet.jsp.JspTagException;

import org.storm.tags.StormTag;

public class NoResults extends StormTag
{
	private static final long serialVersionUID = -31061498662047740L;

	public int doStartTag() throws JspTagException
	{
		StormTag parentTag;

		try
		{
	      parentTag = (StormTag)getParent();
	    }
	    catch (Exception ex)
	    {
			throw new JspTagException("NoResults Tag: nesting error");
	    }

		if (!parentTag.supportsNoResults())
		{
			throw new JspTagException("NoResults Tag: nesting error");
		}

		if (parentTag.error)
		{
			return (SKIP_BODY);
		}

	    return (parentTag.doNoResults());
	}
}
