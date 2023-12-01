import math
import sys

import inreader as inReader
import pdbreader as pdbReader
import rotateHelper as r

# example usage
#   python3 structureRotate.py input_labelled.in *g1 13 > ../rotated.in 

# get file name from command line argument
filename = sys.argv[1]
group = sys.argv[2]
angle = sys.argv[3]
#convert angle from a string to a number
desiredRotate = float(angle)

if (filename.endswith('.in')):
    result = inReader.INReader(filename)
if (filename.endswith('.pdb')):
    result = pdbReader.PDBReader(filename)
    
if (result == None):
    print('unknown file type '+filename+' must be .in or .pdb')
    sys.exit(1)

rootAtom = 0
endAtom = 0

atomCounter = 0;

mapIndexToAtom = {}

for (item) in result.atoms:
    if item.flags == group+'-axis-start':
        r.apply_cell_parameters(item.position,result.cellParameters)
        rootAtom = item.position
        mapIndexToAtom[atomCounter] =item
        atomCounter = atomCounter + 1
    if item.flags == group+'-axis-end':
        r.apply_cell_parameters(item.position,result.cellParameters)
        endAtom = item.position
        mapIndexToAtom[atomCounter] =item
        atomCounter = atomCounter + 1

if (rootAtom == 0 or endAtom == 0):
    print('could not find any flagged atoms, add some flags to the input file. eg '+group+'-axis and '+group)
    sys.exit(1)


atoms = []
atoms.append(rootAtom)
atoms.append(endAtom)

for (item) in result.atoms:
    if item.flags == group:
        r.apply_cell_parameters(item.position,result.cellParameters)
        atoms.append(item.position)
        mapIndexToAtom[atomCounter] =item
        atomCounter = atomCounter + 1
    if item.flags == group+'r':
        # flip the y axis outside of the unit cell for the rotation on this atom
        if (item.position[1] > 0.5):
            item.position[1] = item.position[1]  -1
        else:
            item.position[1] = item.position[1]  +1

        r.apply_cell_parameters(item.position,result.cellParameters)

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

rotated = r.rotate_vectors(alignedWithAxis, r.get_rotation_from_angles(0,0,desiredRotate))

rotatedBack = r.rotate_vectors(rotated, axisRotationMatrix)

backAtOrigin = r.apply_translation_matrix(rotatedBack, orginalRoot)

i=0 
for (atom) in backAtOrigin:
    temp =r.revert_cell_parameters(atom,result.cellParameters)
    mapIndexToAtom[i].position[0] = temp[0]
    mapIndexToAtom[i].position[1] = temp[1]
    mapIndexToAtom[i].position[2] = temp[2]
    i = i + 1


for (item) in result.lines:
    print(item)
    
        