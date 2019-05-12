package org.storm.tags.general.utilities.date;

import java.util.TimeZone;

import org.storm.tags.StormLoopTag;

public class DisplayTimeZonesTag extends StormLoopTag
{
	private static final long serialVersionUID = -574106041791560471L;
	private String []	ids;

	public int doStartTag()
	{
		count = 0;
		ids = TimeZone.getAvailableIDs();

		if (ids.length != 0) initialRequired = true;
		else initialRequired = false;

		return (EVAL_BODY_AGAIN);
	}

	public int doLoop()
	{
		if (count < ids.length)
		{
			pageContext.setAttribute("timezone",ids[count]);
			count++;
			
			pageContext.setAttribute("rowcount",new Integer(count));

			return EVAL_BODY_AGAIN;
		}
		else return SKIP_BODY;
	}
}

