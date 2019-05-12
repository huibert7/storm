package org.storm.tags.general;

import java.io.BufferedReader;
import java.io.FileReader;

import javax.servlet.jsp.tagext.TagSupport;

public class IncludeFile extends TagSupport
{
	private static final long	serialVersionUID = 9107740370422480838L;
	private String				file;
	private boolean				includecr = true;

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

	public int doEndTag()
	{
		StringBuffer	sb = new StringBuffer();
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
			pageContext.getOut().write(sb.toString());
		}
		catch(Exception e)
		{
			try
			{
				pageContext.getOut().write("<h2>Error 404: File not found</h2>");
			}
			catch(Exception ex)
			{
			}
		}

		includecr = true;

		return EVAL_PAGE;
	}

}
