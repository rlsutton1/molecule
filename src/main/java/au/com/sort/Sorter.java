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
		List<Atom> primary = loader.getPrimary();

		List<Atom> secondary = loader.getSecondary();

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

	List<Atom> bestResult = null;

	double bestDisturbance = 1000000000;

	void start(List<Atom> primary, List<Atom> secondary)
			throws InterruptedException
	{

		double lastBest = bestDisturbance;
		for (int i = 0; i < 10000000; i++)
		{
			List<Atom> removable = new LinkedList<>();
			removable.addAll(secondary);
			List<Atom> primaryremovable2 = new LinkedList<>();
			primaryremovable2.addAll(primary);
			attempt(primaryremovable2, removable);

			if (lastBest > bestDisturbance)
			{
				lastBest = bestDisturbance;

				// Thread.sleep(1000);
				System.out.println();

				System.out.println(primary.size());
				for (Atom molecule : primary)
				{
					System.out.println(molecule);
				}
				if (bestResult != null)
				{
					System.out.println(bestResult.size());
					for (Atom molecule : bestResult)
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

	private void attempt(List<Atom> primary, List<Atom> secondary)
	{

		double totalDisturbance = 0;
		Atom[] tertiary = new Atom[primary.size()];

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
			Atom primaryMolecule = primary.get(ran);
			Atom bestMatch = null;
			double bestMatchDistance = 1000000000;
			for (Atom molecule : secondary)
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
			List<Atom> tmp = new LinkedList<>();
			for (Atom m : tertiary)
			{
				tmp.add(m);
			}
			bestResult = tmp;
		}

	}

}
