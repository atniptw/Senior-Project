#!/usr/bin/env python


c_z = 12
c_x = 1054
c_y = 1558

max_zoom = 18
avg_filesize = 14 #kb

count = 0
for z in xrange(0, max_zoom - c_z):
    modifier = 2 ** z
    print "mkdir {0}".format(z)

    for x in xrange(2**z):
       print "mkdir {0}/{1}".format(z,x)
 
       for y in xrange(2**z):
            count += 1
            z_t = c_z + z
            x_t = modifier * c_x + x
            y_t = modifier * c_y + y

            
#            print z, x, y, "\t", z_t, x_t, y_t
            print "wget a.tile.openstreetmap.org/{0}/{1}/{2}.png".format(z_t, x_t, y_t)
            print "mv {0}.png {1}/{2}/{3}.png".format(y_t, z, x, y)


#print count, "tiles"
#print "estimated size =", avg_filesize * count / 1024, "megs"
