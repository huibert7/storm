package org.storm.tags.general;

import javax.servlet.jsp.JspTagException;

import org.storm.tags.StormTag;

public class Case extends StormTag
{
	private static final long serialVersionUID = -2420766165566978372L;
	String	value = null;

	public void setValue(String theValue)
	{
		if (theValue == null) theValue = "null";

		value = theValue;
	}

	public int doStartTag() throws JspTagException
	{
		if (getParent() instanceof Switch || getParent() instanceof IsDefined || getParent() instanceof IsNull)
		{
			if (getParent() instanceof Switch)
			{
				if (((Switch)getParent()).value.equals(value))
				{
					((Switch)getParent()).match = true;
				}
				else return (SKIP_BODY);
			}
			else
			{
				if (getParent() instanceof IsDefined)
				{
					if (((IsDefined)getParent()).value.equals(value))
					{
						((IsDefined)getParent()).match = true;
					}
					else return (SKIP_BODY);
				}
				else
				{
					if (((IsNull)getParent()).value.equals(value))
					{
						((IsNull)getParent()).match = true;
					}
					else return (SKIP_BODY);
				}
			}
		}
		else throw new JspTagException("Case Tag: nesting error");

	    return (EVAL_BODY_AGAIN);
	}
}
