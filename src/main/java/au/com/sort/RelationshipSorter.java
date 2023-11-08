package au.com.sort;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import au.com.sort.fileloader.FileLoader;
import au.com.sort.fileloader.FileLoaderFactory;

public class RelationshipSorter
{

	public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException
	{

		FileLoader loader = FileLoaderFactory.getFileLoader(args[0], args[1], args[2], args[3]);
		List<Atom> primary = loader.getPrimary();

		List<Atom> secondary = loader.getSecondary();

		getSorted(primary, secondary);
	}

	public static List<Atom> getSorted(
			List<Atom> primary, List<Atom> secondary)
			throws InterruptedException
	{
		List<Atom> results = new LinkedList<>();
		double disturb = 0;
		for (int i = 0; i < primary.size(); i++)
		{
			disturb += primary.get(i).position
					.distance(secondary.get(i).position);
		}
		System.out.println("Total disturbance " + disturb);
		Thread.sleep(2000);

		List<Relationship> primaryRelationships = identify(primary);
		List<Relationship> secondaryRelationships = identify(secondary);

		disturb = 0;
		for (Relationship pr : primaryRelationships)
		{
			double bestMatch = 1000000;
			Relationship bestRelationship = null;
			for (Relationship sr : secondaryRelationships)
			{
				double similarity = pr.similarity(sr);
				if (similarity < bestMatch &&
						pr.origin.distance(sr.origin) > 0.7)
				{
					bestMatch = similarity;
					bestRelationship = sr;

				}

			}
			if (bestRelationship == null)
			{
				return results;
			}
			results.add(bestRelationship.molecule);
			secondaryRelationships.remove(bestRelationship);
			disturb += pr.origin.distance(bestRelationship.origin);
			System.out.println(pr.molecule + " " + bestRelationship.molecule
					+ " disturbance "
					+ pr.origin.distance(bestRelationship.origin));

		}
		System.out.println("Total disturbance " + disturb);

		System.out.println(primary.size());
		for (Atom m : primary)
		{
			System.out.println(m);
		}

		System.out.println(primary.size());
		for (Atom m : results)
		{
			System.out.println(m);
		}
		return results;
	}

	static List<Relationship> identify(List<Atom> molecules)
	{
		List<Relationship> relationships = new LinkedList<>();

		for (Atom molecule : molecules)
		{
			Relationship r = new Relationship(molecule);
			for (Atom other : molecules)
			{
				if (other.position.distance(molecule.position) < 3.1)
				{
					r.addNeighbour(other);
				}
			}
			System.out.println(r.neighbours.size());
			relationships.add(r);
		}

		return relationships;

	}

}
