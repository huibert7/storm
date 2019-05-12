package org.storm.tags.general;

import java.util.Vector;

import org.storm.tags.StormLoopTag;

public class For extends StormLoopTag
{
	private static final long 	serialVersionUID = 1888341291147614814L;
	String						indexName = null,indexName2;
	int							index = 1,index2;
	int							finalValue,final2;
	int							step = 1,step2;
	boolean						finished;
	Vector<ForStackElement>		stack = new Vector<ForStackElement>();

	public void setIndexAttribute(String tempParam)
	{
		indexName2 = tempParam;
	}

	public void setInitialValue(String tempParam)
	{
		if (tempParam==null || tempParam.equals("null"))
		{
			index2 = 1;
		}
		else
		{
			try
			{
				index2 = (new Integer(tempParam)).intValue();
			}
			catch(Exception ex)
			{
				error = true;
				errorMsg = "Illegal value for 'initialValue' parameter";
				errorCode = -1007;
			}
		}
	}

	public void setFinalValue(String tempParam)
	{
		if (tempParam==null || tempParam.equals("null"))
		{
			error = true;
			errorMsg = "Missing 'finalValue' parameter";
			errorCode = -1033;
		}
		else
		{
			try
			{
				final2 = (new Integer(tempParam)).intValue();
			}
			catch(Exception ex)
			{
				error = true;
				errorMsg = "Illegal value for 'finalValue' parameter";
				errorCode = -1025;
			}
		}
	}

	public void setStep(String tempParam)
	{
		if (tempParam == null || tempParam.equals("null"))
		{
			step2 = 1;
		}
		else
		{
			try
			{
				step2 = (new Integer(tempParam)).intValue();
			}
			catch(Exception ex)
			{
				error = true;
				errorMsg = "Illegal value for 'step' parameter";
				errorCode = -1082;
			}
		}
	}

	public int doStartTag()
	{
		if (index2 >= final2) return (SKIP_BODY);

		push();
		indexName = indexName2;
		index = index2;
		finalValue = final2;
		step = step2;
		finished = false;
		count = 0;
		initialRequired = true;

	    return (EVAL_BODY_AGAIN);
	}

	public int doLoop()
	{
		if (!error)
		{
			if (!finished)
			{
				count ++;
				pageContext.setAttribute(indexName,new Integer(index));
				pageContext.setAttribute("rowcount",new Integer(count));
			
				index += step;
				if (index > finalValue) finished = true;
	
				return EVAL_BODY_AGAIN;
			}
		}

		return SKIP_BODY;
	}

	public int doEndTag()
	{
		if (stack.size() > 0) pull();
		
		return EVAL_PAGE;
	}

	public void push()
	{
		stack.addElement(new ForStackElement(indexName,index,finalValue,step,finished));
	}

	public void pull()
	{
		ForStackElement temp = (ForStackElement)stack.lastElement();
		indexName = temp.getIndexName();
		index = temp.getIndex();
		finalValue = temp.getFinal();
		step = temp.getStep();
		finished = temp.getFinished();
		stack.removeElementAt(stack.size()-1);
	}
}
