package org.storm.tags.general;

public class StackElement extends Object
{
	String		value = null;
	boolean		match = false;

	public StackElement(String value,boolean match)
	{
		this.value = value;
		this.match = match;
	}

	public String getValue()
	{
		return value;
	}

	public boolean getMatch()
	{
		return match;
	}
}
