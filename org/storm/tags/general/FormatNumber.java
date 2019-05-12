package org.storm.tags.general;

import org.storm.tags.StormTag;

public class FormatNumber extends StormTag
{
	private static final long serialVersionUID = 7896563393194386063L;
	String		theNumber,theDecimals;
	int			position = 0,decimals = 0;
	char		decimalsSeparator = '.';
	String		thousandsSeparator = null;
	String		numberPart,intPart,fracPart,expPart;
		
	public boolean supportsSuccess()
	{
		return true;
	}

	public boolean supportsError()
	{
		return true;
	}

	public void setNumber(String tempParam)
	{
		@SuppressWarnings("unused")
		long		intPartValue;
		@SuppressWarnings("unused")
		long		fracPartValue;
		int			exp = 0;

		if (tempParam == null || tempParam.equals("null"))
		{
			error = true;
			errorMsg = "Missing 'number' parameter";
			errorCode = -1114;
		}
		else
		{
			theNumber = tempParam.trim();
			
//			System.out.println("El numero que se procesa es: "+theNumber);

			position = theNumber.indexOf("E");
			
			if (position == -1)
			{
				numberPart = theNumber.substring(0);
				expPart = "0";
			}
			else
			{
				numberPart = theNumber.substring(0,position);
				if (position+1 < theNumber.length()) expPart = theNumber.substring(position+1);
				else
				{
					error = true;
					errorMsg = "Illegal 'number' parameter";
					errorCode = -1248;
				}
			}
			
			position = numberPart.indexOf(".");
			
			if (position == -1)
			{
				intPart = numberPart.substring(0);
				fracPart = "0";
			}
			else
			{
				if (position == 0)
				{
					intPart = "0";
					if (numberPart.length() > 1) fracPart = numberPart.substring(1);
					else
					{
						error = true;
						errorMsg = "Illegal 'number' parameter";
						errorCode = -1248;
					}
				}
				else
				{
					intPart = numberPart.substring(0,position);
					if (position+1 < numberPart.length()) fracPart = numberPart.substring(position+1);
					else fracPart = "0";
				}
			}
			
			if (!error)
			{
				try
				{
					intPartValue = (new Long(intPart)).longValue();
					fracPartValue = (new Long(fracPart)).longValue();
					exp = (new Integer(expPart)).intValue();
				}
				catch(Exception ex)
				{
					error = true;
					errorMsg = "Illegal 'number' parameter";
					errorCode = -1248;
				}
			}
			
			if (!error && exp != 0)
			{
				if (exp > 0)
				{
					for (int i = 0;i < exp;i++)
					{
						if (fracPart.length() > 0)
						{
							intPart += fracPart.charAt(0);
							if (fracPart.length() > 1) fracPart = fracPart.substring(1);
							else fracPart = "";
						}
						else intPart += "0";
					}
				}
				else
				{
					for (int i = 0;i < 0-exp;i++)
					{
						if (intPart.length() > 0)
						{
							fracPart = intPart.charAt(intPart.length()-1) + fracPart;
							if (intPart.length() > 1) intPart = intPart.substring(0,intPart.length()-1);
							else intPart = "";
						}
						else fracPart = "0" + fracPart;
					}
				}
				
				if (intPart == "") intPart = "0";
				if (fracPart == "") fracPart = "0";
			}
			
			if (!error)
			{
				theNumber = intPart + "." + fracPart;
			}
		}
	}
		
	public void setDecimals(String tempParam)
	{
		if (tempParam == null || tempParam.equals("null"))
		{
			error = true;
			errorMsg = "Missing 'decimals' parameter";
			errorCode = -1115;
		}
		else
		{
			try
			{
				decimals = (new Integer(tempParam)).intValue();
			}
			catch(Exception ex)
			{
				error = true;
				errorMsg = "Illegal value for 'decimals' parameter";
				errorCode = -1116;
			}
		}
	}
	
	public void setThousandsSeparator(String tempParam)
	{
		if (!(tempParam == null || tempParam.equals("null")))
		{
			if (tempParam.length() != 1)
			{
				error = true;
				errorMsg = "Illegal value for 'thousandsSeparator' parameter";
				errorCode = -1247;
			}
			else thousandsSeparator = tempParam;
		}
	}
		
	public void setDecimalsSeparator(String tempParam)
	{
		if (!(tempParam == null || tempParam.equals("null")))
		{
			if (tempParam.length() != 1)
			{
				error = true;
				errorMsg = "Illegal value for 'decimalsSeparator' parameter";
				errorCode = -1246;
			}
			else decimalsSeparator = tempParam.charAt(0);
		}
	}
		
	public int doStartTag()
	{
		if (!error)
		{
			position = theNumber.indexOf(".");
			if (position == -1)
			{
				if (decimals != 0)
				{
					theNumber += decimalsSeparator;
					for (int i=0;i<decimals;i++) theNumber += "0";
				}
			}
			else
			{
				if (position == 0) theNumber = "0" + decimalsSeparator + theNumber.substring(1);
				else theNumber = theNumber.substring(0,position) + decimalsSeparator + theNumber.substring(position+1);

				if (theNumber.length()-position-1 < decimals)
				{
					for (int i=0;i<decimals-theNumber.length()+position+1;i++) theNumber += "0";
				}
				else if (theNumber.length()-position-1 > decimals)
				{
					theNumber = theNumber.substring(0,position+1+decimals);
					if (decimals == 0) theNumber = theNumber.substring(0,theNumber.length()-1);
				}
			}
			
//			System.out.println("El numero ya procesado: "+theNumber);
			if (thousandsSeparator != null)
			{
				String tempNumber = "";
				
				position = theNumber.indexOf(decimalsSeparator);

				if (position == -1)
				{
					for (int i = 0; i < theNumber.length(); i++)
					{
						if (i != 0 && i%3 == 0) tempNumber = thousandsSeparator + tempNumber;
						tempNumber = theNumber.charAt(theNumber.length() - i - 1) + tempNumber; 
					}
				}
				else
				{
					for (int i = 0; i < position; i++)
					{
						if (i != 0 && i%3 == 0) tempNumber = thousandsSeparator + tempNumber;
						tempNumber = theNumber.charAt(position - i - 1) + tempNumber; 
					}
				
					tempNumber = tempNumber + theNumber.substring(position);
				}
				
				theNumber = tempNumber;
			}
		}
		return EVAL_BODY_AGAIN;
	}

	public int doSuccess()
	{
		if (!error)
		{
			pageContext.setAttribute("formattedNumber",theNumber);

			return EVAL_BODY_AGAIN;
		}
		return SKIP_BODY;
	}
	
	public int doEndTag()
	{
		decimalsSeparator = '.';
		thousandsSeparator = null;

		return EVAL_PAGE;
	}
}
