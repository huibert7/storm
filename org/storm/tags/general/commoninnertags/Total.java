package org.storm.tags.general.commoninnertags;

import javax.servlet.jsp.JspTagException;

import org.storm.tags.StormTag;

public class Total extends StormTag
{
	private static final long serialVersionUID = 7597828654355528820L;

	public int doStartTag() throws JspTagException
	{
		StormTag parentTag;

		try
		{
	      parentTag = (StormTag)getParent();
	    }
	    catch (Exception ex)
	    {
			throw new JspTagException("Total Tag: nesting error");
	    }

		if (!parentTag.supportsTotal())
		{
			throw new JspTagException("Total Tag: nesting error");
		}

		if (parentTag.error)
		{
			return (SKIP_BODY);
		}

		return (parentTag.doTotal());
	}

	public void doInitBody()
	{
		pageContext.setAttribute("total",""+((StormTag)getParent()).total);
	}
}
