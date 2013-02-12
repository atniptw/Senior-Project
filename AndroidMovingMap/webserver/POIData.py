import math
import poi
import itertools

rose = poi.POI(0,
    "Rose",
    lambda t:40.0,
    lambda t:-88.0, 
    "type 1",
    {"desc":"rose"})
fred = poi.POI(1,
    "Fred",
    lambda t: 15*math.cos(6.28*t/10.0),
    lambda t:0,
    "type 2",
    {})
bob = poi.POI(2,
    "Bob",
    lambda t:0,
    lambda t: 15*math.sin(6.28*t/15.0),
    "type 3",
    {"describtion":"Bob"})
drogo = poi.POI(3, "Drogo",
    lambda t:-25+20*math.sin(6.28*t/15.0),
    lambda t:45+25*math.cos(6.28*t/15.0),
    "type 1",
    {"manlevel":"15"})

POIelements = {rose.getUID():rose, 
                fred.getUID():fred,
                bob.getUID():bob,
                drogo.getUID():drogo}

nextUIDgenerator = itertools.count(4)
