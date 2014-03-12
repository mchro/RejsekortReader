import binascii
from datatypes import *
from operator import xor

class RKFCard():
    def __init__(self, mifarecard):
        self.mifarecard = mifarecard
        
        #print "abemad", binascii.hexlify(self.mifarecard.sector(9).block(0))

        self.parseCMI()
        self.parseTCCI()
        self.parseTCDI()
        self.TCAS1 = self.parseTCAS(self.mifarecard.sector(0).block(2))
        self.TCST = []
        self.TCST.append(self.parseTCST(
                self.mifarecard.sector(36).block(0) + 
                self.mifarecard.sector(36).block(1) + 
                self.mifarecard.sector(36).block(2)
                ))
        #XXX skipping blocks?
        self.TCST.append(self.parseTCST(
                self.mifarecard.sector(36).block(6) + 
                self.mifarecard.sector(36).block(7) + 
                self.mifarecard.sector(36).block(8)
                ))
        #XXX is this one seems not good?
        #self.TCST.append(self.parseTCST(
        #        self.mifarecard.sector(36).block(9) + 
        #        self.mifarecard.sector(36).block(10) + 
        #        self.mifarecard.sector(36).block(11)
        #        ))
        self.TCST.append(self.parseTCST(
                self.mifarecard.sector(36).block(12) + 
                self.mifarecard.sector(36).block(13) + 
                self.mifarecard.sector(36).block(14)
                ))
        self.TCST.append(self.parseTCST(
                self.mifarecard.sector(37).block(3) + 
                self.mifarecard.sector(37).block(4) + 
                self.mifarecard.sector(37).block(5)
                ))
        self.TCST.append(self.parseTCST(
                self.mifarecard.sector(37).block(9) + 
                self.mifarecard.sector(37).block(10) + 
                self.mifarecard.sector(37).block(11)
                ))
        self.TCST.append(self.parseTCST(
                self.mifarecard.sector(38).block(0) + 
                self.mifarecard.sector(38).block(1) + 
                self.mifarecard.sector(38).block(2)
                ))
        #XXX is this one seems not good?
        #self.TCST.append(self.parseTCST(
        #        self.mifarecard.sector(38).block(3) + 
        #        self.mifarecard.sector(38).block(4) + 
        #        self.mifarecard.sector(38).block(5)
        #        ))
        self.TCST.append(self.parseTCST(
                self.mifarecard.sector(38).block(6) + 
                self.mifarecard.sector(38).block(7) + 
                self.mifarecard.sector(38).block(8)
                ))
        self.TCPUDynamic = []
        self.parseTCPU(9)
        self.parseTCPU(11) #also reads 12!
        self.TCEL = []
        self.parseTCEL(3)
        self.parseTCEL(4)
        self.parseTCEL(5)
        self.parseTCEL(6)
        self.parseTCEL(7)

        self.parseTCDB(15)
        self.parseTCCP(self.mifarecard.sector(13).block(0) + \
                self.mifarecard.sector(13).block(1), num=1)
        self.parseTCCP(self.mifarecard.sector(13).block(2) + \
                self.mifarecard.sector(14).block(0), num=2)

    def parseCMI(self):
        #RKF-0022 3.2.1
        self.CMI = InterpretedBlock(self.mifarecard.sector(0).block(0),
                "CardSerialNumber", ByteString(32),
                "CardSerialNumberCheckByte", ByteString(8),
                "ManufacturerData", BitArray(88),
                )
        #Calculate Checksum
        self.CMI.CalculatedCardSerialNumberCheckByte = binascii.hexlify(chr(reduce(xor, map(ord, list(self.CMI.CardSerialNumber.bits.tobytes())))))
        self.CMI.names += ["CalculatedCardSerialNumberCheckByte"]

    def parseTCCI(self):
        #RKF-0022 4.1.3
        self.TCCI = InterpretedBlock(self.mifarecard.sector(0).block(1),
                "MADInfoByte", BitArray(16),
                "CardVersion", Integer(6),
                "CardProvider", AIDPTA(12),
                "CardValidityEndDate", DateCompact(14),
                "CardStatus", BitArray(8),
                "CardCurrencyUnit", CardCurrencyUnit(16),
                "EventLogVersionNumber", Integer(6),
                "MACAlgorithmIdentifier", BitArray(2),
                "MACKeyIdentifier", BitArray(6),
                "MACAuthenticator", BitArray(16),
                "Unused", BitArray(26),
                )

    def parseTCDI(self):
        #RKF-0022 4.2.3
        #print binascii.hexlify(self.mifarecard.sector(1).block(0))
        self.TCDI1 = InterpretedBlock(self.mifarecard.sector(1).block(0),
                "AIDPIX1", AIDPIXPair(12+12),
                "AIDPIX2", AIDPIXPair(12+12),
                "AIDPIX3", AIDPIXPair(12+12),
                "AIDPIX4", AIDPIXPair(12+12),
                "AIDPIX5", AIDPIXPair(12+12),
                "Checksum", ByteString(8),
                )
        self.TCDI2 = InterpretedBlock(self.mifarecard.sector(1).block(1),
                "AIDPIX6", AIDPIXPair(12+12),
                "AIDPIX7", AIDPIXPair(12+12),
                "AIDPIX8", AIDPIXPair(12+12),
                "AIDPIX9", AIDPIXPair(12+12),
                "AIDPIX10", AIDPIXPair(12+12),
                "Checksum", ByteString(8),
                )
        self.TCDI3 = InterpretedBlock(self.mifarecard.sector(1).block(2),
                "AIDPIX11", AIDPIXPair(12+12),
                "AIDPIX12", AIDPIXPair(12+12),
                "AIDPIX13", AIDPIXPair(12+12),
                "AIDPIX14", AIDPIXPair(12+12),
                "AIDPIX15", AIDPIXPair(12+12),
                "Checksum", ByteString(8),
                )


    def parseTCAS(self, block):
        #RKF-0022 4.3.3
        tcas = InterpretedBlock(block,
                "Identifier", ByteString(8),
                "VersionNumber", ByteString(6),
                #General Sector Status
                "SectorStatus0", SectorStatus(2),
                "SectorStatus1", SectorStatus(2),
                "SectorStatus2", SectorStatus(2),
                "SectorStatus3", SectorStatus(2),
                "SectorStatus4", SectorStatus(2),
                "SectorStatus5", SectorStatus(2),
                "SectorStatus6", SectorStatus(2),
                "SectorStatus7", SectorStatus(2),
                "SectorStatus8", SectorStatus(2),
                "SectorStatus9", SectorStatus(2),
                "SectorStatus10", SectorStatus(2),
                "SectorStatus11", SectorStatus(2),
                "SectorStatus12", SectorStatus(2),
                "SectorStatus13", SectorStatus(2),
                "SectorStatus14", SectorStatus(2),
                "SectorStatus15", SectorStatus(2),
                #Transaction Number
                "TransactionNumber", Integer(8),
                #Event Log
                "EventLogRecordNumber", Integer(4),
                #Ticket/Log Area
                "TicketLogAreaSectorPointer", Integer(4),
                "TicketLogSectorPointer1", Integer(4),
                "TicketLogSectorPointer2", Integer(4),
                "TicketLogSectorPointer3", Integer(4),
                "TicketLogSectorPointer4", Integer(4),
                "TicketLogSectorPointer5", Integer(4),
                "TicketLogSectorPointer6", Integer(4),
                "TicketLogSectorPointer7", Integer(4),
                "TicketLogSectorPointer8", Integer(4),
                #MAC
                "MACAlgorithmIdentifier", BitArray(2),
                "MACKeyIdentifier", BitArray(6),
                "MACAuthenticator", BitArray(16),
                "Unused", BitArray(10),
                )
        return tcas

    def parseTCPU(self, sector):
        ##XXX bruteforce!
        #for j in range(100):
        #    TCPUStatic= InterpretedBlock(self.mifarecard.sector(sector).block(0),
        #        "Identifier", ByteString(8, reverse=False),
        #        "VersionNumber", Integer(6),
        #        "AID", AIDPTA(12),
        #        "XXX", BitArray(j),
        #        "Value", MoneyAmount24(24),
        #        )
        #    print TCPUStatic.Value, j


        #RKF-0022 4.1.3, RKF-0023 3.4.1.1
        TCPUStatic = InterpretedBlock(self.mifarecard.sector(sector).block(0),
                "Identifier", ByteString(8, reverse=False),
                "VersionNumber", Integer(6),
                "AID", AIDPTA(12),
                "PurseSerialNumber", ByteString(32),
                "StartDate", DateCompact(14),
                "DataPointer", Integer(4),
                "MinimumValue", MoneyAmount24(24),
                "AutoLoadValue", MoneyAmount24(24),
                "Unused", BitArray(4),
                )



        if str(TCPUStatic.Identifier) != "85":
            #not a TCPU
            return
        #print "Found TCPUStatic at sector %d, version %s" % (sector, TCPUStatic.VersionNumber)
        #clear stuff from old version of TCPU
        if hasattr(self, "TCPUStatic") and self.TCPUStatic.VersionNumber.number < TCPUStatic.VersionNumber.number:
            self.TCPUDynamic = []
        #Is this outdated version?
        if hasattr(self, "TCPUStatic") and self.TCPUStatic.VersionNumber.number > TCPUStatic.VersionNumber.number:
            return
        self.TCPUStatic = TCPUStatic

        # (parse all known versions, to let Java codegen work nice)
        # VERSION 4
        #Where "Value" seems to match
        #What we have discovered:
        #  Value for the oldest block is (when correctly checked out) =
        #      newest value - 5000 + value of last travel
        #      (in oldest value 50 DKK is "reserved")
        self.TCPUDynamicv4 = tcpudynv4_1 = InterpretedBlock(self.mifarecard.sector(sector).block(1),
            "PurseTransactionNumber", Integer(16),
            "Value", MoneyAmount24(24),
            "XXX", BitArray(128-24-16-16-6-2),
            "MACAlgorithmIdentifier", BitArray(2),
            "MACKeyIdentifier", BitArray(6),
            "MACAuthenticator", BitArray(16),
            #"Status", BitArray(x),
            #"EndDate", DateCompact(14),
            #"Deposit", Integer(20),
            #"Unused", BitArray(128-24-16),
            )
        self.TCPUDynamicv4 = tcpudynv4_2 = InterpretedBlock(self.mifarecard.sector(sector).block(2),
            "PurseTransactionNumber", Integer(16),
            "Value", MoneyAmount24(24),
            "XXX", BitArray(128-24-16-16-6-2),
            "MACAlgorithmIdentifier", BitArray(2),
            "MACKeyIdentifier", BitArray(6),
            "MACAuthenticator", BitArray(16),
            #"Status", BitArray(x),
            #"EndDate", DateCompact(14),
            #"Deposit", Integer(20),
            #"Unused", BitArray(128-24-16),
            )

        # VERSION 6, introduced sometime around september-november 2013
        ##XXX bruteforce!
        #for i in range(255):
        #    b = InterpretedBlock(self.mifarecard.sector(sector).block(2) + self.mifarecard.sector(sector+1).block(0),
        #        "XXX", BitArray(i),
        #        "Value", MoneyAmount24(24),
        #        #"PurseTransactionNumber", Integer(16),
        #        )
        #    print 1, i, b.Value
        #    b = InterpretedBlock(self.mifarecard.sector(sector+1).block(1) + self.mifarecard.sector(sector+1).block(2),
        #        "XXX", BitArray(i),
        #        "Value", MoneyAmount24(24),
        #        #"PurseTransactionNumber", Integer(16),
        #        )
        #    print 2, i, b.Value
        self.TCPUDynamicv6 = tcpudynv6_1 = InterpretedBlock(self.mifarecard.sector(sector).block(2) + self.mifarecard.sector(sector+1).block(0),
            "PurseTransactionNumber", Integer(16),
            "Value", MoneyAmount24(24),
            "XXX", BitArray(216),
            )
        self.TCPUDynamicv6 = tcpudynv6_2 = InterpretedBlock(self.mifarecard.sector(sector+1).block(1) + self.mifarecard.sector(sector+1).block(2),
            "PurseTransactionNumber", Integer(16),
            "Value", MoneyAmount24(24),
            "XXX", BitArray(216),
            )


        if self.TCPUStatic.VersionNumber.number == 4:
            self.TCPUDynamic.append(tcpudynv4_1)
            self.TCPUDynamic.append(tcpudynv4_2)
        elif self.TCPUStatic.VersionNumber.number == 6:
            self.TCPUDynamic.append(tcpudynv6_1)
            self.TCPUDynamic.append(tcpudynv6_2)
            
        
        #Per the spec:
        #self.TCPUDynamic1 = InterpretedBlock(self.mifarecard.sector(sector).block(1),
        #        "PurseTransactionNumber", Integer(16),
        #        "EndDate", DateCompact(14),
        #        "Value", MoneyAmount24(24),
        #        "Status", BitArray(8),
        #        "Deposit", Integer(20),#XXX
        #        "AutoLoadStatus", BitArray(2),
        #        "MACAlgorithmIdentifier", BitArray(2),
        #        "MACKeyIdentifier", BitArray(6),
        #        "MACAuthenticator", BitArray(16),
        #        "Unused", BitArray(20),
        #        )
        #self.TCPUDynamic2 = InterpretedBlock(self.mifarecard.sector(sector).block(2),
        #        "PurseTransactionNumber", Integer(16),
        #        "EndDate", DateCompact(14),
        #        "Value", MoneyAmount24(24),
        #        "Status", BitArray(8),
        #        "Deposit", Integer(20),#XXX
        #        "AutoLoadStatus", BitArray(2),
        #        "MACAlgorithmIdentifier", BitArray(2),
        #        "MACKeyIdentifier", BitArray(6),
        #        "MACAuthenticator", BitArray(16),
        #        "Unused", BitArray(20),
        #        )

    def parseTCST(self, blocks):
        common = InterpretedBlock(blocks,
                "Identifier", ByteString(8, reverse=False),
                "VersionNumber", Integer(6),
                )
        self.CommonTCST = common

        # (parse all known versions, to let Java codegen work nice)
        # VERSION 4
        #What seems to work
        self.TCSTv4 = tcst = InterpretedBlock(blocks,
                "Identifier", ByteString(8, reverse=False),
                "VersionNumber", Integer(6),
                "AID", AIDPTA(12),
                "PIX", ByteString(12),
                "Status", BitArray(8),
                "XXX", BitArray(138),
                "JourneyOriginDateTime", DateTime(24),

                )

        # VERSION 5 introduced sometime around september-november 2013
        #XXX!!!
        #for i in range(78):
        #    self.TCSTv5 = tcst = InterpretedBlock(blocks,
        #        "Identifier", ByteString(8, reverse=False),
        #        "VersionNumber", Integer(6),
        #        "AID", AIDPTA(12),
        #        "PIX", ByteString(12),
        #        "Status", BitArray(8),
        #        "XXX", BitArray(47),
        #        "Value", MoneyAmount24(24),
        #        "XXX2", BitArray(i),
        #        "Location", Location(14),
        #        "XXX2", BitArray(78-14-i),
        #        "JourneyOriginDateTime", DateTime(24),
        #        #"JourneyOriginDateTime", DateCompact(14),

        #        )
        #    print self.TCSTv5.JourneyOriginDateTime, i, self.TCSTv5.Location
        self.TCSTv5 = tcst = InterpretedBlock(blocks,
            "Identifier", ByteString(8, reverse=False),
            "VersionNumber", Integer(6),
            "AID", AIDPTA(12),
            "PIX", ByteString(12),
            "Status", BitArray(8),
            "XXX", BitArray(47),
            "Value", MoneyAmount24(24),
            "XXX2", BitArray(64),
            "Location", Location(14),
            "JourneyOriginDateTime", DateTime(24),
            "XXX3", BitArray(76),
            )

        
        #Per the spec
        #RKF-0023 3.4.6
        #tcst = InterpretedBlock(blocks,
        #        "Identifier", ByteString(8, reverse=False),
        #        "VersionNumber", Integer(6),
        #        "AID", ByteString(12),
        #        "PIX", ByteString(12),
        #        "Status", BitArray(8),
        #        #Passenger Group
        #        "PassengerClass", BitArray(2),
        #        "PassengerSubgroup1", BitArray(14),
        #        "PassengerSubgroup2", BitArray(14),
        #        "PassengerSubgroup3", BitArray(14),
        #        #Validation
        #        "ValidationModel", BitArray(2),
        #        "ValidationStatus", BitArray(2),
        #        "ValidationLevel", BitArray(2),
        #        #Price
        #        "Price", Integer(20),
        #        "PriceModificationLevel", BitArray(6),
        #        "JourneyOriginAID", ByteString(12),
        #        "JourneyOriginPlace", ByteString(14),
        #        "JourneyOriginDateTime", DateTime(24),
        #        #XXX
        #        "Unused", BitArray(34),
        #        )

        if common.VersionNumber.number == 4:
            tcst = self.TCSTv4
        elif common.VersionNumber.number == 5:
            tcst = self.TCSTv5
        return tcst

    def parseTCEL(self, sector):
        #RKF-0023 3.4.2
        for i in [0,1,2]:
            ##XXX bruteforce parse
            #for j in range(42):
            #    pass


            tcelblock = InterpretedBlock(self.mifarecard.sector(sector).block(i),
                "Identifier", ByteString(8, reverse=False),
                "EventDateStamp", DateCompact(14),
                "EventTimeStamp", TimeCompact(16),
                "AID", AIDPTA(12),
                #XXX, I cannot get DeviceTransNo and Device to make sense. Could be something else.
                "DeviceTransNo", Integer(6),
                "Device", ByteString(16), # Unique within AID
                "StatusBits", TCELStatus(6, reverse=False),
                #"Location", Location(12),
                #"Location", BitArray(12),
                #"XXX2", BitArray(128),
                )

            if tcelblock.StatusBits.string in ("1b", "1c", "1d", "17"): #RKF-0023 sec. 3.4.2.4 Event data C
                tcelblock.parseTypes(
                    "PointerToTicket", Integer(6, reverse=False),
                    "Location", Location(14),
                    "XXX3", Integer(8),
                )
            elif tcelblock.StatusBits.string in ("08",): #RKF-0023 sec. 3.4.2.4 Event data C
                tcelblock.parseTypes(
                    "XXX3", BitArray(24),
                    "Value", MoneyAmount24(24),

                )
            elif tcelblock.StatusBits.string in ("0f",): #RKF-0023 sec. 3.4.2.4 Event data C
                #New statuscode, introduced sometime around september-november 2013
                ###XXX bruteforce parse
                for j in range(0):
                    tcelblock2 = InterpretedBlock(self.mifarecard.sector(sector).block(i),
                        "Identifier", ByteString(8, reverse=False),
                        "EventDateStamp", DateCompact(14),
                        "EventTimeStamp", TimeCompact(16),
                        "XXX", BitArray(j),
                        "Value", Integer(16),
                        )
                    #print tcelblock2.EventDateStamp, tcelblock2.Value, i, j

                #reparse everything
                tcelblock = InterpretedBlock(self.mifarecard.sector(sector).block(i),
                    "Identifier", ByteString(8, reverse=False),
                    "EventDateStamp", DateCompact(14),
                    "EventTimeStamp", TimeCompact(16),
                    "XXX", BitArray(1),
                    "AID", AIDPTA(12),
                    "Location", Location(14),
                    "XXX2", BitArray(7),
                    "StatusBits", TCELStatus(6, reverse=False),
                    "XXX3", BitArray(16),
                    "Value", Integer(16),
                    )

            #catch the last junk
            tcelblock.parseTypes(
                    "XXX2", BitArray(128),
            )
            self.TCEL += [tcelblock]
            continue


    def parseTCDB(self, sector):
        #RKF-0023 3.4.4
        tcdbstatic = InterpretedBlock(self.mifarecard.sector(sector).block(0),
            "Identifier", ByteString(8, reverse=False),
            "VersionNumber", Integer(6),
            "AID", AIDPTA(12),
            "DiscountType1", Integer(8),
            "DiscountType2", Integer(8),
            "DiscountType3", Integer(8),
            "Free", BitArray(78),
            )
        if str(tcdbstatic.Identifier) != "a1":
            return
        if tcdbstatic.VersionNumber.number == 2:
            self.TCDBStatic = tcdbstatic

        for i in [1,2]:
            ###XXX bruteforce parse
            #for j in range(111):
            #    tcdbdynamic = InterpretedBlock(self.mifarecard.sector(sector).block(i),
            #        "Status", Status(8),
            #        "FirsthMonth", DateMonth8(8),
            #        "XXX2", BitArray(j),
            #        "XXX1", Integer(8),
            #        "XXX", BitArray(128),
            #        )
            #    print tcdbdynamic.XXX1, i, j
            tcdbdynamic = InterpretedBlock(self.mifarecard.sector(sector).block(i),
                "Status", Status(8),
                "FirsthMonth", DateMonth8(8),
                "XXX1", BitArray(55),
                "DiscountStep", Integer(8),
                "XXX", BitArray(128),
                )
            setattr(self, "TCDBDynamic%d" % i, tcdbdynamic)
        else: #unknown version
            return

    def parseTCCP(self, blocks, num=1):
        ###XXX bruteforce parse
        #for j in range(187):
        #    tccpstatic = InterpretedBlock(blocks,
        #        "Identifier", ByteString(8, reverse=False),
        #        "VersionNumber", Integer(6),
        #        "AID", AIDPTA(12),
        #        "Status", Status(8),
        #        "CustomerNumber", Integer(34), #Kort nr.!
        #        "XXX1", BitArray(j),
        #        "Int1", Integer(5),
        #        #"Birthday", DateMonth11(11),
        #        "XXX2", BitArray(128),
        #        )
        #    print tccpstatic.Int1, j


        #RKF-0023 3.4.5 Customer Profile
        tccpstatic = InterpretedBlock(
                blocks,
            "Identifier", ByteString(8, reverse=False),
            "VersionNumber", Integer(6),
            "AID", AIDPTA(12),
            "Status", Status(8),
            "CustomerNumber", Integer(34), #Kort nr.!
            "XXX1", BitArray(61),
            "SequenceNumber", Integer(8), #XXX not sure!
            "XXX2", BitArray(23),
            "Birthday", DateMonth11(11),
            "XXX", BitArray(256),
            )

        if str(tccpstatic.Identifier) != "a2":
            return
        if tccpstatic.VersionNumber.number == 2:
            setattr(self, "TCCPStatic%d" % num, tccpstatic)


    def __str__(self):
        return (
            ("CMI: %s\n" % self.CMI) + 
            ("TCCI: %s\n" % self.TCCI) +
            #TCDI seems quite unused, only 0 bits + checksum
            #("TCDI1: %s\n" % self.TCDI1) +
            #("TCDI2: %s\n" % self.TCDI2) +
            #("TCDI3: %s\n" % self.TCDI3) +
            ("TCAS1: %s\n" % self.TCAS1) +
            ("TCPUStatic: %s\n" % self.TCPUStatic) +
            ("TCPUDynamic: [\n%s\n]\n" % "\n".join(map(str, self.TCPUDynamic))) +
            ("TCST: [\n%s\n]\n" % "\n".join(map(str, self.TCST))) +
            ("TCEL: [\n%s\n]\n" % "\n".join(map(str, self.TCEL))) +
            ("TCDBStatic1: %s\n" % self.TCDBStatic) + 
            ("TCDBDynamic1: %s\n" % self.TCDBDynamic1) +
            ("TCDBDynamic2: %s\n" % self.TCDBDynamic2) +
            ("TCCPStatic1: %s\n" % self.TCCPStatic1) +
            ("TCCPStatic2: %s\n" % self.TCCPStatic2)
            )
