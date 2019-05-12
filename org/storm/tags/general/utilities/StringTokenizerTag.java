package org.storm.tags.general.utilities;

import java.util.StringTokenizer;

import org.storm.tags.StormLoopTag;

public class StringTokenizerTag extends StormLoopTag
{
	private static final long serialVersionUID = 3237298976785081659L;
	String				string = null;
	String				token = ",";
	StringTokenizer		tokenizer;
	boolean			includeToken = false;
	boolean			finished;

	public void setString(String string)
	{
		if (string == null || string.equals("null"))
		{
			error = true;
			errorMsg = "Missing value for 'string' parameter";
			errorCode = -9999;
		}
		else
		{
			this.string = string;
		}
	}

	public void setToken(String token)
	{
		if (token == null || token.equals("null"))
		{
			this.token = ",";
		}
		else
		{
			this.token = token;
		}
	}

	public void setIncludeToken(String tempParam)
	{
		if (tempParam == null || tempParam.equals("null"))
		{
			includeToken = false;
		}
		else
		{
			tempParam = tempParam.toLowerCase();
			
			if (!tempParam.equals("true") && !tempParam.equals("false"))
			{
				error = true;
				errorMsg = "Illegal value for 'debug' parameter: " + tempParam;
				errorCode = -9999;
			}
			else
			{
				if (tempParam.equals("true")) includeToken = true;
				else includeToken = false;
			}
		}
	}

	public int doStartTag()
	{
		tokenizer = new StringTokenizer(string,token,includeToken);
		
		count = 0;
		finished = true;
		initialRequired = false;

		if (tokenizer.hasMoreTokens())
		{
			finished = false;
			initialRequired = true;
		}

		return EVAL_BODY_AGAIN;
	}

	public int doLoop()
	{
		if (!error)
		{
			if (!finished)
			{
				count++;
				
				pageContext.setAttribute("string",tokenizer.nextToken());

				pageContext.setAttribute("rowcount",new Integer(count));
					
				if (!tokenizer.hasMoreElements()) finished = true;

				return EVAL_BODY_AGAIN;
			}
		}

		return SKIP_BODY;
	}

	public int doEndTag()
	{
		string = null;
		token = ",";
		includeToken = false;
		error = false;

		return EVAL_PAGE;
	}
}
