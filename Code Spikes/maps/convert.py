#!/usr/bin/python

import math
import os
import subprocess

squares = 16

max_zoom = int(math.log(squares, 2) + 0.01)

filename = "map2.jpg"
folder = filename[:-4]

os.mkdir(folder)
os.chdir(folder)
filename = "../" + filename

print "working on {0}".format(filename)
print "\tmax_zoom = {0}".format(max_zoom)

for zoom in xrange(max_zoom+1):
    print "zoom: {0}".format(zoom)

    os.mkdir("{0}".format(zoom))

    ratio = 2 ** (max_zoom - zoom)
    size = 256 * ratio

    for x in xrange(squares / ratio):
        os.mkdir("{0}/{1}".format(zoom, x))

        for y in xrange(squares / ratio):
            parameter = "{0}x{0}+{1}+{2}".format(size, x * size, y * size)
            newfile = "{0}/{1}/{2}.png".format(zoom, x, y)

            print "\tcreating tile {0}x{1} with zoom {2}".format(x,y,zoom)
            print "\t\t", parameter

#            command = "convert -crop {0}x{0}+{1}+{2} {3} -resize 256 {4}{5}.png"
            subprocess.call(["convert", filename, "-crop", parameter, "-resize", "256", newfile])
