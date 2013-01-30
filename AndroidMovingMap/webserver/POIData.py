import math
import poi

rose = poi.POI(0,
    "Rose",
    lambda t:40.0,
    lambda t:-88.0, 
    0,
    {"desc":"rose"})
fred = poi.POI(1,
    "Fred",
    lambda t: 10*math.cos(6.28*t/10.0),
    lambda t:0,
    0,
    {})
bob = poi.POI(2,
    "Bob",
    lambda t:0,
    lambda t: 10*math.sin(6.28*t/15.0),
    0,
    {"describtion":"Bob"})
drogo = poi.POI(3, "Drogo",
    lambda t:-25+15*math.sin(6.28*t/15.0),
    lambda t:45+20*math.cos(6.28*t/15.0),
    0,
    {"manlevel":"15"})

POIelements = {rose.getUID():rose, 
                fred.getUID():fred,
                bob.getUID():bob,
                drogo.getUID():drogo}

