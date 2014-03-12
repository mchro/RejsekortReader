# vim: set fileencoding=utf-8 :

import binascii
from bitarray import bitarray

from datetime import date, time
from dateutil.relativedelta import relativedelta

class DataType(object):
    def __init__(self, bitlength, reverse=True):
        self.reverse = reverse
        self.bitlength = bitlength

    def frombits(self, bits):
        self.bits = bits
        if self.reverse:
            self.bits.reverse()
        self.interpret()

    def interpret(self):
        pass

class BitArray(DataType):
    def __str__(self):
        return str(self.bits)

class Integer(BitArray):
    def interpret(self):
        self.number = int(self.bits.to01(), 2)

    def __str__(self):
        return "Integer %d (%s)" % (self.number, str(self.bits))

class SectorStatus(Integer):
    def interpret(self):
        super(SectorStatus, self).interpret()
        self.status = {
                0: 'unused/undefined/AT3/AT4/2nd of app',
                1: 'AT1/AT5 1st dynamic',
                2: 'AT1/AT5 2nd dynamic',
                3: 'AT2',
                }[self.number]

    def __str__(self):
        return "SectorStatus " + self.status

class ByteString(DataType):
    def interpret(self):
        self.string = binascii.hexlify(self.bits.tobytes())

    def __str__(self):
        return self.string

class CardCurrencyUnit(DataType):
    units = {
            0: "kroner",
            1: u"10-oere",
            2: u"oerer",
            9: u"50-oere",
            }
    currencies = {
            208: "DKK",
            578: "NOK",
            752: "SEK",
            }

    def bcd_digit(self, sn):
        #damn you, indians!
        return {
                0b0000: 0,
                0b1000: 1,
                0b0100: 2,
                0b1100: 3,
                0b0010: 4,
                0b1010: 5,
                0b0110: 6,
                0b1110: 7,
                0b0001: 8,
                0b1001: 9,
                }.get(sn, 'X')

    def interpret(self):
        #bits = bitarray(bits, endian="big")
        self.digits = []
        for i in range(0, len(self.bits), 4):
            self.digits += [self.bcd_digit(
                    ord(self.bits[i:i+4].tobytes())
                )]
        self.digits = "".join(map(str, self.digits))
        try:
            self.unit = self.currencies[int(self.digits[1:4])] + " " + \
                    self.units[int(self.digits[0])]
        except:
            self.unit = "unparseable"

    def __str__(self):
        return "CurrencyUnit: " + self.unit + " - " + self.digits

class DateCompact(DataType):
    def interpret(self):
        self.numdays = int(self.bits.to01(), 2)
        self.date = date(1997,1,1)+relativedelta(days=+self.numdays)

    def __str__(self):
        return "DateCompact: " + str(self.numdays) + " " + str(self.date) + " " + str(self.bits)

class TimeCompact(DataType):
    def interpret(self):
        self.hour = int(self.bits.to01()[0:5], 2)
        self.minute = int(self.bits.to01()[5:11], 2)
        self.seconds = int(self.bits.to01()[11:16], 2) * 2
        try:
            self.time = time(self.hour, self.minute, self.seconds)
        except:
            self.time = "inconsistent"

    def __str__(self):
        return "TimeCompact: " + str(self.hour) + ":" + str(self.minute) + ":" + str(self.seconds) + " " + str(self.time) + " " + str(self.bits)

class DateTime(DataType):
    def interpret(self):
        self.numminutes = int(self.bits.to01(), 2)
        self.datetime = date(2000,1,1)+relativedelta(minutes=+self.numminutes)

    def __str__(self):
        return "DateTime: " + str(self.numminutes) + " " + str(self.datetime) + " " + str(self.bits)

class MoneyAmount24(DataType):
    def interpret(self):
        self.sign = self.bits.to01()[0]
        self.amount = int(self.bits.to01()[1:], 2)
        #import pdb; pdb.set_trace()

        self.money = ""
        if self.sign == "1":
            self.money = "-"
        self.money += "%d" % self.amount

    def __str__(self):
        return "MoneyAmount24: " + self.money + " " + str(self.bits)

class AIDPIXPair(DataType):
    def interpret(self):
        self.AIDbits = self.bits[0:12]
        self.AIDbits.reverse()
        self.PIXbits = self.bits[12:]
        self.PIXbits.reverse()

        self.AID = int(self.AIDbits.to01(), 2)
        self.PIX = int(self.PIXbits.to01(), 2)

    def __str__(self):
        #XXX, add interpretation
        return "AID: " + hex(self.AID) + " PIX: " + hex(self.PIX)

class Location(Integer):
    mangled = False
    #XXX, these numbers are 24 bits, should be migrated to the new locationdict
    legacylocationdict = {
            4131784229: "Tornhøjcentret",
            4131973157: "Aalborg St",
            4131952677: "Nordkraft", #should be 2200
            4131920677: "Nytorv",
            4131950629: "Lindenborgvej",
            4132342053: "Hobro St. (tog)",
            4132047397: "Mejrupstien",
            4132052773: "Teglværks Allé",
            4128095013: "Hadsten St",
            4131617573: "Bornholmsgade",
            4132011813: "Aalborg Lufthavn",
            4100870693: "Københavns Lufthavn, Kastrup st.",
            4100809509: "Hedehusene st.",
            4100811301: "København H?",
            4132097317: "Postgården",
            4131654181: "Politigården",
            4124152101: "Fredericia St (tog)",
            4123339557: "Esbjerg St (tog)",
            3222425700: "Tank op, Esbjerg St (tog)",
            4123600677: "Brørup St (tog)",
            4131947813: "Scoresbyvej",
            4132132645: "Tornhøjparken",
            4131778853: "AAU Busterminal",
            4132046117: "Sebbersundvej",
            }

    locationdict = {
            11798: "Høje Taastrup st. (tog)",
            11624: "Valby st.",
            2177: "Østre Allé",
            1996: "Rungsvej",
            1561: "Skalborgstien",
            1636: "Sygehus Syd",
            1646: "Sofiendal Skole",
            }

    def getLocation(self, locnumber):
        if not Location.mangled:
            for i in Location.legacylocationdict:
                Location.locationdict[(i >> 8) & 0x3fff] = Location.legacylocationdict[i]
            Location.mangled = True

        return Location.locationdict.get(self.number, "Unknown %d" % self.number)

    def interpret(self):
        super(Location, self).interpret()
        self.location = self.getLocation(self.number)


    def __str__(self):
        return "Location " + str(self.number) + ": " + self.location

class EventCode(ByteString):
    def interpret(self):
        super(EventCode, self).interpret()
        self.code = {
                "00": "Undefined",
                "01": "Purchase of TCTI using TCPU",
                "02": "Purchase of TCCO using TCPU",
                "03": "Purchase of TCTI with another payment than TCPU",
                "04": "Purchase of TCCO with another payment than TCPU",
                "05": "TCTI issued by TCCO",
                "06": "Check-in validation of TCTI/TCCO/ TCST-ci/co",
                "07": "Extension of a TCTI / TCCO",
                "08": "Charge of the TCPU",
                "09": "TCTI removed",
                "0a": "TCOO removed",
                "0b": "TCPU removed",
                "0c": "TCST-ci/co removed",
                "0d": "TCCP removed",
                "0e": "TCDB removed",
                "0f": "TCRE removed",
                #10...15 NUV-B = Not Used in Volume B?
                "16": "Card initialised",
                "17": "Application object cerated",
                "18": "Application object repurchased/reimbursed",
                "19": "TCCO activated",
                "1a": "Purchase of paper ticket using TCPU",
                "1b": "TCST-ci/co check-in",
                "1c": "Check-out validation of TCST-ci/co",
                "1d": "Control validation of TCTI/TCCO/TCST-ci/co",
                "1e": "Excess fare validation of TCTI/TCCO/TCST-ci/co",
                }.get(self.string, "Undefined")

    def __str__(self):
        return "EventCode " + self.string + ": " + self.code

class AIDPTA(ByteString):
    def interpret(self):
        super(AIDPTA, self).interpret()
        self.PTA = {
                "be00": "Rejsekortet A/S",
                "be04": "DSB",
                "be09": "Nordjyllands Trafikselskab",
                }.get(self.string, "Unknown PTA")

    def __str__(self):
        return "PTA " + self.string + ": " + self.PTA

class TCELStatus(ByteString):
    #This seems to be according to RKF-0022 sec. 5.2.3
    def interpret(self):
        super(TCELStatus, self).interpret()
        self.rkfstatus = {
                "1b": "Checkin validation of TCST-ci/co", # from RKF-0023 sec. 3.4.2.4
                "1c": "Checkout validation of TCST-ci/co",
                "1d": "Control validation of TCST-ci/co",
                "08": "Charge of the TCPU",
                "16": "Initialisation of card",
                "17": "Application object created",
                }.get(self.string, "Unknown Status")
        self.rejsekortstatus = {
                "1b": "Checkin",
                "1c": "Checkout",
                "1d": "Kontrol",
                "08": "Produkthandling",
                }.get(self.string, "Unknown Status")

    def __str__(self):
        return "TCELStatus %s - %s (%s)" % (self.rejsekortstatus, self.rkfstatus, self.string)

class Status(Integer):
    #RKF-0023 p. 17
    def interpret(self):
        super(Status, self).interpret()
        self.status = {
                0x01: 'Ok',
                0x21: 'Suspended, action pending',
                0x3f: 'Temporarily suspended',
                0x58: 'Suspended',
                }.get(self.number, 'Undefined')

    def __str__(self):
        return "Status " + self.status

class DateMonth8(DataType):
    def interpret(self):
        self.nummonths = int(self.bits.to01(), 2)
        self.date = date(2000,1,1)+relativedelta(months=+self.nummonths)

    def __str__(self):
        return "DateMonth8 " + str(self.nummonths) + " " + str(self.date) + " " + str(self.bits)

class DateMonth11(DataType):
    def interpret(self):
        self.nummonths = int(self.bits.to01(), 2)
        self.date = date(1900,1,1)+relativedelta(months=+self.nummonths)

    def __str__(self):
        return "DateMonth11 " + str(self.nummonths) + " " + str(self.date) + " " + str(self.bits)

class InterpretedBlock():

    def __init__(self, mifareblock, *args):
        self.elements = {}
        self.names = []
        self.mifareblock = mifareblock
        self.bits = bitarray(endian="little")
        self.bits.frombytes(self.mifareblock)

        self.i = 0
        self.parseTypes(*args)

    def parseTypes(self, *args):
        for (name, datatype) in zip(args[0::2], args[1::2]):
            self.names += [name]
            self.elements[name] = [(name, datatype)]
            bits = self.bits[ self.i : self.i+datatype.bitlength]

            datatype.frombits(bits)
            setattr(self, name, datatype)

            self.i += datatype.bitlength

    def __str__(self):
        toret = []
        for a in self.names:
            toret += ["'%s': %s" % (a, str(getattr(self,a)))]
        return "{" + ", \n  ".join(toret) + "\n}"
        #return str(
        #        { a: self.__dict__[a] for a in self.elements.keys() })


