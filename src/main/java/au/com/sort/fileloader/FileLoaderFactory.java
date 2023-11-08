/* Copyright (C) OnePub IP Pty Ltd - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Brett Sutton <bsutton@onepub.dev>, Jan 2022
 */

package au.com.sort.fileloader;

import java.io.FileNotFoundException;
import java.io.IOException;

public class FileLoaderFactory
{
	public static FileLoader getFileLoader(String primaryFileName, String secondaryFileName, String type,
			String excludeFileName) throws FileNotFoundException, IOException
	{
		if (primaryFileName.endsWith(".xyz"))
		{
			return new FileLoaderXYZ(primaryFileName, secondaryFileName, type, excludeFileName);
		}
		if (primaryFileName.endsWith("in"))
		{
			return new FileLoaderIN(primaryFileName, secondaryFileName, type, excludeFileName);
		}

		throw new RuntimeException("Unknown file type " + primaryFileName + ", known types are .in and .xyz");
	}
}
