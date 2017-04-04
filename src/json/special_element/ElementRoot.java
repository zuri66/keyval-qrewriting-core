package json.special_element;

import json.Element;
import printer.Printer;

public class ElementRoot extends Element
{
	public Element	e;

	public ElementRoot()
	{
	}

	ElementRoot(ElementRoot e)
	{
		this.e = (Element) e.clone();
	}

	@Override
	public Object getVal()
	{
		return getRoot();
	}

	public Element getRoot()
	{
		return e;
	}

	public String toString()
	{
		return "JSON(" + this.e + ")";
	}

	@Override
	public void prints(Printer p)
	{
		this.e.print(p);
		p.flush();
	}

	@Override
	public Object clone()
	{
		return new ElementRoot(this);
	}
}
