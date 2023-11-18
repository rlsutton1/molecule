
class PDBLine:
    def __init__(self, line):
        
        # split line on white space, ignoring duplicate white space
        parts = line.split()
        
        
        
        self.record_name = parts[0].strip()
        self.atom_serial_number = int(parts[1])
        self.atom_name = parts[2].strip()
        self.position = [float(parts[3]),float(parts[4]),float(parts[5])]
        self.occupancy = float(parts[6])
        self.temperature_factor = float(parts[7])
 #       self.segment_identifier = line[72:76].strip()
        self.element_symbol = parts[8].strip()
  #      self.charge = line[78:80].strip()
  
        if (len(parts) > 9):
            self.flags = parts[9].strip()
        else:
            self.flags = ''

# pretty print PDBLine
    def __str__(self):
        return f'{self.record_name} {self.atom_serial_number} {self.atom_name} {self.position} {self.occupancy} {self.temperature_factor} {self.element_symbol} {self.flags}'

class PDBReader:
    def __init__(self, filename):
        self.filename = filename
        self.lines = []
        with open(filename, 'r') as f:
            for line in f:
                if line.startswith('HETATM'):
                    self.lines.append(PDBLine(line))
                    
#result =PDBReader('test.pdb')
#for (item) in result.lines:
#    print(item)

# extract atom coordinates to list of 3d vectors
# vectors = [[line.x, line.y, line.z] for line in result.lines]
# print(vectors)
