#!/usr/bin/python
import rkf.crypto
import binascii
import glob
from rfid.formats import mifare
import rkf.card
import sys


#XXX
from rkf.datatypes import *
from bitarray import bitarray

allfiles = glob.glob("dumps/*.mfd")

if sys.argv[1:]:
    files = sys.argv[1:]
else:
    files = allfiles

for f in files:
    mifarecard = mifare.load(f) # mifare dump
    print "Dumping %s:" % f
    card = rkf.card.RKFCard(mifarecard)
    print card
    #print mifarecard.sector(15).block(1).encode("hex")
    #print card.TCDBDynamic2
    #continue

    #for (index, b) in enumerate(mifarecard.blocks()):
    #    print index, InterpretedBlock(b,
    #            "Identifier", ByteString(8, reverse=False)
    #            ).Identifier

        #bits = bitarray(endian="little")
        #bits.fromstring(b)
        #print 
        #for i in range(0, 16*8-24):
        #    databits = bits[ i : i+24]


        #    datetime = DateTime(24)
        #    datetime.frombits(databits)
            #if datetime.datetime.year == 2012:
            #if i == 56:
            #    print "bingo", index, i, datetime

            
            
            #amount = MoneyAmount24(24)
            #amount.frombits(databits)

            ##print amount.amount
            #if amount.amount == 23423:
            #    print "bingo", index, i
            #if amount.amount == 26209:
            #    print "banko", index, i

    #print repr(mifarecard.block(0))
    #print repr(mifarecard.sector(0).block(0))

    print "Rejsehistorik:"
    for t in sorted(card.TCEL, key=lambda x: (x.EventDateStamp.date, x.EventTimeStamp.time)):
        if hasattr(t, "Location"):
            print "{0} {1}  {2:8}   {3:35} {4:20}".format(
                t.EventDateStamp.date, t.EventTimeStamp.time, 
                t.StatusBits.rejsekortstatus, t.Location.location, t.AID.PTA)
        elif hasattr(t, "Value"):
            print "{0} {1}  {2} {3} {4:24} {5:20}".format(
                t.EventDateStamp.date, t.EventTimeStamp.time, 
                t.StatusBits.rejsekortstatus, int(t.Value.money)/100.0, "", t.AID.PTA)


