#!/usr/bin/env python
#-*- coding:utf-8 -*-

from rfid.formats.mifare.MifareClassic1k import MifareClassic1k
from rfid.formats.mifare.MifareClassic4k import MifareClassic4k

ATQA_LIST = {"0200": {
                 "size": 4096, 
                 "name": "Mifare Classic 4k",
                 "handler": MifareClassic4k},
             "0400": {
                 "size": 1024, 
                 "name": "Mifare Classic 1k",
                 "handler": MifareClassic1k},
             "4400": {
                 "name": "Mifare Ultralight"},
             "4403": {
                 "name": "Mifare DESFire"},
             "0407": {
                 "name": "Mifare ProX"},
             }

def load(filename):

    mfcfile = file(filename, 'rb')
    mfcdata = mfcfile.read()
    atqa = mfcdata[6:8].encode("hex")
    
    try:
        cardinfo = ATQA_LIST[atqa]
    except:
        raise Exception("Not a Mifare Tag, ATQA was %s" % atqa)
        
    return cardinfo["handler"](mfcdata)

