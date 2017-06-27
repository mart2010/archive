"""Module to manage  HGT file 

"""


def get_cell_type(ctype):
    if ctype in ('3"', '3', 3):
        return 3
    elif ctype in ('1"', '1', 1):
        return 1
    else:
        raise ValueError("Unsupported cell type")

def get_tile_n(ctype):
    if get_cell_type(ctype) == 3:
        return 1201
    else:
        return 3601

def get_cell_size(ctype):
    return 1.0 / get_tile_n(ctype)



class Point(object):
    """Simple class holding point coordinate in any arbitrary format
    """

    def __init__(self, x, y):
        # for now we use Decimal degrees (ex. +123.3232,+45.4324)
        self.x = x
        self.y = y

    def getX(self):
        return self.x

    def getY(self):
        return self.y

    def getTileName(self):
        """Simple doctest
            >>> Point(23.43,45.03).getTileName()
            'N45E023'
            >>> Point(-23.43,5.03).getTileName()
            'N05W024'
            >>> Point(123.43,-45.03).getTileName()
            'S46E123'
        """
        s = ''
        if self.y >= 0:
            s = 'N' + '%02d' % self.y
        else:
            s = 'S' + '%02d' % abs(self.y - 1)

        if self.x >= 0:
            s += 'E' + '%03d' % self.x
        else:
            s += 'W' + '%03d' % abs(self.x - 1)

        return s

    def __str__(self):
        return 'X:' + str(self.x) + ', Y:' + str(self.y)


class Zone(object):
    """Zone is a rectangle described as 2 Points
    """

    def __init__(self, ll=None, ur=None, ul=None, lr=None):
        if ll != None and ur != None:
            self.pointll = Point(ll.getX(), ll.getY())
            self.pointur = Point(ur.getX(), ur.getY())
        elif ul != None and lr != None:
            self.pointll = Point(ul.getX(), lr.getY())
            self.pointur = Point(lr.getX(), ul.getY())
        else:
            raise ValueError('Zone definition error')

    def getPointLowerLeft(self):
        return self.pointll

    def getPointUpperRight(self):
        return self.pointur

    def getTileLong(self):
        """Return tiles longitude coverage, ex. [-23,-22] 
        """
        llx = int(self.pointll.getX())
        urx = int(self.pointur.getX())
        return [x for x in range(llx, urx + 1)]

    def getTileLat(self):
        """Return tiles latitude coverage, ex. [45, 46, 47]
        """
        lly = int(self.pointll.getY())
        ury = int(self.pointur.getY())
        return [y for y in range(lly, ury + 1)]

    def getTileLatDesc(self):
        """Return descending order: north to south, ex. [47, 46, 45]
        """
        lly = int(self.pointll.getY())
        ury = int(self.pointur.getY())
        return [y for y in range(ury, lly - 1, -1)]

    def getTilesNameFromTop(self):
        """Return tiles row by row starting from northernmost
        >>> Zone(ll=Point(23,43),ur=Point(24,44)).getTilesNameFromTop()
        ['N44E023', 'N44E024', 'N43E023', 'N43E024']
        >>> Zone(ul=Point(-24,-43),lr=Point(-23,-44)).getTilesNameFromTop()
        ['S44W025', 'S44W024', 'S45W025', 'S45W024']
        """
        n = []
        for ty in self.getTileLatDesc():
            for tx in self.getTileLong():
                tileP = Point(tx, ty)
                n.append(tileP.getTileName())
        return n

    def getTilesNameFromTopAs2DList(self):
        """Return tiles row by row starting from northernmost
        >>> Zone(ll=Point(23,43),ur=Point(24,44)).getTilesNameFromTopAs2DList()
        [['N44E023', 'N44E024'], ['N43E023', 'N43E024']]
        >>> Zone(ul=Point(-24,-43),lr=Point(-23,-44)).getTilesNameFromTopAs2DList()
        [['S44W025', 'S44W024'], ['S45W025', 'S45W024']]
        """
        n = []
        for ty in self.getTileLatDesc():
            r = []
            for tx in self.getTileLong():
                tileP = Point(tx, ty)
                r.append(tileP.getTileName())
            n.append(r)
        return n
    #to destroy!!
    def get_ulskip_of_ul_tile(self, cellType):
        csize = get_cell_size(cellType)
        skipx = int((self.getPointLowerLeft().getX() - self.getTileLong()[0]) / csize)
        skipy = int((self.getTileLatDesc()[0] - self.getPointUpperRight().getY()) / csize)
        return {skipx, skipy}

    def get_lrcover_of_lr_tile(self, cellType):
        csize = get_cell_size(cellType)
        coverx = int((self.getPointUpperRight().getX() - self.getTileLong()[-1]) / csize)
        covery = int((self.getTileLat()[0] - self.getPointUpperRight().getY()) / csize)
        return {coverx, covery}

    def __str__(self):
        return 'Lower-left (' + str(self.pointll) + '), Upper-right (' + str(self.pointur) + ')'



def get_unpack_arg(ctype):
    if get_cell_type(ctype) == 3:
        return "<1442401H"
    else:
        return "<12967201H"


# SRTM HGT binary file has 3601rowsX3601cells (1") or 1201rowsX1201cells (3") of words data (2bytes big-endian)
# (last row/cell overlapping with next tile), corresponding to elevation (=1byte*256+2byte) points stored
# row by row starting with the northernmost row.
import struct

class HgtDemFile(object):
    """
    Read one HGT file and expose its content as tuple (self.tupleDem)
    """

    def __init__(self, filename, cellType):
        # read 1201x1201= (3") or 3601X3601+ (1") of Hgt-Dem file content
        # (ex. '..dir/44N023E.hgt' and store it into into self.tupleDem
        self.width = get_tile_n(cellType)
        self.cellSize = 1.0 / self.width
        self.lowerLong = float(filename[-8:-5])
        self.lowerLat = float(filename[-11:-9])
        fi = open(filename, "rb")
        content = fi.read()
        fi.close()
        self.tupleDem = struct.unpack(get_unpack_arg(cellType), content)


    def readRow(self, irow):
        """ Return i-th row (0-based) as tuple
        """
        first = irow * self.width
        t = self.tupleDem[first: first + self.width]
        return t

    def readBeginOfRow(self, irow, j):
        """Return part of i-th row from begin of tile to j (excl) (0-based) as tuple
        """
        t = {}
        first = irow * self.width
        t = self.tupleDem[first: first + j]
        return t

    def readEndofRow(self, irow, j):
        """Return part of i-th row from j (0-based) to end of tile as tuple
        """
        t = {}
        first = irow * self.width
        t = self.tupleDem[first + j: first + self.width]
        return t

    def getWidth(self):
        return self.width

    def getCellSize(self):
        return self.cellSize

    def getLowerLong(self):
        #westernmost longitude  
        return self.lowerLong

    def getLowerLat(self):
        #southermost latitude
        return self.lowerLat




# generate new custom tile with self.mosaic ({new tuple})

# let GDAL library manage the file
from gdalconst import *
import gdal

class HgtDemFileGDAL(object):

    def __init__(self, filename):
        self.hgtDataset = gdal.Open(filename, GA_ReadOnly)
        self.demBand = self.hgtDataset.GetRasterBand(1)
        self.fmt = ">"

class HgtMosaicGDAL(object):
    pass




# calculate new self.width and self.height

class HGTDemMosaic(object):
    """
    Fetch HGT files within a given Zone, tile and crop them into 
    a single list: mosaicList
    """

    def __init__(self, fileroot, zone, cellType):
        self.zone = zone
        self.tileWidth, self.cellSize = 0, 0.0
        t = self.zone.getTilesNameFromTopAs2DList()
        filenames = [["" + fileroot + cell + ".hgt" for cell in row] for row in t]

        #load the mosaic as 2-D list from HgtDemfile content
        self.mosaicontent = []
        for row in range(0, self.getMosaicHeight):
            fl = []
            for cell in range(0, self.getMosaicWidth):
                #initialize tileWidth and cellSize on first file
                if self.tileWidth == 0:
                    self.tileWidth = f.getWidth()
                    self.cellSize = f.getCellSize()
                f = HgtDemFile(filenames[row][cell], cellType)
                fl.extend(f.tupleDem[])

            self.mosaicHgtfiles.append(fl)


    def cropAndGenerateASCIIGrid(self, outfilename):
        # First cell to consider is located just before the cell encompassing self.zone
        skipx = int((self.mosaicHgtfiles[0][0].getLowerLong() \
                     - self.zone.getPointLowerLeft().getLongi()) / self.cellSize)
        xll = self.mosaicHgtfiles[0][0].getLowerLong() + float(skipx) * self.cellSize
        skipy = int((self.mosaicHgtfiles[0][0].getLowerLati() \
                     - self.zone.getPointLowerLeft().getLati()) / self.cellSize)

        out = open(outfilename, 'w')

        out.write("ncols\t\t%s\n") % 3
        out.write("nrows\t\t%2\n") % 4
        out.write("xllcorner\t\t%s" % 0.0
                  #out.write("yllcorner\t\t%s" % 0.0
                  #for row in range(0, self.getMosaicHeight):

        #for cell in range(0, self.getMosaicWidth):
        #    hgtFile = self.mosaicHgtfiles[row][cell]
        #    if
        #    fl.append(f)


    def getMosaicWidth(self):
        return len(self.zone.getTileLong())

    def getMosaicHeight(self):
        return len(self.zone.getTileLat())

    def __str__(self):
        return str(self.filenames)


    #
    #def generateASCIIGrid(northWest, southEast, DEMCell, outputFile):
    #    ncolsOri = HgtBinaryFile.CellSize[DEMCell]
    #    nrowsOri = HgtBinaryFile.CellSize[DEMCell]

    #output = open(outputFile, "w")
    #output.write()


# activate doctest when module executed in shell: 'python module.py'
if __name__ == "__main__":
    import doctest
    doctest.testmod()

    # call main()