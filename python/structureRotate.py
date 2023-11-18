import math
import sys

import inreader as inReader
import pdbreader as pdbReader
import rotateHelper as r

# get file name from command line argument
filename = sys.argv[1]

if (filename.endswith('.in')):
    print('in file')
    result = inReader.INReader(filename)
if (filename.endswith('.pdb')):
    print('pdb file')
    result = pdbReader.PDBReader(filename)
    
if (result == None):
    print('unknown file type '+filename+' must be .in or .pdb')
    sys.exit(1)

rootAtom = 0
endAtom = 0

atomCounter = 0;

mapIndexToAtom = {}

for (item) in result.atoms:
    if item.flags == '*g1-axis-start':
        rootAtom = item.position
        mapIndexToAtom[atomCounter] =item
        atomCounter = atomCounter + 1
    if item.flags == '*g1-axis-end':
        endAtom = item.position
        mapIndexToAtom[atomCounter] =item
        atomCounter = atomCounter + 1

if (rootAtom == 0 or endAtom == 0):
    print('could not find any flagged atoms, add some flags to the input file. eg *g1-axis and *g1')
    sys.exit(1)


atoms = []
atoms.append(rootAtom)
atoms.append(endAtom)

for (item) in result.atoms:
    if item.flags == '*g1':
        atoms.append(item.position)
        mapIndexToAtom[atomCounter] =item
        atomCounter = atomCounter + 1
        

orginalRoot = rootAtom.copy()

axisVector = r.subtract_matrix(endAtom, rootAtom)

axisRotationMatrix = r.get_rotation_from_vector(axisVector)
inverseAxisRotationMatrix = axisRotationMatrix.inv()


atOrgin = r.apply_translation_matrix(atoms, [orginalRoot[0]*-1,orginalRoot[1]*-1,orginalRoot[2]*-1])

xangle = math.degrees( math.atan2(atOrgin[1][0],atOrgin[1][2]-atOrgin[0][2]))
yangle = math.degrees(math.atan2(atOrgin[1][1],atOrgin[1][2]-atOrgin[0][2]))

axisRotationMatrix = r.get_rotation_from_angles(yangle,xangle,0)
inverseAxisRotationMatrix = axisRotationMatrix.inv()


alignedWithAxis = r.rotate_vectors(atOrgin,inverseAxisRotationMatrix)

desiredRotate = 45

rotated = r.rotate_vectors(alignedWithAxis, r.get_rotation_from_angles(0,0,desiredRotate))

rotatedBack = r.rotate_vectors(rotated, axisRotationMatrix)

backAtOrigin = r.apply_translation_matrix(rotatedBack, orginalRoot)

i=0 
for (atom) in backAtOrigin:
    mapIndexToAtom[i].position[0] = atom[0]
    mapIndexToAtom[i].position[1] = atom[1]
    mapIndexToAtom[i].position[2] = atom[2]
    i = i + 1


for (item) in result.lines:
    print(item)
    
        