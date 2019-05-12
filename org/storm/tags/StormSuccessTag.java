package org.storm.tags;

import javax.servlet.jsp.JspTagException;

public class StormSuccessTag extends StormTag
{
	private static final long serialVersionUID = 7551984830761843026L;

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

	public int doEndTag() throws JspTagException
	{
		return EVAL_PAGE;
	}

	public void doError() throws JspTagException
	{
	}

	public final int doInitial()
	{
		return SKIP_BODY;
	}

	public final int doFinal()
	{
		return SKIP_BODY;
	}

	public int doIllegalOperation()
	{
		return SKIP_BODY;
	}

	public final int doLoop()
	{
		return SKIP_BODY;
	}

	public final int doNoResults()
	{
		return SKIP_BODY;
	}

	public int doSuccess()
	{
		return SKIP_BODY;
	}

	public final int doTotal()
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

	public final boolean supportsFinal()
	{
		return false;
	}

	public final boolean supportsHourlySummary()
	{
		return false;
	}

	public boolean supportsIllegalOperation()
	{
		return false;
	}

	public final boolean supportsInitial()
	{
		return false;
	}

	public final boolean supportsLoop()
	{
		return false;
	}

	public final boolean supportsNoResults()
	{
		return false;
	}

	public final boolean supportsSuccess()
	{
		return true;
	}

	public final boolean supportsTotal()
	{
		return false;
	}
}
