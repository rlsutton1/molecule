package au.com.sort.fileloader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import au.com.sort.Atom;

public class FileLoaderIN implements FileLoader
{
	private List<Atom> primary;
	private List<Atom> secondary;

	public FileLoaderIN(String primaryFileName, String secondaryFileName, String atomFilter,
			String excludeFileName) throws FileNotFoundException, IOException
	{
		this.primary = parseFile(primaryFileName, atomFilter);

		this.secondary = parseFile(secondaryFileName, atomFilter);

		if (excludeFileName != null)
		{
			System.out.println("FileLoaderIN does not support excludeFile");
		}

	}

	@Override
	public List<Atom> getPrimary()
	{
		return this.primary;
	}

	@Override
	public List<Atom> getSecondary()
	{
		return this.secondary;
	}

	private List<Atom> parseFile(String filename, String atomFilter) throws FileNotFoundException, IOException
	{

		List<Atom> atoms = new LinkedList<>();
		try (BufferedReader reader = new BufferedReader(
				new FileReader(filename)))
		{
			boolean foundAtomicPositions = false;
			String line = null;
			while ((line = reader.readLine()) != null)
			{

				if (line.startsWith("ATOMIC_POSITIONS"))
				{
					foundAtomicPositions = true;
					continue;
				}
				if (foundAtomicPositions)
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
						break;
					}
				}
			}
		}
		return atoms;
	}

	private boolean applyAtomFilter(String atomFilter, Atom molecule)
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

	Atom parseLine(String line)
	{

		String temp = line.replace("\t", " ");
		String last = "";
		while (!temp.equals(last))
		{
			last = temp;
			temp = temp.replaceAll("  ", " ");
			temp = temp.replace("\t", " ");
		}

		String[] parts = temp.split(" ");

		String m = "";
		Double x = null;
		Double y = null;
		Double z = null;

		try
		{

			m = parts[0];
			x = Double.parseDouble(parts[1]);
			y = Double.parseDouble(parts[2]);
			z = Double.parseDouble(parts[3]);
		}
		catch (Exception e)
		{
			return null;
		}

		return new Atom(m, x, y, z);

	}

}
