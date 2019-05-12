package org.storm.tags.general;

import java.util.Vector;

import org.storm.tags.StormTag;

public class IsNull extends StormTag
{
	private static final long 	serialVersionUID = 1L;
	String						value = null;
	boolean						match = false;
	Vector<StackElement>		stack = new Vector<StackElement>();

	public void setValue(String theValue)
	{
		push();
		if (theValue==null) theValue = "null";
		value = theValue;
		match = false;
	}

	public int doStartTag()
	{
		if (value.equals("null") || value.length()==0) value = "true";
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
