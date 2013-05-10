import math
import poi
import itertools

rose = poi.POI(1,
    "tree",
    lambda t:39.38,
    lambda t:-87.7, 
    "Fallen Tree",
    {"desc":"corner of 1st and other street"})
fred = poi.POI(2,
    "downed tree",
    lambda t: 39.466,
    lambda t: -87.414,
    "Fallen Tree",
    {"thickness" : "2 feet"})
bob = poi.POI(3,
    "powerline on sidewalk",
    lambda t: 39.462,
    lambda t: -87.4,
    "Downed Power Line",
    {"describtion":"sparks everywhere"})
drogo = poi.POI(4, "fire",
    lambda t: 39.501,
    lambda t: -87.398 + 0.1 * math.sin(2 * math.pi * t / 10),
    "Fire",
    {"desc":"in Mcdonalds on 4th and cherry"})

POIelements = {rose.getUID():rose, 
                fred.getUID():fred,
                bob.getUID():bob,
                drogo.getUID():drogo}

nextUIDgenerator = itertools.count(max(POIelements.keys()) + 1)
