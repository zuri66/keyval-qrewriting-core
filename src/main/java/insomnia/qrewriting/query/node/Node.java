package insomnia.qrewriting.query.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import insomnia.qrewriting.query.Label;
import insomnia.qrewriting.query.QueryInfos;

/**
 * Noeud d'une requête
 * 
 * @author zuri
 * 
 */
public class Node implements Iterable<Node>
{
	private Node			parent;
	private Label			label;
	private NodeChilds		childs;
	private NodeValue		value;
	private int				id				= -1;
	private boolean			isPath			= true;

	private int				nbOfDescendants	= 0;
	private int				nbOfParents		= 0;

	protected QueryInfos	infos;

	public Node()
	{
		childs = new NodeChilds();
		setLabel(new Label());
	}

	// =======================================================

	/**
	 * Constructeur par copie
	 * 
	 * @param n
	 */
	public Node(Node n)
	{
		if (n.label != null)
			setLabel(new Label(n.label));

		id = n.id;
		childs = n.childs.copy(this);
		// Node[] newChilds = new Node[childs.size()];
		// int i = 0;
		//
		// for(Node child : childs)
		// newChilds[i] = child.clone();
		//
		// addChild(newChilds);

		isPath = n.isPath;
		nbOfDescendants = n.nbOfDescendants;
		nbOfParents = n.nbOfParents;

		if (n.value != null)
			setValue(n.value.clone());
	}

	// =======================================================

	public void setParent(Node parent)
	{
		this.parent = parent;
	}

	public Node setParentMe(Node parent)
	{
		setParent(parent);
		return this;
	}

	public Node getParent()
	{
		return parent;
	}

	public void setLabel(Label l)
	{
		label = l;
	}

	public Node setLabelMe(String l)
	{
		setLabel(l);
		return this;
	}

	public void setLabel(String l)
	{
		label = new Label(l);
	}

	public Node setLabelMe(Label l)
	{
		setLabel(l);
		return this;
	}

	public Label getLabel()
	{
		return label;
	}

	public void setValue(NodeValue v)
	{
		value = v;
	}

	public Node setValueMe(NodeValue v)
	{
		setValue(v);
		return this;
	}

	public NodeValue getValue()
	{
		return value;
	}

	public void setId(int i)
	{
		id = i;
	}

	// public Node setIdMe(int i)
	// {
	// setId(i);
	// return this;
	// }

	public int getId()
	{
		return id;
	}

	// =======================================================

	public void setInfos(QueryInfos i)
	{
		infos = i;
	}

	public Node setInfosMe(QueryInfos i)
	{
		setInfos(i);
		return this;
	}

	public QueryInfos setInfosHim(QueryInfos i)
	{
		setInfos(i);
		return infos;
	}

	public QueryInfos getInfos()
	{
		return infos;
	}

	// =======================================================

	public NodeChilds getChilds()
	{
		return childs;
	}

	public boolean isPath()
	{
		return isPath;
	}

	public int getNbOfDescendants()
	{
		return nbOfDescendants;
	}

	public int getNbOfParents()
	{
		return nbOfParents;
	}

	public int getNbOfChilds()
	{
		return childs.size();
	}

	/**
	 * Est une feuille
	 * 
	 * @return true|false
	 */
	public boolean isLeaf()
	{
		return nbOfDescendants == 0;
	}

	public boolean isUnfolded()
	{
		for (Node n : getDescendants())
		{
			if (n.getLabel().size() > 1)
				return false;
		}
		return true;
	}

	public boolean childsArePaths()
	{
		for (Node child : childs)
		{
			if (!child.isPath)
				return false;
		}
		return true;
	}

	// =======================================================

	public Node[] getLeaves()
	{
		ArrayList<Node> ret = new ArrayList<>();

		for (Node n : getDescendants())
		{
			if (n.isLeaf())
				ret.add(n);
		}
		return ret.toArray(new Node[0]);
	}

	/**
	 * @return Toutes les clés à partir du noeud
	 */
	// public Set<String> getAllKeys()
	// {
	// Set<String> ret = new HashSet<>();
	//
	// for (Node n : getDescendants())
	// {
	// Label l = n.getLabel();
	//
	// if (l != null)
	// {
	// for (String k : l)
	// ret.add(k);
	// }
	// }
	// return ret;
	// }

	/**
	 * Récupère tous les parents du noeud
	 * 
	 * @param ret
	 * @param n
	 */
	public Node[] getParents()
	{
		ArrayList<Node> ret = new ArrayList<>();
		Node n = this.getParent();

		while (n != null)
		{
			ret.add(n);
			n = n.getParent();
		}
		Collections.reverse(ret);
		return ret.toArray(new Node[0]);
	}

	/**
	 * Récupère toutes les racines de chemins
	 * 
	 * @param ret
	 * @param n
	 */
	private Node[] getPaths(boolean ignoreRoot)
	{
		ArrayList<Node> ret = new ArrayList<>();
		s_getPaths(this, ret, ignoreRoot);
		return ret.toArray(new Node[0]);
	}

	/**
	 * Récupère toutes les racines d'arbres
	 * 
	 * @param ret
	 * @param n
	 */
	private Node[] getTrees(boolean ignoreRoot)
	{
		ArrayList<Node> ret = new ArrayList<>();
		s_getTrees(this, ret, ignoreRoot);
		return ret.toArray(new Node[0]);
	}

	public Node[] getTrees()
	{
		return getTrees(true);
	}

	public Node[] getPaths()
	{
		return getPaths(true);
	}

	protected void s_getPaths(Node node, ArrayList<Node> ret, boolean ignoreRoot)
	{
		if (node.isPath && !(ignoreRoot && node.parent == null))
		{
			ret.add(node);
			return;
		}

		for (Node child : node)
			s_getPaths(child, ret, ignoreRoot);
	}

	protected void s_getTrees(Node node, ArrayList<Node> ret, boolean ignoreRoot)
	{
		if (!node.isPath && !(ignoreRoot && node.parent == null))
			ret.add(node);

		for (Node child : node)
			s_getTrees(child, ret, ignoreRoot);
	}

	/**
	 * @return Tous les descendants, noeud this inclus
	 */
	public Node[] getDescendants()
	{
		ArrayList<Node> ret = new ArrayList<>();
		s_getNodes(ret, this);
		ret.remove(this);
		return ret.toArray(new Node[0]);
	}

	protected void s_getNodes(ArrayList<Node> ret, Node n)
	{
		ret.add(n);

		for (Node c : n.getChilds())
			s_getNodes(ret, c);
	}

	/**
	 * @return Le noeud dont l'identifiant est $id
	 */
	public Node getNode(int id)
	{
		for (Node n : getDescendants())
		{
			if (n.getId() == id)
				return n;
		}
		return null;
	}

	// =======================================================

	public void addChild(Node... childs)
	{
		final int moreNbOfDesc;
		final boolean nisPath = isPath;

		// Calcul du nombre de descendants à ajouter
		{
			int tmpNbOfDesc = 0;

			for (Node child : childs)
			{
				this.childs.add(child);
				child.setInfos(infos);
				child.setParent(this);
				tmpNbOfDesc += 1 + child.nbOfDescendants;

				// Calcul des nbParents
				child.nbOfParents = this.nbOfParents + 1;

				if (!child.isLeaf())
					child.deepPropagation(n -> n.nbOfParents++);
			}
			moreNbOfDesc = tmpNbOfDesc;
		}

		 if (infos != null)
		{
			infos.addNode(childs);
		}

		if (isPath)
		{
			final int nbChilds = getNbOfChilds();
			isPath = nbChilds <= 1;

			// 2e verif, le fils est un path ?
			for (Node child : this)
			{
				if (!child.isPath)
				{
					isPath = false;
					break;
				}
			}
		}
		// Mise à jour des noeuds parents
		backPropagation(n ->
		{
			n.nbOfDescendants += moreNbOfDesc;

			if (nisPath && !isPath)
				n.isPath = false;
		});
	}

	public void addChild(Collection<Node> childs)
	{
		addChild(childs.toArray(new Node[0]));
	}

	public Node addChildMe(Collection<Node> childs)
	{
		addChild(childs);
		return this;
	}

	public Node addChildMe(Node... childs)
	{
		addChild(childs);
		return this;
	}

	public Node addChildHim(Node child)
	{
		addChild(child);
		return child;
	}

	public Node newChildMe()
	{
		return addChildMe(new Node());
	}

	public Node newChildHim()
	{
		return addChildHim(new Node());
	}

	// =======================================================
	/**
	 * Applique une méthode depuis le noeud courant exclu jusqu'aux feuilles
	 */
	private void deepPropagation(Consumer<Node> method)
	{
		for (Node n : this.getDescendants())
		{
			method.accept(n);
			n = n.getParent();
		}
	}

	/**
	 * Applique une méthode depuis le noeud courant exclu jusqu'à la racine
	 */
	private void backPropagation(Consumer<Node> method)
	{
		Node n = this;

		do
		{
			method.accept(n);
			n = n.getParent();
		}
		while (n != null);
	}

	/**
	 * Retourne les données de la fonction collect jusqu'à la racine
	 * 
	 * @param collect
	 */
	public <T> List<T> backCollect(Function<Node, T> collect, boolean reverse, boolean jumpRoot)
	{
		ArrayList<T> ret = new ArrayList<>();
		Node n = this;

		do
		{
			if (jumpRoot && n.getParent() == null)
				break;

			ret.add(collect.apply(n));
			n = n.getParent();
		}
		while (n != null);

		if (reverse && ret.size() > 1)
			Collections.reverse(ret);

		ret.trimToSize();
		return ret;
	}

	public <T> List<T> backCollect(Function<Node, T> collect)
	{
		return backCollect(collect, true, true);
	}

	// =======================================================

	@Override
	public String toString()
	{
		String ret = "<" + id + label + "[p" + nbOfParents + ":d"
				+ nbOfDescendants + ":c" + getNbOfChilds() + ":P" + isPath
				+ "]";

		if (value != null)
			ret += "=" + value;

		ret += id + ">";

		if (!isLeaf())
			ret += " => " + childsToString();

		return ret;
	}

	public String childsToString()
	{
		StringBuilder ret = new StringBuilder();
		boolean doonce = false;

		ret.append("{" + id + " ");

		for (Node c : childs.getChilds())
		{
			if (doonce)
				ret.append(", ");

			ret.append(c);
			doonce = true;
		}
		return ret + " " + id + "}";
	}

	// @Override
	// public Node clone()
	// {
	// return new Node(this);
	// }

	@Override
	public Iterator<Node> iterator()
	{
		return getChilds().iterator();
	}

	/**
	 * La comparaison se fait sur le hashcode
	 * 
	 * @see #hashCode()
	 */
	@Override
	public boolean equals(Object e)
	{
		if (e instanceof Node)
		{
			return ((Node) e).hashCode() == hashCode();
		}
		return false;
	}

	/**
	 * Le code est fonction de l'id du noeud et de sa requête d'appartenance Il
	 * faut faire attention à bien comparer des noeuds déjà présents dans une
	 * requête
	 */
	@Override
	public int hashCode()
	{
		return id;
	}
}
