#!/usr/bin/env python


c_z = 12
c_x = 1054
c_y = 1558

max_zoom = 17
avg_filesize = 14 #kb

# get tiles above
for z in xrange(0, c_z):
    x = c_x / 2 ** (c_z - z)
    y = c_y / 2 ** (c_z - z)
    print "mkdir {0}".format(z)
    print "mkdir {0}/{1}".format(z,x)
    print "wget a.tile.openstreetmap.org/{0}/{1}/{2}.png".format(z, x, y)
    print "mv {0}.png {1}/{2}/{0}.png".format(y, z, x)


count = 0
for z in xrange(0, max_zoom - c_z):
    modifier = 2 ** z
    z_t = c_z + z
    print "mkdir {0}".format(z_t)

    for x in xrange(2**z):
        x_t = modifier * c_x + x
        print "mkdir {0}/{1}".format(z_t,x_t)
 
        for y in xrange(2**z):
            y_t = modifier * c_y + y

            count += 1
            
#            print z, x, y, "\t", z_t, x_t, y_t
            print "wget a.tile.openstreetmap.org/{0}/{1}/{2}.png".format(z_t, x_t, y_t)
#            print "mv {0}.png {1}/{2}/{3}.png".format(y_t, z, x, y)
            print "mv {0}.png {1}/{2}/{3}.png".format(y_t, z_t, x_t, y_t)


#print count, "tiles"
#print "estimated size =", avg_filesize * count / 1024, "megs"
