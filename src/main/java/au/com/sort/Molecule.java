package au.com.sort;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Molecule implements Comparable<Molecule>
{
	String type;
	Vector3D position;

	Molecule(String type, double x, double y, double z)
	{
		this.type = type;
		position = new Vector3D(x, y, z);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Molecule other = (Molecule) obj;
		if (position == null)
		{
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (type == null)
		{
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public int compareTo(Molecule o)
	{
		int c = o.type.compareTo(type);
		if (c == 0)
		{
			c = (int) (o.position.distance(position) * 1000.9);
		}

		return c;
	}

	@Override
	public String toString()
	{
		return type + " " + position.getX() + " " + position.getY() + " "
				+ position.getZ();
	}

}
