package org.storm.tags.general.utilities.encryption;

import java.io.BufferedReader;
import java.io.FileReader;

import org.storm.tags.StormTag;
import org.storm.util.encryption.SHA1;

public class CreateHashKeyTag extends StormTag
{
	private static final long serialVersionUID = -4158074554775979172L;
	String		string = null;
	String		path = null;
	SHA1		s = null;

	public boolean supportsSuccess()
	{
		return true;
	}

	public boolean supportsError()
	{
		return true;
	}

	public void setString(String tempParam)
	{
		if (tempParam == null || tempParam.equals("null")) string = null;
		else string = tempParam;
	}

	public void setFilePath(String tempParam)
	{
		if (tempParam == null || tempParam.equals("null")) path = null;
		else path = tempParam;
	}

	public int doStartTag()
	{
		if (string == null && path == null)
		{
			error = true;
			errorMsg = "Missing 'string' or 'filePath' parameter";
			errorCode = -1501;
		}
		else
		{
			if (path != null)
			{
				StringBuffer	sb = new StringBuffer();
				BufferedReader in;
				String			line;

				try
				{
					in = new BufferedReader(new FileReader(path));

					while((line = in.readLine())!=null)
					{
						sb.append(line+System.getProperty("line.separator"));
					}
					in.close();
					string = sb.toString();
					sb = null;
				}
				catch(Exception e)
				{
					error = true;
					errorMsg = "File not found";
					errorCode = -1503;
				}
			}

			s = new SHA1();

			if (!s.selfTest())
			{
				error = true;
				errorMsg = "Failed self-test";
				errorCode = -1502;
			}
		}

		return EVAL_BODY_AGAIN;
	}

	public int doSuccess()
	{
		if (!error)
		{
			s.update(string);
			s.finalize();

			pageContext.setAttribute("hashKey",s.toString());

			s.clear();

			return EVAL_BODY_AGAIN;
		}
		return SKIP_BODY;
	}

	public int doEndTag()
	{
		path = null;
		string = null;

		return EVAL_PAGE;
	}
}

