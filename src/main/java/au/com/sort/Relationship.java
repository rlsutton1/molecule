package au.com.sort;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Relationship
{
	List<Vector3D> neighbours = new LinkedList<>();
	Vector3D origin;
	Atom molecule;

	public Relationship(Atom molecule)
	{
		this.molecule = molecule;
		this.origin = molecule.position;
	}

	double similarity(Relationship o)
	{
		double ret = 0;
		ret = this.origin.distance(o.origin);

		ret += Math.abs(Vector3D.angle(this.origin, o.origin));
		// ret += Math.abs(origin.getX()-o.origin.getX());

		ret += Math.abs(this.origin.getX() - o.origin.getX());
		ret += Math.abs(this.origin.getY() - o.origin.getY());
		ret += Math.abs(this.origin.getZ() - o.origin.getZ());

		int n1 = 0;
		for (Vector3D neighbour : this.neighbours)
		{
			n1 += this.origin.distance(neighbour);
		}

		int n2 = 0;
		for (Vector3D neighbour : o.neighbours)
		{
			n2 += o.origin.distance(neighbour);
		}

		ret += Math.abs(n1 - n2);

		ret += Math.abs(this.neighbours.size() - o.neighbours.size());

		return ret;

	}

	public void addNeighbour(Atom other)
	{
		this.neighbours.add(other.position);

	}

}
