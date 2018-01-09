import numpy as np

class Node(object):
    def __init__(self, innID, nodeID, typ, rec):
        #Innovation ID
        self.innID = innID
        
        #Node ID
        self.nodeID = nodeID
        
        #Structure type
        self.s_type = "node"
        
        #Node Type
        self.n_type = typ
        
        #Value
        if typ == "bias":
            self.value = 1
        else:
            self.value = 0.0
                
        #Recurrent or not
        self.recurr = rec
        
        #Activation curvature
        self.act = np.random.rand(0, 1)
        
        #Position in the nework grid
        self.splitX = None
        
        if typ == "input" or typ == "bias":
            self.splitY = 0
        elif typ == "output":
            self.splitY = 1
        
        #Links into the neuron
        self.i_links = []
        
        #Links out of the neuron
        self.o_links = []
      
    
class Link(object):
    def __init__(self, innID, linkID, inp, out, rec):
        #Innovation ID
        self.innID = innID
        
        #Link ID
        self.linkID = linkID
        
        #Structure type
        self.s_type = "link"
        
        #Input
        self.inp_n = inp
        
        #Output
        self.out_n = out
        
        #Weight
        self.w = np.random.randn()
        
        #Enabled or not
        self.enabled = True
        
        #Recurrent or not
        self.recurr = rec
