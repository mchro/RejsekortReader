class MifareSector:
    def __init__(self, blocks=None):
        self.key_a = None
        self.key_b = None
        self.permission = None
        self.extrabit = None
        self.blocks = blocks

    def block(self, index):
        return self.blocks[index]
        
    def __repr__(self):
        return "Mifare Sector with %d blocks" % len(self.blocks)
        
    
