package org.storm.tags.general.utilities.date;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import storm.DateTime;

import org.storm.tags.StormTag;

public class ParseDateTag extends StormTag
{
	private static final long serialVersionUID = 1072865936054426141L;
	Date			date;
	Calendar		calendar = null;
	TimeZone		tz = null;

	public boolean supportsSuccess()
	{
		return true;
	}

	public boolean supportsError()
	{
		return true;
	}

	public void setDate(String tempParam)
	{
		if (!(tempParam == null || tempParam.equals("null")))
		{
			if (tempParam.trim().toLowerCase().equals("current")) date = new Date();
			else
			{
				try
				{
					date = new Date((new Long(tempParam.trim())).longValue());
				}
				catch (Exception ex)
				{
					error = true;
					errorMsg = "Illegal value for 'date' parameter";
					errorCode = -1501;
				}
			}
		}
	}

	public void setTimezone(String tempParam)
	{
		if (tempParam == null || tempParam.equals("null")) tz = null;
		else
		{
			tz = TimeZone.getTimeZone(tempParam);
			if (tz == null)
			{
				error = true;
				errorMsg = "Specified timezone does not exist";
				errorCode = -1501;
			}
		}
	}

	public int doStartTag()
	{
		calendar = Calendar.getInstance();

		calendar.setTime(date);

		if (tz != null) calendar.setTimeZone(tz);

		return EVAL_BODY_AGAIN;
	}

	public int doSuccess()
	{
		int year,month,day,hour,minute,second,dow;

		if (!error)
		{
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			minute = calendar.get(Calendar.MINUTE);
			second = calendar.get(Calendar.SECOND);
			dow = calendar.get(Calendar.DAY_OF_WEEK);
			
			pageContext.setAttribute("datetime",new DateTime(year,month+1,day,hour,minute,second,dow));

			return EVAL_BODY_AGAIN;
		}
		return SKIP_BODY;
	}

	public int doEndTag()
	{
		return EVAL_PAGE;
	}
}


