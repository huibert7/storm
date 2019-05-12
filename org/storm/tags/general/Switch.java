package org.storm.tags.general;

import java.util.Vector;

import org.storm.tags.StormTag;

public class Switch extends StormTag
{
	private static final long 	serialVersionUID = 6558703580858400844L;
	public String				value = null;
	public boolean				match = false;
	public Vector<StackElement>	stack = new Vector<StackElement>();

	public void setValue(String theValue)
	{
		push();

		if (theValue == null) theValue = "null";

		value = theValue;
		match = false;
	}

	public int doStartTag()
	{
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
