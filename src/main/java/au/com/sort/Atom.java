package au.com.sort;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Atom implements Comparable<Atom>
{
	public String type;
	public Vector3D position;
	public String label;

	public Atom(String type, double x, double y, double z)
	{
		this.type = type;
		this.position = new Vector3D(x, y, z);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.position == null) ? 0 : this.position.hashCode());
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
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
		Atom other = (Atom) obj;
		if (this.position == null)
		{
			if (other.position != null)
				return false;
		}
		else if (!this.position.equals(other.position))
			return false;
		if (this.type == null)
		{
			if (other.type != null)
				return false;
		}
		else if (!this.type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public int compareTo(Atom o)
	{
		int c = o.type.compareTo(this.type);
		if (c == 0)
		{
			c = (int) (o.position.distance(this.position) * 1000.9);
		}

		return c;
	}

	@Override
	public String toString()
	{
		return this.type + "X " + this.position.getX() + " " + this.position.getY() + " "
				+ this.position.getZ();
	}

}
