/* Copyright (C) OnePub IP Pty Ltd - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Brett Sutton <bsutton@onepub.dev>, Jan 2022
 */

package au.com.sort.fileloader;

import java.util.List;

import au.com.sort.Atom;

public interface FileLoader
{

	List<Atom> getPrimary();

	List<Atom> getSecondary();

}
