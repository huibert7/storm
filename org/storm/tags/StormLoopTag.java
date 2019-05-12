package org.storm.tags;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;

public class StormLoopTag extends StormTag
{
	private static final long serialVersionUID = -3828991345099710752L;
	private	Integer		PreviousCount;

	public final void doInitBody()
	{
		PreviousCount = (Integer)pageContext.getAttribute("rowcount");
		pageContext.setAttribute("rowcount",new Integer(0));
	}

	public final int doAfterBody()
	{
		BodyContent body = getBodyContent();
		JspWriter writer = body.getEnclosingWriter();

		try
		{
			writer.print(body.getString());
			getBodyContent().clear();
		}
		catch(Exception ex) {}

		if (PreviousCount == null) pageContext.setAttribute("rowcount",new Integer(0));
		else pageContext.setAttribute("rowcount",PreviousCount);

		return SKIP_BODY;
	}

	public final int doDailyReportEnd()
	{
		return SKIP_BODY;
	}

	public final int doDailyReportLoop()
	{
		return SKIP_BODY;
	}

	public final int doDailyReportStart()
	{
		return SKIP_BODY;
	}

	public final int doDailySummaryEnd()
	{
		return SKIP_BODY;
	}

	public final int doDailySummaryLoop()
	{
		return SKIP_BODY;
	}

	public final int doDailySummaryStart()
	{
		return SKIP_BODY;
	}

	public final int doHourlySummaryEnd()
	{
		return SKIP_BODY;
	}

	public final int doHourlySummaryLoop()
	{
		return SKIP_BODY;
	}

	public final int doHourlySummaryStart()
	{
		return SKIP_BODY;
	}

	public final int doInitial()
	{
		if (!error && initialRequired)
		{
			pageContext.setAttribute("rowcount",new Integer(0));

			initialRequired = false;
			return EVAL_BODY_AGAIN;
		}
		else return SKIP_BODY;
	}

	public final int doFinal()
	{
		if (!error && count != 0)
		{
			pageContext.setAttribute("rowcount",new Integer(count));

			return EVAL_BODY_AGAIN;
		}
		else return SKIP_BODY;
	}

	public int doIllegalOperation()
	{
		return SKIP_BODY;
	}

	public int doNoResults()
	{
		if (!error && count == 0) return EVAL_BODY_AGAIN;
		else return SKIP_BODY;
	}

	public final int doSuccess()
	{
		return SKIP_BODY;
	}

	public int doTotal()
	{
		return SKIP_BODY;
	}

	public final boolean supportsDailyReport()
	{
		return false;
	}

	public final boolean supportsDailySummary()
	{
		return false;
	}

	public final boolean supportsError()
	{
		return true;
	}

	public final boolean supportsHourlySummary()
	{
		return false;
	}

	public final boolean supportsIllegalOperation()
	{
		return false;
	}

	public final boolean supportsInitial()
	{
		return true;
	}

	public final boolean supportsLoop()
	{
		return true;
	}

	public final boolean supportsFinal()
	{
		return true;
	}

	public final boolean supportsNoResults()
	{
		return true;
	}

	public final boolean supportsSuccess()
	{
		return false;
	}

	public boolean supportsTotal()
	{
		return false;
	}
}
