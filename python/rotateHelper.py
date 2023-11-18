
import numpy as np
from numpy.linalg import norm
from scipy.spatial.transform import Rotation


# method to rotate all vectors by a rotation matrix
def rotate_vectors(vectors, rotation):
    # rotate all vectors by the rotation matrix
    
    text = [1,1,1]
    rm = rotation.as_matrix()
    t2 = np.dot(text,rm)
    result = []
    for vector in vectors:
        result.append( rotation.as_matrix().dot(vector))
        
    return result


# method to subtract one 3d matrix from another
def subtract_matrix(matrix1, matrix2):
    # subtract matrix2 from matrix1
    matrix = [matrix1[i] - matrix2[i] for i in range(len(matrix1))]

    return matrix


# method to create rotation matrix from x,y and z angles
def get_rotation_from_angles(x, y, z):
    # create a rotation object from the angles
    r = Rotation.from_euler('xyz', [x, y, z], degrees=True)

  

    return r


# method to take a 3d vector and return a Rotation
def get_rotation_from_vector(vector):
    
    axis = vector / norm(vector)
    # create a rotation object from the vector
    r = Rotation.from_rotvec(axis)

    return r

# method to apply transaction matrix to list of vectors
def apply_translation_matrix(vectors, translation_matrix):
    # apply translation matrix to all vectors
    
    result = []
    
    
    
    for vector in vectors:
        pos = []
        pos.append(vector[0]+translation_matrix[0])
        pos.append(vector[1]+translation_matrix[1])
        pos.append(vector[2]+translation_matrix[2])
        result.append(pos)
    
   
    return     result 





#rootAtom  = [1,1,1]
#endAtom   = [1,1,2]
# copy rootAtom to originalMatrix

#originalMatrix = rootAtom.copy()
#atoms = [rootAtom, endAtom, [3, 3, 3]]



#axisVector = subtract_matrix(endAtom, rootAtom)
#axisAngles = get_angles_from_vector(axisVector)

#atOrgin = translate_to_origin(atoms, 0);

#inverseAxisAngle = axisAngles.inv().as_matrix()

#alignedWithAxis = rotate_vectors(atOrgin,inverseAxisAngle);

#desiredRotate = 90

#rotated = rotate_vectors(alignedWithAxis, get_rotation_matrix_from_angles(0,0,desiredRotate))

#rotatedBack = rotate_vectors(rotated, axisAngles.as_matrix())

#backAtOrigin = apply_translation_matrix(rotatedBack, originalMatrix)

#print(backAtOrigin)


