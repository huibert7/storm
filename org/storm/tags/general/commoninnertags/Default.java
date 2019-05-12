package org.storm.tags.general.commoninnertags;

import javax.servlet.jsp.JspTagException;

import org.storm.tags.StormTag;
import org.storm.tags.general.Switch;

public class Default extends StormTag
{
	private static final long serialVersionUID = -4360676580071535238L;

	public int doStartTag() throws JspTagException
	{
		if (getParent() instanceof Switch)
		{
			if (((Switch)getParent()).match) return (SKIP_BODY);
		}
		else throw new JspTagException("Default Tag: nesting error");

	    return (EVAL_BODY_AGAIN);
	}
}