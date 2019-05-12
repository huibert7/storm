package org.storm.tags.general.utilities.i18n;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import storm.DateTime;

import org.storm.tags.StormTag;

public class FormatDateTag extends StormTag
{
	private static final long serialVersionUID = 6226576172903825141L;
	Date			date = null;
	Calendar		calendar = null;
	TimeZone		tz = null;
	Locale			tempLocale;
	String			language, country = "";
	int			dateStyle = DateFormat.DEFAULT, timeStyle = DateFormat.DEFAULT;
	DateFormat		dateFormatter, timeFormatter, dateTimeFormatter;

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

	public void setDateYMD(String tempParam)
	{
		if (!(tempParam == null || tempParam.equals("null")))
		{
			if (tempParam.trim().toLowerCase().equals("current")) date = new Date();
			else
			{
				try
				{
					SimpleDateFormat sdf = new SimpleDateFormat("y-M-d");
					date = sdf.parse(tempParam);
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

    public void setDateStyle(String tempParam)
	{
		if (tempParam == null || tempParam.equals("null")) dateStyle = DateFormat.DEFAULT;
		else
		{
			String temp = tempParam.trim().toLowerCase();
			if (temp.equals("short")) dateStyle = DateFormat.SHORT;
			else if (temp.equals("medium")) dateStyle = DateFormat.MEDIUM;
			else if (temp.equals("long")) dateStyle = DateFormat.LONG;
			else if (temp.equals("full")) dateStyle = DateFormat.FULL;
			else if (temp.equals("default")) dateStyle = DateFormat.DEFAULT;
		}
	}

    public void setTimeStyle(String tempParam)
	{
		if (tempParam == null || tempParam.equals("null")) timeStyle = DateFormat.DEFAULT;
		else
		{
			String temp = tempParam.trim().toLowerCase();
			if (temp.equals("short")) timeStyle = DateFormat.SHORT;
			else if (temp.equals("medium")) timeStyle = DateFormat.MEDIUM;
			else if (temp.equals("long")) timeStyle = DateFormat.LONG;
			else if (temp.equals("full")) timeStyle = DateFormat.FULL;
			else if (temp.equals("default")) timeStyle = DateFormat.DEFAULT;
		}
	}

	public int doStartTag()
	{
		if (date == null) date = new Date();
		
		tempLocale = null;
		if (!error)
		{
			try
			{
				tempLocale = new Locale(language,country);
			}
			catch (Exception ex)
			{
				error = true;
				errorMsg = "Invalid 'language' or 'country' parameter. Should be a valid locale. " + ex.getMessage();
				errorCode = 9999;
			}
		}
		
		if (!error)
		{
			try
			{
				calendar = Calendar.getInstance();
				calendar.setTime(date);
				if (tz != null) calendar.setTimeZone(tz);
				
				dateFormatter = DateFormat.getDateInstance(dateStyle, tempLocale);
				timeFormatter = DateFormat.getTimeInstance(timeStyle, tempLocale);
				dateTimeFormatter = DateFormat.getDateTimeInstance(dateStyle, timeStyle, tempLocale);
			}
			catch (Exception ex)
			{
				error = true;
				errorMsg = "Exception: " + ex.getMessage();
				errorCode = 9999;
			}
		}
		return EVAL_BODY_AGAIN;
	}

	public int doSuccess()
	{
		int year,month,day,hour,minute,second,dow;
//		int pm;
		String formattedDate, formattedTime, formattedDateTime;
//		String patternDate;

		if (!error)
		{
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			minute = calendar.get(Calendar.MINUTE);
			second = calendar.get(Calendar.SECOND);
			dow = calendar.get(Calendar.DAY_OF_WEEK);
			
			formattedDate = dateFormatter.format(calendar.getTime());
			formattedTime = timeFormatter.format(calendar.getTime());
			formattedDateTime = dateTimeFormatter.format(calendar.getTime());
			
			pageContext.setAttribute("datetime",new DateTime(year,month+1,day,hour,minute,second,dow,formattedDate,formattedTime,formattedDateTime,calendar.getTime(),tempLocale));

			return EVAL_BODY_AGAIN;
		}
		return SKIP_BODY;
	}

	public int doEndTag()
	{
		country = "";
		date = null;
		calendar = null;
		tz = null;
		dateStyle = DateFormat.DEFAULT;
		timeStyle = DateFormat.DEFAULT;
		return EVAL_PAGE;
	}
}

/*

http://java.sun.com/j2se/1.3/docs/api/java/text/SimpleDateFormat.html


 Symbol   Meaning                 Presentation        Example
 ------   -------                 ------------        -------
 G        era designator          (Text)              AD
 y        year                    (Number)            1996
 M        month in year           (Text & Number)     July & 07
 d        day in month            (Number)            10
 h        hour in am/pm (1~12)    (Number)            12
 H        hour in day (0~23)      (Number)            0
 m        minute in hour          (Number)            30
 s        second in minute        (Number)            55
 S        millisecond             (Number)            978
 E        day in week             (Text)              Tuesday
 D        day in year             (Number)            189
 F        day of week in month    (Number)            2 (2nd Wed in July)
 w        week in year            (Number)            27
 W        week in month           (Number)            2
 a        am/pm marker            (Text)              PM
 k        hour in day (1~24)      (Number)            24
 K        hour in am/pm (0~11)    (Number)            0
 z        time zone               (Text)              Pacific Standard Time
 '        escape for text         (Delimiter)
 ''       single quote            (Literal)           '

The count of pattern letters determine the format. 
(Text): 4 or more pattern letters--use full form, < 4--use short or abbreviated form if one exists. 

(Number): the minimum number of digits. Shorter numbers are zero-padded to this amount.
Year is handled specially; that is, if the count of 'y' is 2, the Year will be truncated to 2 digits. 

(Text & Number): 3 or over, use text, otherwise use number. 

Any characters in the pattern that are not in the ranges of ['a'..'z'] and ['A'..'Z'] will be treated as quoted text. For instance, characters like ':', '.', ' ', '#' and '@' will appear in the resulting time text even they are not embraced within single quotes. 

A pattern containing any invalid pattern letter will result in a thrown exception during formatting or parsing. 

Examples Using the US Locale: 

 Format Pattern                         Result
 --------------                         -------
 "yyyy.MM.dd G 'at' hh:mm:ss z"    ->>  1996.07.10 AD at 15:08:56 PDT
 "EEE, MMM d, ''yy"                ->>  Wed, July 10, '96
 "h:mm a"                          ->>  12:08 PM
 "hh 'o''clock' a, zzzz"           ->>  12 o'clock PM, Pacific Daylight Time
 "K:mm a, z"                       ->>  0:00 PM, PST
 "yyyyy.MMMMM.dd GGG hh:mm aaa"    ->>  1996.July.10 AD 12:08 PM




http://java.sun.com/docs/books/tutorial/i18n/format/dateFormat.html

Style		U.S. Locale											French Locale  
DEFAULT		25-Jun-98 1:32:19 PM								25 jun 98 22:32:20  
SHORT		6/25/98 1:32 PM										25/06/98 22:32  
MEDIUM		25-Jun-98 1:32:19 PM								25 jun 98 22:32:20  
LONG		June 25, 1998 1:32:19 PM PDT						25 juin 1998 22:32:20 GMT+02:00  
FULL		Thursday, June 25, 1998 1:32:19 o'clock PM PDT		jeudi, 25 juin 1998 22 h 32 GMT+02:00  

*/


