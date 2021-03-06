package org.storm.tags.general.commoninnertags;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class LoopTEI extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo(TagData data) 
	{
		return new VariableInfo[]
		{
			new VariableInfo(data.getAttributeString("name"),data.getAttributeString("type"),true,VariableInfo.NESTED)
		};
	}
}
