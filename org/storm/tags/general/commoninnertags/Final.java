package org.storm.tags.general.commoninnertags;

import javax.servlet.jsp.JspTagException;

import org.storm.tags.StormTag;

public class Final extends StormTag
{
	private static final long serialVersionUID = 4538064419560894879L;

	public int doStartTag() throws JspTagException
	{
		StormTag parentTag;

		try
		{
	      parentTag = (StormTag)getParent();
	    }
	    catch (Exception ex)
	    {
			throw new JspTagException("Final Tag: nesting error");
	    }

		if (!parentTag.supportsFinal())
		{
			throw new JspTagException("Final Tag: nesting error");
		}

		if (parentTag.error)
		{
			return (SKIP_BODY);
		}

	    return (parentTag.doFinal());
	}
}
