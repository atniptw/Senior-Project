#!/usr/bin/python

import math
import os
import subprocess

size_x = 21
size_y = 14

max_zoom = int(1 + math.log(max(size_x, size_y), 2))

filename = "21x14.jpg"
folder = "map1"
os.chdir(folder)

print "working on {0}".format(filename)
print "\tmax_zoom = {0}".format(max_zoom)

for zoom in xrange(max_zoom):
    print "zoom: {0}".format(zoom)

    os.mkdir("{0}".format(zoom))

    ratio = 2 ** ((max_zoom-1) - zoom)
    for x in xrange(size_x / ratio):
        os.mkdir("{0}/{1}".format(zoom, x))

        for y in xrange(size_y / ratio):
            size = 256 * ratio
            parameter = "{0}x{0}+{1}+{2}".format(size, x * size, y * size)
            newfile = "{0}/{1}/{2}.jpg".format(zoom, x, y)

            print "\tcreating tile {0}x{1} with zoom {2}".format(x,y,zoom)
            print "\t\t", parameter

#            command = "convert -crop {0}x{0}+{1}+{2} {3} -resize 256 {4}{5}.jpg"
            subprocess.call(["convert", filename, "-crop", parameter, "-resize", "256", newfile])
