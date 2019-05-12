package org.storm.tags;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

public class StormTag extends BodyTagSupport
{
	private static final long serialVersionUID = -2600706896439967842L;
	public	boolean		error = false;
	public	String		errorMsg = null;
	public	int			errorCode = 0;
	public	double		total;
	public	int			count = 0;
	public	boolean		initialRequired = false;
//	private	Integer		PreviousCount;

	public int doAfterBody() throws JspTagException
	{
		BodyContent body = getBodyContent();
		JspWriter writer = body.getEnclosingWriter();

		try
		{
			writer.print(body.getString());
			getBodyContent().clear();
		}
		catch(Exception ex) {}

		return SKIP_BODY;
	}

	public int doEndTag() throws JspTagException
	{
		return EVAL_PAGE;
	}

	public void doError() throws JspTagException
	{
	}

	public int doInitial()
	{
		if (!error && initialRequired)
		{
			initialRequired = false;
			return EVAL_BODY_AGAIN;
		}
		else return SKIP_BODY;
	}

	public int doFinal()
	{
		if (!error && count != 0)
		{
			pageContext.setAttribute("rowcount",new Integer(count));

			return EVAL_BODY_AGAIN;
		}
		else return SKIP_BODY;
	}

	public int doIllegalOperation()
	{
		return SKIP_BODY;
	}

	public int doLoop()
	{
		return SKIP_BODY;
	}

	public int doNoResults()
	{
		if (!error && count == 0) return EVAL_BODY_AGAIN;
		else return SKIP_BODY;
	}

	public int doSuccess()
	{
		return SKIP_BODY;
	}

	public int doTotal()
	{
		return SKIP_BODY;
	}

	public boolean supportsError()
	{
		return false;
	}

	public boolean supportsFinal()
	{
		return false;
	}

	public boolean supportsIllegalOperation()
	{
		return false;
	}

	public boolean supportsInitial()
	{
		return false;
	}

	public boolean supportsLoop()
	{
		return false;
	}

	public boolean supportsNoResults()
	{
		return false;
	}

	public boolean supportsSuccess()
	{
		return false;
	}

	public boolean supportsTotal()
	{
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public Tag findParentWithRestrictions(Class theClass,Class[] classes) throws Exception
	{
		Tag	tag = getParent();

		while (tag != null && !(theClass.isInstance(tag)))
		{
			if (classes != null)
			{
				for (int i = 0;i < classes.length;i++)
				{
					if (classes[i].isInstance(tag)) throw new Exception ("Illegal nesting");
				}
			}
			tag = tag.getParent();
		}
		
		return tag;
	}
}
