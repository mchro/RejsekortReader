from rfid.formats.mifare.Mifare import MifareSector

class MifareClassic4k():
    
    def __init__(self, carddata):
        self._carddata = carddata

        self._num_blocks = 256
        self._num_sectors = 40
        
        self.UID = carddata[0:4].encode("hex")
        self.BCC = carddata[4:5].encode("hex")
        self.ATQA = carddata[6:8].encode("hex")
        self.MANIFACTURER_DATA = carddata[8:16].encode("hex")

    def __repr__(self):
        return "Mifare Classic 4k card with UID: %s" % self.UID

    def blocks(self):
        for i in range(0, self._num_blocks):
            yield self.block(i)
            
    def sectors(self):
        for i in range(0, self._num_sectors):
            yield self.sector(i)
            
    def block(self, index):
        return self._carddata[index*16:(index+1)*16]
        
    def sector(self, index):
        if index < 32:
            width = 4
            index = index * width
        else:
            width = 16
            index = 32 * 4 + width * (index-32)
        
        data_blocks = []
        
        for i in range(index, index + width - 1):
            data_blocks.append(self.block(i))
            
        trailing_block = self.block(index + width -1)
        
        sector = MifareSector(data_blocks)
        sector.trailing_block = trailing_block
        sector.key_a = trailing_block[0:6]
        sector.key_b = trailing_block[10:16]
        sector.permission = trailing_block[6:9]
        sector.extrabit = trailing_block[9:10]
        
        return sector

            
    
