RejsekortReader
===============

Python code and basic Android app to parse the danish Rejsekort Mifare NFC cards.

Currently parsed is: the purse (how much money is on the card), ticket and event
log entries (what travels have taken place), card meta information (card
number), and customer information (e.g. birth-month).

The card stores the last 15 events, and the last 7 tickets, including locations
and timestamps.

We have been working on and off (mostly off) for quite some time, and originally
planed to do an app like http://blog.bangbits.com/2014/03/rejsekort-scanner.html
But now we are dumping whatever we have, in the hope that it might be useful for
Casper Bang (or others).

Code Structure
--------------
The Python code is the primary prototyping place. The data is parsed in
`rkf/card.py`, with the different data types parsed in `rkf/datatypes.py`.

The main utility is `readtest.py`, which parses a binary dump (e.g.
`dumps/*.mfd`). A dump can be obtained using "Raw Dump" in the app, and using
that string with the `hexdumptobin.py` utility.

The Python code is used to generate Java code with the same functionality, using
the `rkftojava.py` utility.

The Android app lives in `code/android` and is very rudimentary. A pre-compiled
package is included as `RejsekortReader.apk`.

Dataformat on the Rejsekort
===========================

Background
----------
The Rejsekort builds on the Mifare Classic standard, which uses a proprietary
crypto to protect the reading and writing of card-data. This crypto has been
broken for years, which Rejsekortet A/S knows [1]. Anyone can retrieve the
crypto keys with a cheap reader and some available software [2]. The Mifare keys
are the same for all cards.

However, the data is (probably) also protected by some form of cryptographic
hash, for which (to the best of our knowledge) the keys have not been broken
(yet). We have not done any research in that direction.

This means that anyone can read (and write) data to the card, but not
necessarily have the data accepted as authentic by the system. Anyway, tampering
attacks can probably be detected by the backend of the system.

The Mifare read keys (key A) is included in the Android application, but not the
write key (key B).

Resekortsföreningen
-------------------
The Rejsekort uses something similar to a standard that was publicly available
from "Resekortsföreningen" [3] under "Specifikationer", also included in the
folder `resekortsforeningen`. The datatypes, and much of the format, is re-used
from this standard.

There is a number of "objects" specified, each occupying a number of blocks:
 * CMI: Card Manufacturer Information
 * TCCI: Card Information
 * TCDI: Directory
 * TCAS: Application Status
 * TCPU: Purse (money on the card)
 * TCEL: Event log (checkin, checkout, control, etc.)
 * TCST: Special ticket (travels done on the card, including current)
 * TCDB: Discount basis
 * TCCP: Customer profile (birth-month, type of card, etc.)

Generally, everything is specified in number of bits, and the data is quite
crammed in. For fun, have a look at the datatypes in RKF-0023.

The data on the Rejsekort does not strictly adher to the Resekortsföreningen
standard, but the objects seem to be the same (however in newer versions) and
the datatypes are definitely shared. Look in the Python code and `absorter.py`
for hints at how we have tried to find various fields.

[1] http://www.computerworld.dk/art/213066/hacker-stadig-kaempehuller-i-rejsekortets-sikkerhed

[2] http://www.openpcd.org/OpenPCD_2_RFID_Reader_for_13.56MHz#Mifare_Classic_Offline_Cracker_installation

[3] https://web.archive.org/web/20060615031700/http://www.resekortsforeningen.se/

Authors
=======
Mads Chr. Olesen <mads@mchro.dk> and Georg Sluyterman <georg@sman.dk>,
with thanks to Christian Panton for initial research and python code.

Disclaimer
==========
We have undertaken this project out of curiosity, and do not intend to help
others commit illegal acts (such as travelling for free). To the best of our
knowledge this specification does not help in doing so. We believe that people
have a right to know which information is stored on the Rejsekort, since it can
de-facto be read by anyone else.
