import math
import poi
import itertools

rose = poi.POI(0,
    "tree",
    lambda t:39.38,
    lambda t:-87.7, 
    "Fallen Tree",
    {"desc":"corner of 1st and other street"})
fred = poi.POI(1,
    "downed tree",
    lambda t: 39.466,
    lambda t: -87.414,
    "Fallen Tree",
    {"thickness" : "2 feet"})
bob = poi.POI(2,
    "powerline on sidewalk",
    lambda t: 39.462,
    lambda t: -87.4 + .01 * math.sin(6.28*t/15.0),
    "Downed Power Line",
    {"describtion":"sparks everywhere"})
drogo = poi.POI(3, "fire",
    lambda t: 39.501,
    lambda t: -87.398,
    "Fire",
    {"desc":"in Mcdonalds on 4th and cherry"})

POIelements = {rose.getUID():rose, 
                fred.getUID():fred,
                bob.getUID():bob,
                drogo.getUID():drogo}

nextUIDgenerator = itertools.count(4)
