package au.com.sort;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FileLoader
{
	private List<Molecule> primary;
	private List<Molecule> secondary;

	FileLoader(String primaryFileName, String secondaryFileName, String type,
			String excludeFileName) throws FileNotFoundException, IOException
	{
		primary = parseFile(primaryFileName, type, 1);

		secondary = parseFile(secondaryFileName, type, 1);

		List<Molecule> collapsedStripList = parseFile(excludeFileName, type, 1);

		List<Molecule> openStripList = parseFile(excludeFileName, type, 3);

		
		primary = stripAtoms(primary, collapsedStripList);
		secondary = stripAtoms(secondary, openStripList);

		if (primary.size() != secondary.size())
		{
			throw new RuntimeException(
					"stripping resulted in incorrect list sizes");
		}

	}
	
	public List<Molecule> getPrimary()
	{
		return primary;
	}
	
	public List<Molecule> getSecondary()
	{
		return secondary;
	}
	

	List<Molecule> stripAtoms(List<Molecule> target, List<Molecule> stripList)
	{
		double tollerance = 0.0001;
		List<Molecule> result = new LinkedList<Molecule>();

		for (Molecule t : target)
		{
			boolean matched = false;
			for (Molecule s : stripList)
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

	private static List<Molecule> parseFile(String filename, String atomFilter,
			int requiredBlock) throws FileNotFoundException, IOException
	{

		List<Molecule> primary = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new FileReader(filename));

		int block = 0;
		String line = null;
		while ((line = reader.readLine()) != null)
		{
			boolean isNewBlock = true;
			try
			{
				Integer.parseInt(line.trim());
			} catch (Exception e)
			{
				isNewBlock = false;
			}
			if (isNewBlock)
			{
				block++;
			}
			if (block == requiredBlock)
			{

				Molecule molecule = parseLine(line);
				if (molecule != null)
				{
					if (atomFilter == null
							|| molecule.type.equalsIgnoreCase(atomFilter))
					{
						primary.add(molecule);
					}
				} else
				{
					// System.out.println("bad");
				}
			}
		}
		return primary;
	}

	static Molecule parseLine(String line)
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
				} catch (Exception e)
				{
					return null;
				}
			}
		}

		if (x != null && y != null && z != null)
		{

			return new Molecule(m, x, y, z);
		}
		return null;
	}

}
