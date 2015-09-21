package au.com.sort;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Relationship
{
	List<Vector3D> neighbours = new LinkedList<>();
	Vector3D origin;
	Molecule molecule;

	public Relationship(Molecule molecule)
	{
		this.molecule = molecule;
		origin = molecule.position;
	}

	double similarity(Relationship o)
	{
		double ret = 0;
		ret = origin.distance(o.origin);

		 ret += Math.abs(Vector3D.angle(origin, o.origin)) ;
		// ret += Math.abs(origin.getX()-o.origin.getX());

		ret += Math.abs(origin.getX() - o.origin.getX());
		ret += Math.abs(origin.getY() - o.origin.getY());
		ret += Math.abs(origin.getZ() - o.origin.getZ());

		int n1 = 0;
		for (Vector3D neighbour : neighbours)
		{
			n1 += origin.distance(neighbour);
		}

		int n2 = 0;
		for (Vector3D neighbour : o.neighbours)
		{
			n2 += o.origin.distance(neighbour);
		}

		ret += Math.abs(n1 - n2);

		ret += Math.abs(neighbours.size() - o.neighbours.size());

		return ret;

	}

	public void addNeighbour(Molecule other)
	{
		neighbours.add(other.position);

	}

}
