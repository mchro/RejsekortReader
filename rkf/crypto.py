#import pyDes
from Crypto.Cipher import DES

def _paginate(seq, rowlen):
    for start in xrange(0, len(seq), rowlen):
        yield seq[start:start+rowlen]

def desMAC(data, key):
    
    enc = "\0" * 8 # this is the IV
    padding = len(data) % 8
    data += "\0" * (8-padding)
    
    des = DES.new(key, DES.MODE_CBC)
    return des.encrypt(data)

    #for chunk in _paginate(data, 8):
    #    enc = pyDes.des(key, pyDes.CBC, enc).encrypt(chunk)
    #return enc
