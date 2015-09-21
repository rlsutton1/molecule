package au.com.sort;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class RelationshipSorter
{

	public static void main(String[] args) throws IOException,
			InterruptedException
	{

		

		FileLoader loader = new FileLoader(args[0], args[1], args[2], args[3]);
		List<Molecule> primary = loader.getPrimary();

		List<Molecule> secondary = loader.getSecondary();

		
		getSorted( primary, secondary);
	}

	public static List<Molecule> getSorted(
			List<Molecule> primary, List<Molecule> secondary)
			throws InterruptedException
	{
		List<Molecule> results = new LinkedList<>();
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
			results.add(bestRelationship.molecule);
			secondaryRelationships.remove(bestRelationship);
			disturb += pr.origin.distance(bestRelationship.origin);
			System.out.println(pr.molecule + " " + bestRelationship.molecule
					+ " disturbance "
					+ pr.origin.distance(bestRelationship.origin));

		}
		System.out.println("Total disturbance " + disturb);

		System.out.println(primary.size());
		for (Molecule m : primary)
		{
			System.out.println(m);
		}

		System.out.println(primary.size());
		for (Molecule m : results)
		{
			System.out.println(m);
		}
		return results;
	}

	static List<Relationship> identify(List<Molecule> molecules)
	{
		List<Relationship> relationships = new LinkedList<>();

		for (Molecule molecule : molecules)
		{
			Relationship r = new Relationship(molecule);
			for (Molecule other : molecules)
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
