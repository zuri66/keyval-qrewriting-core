package insomnia.qrewriting.database.driver.internal;

import java.util.Map;

import insomnia.json.Element;
import insomnia.json.ElementArray;
import insomnia.json.ElementLiteral;
import insomnia.json.ElementNumber;
import insomnia.json.ElementObject;
import insomnia.json.ElementString;
import insomnia.json.Json;
import insomnia.json.JsonBuilder;
import insomnia.json.JsonBuilderException;
import insomnia.qrewriting.query.Label;
import insomnia.qrewriting.query.Query;
import insomnia.qrewriting.query.node.Node;
import insomnia.qrewriting.query.node.NodeChilds;
import insomnia.qrewriting.query.node.NodeValue;
import insomnia.qrewriting.query.node.NodeValueExists;
import insomnia.qrewriting.query.node.NodeValueFantom;
import insomnia.qrewriting.query.node.NodeValueLiteral;
import insomnia.qrewriting.query.node.NodeValueNumber;
import insomnia.qrewriting.query.node.NodeValueString;

public class JsonBuilder_query extends JsonBuilder
{
	Query query;

	public JsonBuilder_query()
	{
		super();
	}

	public JsonBuilder_query(Json j)
	{
		super(j);
	}

	public JsonBuilder_query(Json j, Query q)
	{
		super(j);
		setQuery(q);
	}

	public void setQuery(Query q)
	{
		query = q;
	}

	@Override
	public void build() throws JsonBuilderException
	{
		Json json = getJson();
		Node queryRoot = query.getRoot();

		if (queryRoot.isLeaf())
		{
			throw new JsonBuilderException("Bad query structure for " + query);
		}
		json.setDocument(makeJson(queryRoot));
	}

	private Element makeJson(Node node) throws JsonBuilderException
	{
		Element ret;
		Element newVal;
		NodeChilds childs = node.getChilds();

		Map<Label, Integer> labelCount = childs.getChildsLabelCount();

		// On vérifie les labels vides
		if (labelCount.get(new Label("")) != null)
		{
			boolean haveOthers = labelCount.size() > 1;

			if (haveOthers)
				throw new JsonBuilderException("Bad structure of " + node);

			ret = new ElementArray();
		}
		else
			ret = new ElementObject();

		for (Node ncur : node)
		{
			NodeValue vcur = ncur.getValue();
			Label lcur = ncur.getLabel();

			if (ncur.isLeaf())
			{
				if (vcur instanceof NodeValueNumber)
				{
					newVal = new ElementNumber(
						((NodeValueNumber) vcur).getNumber());
				}
				else if (vcur instanceof NodeValueString)
				{
					newVal = new ElementString(
						((NodeValueString) vcur).getString());
				}
				else if (vcur instanceof NodeValueLiteral)
				{
					newVal = new ElementLiteral(
						((NodeValueLiteral) vcur).getLiteral());
				}
				else if (vcur instanceof NodeValueExists)
				{
					newVal = new ElementLiteral(ElementLiteral.Literal.TRUE);
				}
				else if (vcur instanceof NodeValueFantom)
				{
					newVal = null;
				}
				else
				{
					throw new JsonBuilderException("Cannot make value " + vcur);
				}
			}
			else
			{
				newVal = makeJson(ncur);
			}

			if (newVal != null)
				ret.add(newVal, lcur.get());
		}
		return ret;
	}

	@Override
	public Json newBuild() throws JsonBuilderException
	{
		Json json = new Json();
		setJson(json);
		build();
		return json;
	}
}