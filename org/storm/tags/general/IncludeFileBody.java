package org.storm.tags.general;

import java.io.BufferedReader;
import java.io.FileReader;

import org.storm.tags.StormTag;

public class IncludeFileBody extends StormTag
{
	private static final long serialVersionUID = -2405841890144055065L;
	String	file;
	boolean	includecr = true;
	StringBuffer	sb = new StringBuffer();

	public void setFile(String file)
	{
		if (!(file == null || file.equals("null")))
		{
			this.file = file;
		}
		else file = null;
	}
	
	public void setIncludecr(String tempParam)
	{
		if (tempParam == null || tempParam.equals("null"))
		{
			this.includecr = true;
		}
		else
		{
			tempParam = tempParam.trim().toLowerCase();
			if (!(tempParam.equals("true") || tempParam.equals("false"))) this.includecr = true;
			else
			{
				if (tempParam.equals("true")) includecr = true;
				else includecr = false;
			}
		}
	}

	public int doStartTag()
	{
		
		BufferedReader in;
		String			line;

		try
		{
			in = new BufferedReader(new FileReader(file));

			if (includecr)
			{
				while((line=in.readLine())!=null)
				{
					sb.append(line+System.getProperty("line.separator"));
				}
			}
			else
			{
				while((line=in.readLine())!=null)
				{
					sb.append(line);
				}
			}
			in.close();
			//pageContext.getOut().write(sb.toString());
		}
		catch(Exception e)
		{
			try
			{
				error = true;
				errorMsg = "Error 404: File not found: "+e.getMessage()+".";
				errorCode = -9999;
			}
			catch(Exception ex)
			{
			}
		}

		return EVAL_BODY_AGAIN;
	}
	
	public int doSuccess()
	{
		if (!error)
		{
			pageContext.setAttribute("text",sb.toString());
			return EVAL_BODY_AGAIN;
		}
		return SKIP_BODY;
	}
	
	public int doEndTag()
	{
		includecr = true;
		return EVAL_PAGE;
	}
	
	public boolean supportsError()
	{
		return true;
	}
		
	public boolean supportsSuccess()
	{
		return true;
	}
	
	
	

}
