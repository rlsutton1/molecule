
class INLineAtom:
    def __init__(self, line):
        
        # split line on white space, ignoring duplicate white space
        parts = line.split()
        
        self.atom_name = parts[0].strip()
        self.position = [float(parts[1]),float(parts[2]),float(parts[3])]
  
        if (len(parts) > 4):
            self.flags = parts[4].strip()
        else:
            self.flags = ''
        
        

# pretty print INLine
    def __str__(self):
        return f'{self.atom_name} {self.position[0]} {self.position[1]} {self.position[2]} {self.flags}'
    
class INLineLine:
    def __init__(self, line):
        
        self.line = line
        self.flags = ''
     
    def __str__(self):
        return f'{self.line}'   
        

class INReader:
    def __init__(self, filename):
        self.filename = filename
        self.lines = []
        self.atoms = []
        
        foundAtomicPositions = False
        
        with open(filename, 'r') as f:
            for line in f:
                if (line.startswith('ATOMIC_POSITIONS')): 
                    foundAtomicPositions = True
                if (line.startswith('K_POINTS')):
                    foundAtomicPositions = False
                if (foundAtomicPositions == True and line.startswith('ATOMIC_POSITIONS')==False):
                    atom =INLineAtom(line)
                    self.lines.append(atom)
                    self.atoms.append(atom)
                else:
                    self.lines.append(INLineLine(line))
                    
#result =INReader('input.in')
#for (item) in result.lines:
    #print(item)

# extract atom coordinates to list of 3d vectors
#vectors = [line.position for line in result.lines]
#print(vectors)
