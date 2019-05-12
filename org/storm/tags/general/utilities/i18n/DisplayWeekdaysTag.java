package org.storm.tags.general.utilities.i18n;

import java.text.DateFormatSymbols;
import java.util.Locale;

import org.storm.tags.StormLoopTag;

/*
 * Locale locale = new Locale("es", "ES");
DateFormatSymbols symbols = new DateFormatSymbols(locale);
String monthNames[] = symbols.getMonths();
*/


public class DisplayWeekdaysTag extends StormLoopTag
{
	private static final long serialVersionUID = 8515022285044406459L;
	boolean	finished, shortnames = false;
	String		country = "", language = null;
	int		weekday = -1, arrayLength = 0;
	String		dayNames[];

	public void setWeekday(String idParam)
	{
		if (idParam!=null && !idParam.equals("null"))
		{
			try
			{
				weekday = (new Integer(idParam)).intValue();
	
				if (weekday < 0 || weekday > 7)
				{
					error = true;
					errorMsg = "Invalid 'weekday' parameter. Should be 0 (Sunday) to 7 (Saturday)";
					errorCode = 2002;
				}
			}
			catch(Exception ex)
			{
				error = true;
				errorMsg = "Invalid 'weekday' parameter";
				errorCode = 2002;
			}
		}
	}

	public void setUseShortNames(String tempParam)
	{
		if (tempParam == null || tempParam.equals("null")) shortnames = false;
		else
		{
			tempParam = tempParam.toLowerCase().trim();
			if (tempParam.equals("true")) shortnames = true;
			else shortnames = false;
		}
	}

	public void setLanguage(String tempParam)
	{
		if (tempParam==null || tempParam.equals("null"))
		{
			error = true;
			errorMsg = "Missing 'language' parameter";
			errorCode = 9999;
		}
		else
		{
			if (tempParam.length() == 2)
			{
				language = tempParam;
			}
			else
			{
				error = true;
				errorMsg = "Invalid 'language' parameter. Should have language two letter abbreviation.";
				errorCode = 9999;
			}
		}
	}

	public void setCountry(String tempParam)
	{
		if (!(tempParam==null || tempParam.equals("null")))
		{
			if (tempParam.length() == 2)
			{
				country = tempParam;
			}
			else
			{
				error = true;
				errorMsg = "Invalid 'country' parameter. Should have country two letter abbreviation.";
				errorCode = 9999;
			}
		}
	}
	
	public int doStartTag()
	{
		if (!error)
		{
			count = 0;
			initialRequired = false;
			finished = false;

			try
			{
				Locale locale = new Locale(language, country);
				DateFormatSymbols symbols = new DateFormatSymbols(locale);
				if (!shortnames) dayNames = symbols.getWeekdays();
				else dayNames = symbols.getShortWeekdays();
				arrayLength = dayNames.length;
				
				if (weekday != -1)
				{
					String temp = dayNames[weekday];
					dayNames = null;
					dayNames[0] = temp;
					arrayLength = 1;
				}
				
				if (arrayLength > 0) initialRequired = true;
				else finished = true;
			}
			catch (Exception exc)
			{
				error = true;
				errorMsg = "Error while retrieving weekdays: "+exc.getMessage();
				errorCode = -1002;
			}
		}

		return (EVAL_BODY_AGAIN);
	}

	public int doLoop()
	{
		if (!error)
		{
//			Locale tempLocale;
			try
			{
				if (count < arrayLength-1)
				{
					count ++;
					pageContext.setAttribute("weekdayname", dayNames[count]);
					pageContext.setAttribute("rowcount",new Integer(count));
					
					return EVAL_BODY_AGAIN;
				}
			}
			catch (Exception ex)
			{
				error = true;
				errorMsg = "Error: " + ex.getMessage();
				errorCode = -1002;
			}
		}

		return SKIP_BODY;
	}

	public int doEndTag()
	{
		country = null;
		language = null;
		weekday = -1;
		shortnames = false;
		arrayLength = 0;

		return EVAL_PAGE;
	}
}
