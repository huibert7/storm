package org.storm.tags.general;

public class ForStackElement extends Object
{
	String		indexName;
	int			index;
	int			finalValue;
	int			step;
	boolean		finished;

	public ForStackElement(String indexName,int index,int finalValue,int step,boolean finished)
	{
		this.indexName = indexName;
		this.index = index;
		this.finalValue = finalValue;
		this.step = step;
		this.finished = finished;
	}

	public String getIndexName()
	{
		return indexName;
	}

	public int getIndex()
	{
		return index;
	}

	public int getFinal()
	{
		return finalValue;
	}

	public int getStep()
	{
		return step;
	}

	public boolean getFinished()
	{
		return finished;
	}
}
