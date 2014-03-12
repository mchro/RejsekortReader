import rkf.crypto
import binascii
import glob
from rfid.formats import mifare
import rkf.card
import sys

from rkf.datatypes import *
from bitarray import bitarray

#just parse some random file
allfiles = glob.glob("dumps/*.mfd")
if sys.argv[1:]:
    files = sys.argv[1:]
else:
    files = allfiles

f = files[0]
mifarecard = mifare.load(f)
card = rkf.card.RKFCard(mifarecard)
print card

type_to_java = {
    rkf.datatypes.ByteString: 'ByteString',
    rkf.datatypes.BitArray: 'ByteString', #XXX
    rkf.datatypes.Integer: 'RKFInteger',
    rkf.datatypes.SectorStatus: 'RKFInteger', #XXX
    rkf.datatypes.CardCurrencyUnit: 'RKFInteger', #XXX
    rkf.datatypes.DateCompact: 'DateCompact',
    rkf.datatypes.TimeCompact: 'TimeCompact',
    rkf.datatypes.DateTime: 'DateTime',
    rkf.datatypes.MoneyAmount24: 'MoneyAmount24',
    rkf.datatypes.AIDPIXPair: 'ByteString', #XXX
    rkf.datatypes.Location: 'RKFInteger', #XXX
    rkf.datatypes.EventCode: 'ByteString', #XXX
    rkf.datatypes.AIDPTA: 'ByteString', #XXX
    rkf.datatypes.TCELStatus: 'ByteString', #XXX
    rkf.datatypes.Status: 'ByteString', #XXX
    rkf.datatypes.DateMonth8: 'DateMonth8',
    rkf.datatypes.DateMonth11: 'DateMonth11',
}

def genJavaCode(block, bname, outf, supername="InterpretedBlock", inheritedfields=None):
    template = \
"""package info.rejsekort.reader.rkf.blocks;

import info.rejsekort.reader.rkf.datatypes.DataType;
%(datatypeimports)s

public class %(bname)sBlock extends %(supername)s {

%(fieldmembers)s

	public %(bname)sBlock(byte[] bits) {
		super("%(bname)s", bits);

%(initfields)s

		mFields = new DataType[] {
%(fieldsarray)s
		};
        mFieldNames = new String[] {
%(fieldnamesarray)s
        };

		interpretBlock();
	}
}
"""
    fieldmembers = []
    fieldsarray = []
    fieldnamesarray = []
    initfields = []
    datatypeimports = {}
    inheritedfields = inheritedfields or []
    for n in block.names:
        nt = getattr(block, n)
        name = n
        javatype = type_to_java.get(nt.__class__, None)
        if not javatype:
            continue
        datatypeimports[javatype] = "import info.rejsekort.reader.rkf.datatypes.%(javatype)s;" % locals()
        length = nt.bitlength
        reverse = str(nt.reverse).lower()

        if not name in inheritedfields:
            fieldmembers.append("\tpublic %(javatype)s m%(name)s;" % locals())
        initfields.append("\t\tm%(name)s = new %(javatype)s(%(length)s, %(reverse)s);" % locals())
        fieldsarray.append("\t\t\tm%(name)s" % locals())
        fieldnamesarray.append("\t\t\t\"%(name)s\"" % locals())
        

    datatypeimports = "\n".join(list(datatypeimports.values()))
    fieldmembers = "\n".join(fieldmembers)
    fieldsarray = ",\n".join(fieldsarray)
    fieldnamesarray = ",\n".join(fieldnamesarray)
    initfields = "\n".join(initfields)
    outf.write(template % locals())


        

    

for b in dir(card):
    a = getattr(card, b)
    if a.__class__ == rkf.datatypes.InterpretedBlock:
        print "Bingo", b
        if b in ["TCDBDynamic1", "TCCPStatic1"]:
            b = b[:-1]
        elif b in ["TCDBDynamic2", "TCCPStatic2"]:
            continue

        outf = open("./javadump/%sBlock.java" % (b), "w")
        #Special case TCST
        if b == "TCSTv4" or b == "TCSTv5":
            genJavaCode(a, b, outf, supername="info.rejsekort.reader.rkf.TCSTBlock", inheritedfields=["JourneyOriginDateTime"])
        else:
            genJavaCode(a, b, outf)

#special case TCEL
a = card.TCEL[0]
b = "TCEL"
outf = open("./javadump/%sBlock.java" % (b), "w")
genJavaCode(a, b, outf)

