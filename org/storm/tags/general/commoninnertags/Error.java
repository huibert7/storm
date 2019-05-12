package org.storm.tags.general.commoninnertags;

import javax.servlet.jsp.JspTagException;

import storm.ErrorMessage;

import org.storm.tags.StormTag;

public class Error extends StormTag
{
	private static final long serialVersionUID = -7205862410081942303L;

	public int doStartTag() throws JspTagException
	{
		StormTag parentTag;

		try
		{
	      parentTag = (StormTag)getParent();
	    }
	    catch (Exception ex)
	    {
			throw new JspTagException("Error Tag: nesting error");
	    }

		if (!parentTag.supportsError())
		{
			throw new JspTagException("Error Tag: nesting error");
		}

		parentTag.doError();
		
		if (!parentTag.error) return (SKIP_BODY);
		else
		{
			pageContext.setAttribute("error",new ErrorMessage(parentTag.errorCode,parentTag.errorMsg));

		   return (EVAL_BODY_AGAIN);
		}
	}
}
