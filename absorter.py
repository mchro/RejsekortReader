#!/usr/bin/python
from __future__ import print_function

def createOptionParser():
    from optparse import OptionParser
    parser = OptionParser(usage="usage: %prog [options]")

    parser.add_option("-a",
              action="append", dest="aes", nargs=1, default=list(),
              help="Add a positive (a) item, e.g. '-a 1000'")
    parser.add_option("-b",
              action="append", dest="bes", nargs=1, default=list(),
              help="Add a negative (b) item, e.g. '-b 0001'")

    return parser

def absort(aes, bes):
    a1 = aes[0]
    eqbits = [True]*len(a1)

    #find common bits for all the a'es
    for i in range(len(eqbits)):
        for a2 in aes:
            assert len(a2) == len(a1)
            if a1[i] != a2[i]:
                eqbits[i] = False
                break

    #remove common bits from all the b'es
    for i in range(len(eqbits)):
        for b in bes:
            assert len(b) == len(a1)
            if a1[i] == b[i]:
                eqbits[i] = False
                break

    return "".join(map(lambda x: x and "X" or " ", eqbits))

parser = createOptionParser()
(options, args) = parser.parse_args()

eqbits = absort(options.aes, options.bes)

for a in options.aes:
    print(" + ", a)
for b in options.bes:
    print(" - ", b)
print("   ", eqbits)
print("   ", end='')
for i in range(len(options.aes[0])):
    if i % 8 == 0:
        print("^",end='')
    else:
        print(" ", end='')
print()
print("   ", end='')
i = 0
#for i in range(len(options.aes[0])):
while i < len(options.aes[0]):
    if i == 0:
        print(i, end='')
    elif i % 8 == 0 and i < 10:
        print(i, end='')
    elif i % 8 == 7 and i > 10:
        print(i+1, end='')
        i += 1
    else:
        print(" ", end='')
    i += 1
print()
