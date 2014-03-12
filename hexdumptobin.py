
a = raw_input()
b = a.decode("hex")

outf = open("blah.mfd", "w")
outf.write(b)
outf.close()
