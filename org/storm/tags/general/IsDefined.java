package org.storm.tags.general;

import java.io.InputStream;
import java.util.Vector;

import javax.servlet.jsp.JspTagException;

import org.storm.tags.StormTag;

public class IsDefined extends StormTag
{
	private static final long 	serialVersionUID = -1743516705337955058L;
	String						value = null;
	boolean						match = false;
	Vector<StackElement>		stack = new Vector<StackElement>();
	InputStream					in;
//	private String				boundary;
//	private FilePart			lastFilePart;
//	private byte[] 				buf = new byte[8 * 1024];
//	private String				tempParamName;

	public void setParamName(String theValue)
	{
		push();
		value = theValue;
		match = false;
	}

	public int doStartTag() throws JspTagException
	{
		if (pageContext.getRequest().getParameter(value) != null) value = "true";
		else value = "false";

	    return (EVAL_BODY_AGAIN);
	}

	public int doEndTag()
	{
		pull();
		return EVAL_PAGE;
	}

	public void push()
	{
		stack.addElement(new StackElement(value,match));
	}

	public void pull()
	{
		StackElement temp = (StackElement)stack.lastElement();
		value = temp.getValue();
		match = temp.getMatch();
		stack.removeElementAt(stack.size()-1);
	}
}
