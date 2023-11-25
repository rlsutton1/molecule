#!/bin/bash

# eample usage ./rotateMany.sh inputfile.in 45



rm -f temp*.in

python3 structureRotate.py $1 *g1 $2 > temp1.in 
python3 structureRotate.py temp1.in *g1 $2 > temp2.in
python3 structureRotate.py temp2.in *g1 $2 > temp3.in 
python3 structureRotate.py temp3.in *g1 $2 > temp4.in 

cat temp4.in