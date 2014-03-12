import mailbox
import sys
import datetime
import dateutil.parser

mbox = mailbox.mbox(sys.argv[1])
failedmbox = mailbox.mbox("failed.mbox")

i = 1
prevdate = None
for m in reversed(mbox):
    print m['Date']
    dt = dateutil.parser.parse(m['Date'])
    if prevdate and prevdate.day == dt.day and \
            prevdate.month == dt.month and \
            prevdate.year == dt.year:
        i += 1
    else:
        i = 1

    fname = 'dumps2/georgdump-%i%02i%02i-%02i.mfd' % (dt.year, dt.month, dt.day, i)

    comment = []
    try:
        lines = m.get_payload(0).get_payload().split("\n")
        dump = lines[0]
        comment = lines[1:]

        bindump = dump.decode("hex")
        outf = open(fname, "w")
        outf.write(bindump)
        outf.close()
    except:
        print "Failed!", m['Date'], i
        failedmbox.add(m)

    if comment:
        fname = 'dumps2/georgdump-%i%02i%02i-%02i.txt' % (dt.year, dt.month, dt.day, i)
        outf = open(fname, "w")
        outf.write("\n".join(comment))
        outf.close()

    prevdate = dt
failedmbox.close()
