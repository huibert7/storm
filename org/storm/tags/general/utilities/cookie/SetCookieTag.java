package org.storm.tags.general.utilities.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.storm.tags.StormTag;

/**
 * A tag to set cookies
 */
public class SetCookieTag extends StormTag
{
	private static final long serialVersionUID = -6312981488896404380L;
	String	name = null;
	String	value = null;
	String	comment= null;
	String	domain = null;
	String	path = null;
	Boolean	secure = new Boolean(false);
	int		version = 0;
	int		maxAge = -1;

	public boolean supportsError()
	{
		return true;
	}

	public boolean supportsSuccess()
	{
		return true;
	}

	public void setComment(String comment)
	{
		if (!(comment == null || comment.equals("null")))
		{
			this.comment = comment;
		}
		else
		{
			this.comment = null;
		}
	}

	public void setDomain(String domain)
	{
		if (!(domain == null || domain.equals("null")))
		{
			this.domain = domain;
		}
		else
		{
			this.domain = null;
		}
	}

	public void setMaxAge(String maxAge)
	{
		if (!(maxAge == null || maxAge.equals("null")))
		{
			try
			{
				this.maxAge = (new Integer(maxAge)).intValue();
			}
			catch (Exception ex)
			{
				error = true;
				errorCode = -1244;
				errorMsg = "Illegal value for 'maxAge' parameter";
			}
		}
		else
		{
			this.maxAge = 0;
		}
	}

	public void setName(String name)
	{
		if (!(name == null || name.equals("null")))
		{
			this.name = name;
		}
		else
		{
			error = true;
			errorCode = -1019;
			errorMsg = "Missing 'name' parameter";
		}
	}

	public void setPath(String path)
	{
		if (!(path == null || path.equals("null")))
		{
			this.path = path;
		}
		else
		{
			this.path = null;
		}
	}

	public void setSecure(String secure)
	{
		if (!(secure == null || secure.equals("null")))
		{
			secure = secure.toLowerCase().trim();
			try
			{
				this.secure = new Boolean(secure);
			}
			catch (Exception ex)
			{
				error = true;
				errorCode = -1243;
				errorMsg = "Illegal value for 'secure' parameter";
			}
		}
		else
		{
			this.secure = null;
		}
	}

	public void setValue(String value)
	{
		if (!(value == null || value.equals("null") || value.length() == 0))
		{
			this.value = value;
		}
		else
		{
			this.value = null;
		}
	}

	public void setVersion(String version)
	{
		if (!(version == null || version.equals("null")))
		{
			try
			{
				this.version = (new Integer(version)).intValue();
				if (this.version != 0 && this.version != 1)
				{
					error = true;
					errorCode = -1244;
					errorMsg = "Illegal value for 'version' parameter";
				}
			}
			catch (Exception ex)
			{
				error = true;
				errorCode = -1244;
				errorMsg = "Illegal value for 'version' parameter";
			}
		}
		else
		{
			this.version = 0;
		}
	}

	public int doStartTag()
	{
		return EVAL_BODY_AGAIN;
	}

	public int doSuccess()
	{
		if (!error)
		{
			return EVAL_BODY_AGAIN;
		}
		return SKIP_BODY;
	}

	public int doEndTag()
	{
		if (!error)
		{
			Cookie cookie;
			
			if (value != null)
			{
				cookie = new Cookie(name,value);
				cookie.setMaxAge(maxAge);
			}
			else
			{
				cookie = new Cookie(name,"");
				cookie.setMaxAge(0);
			}
			cookie.setVersion(version);
			if (comment != null) cookie.setComment(comment);
			if (domain != null) cookie.setDomain(domain);
			if (path != null) cookie.setPath(path);
			if (secure != null) cookie.setSecure(secure.booleanValue());

			((HttpServletResponse)pageContext.getResponse()).addCookie(cookie);
		}

		name = null;
		value = null;
		comment= null;
		domain = null;
		path = null;
		secure = new Boolean(false);
		version = 0;
		maxAge = -1;

		return EVAL_PAGE;
	}
}
