import json
import time


class POI:
    def __init__(self, *args, **kwargs):
        #args -- tuple of unnamed arguments
        #kwargs -- dictionary of named arguments

        if len(args) >= 4:
            if len(args) >= 6:
                self.attributes = args[5]
            else:
                self.attributes = {}

            if len(args) >= 5:
                self.attributes["POItype"] = args[4]
            else:
                self.attributes["POItype"] = "type 0"

            self.attributes["UID"] = args[0]
            self.attributes["name"] = args[1]
            self.latitudeF = args[2]
            self.longitudeF = args[3]

        elif len(args) == 1:
            self.attributes = {}
            self.attributes["UID"] = -1
            self.attributes["name"] = "BAD NAME"
            self.attributes["POItype"] = "type 0"
            self.attributes["latitude"] = 0
            self.attributes["longitude"] = 0

            for k,v in json.loads(args[0]).items():
                self.attributes[k] = v

            # for Demo reasons
            self.latitudeF = lambda x : self.attributes["latitude"]
            self.longitudeF = lambda x : self.attributes["longitude"]
        
        else:
            raise Exception("bad init data for poi")


    def toDict(self):
        newAttr = self.attributes
        # for Demo reasons so that POI can move without constant update
        newAttr["latitude"] = self.latitudeF(time.time())
        newAttr["longitude"] = self.longitudeF(time.time())
        return newAttr

    def getUID(self):
        return self.attributes["UID"]

    def getName(self):
        return self.attributes["name"]

    def __eq__(self, other):
        if type(other) is type(self):
            return self.getUID() == other.getUID()
        return False

    def __ne__(self, other):
        return not self.__eq__(other)

    def __hash__(self):
        return self.getUID()

    def __repr__(self):
        return str(self.toDict())
