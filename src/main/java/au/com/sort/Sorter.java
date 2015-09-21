package au.com.sort;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Sorter
{

	public static void main(String[] args) throws IOException,
			InterruptedException
	{
		FileLoader loader = new FileLoader(args[0], args[1], args[2], args[3]);
		List<Molecule> primary = loader.getPrimary();

		List<Molecule> secondary = loader.getSecondary();

		Sorter sorter = new Sorter();

		double disturb = 0;
		for (int i = 0; i < primary.size(); i++)
		{
			disturb += primary.get(i).position
					.distance(secondary.get(i).position);
		}
		System.out.println("Total disturbance " + disturb);
		Thread.sleep(2000);

		sorter.start(primary, secondary);

	}

	List<Molecule> bestResult = null;

	double bestDisturbance = 1000000000;

	void start(List<Molecule> primary, List<Molecule> secondary)
			throws InterruptedException
	{

		double lastBest = bestDisturbance;
		for (int i = 0; i < 10000000; i++)
		{
			List<Molecule> removable = new LinkedList<>();
			removable.addAll(secondary);
			List<Molecule> primaryremovable2 = new LinkedList<>();
			primaryremovable2.addAll(primary);
			attempt(primaryremovable2, removable);

			if (lastBest > bestDisturbance)
			{
				lastBest = bestDisturbance;

				// Thread.sleep(1000);
				System.out.println();

				System.out.println(primary.size());
				for (Molecule molecule : primary)
				{
					System.out.println(molecule);
				}
				if (bestResult != null)
				{
					System.out.println(bestResult.size());
					for (Molecule molecule : bestResult)
					{
						System.out.println(molecule);
					}

					System.out.println(bestResult.size());

					for (int j = 0; j < bestResult.size(); j++)
					{
						System.out.println(primary.get(j)
								+ " "
								+ bestResult.get(j)
								+ " distance "
								+ primary.get(j).position.distance(bestResult
										.get(j).position));
					}
				}
				System.out.println("Best " + bestDisturbance);
			}
		}

	}

	private void attempt(List<Molecule> primary, List<Molecule> secondary)
	{

		double totalDisturbance = 0;
		Molecule[] tertiary = new Molecule[primary.size()];

		Random rand = new Random();

		double minThresh = 0.7;
		List<Integer> randSet = new LinkedList<>();
		for (int r = 0; r < primary.size(); r++)
		{
			randSet.add(r);
		}

		List<Integer> randSource = new LinkedList<>();
		while (randSet.size() > 0)
		{
			randSource.add(randSet.remove(rand.nextInt(randSet.size())));
		}

		for (int ran : randSource)
		{
			Molecule primaryMolecule = primary.get(ran);
			Molecule bestMatch = null;
			double bestMatchDistance = 1000000000;
			for (Molecule molecule : secondary)
			{
				if (primaryMolecule.type.equals(molecule.type))
				{
					double distance = Math.abs(molecule.position
							.distance(primaryMolecule.position));
					if (bestMatch == null)
					{
						bestMatch = molecule;
						bestMatchDistance = distance;
					} else
					{

						if (distance < bestMatchDistance
								&& distance > minThresh)
						{
							bestMatchDistance = distance;
							bestMatch = molecule;
						}
					}
				}
			}
			if (bestMatchDistance == 1000000000)
			{
				System.out.println("SLKDJFLSDK");
			}
			if (bestMatchDistance > 10.0)
			{
				bestMatchDistance = 30;
				return;
			}
			totalDisturbance += bestMatchDistance;
			secondary.remove(bestMatch);
			tertiary[ran] = bestMatch;

		}

		if (totalDisturbance < bestDisturbance || bestResult == null)
		{
			bestDisturbance = totalDisturbance;
			List<Molecule> tmp = new LinkedList<>();
			for (Molecule m : tertiary)
			{
				tmp.add(m);
			}
			bestResult = tmp;
		}

	}

}
