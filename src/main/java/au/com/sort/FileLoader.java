package au.com.sort;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FileLoader
{
	private List<Atom> primary;
	private List<Atom> secondary;

	FileLoader(String primaryFileName, String secondaryFileName, String type,
			String excludeFileName) throws FileNotFoundException, IOException
	{
		this.primary = parseFile(primaryFileName, type, 1);

		this.secondary = parseFile(secondaryFileName, type, 1);

		if (excludeFileName != null)
		{
			List<Atom> collapsedStripList = parseFile(excludeFileName, type, 1);
			List<Atom> openStripList = parseFile(excludeFileName, type, 3);

			this.primary = stripAtoms(this.primary, collapsedStripList);
			this.secondary = stripAtoms(this.secondary, openStripList);

			if (this.primary.size() != this.secondary.size())
			{
				throw new RuntimeException(
						"stripping resulted in incorrect list sizes");
			}
		}

	}

	public List<Atom> getPrimary()
	{
		return this.primary;
	}

	public List<Atom> getSecondary()
	{
		return this.secondary;
	}

	List<Atom> stripAtoms(List<Atom> target, List<Atom> stripList)
	{
		double tollerance = 0.0001;
		List<Atom> result = new LinkedList<>();

		for (Atom t : target)
		{
			boolean matched = false;
			for (Atom s : stripList)
			{
				if (Math.abs(s.position.getX() - t.position.getX()) < tollerance
						&& Math.abs(s.position.getY() - t.position.getY()) < tollerance
						&& Math.abs(s.position.getZ() - t.position.getZ()) < tollerance)
				{
					matched = true;

				}
			}
			if (!matched)
			{
				result.add(t);
			}
		}

		return result;
	}

	private static List<Atom> parseFile(String filename, String atomFilter,
			int requiredBlock) throws FileNotFoundException, IOException
	{

		List<Atom> atoms = new LinkedList<>();
		try (BufferedReader reader = new BufferedReader(
				new FileReader(filename)))
		{

			int block = 0;
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				boolean isNewBlock = true;
				try
				{
					Integer.parseInt(line.trim());
				}
				catch (Exception e)
				{
					isNewBlock = false;
				}
				if (isNewBlock)
				{
					block++;
				}
				if (block == requiredBlock)
				{

					Atom atom = parseLine(line);
					if (atom != null)
					{
						if (atomFilter == null
								|| applyAtomFilter(atomFilter, atom))
						{
							atoms.add(atom);
						}
					}
					else
					{
						// System.out.println("bad");
					}
				}
			}
		}
		return atoms;
	}

	private static boolean applyAtomFilter(String atomFilter, Atom molecule)
	{
		String[] filters = atomFilter.split(",");
		boolean matches = false;
		for (String filter : filters)
		{
			if (molecule.type.equalsIgnoreCase(filter))
			{
				matches = true;
				break;
			}
		}

		return matches;
	}

	static Atom parseLine(String line)
	{

		String temp = line.replace("\t", " ");
		String[] parts = temp.split(" ");

		String m = "";
		Double x = null;
		Double y = null;
		Double z = null;
		int i = 0;
		for (String part : parts)
		{
			if (part.length() > 0)
			{
				if (i == 0)
				{
					m = part;

				}
				try
				{
					if (i == 1)
					{
						x = Double.parseDouble(part);
					}
					if (i == 2)
					{
						y = Double.parseDouble(part);
					}
					if (i == 3)
					{
						z = Double.parseDouble(part);
					}
					i++;
				}
				catch (Exception e)
				{
					return null;
				}
			}
		}

		if (x != null && y != null && z != null)
		{

			return new Atom(m, x, y, z);
		}
		return null;
	}

}
