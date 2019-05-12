package org.storm.tags.general;

import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import org.storm.tags.StormTag;

public class Evaluate extends StormTag
{
	private static final long 	serialVersionUID = -4770964923352435930L;
	@SuppressWarnings("rawtypes")
	Vector<Comparable>			theStack = new Vector<Comparable>();
	Random						r;
	String						formula = null,format = null;
	StringTokenizer				tokenizedFormula;
	int							temp;
	String						token,context;
	float						tempNum,x,y;
	char						firstChar;
	String						finalOutput;

	public boolean supportsSuccess()
	{
		return true;
	}

	public boolean supportsError()
	{
		return true;
	}

	public void setFormula(String tempParam)
	{
		if (tempParam.equals("null"))
		{
			error = true;
			errorMsg = "Missing 'formula' parameter";
			errorCode = -1060;
		}
		else
		{
			formula = tempParam;
		}
	}
		
	public void setFormat(String tempParam)
	{
		if (!tempParam.equals("null"))
		{
			format = tempParam;
		}
	}

	public int doStartTag()
	{
		tokenizedFormula = new StringTokenizer(formula," ",false);
		token = "";

		while (tokenizedFormula.hasMoreElements() && !error)
		{
			token = tokenizedFormula.nextToken();

			firstChar = token.charAt(0);
			if ((firstChar >= '0' && firstChar <= '9') || firstChar == '.' || firstChar == '-')
			{
				try
				{
					theStack.insertElementAt(Float.valueOf(token),0);
				}
				catch (NumberFormatException ex)
				{
					error = true;
					errorCode = -2;
				}
			}
			else
			{
				if (token.length() == 1)
				{
					switch(firstChar)
					{
						case ('+'):
						{
							x = getX();
							y = getX();
							theStack.insertElementAt(new Float(x+y),0);
							break;
						}
						case ('-'):
						{
							x = getX();
							y = getX();
							theStack.insertElementAt(new Float(y-x),0);
							break;
						}
						case ('*'):
						{
							x = getX();
							y = getX();
							theStack.insertElementAt(new Float(x*y),0);
							break;
						}
						case ('/'):
						{
							x = getX();
							y = getX();
							if (x != 0) theStack.insertElementAt(new Float(y/x),0);
							else
							{
								error = true;
								errorCode = -3;
							}
							break;
						}
						case ('%'):
						{
							x = getX();
							y = getX();
							if (x != 0) theStack.insertElementAt(new Float(y%x),0);
							else
							{
								error = true;
								errorCode = -3;
							}
							break;
						}
						case ('e'):
						{
							theStack.insertElementAt(new Float(Math.E),0);
							break;
						}
						case ('>'):
						{
							x = getX();
							y = getX();
							theStack.insertElementAt(new Float(y>x?1:0),0);
							break;
						}
						case ('<'):
						{
							x = getX();
							y = getX();
							theStack.insertElementAt(new Float(y<x?1:0),0);
							break;
						}
						case ('='):
						{
							x = getX();
							y = getX();
							theStack.insertElementAt(new Float(y==x?1:0),0);
							break;
						}
					}
				}
				else
				{
					switch(firstChar)
					{
						case ('A'):
						{
							if (token.equals("ACOS"))
							{
								x = getX();
								theStack.insertElementAt(new Float(Math.acos(x)),0);
							}
							if (token.equals("ASIN"))
							{
								x = getX();
								theStack.insertElementAt(new Float(Math.asin(x)),0);
							}
							if (token.equals("ATAN"))
							{
								x = getX();
								theStack.insertElementAt(new Float(Math.atan(x)),0);
							}
							if (token.equals("ABS"))
							{
								x = getX();
								theStack.insertElementAt(new Float(Math.abs(x)),0);
							}
							break;
						}
						case ('C'):
						{
							if (token.equals("COS"))
							{
								x = getX();
								theStack.insertElementAt(new Float(Math.cos(x)),0);
							}
							if (token.equals("CHS"))
							{
								x = getX();
								theStack.insertElementAt(new Float(0 - x),0);
							}
							break;
						}
						case ('F'):
						{
							if (token.equals("FRAC"))
							{
								x = getX();
								y = (float)(x<0?Math.ceil((double)x):Math.floor((double)x));
								x = x - y;
								theStack.insertElementAt(new Float(x),0);
							}
							break;
						}
						case ('I'):
						{
							if (token.equals("INT"))
							{
								x = getX();
								theStack.insertElementAt(new Float(x<0?Math.ceil((double)x):Math.floor((double)x)),0);
							}
							break;
						}
						case ('L'):
						{
							if (token.equals("LOG"))
							{
								x = getX();
								theStack.insertElementAt(new Float(Math.log((double)x)),0);
							}
							break;
						}
						case ('M'):
						{
							if (token.equals("MAX"))
							{
								x = getX();
								y = getX();
								theStack.insertElementAt(new Float(Math.max(x,y)),0);
							}
							if (token.equals("MIN"))
							{
								x = getX();
								y = getX();
								theStack.insertElementAt(new Float(Math.min(x,y)),0);
							}
							break;
						}
						case ('P'):
						{
							if (token.equals("PI"))
							{
								theStack.insertElementAt(new Float(Math.PI),0);
							}
							if (token.equals("POW"))
							{
								x = getX();
								y = getX();
								theStack.insertElementAt(new Float(Math.pow((double)y,(double)x)),0);
							}
							break;
						}
						case ('R'):
						{
							if (token.equals("RND"))
							{
								r = new Random();
								theStack.insertElementAt(new Float(r.nextFloat()),0);
							}
							if (token.equals("ROUND"))
							{
								x = getX();
								theStack.insertElementAt(new Float(Math.round(x)),0);
							}
							break;
						}
						case ('S'):
						{
							if (token.equals("SIN"))
							{
								x = getX();
								theStack.insertElementAt(new Float(Math.sin(x)),0);
							}
							if (token.equals("SQRT"))
							{
								x = getX();
								theStack.insertElementAt(new Float(Math.sqrt(x)),0);
							}
							break;
						}
						case ('T'):
						{
							if (token.equals("TAN"))
							{
								x = getX();
								theStack.insertElementAt(new Float(Math.tan(x)),0);
							}
							break;
						}
/*						case ('b'):
						{
							if (token.substring(0,5).equals("bean:"))
							{
								context = token.substring(5);
								try
								{
									theStack.insertElementAt(new Float(DropletDescriptor.getPropertyValue(request,response,context,true,null,null).toString()),0);
								}
								catch (Exception ex)
								{
									error = true;
									errorCode = -4;
								}
							}
							break;
						} */
						case ('p'):
						{
							if (token.substring(0,6).equals("param:"))
							{
								if (pageContext.getRequest().getParameter(token.substring(6)) != null)
								{
									theStack.insertElementAt(pageContext.getRequest().getParameter(token.substring(6)),0);
								}
								else
								{
									error = true;
									errorCode = -5;
								}
							}
							break;
						}
						case ('>'):
						{
							if (token.equals(">="))
							{
								x = getX();
								y = getX();
								theStack.insertElementAt(new Float(y>=x?1:0),0);
							}
							break;
						}
						case ('<'):
						{
							if (token.equals("<="))
							{
								x = getX();
								y = getX();
								theStack.insertElementAt(new Float(y<=x?1:0),0);
							}
							break;
						}
					}
				}
			}
		}

		if (!error)
		{
			Float result = new Float(getX());

			if (format == null)
			{
				finalOutput = result.toString();
				return (EVAL_BODY_AGAIN);
			}
			else
			{
				int		counter = 0;
				char	theChar = '\u0000';
				for (int i=0; i<format.length() && counter <= 1; i++)
				{
					firstChar = format.charAt(i);
					if (counter < 1)
					{
						if (theChar == '\u0000') theChar = format.charAt(i);
						else if (firstChar != theChar && firstChar != '.')
						{
							error = true;
							errorCode = -6;
						}
					}
					if (counter > 0 && firstChar != '#')
					{
						error = true;
						errorCode = -6;
					}
					if (firstChar == '.') counter++;
				}
				if (counter > 1 || format.length() == 0)
				{
					error = true;
					errorCode = -6;
				}
				if (!error)
				{
					boolean	negative;
					String	intStr;
					String	frcStr;
					Integer	theInt;
					
					String	stringResult = result.toString();

					if (stringResult.indexOf('E') != -1)
					{
						firstChar = stringResult.charAt(0);
						if (firstChar == '-')
						{
							negative = true;
							stringResult = stringResult.substring(1);
						}
						else negative = false;
						
						intStr = stringResult.substring(0,stringResult.indexOf('.'));
						frcStr = stringResult.substring(stringResult.indexOf('.')+1,stringResult.indexOf('E'));
						theInt = new Integer(stringResult.substring(stringResult.indexOf('E')+1));
						counter = theInt.intValue();
						if (counter > 0) counter -= frcStr.length();
						if (negative) stringResult = "-";
						else stringResult = "";
						if (counter < 0)
						{
							stringResult = stringResult + "0.";
							for (int i=0; i > counter; i--) stringResult = stringResult +'0';
							stringResult = stringResult + intStr + frcStr;
						}
						else
						{
							stringResult = stringResult + intStr + frcStr;
							for (int i=0; i < counter; i++) stringResult = stringResult + '0';
							stringResult = stringResult + ".0";
						}
					}

					firstChar = stringResult.charAt(0);
					if (firstChar == '-')
					{
						negative = true;
						stringResult = stringResult.substring(1);
					}
					else negative = false;

					intStr = stringResult.substring(0,stringResult.indexOf('.'));
					frcStr = stringResult.substring(stringResult.indexOf('.')+1);

					String	intFormat,frcFormat;
					counter = format.indexOf('.');
					if (counter == -1)
					{
						intFormat = format;
						frcFormat = "";
					}
					else
					{
						intFormat = format.substring(0,format.indexOf('.'));
						frcFormat = format.substring(format.indexOf('.'));
						if (frcFormat.length()==1) frcFormat = "";
						else frcFormat = frcFormat.substring(1);
					}
					if (intFormat.length() < intStr.length())
					{
						finalOutput = format.replace('0','#');
					}
					else
					{
						if (negative) finalOutput = "-";
						else finalOutput ="";
						
						if (intFormat.length() == intStr.length()) finalOutput = finalOutput+intStr;
						else
						{
							for (int i=0; i < intFormat.length()-intStr.length();i++)
							{
								if (format.charAt(i) == '0') finalOutput = finalOutput+'0';
								else finalOutput = finalOutput+" ";
							}
							finalOutput = finalOutput+intStr;
						}
						if (counter != -1)
						{
							finalOutput = finalOutput+'.';
							if (frcStr.length() > frcFormat.length())
							{
								finalOutput = finalOutput + frcStr.substring(0,frcFormat.length());
							}
							else
							{
								finalOutput = finalOutput+frcStr;
								for (int i=0; i < frcFormat.length()-frcStr.length(); i++) finalOutput = finalOutput +'0';
							}
						}
					}
				}
			}
		}

		if (error)
		{
			switch (errorCode)
			{
				case (-1):
				{
					errorCode = -401;
					errorMsg = "'formula' param is missing";
					break;
				}
				case (-2):
				{
					errorCode = -402;
					errorMsg = "Invalid number";
					break;
				}
				case (-3):
				{
					errorCode = -403;
					errorMsg = "Division by 0";
					break;
				}
				case (-4):
				{
					errorCode = -404;
					errorMsg = "Could not parse bean: "+token.substring(5);
					break;
				}
				case (-5):
				{
					errorCode = -405;
					errorMsg = "Could not parse param: "+token.substring(6);
					break;
				}
				case (-6):
				{
					errorCode = -406;
					errorMsg = "Illegal format string";
					break;
				}
			}
		}

		return (EVAL_BODY_AGAIN);
	}
	
	public int doSuccess()
	{
		if (!error)
		{
			pageContext.setAttribute("result",finalOutput);

			return EVAL_BODY_AGAIN;
		}
		return SKIP_BODY;
	}

	public float getX()
	{
		Object 	theObject;
		Float	tempFloat;
		Integer	tempInteger;
		Double	tempDouble;
		Boolean	tempBoolean;
		String	tempString;
		
		if (theStack.size() > 0)
		{
			theObject = theStack.elementAt(0);
			theStack.removeElementAt(0);
		}
		else theObject = new Integer(0);
		
		if (theObject instanceof Float)
		{
			tempFloat = (Float)theObject;
			return tempFloat.floatValue();
		}
		if (theObject instanceof Integer)
		{
			tempInteger = (Integer)theObject;
			return tempInteger.floatValue();
		}
		if (theObject instanceof Double)
		{
			tempDouble = (Double)theObject;
			return tempDouble.floatValue();
		}
		if (theObject instanceof Boolean)
		{
			tempBoolean = (Boolean)theObject;
			if (tempBoolean.booleanValue()) return 1;
			else return 0;
		}
		if (theObject instanceof String)
		{
			tempString = (String)theObject;
			tempFloat = Float.valueOf(tempString);
			return tempFloat.floatValue();
		}
		
		error = true;
		return 0;
	}
}
