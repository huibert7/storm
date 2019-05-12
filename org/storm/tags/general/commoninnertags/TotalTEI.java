package org.storm.tags.general.commoninnertags;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class TotalTEI extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo(TagData data)
	{
		return new VariableInfo[]
		{
			new VariableInfo("total","java.lang.String",true,VariableInfo.NESTED)
		};
	}
}
