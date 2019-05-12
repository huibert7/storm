package org.storm.tags.general.commoninnertags;

import javax.servlet.jsp.JspTagException;

import org.storm.tags.StormTag;

public class IllegalOperation extends StormTag
{
	private static final long serialVersionUID = -9104857459736611491L;

	public int doStartTag() throws JspTagException
	{
		StormTag parentTag;

		try
		{
	      parentTag = (StormTag)getParent();
	    }
	    catch (Exception ex)
	    {
			throw new JspTagException("IllegalOperation Tag: nesting error");
	    }

		if (!parentTag.supportsIllegalOperation())
		{
			throw new JspTagException("IllegalOperation Tag: nesting error");
		}

		if (parentTag.error)
		{
			return (SKIP_BODY);
		}

	    return (parentTag.doIllegalOperation());
	}
}
