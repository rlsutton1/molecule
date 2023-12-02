import math
import sys

import inreader as inReader
import pdbreader as pdbReader
import rotateHelper as r

# example usage
#   python3 structureRotate.py input_labelled.in *g1 13 > ../rotated.in 

# get file name from command line argument
filename = sys.argv[1]

if (filename.endswith('.in')):
    result = inReader.INReader(filename)
if (filename.endswith('.pdb')):
    result = pdbReader.PDBReader(filename)
    
if (result == None):
    print('unknown file type '+filename+' must be .in or .pdb')
    sys.exit(1)


for (item) in result.atoms:
    item.flags = ''

for (item) in result.lines:
    print(item)
    
        