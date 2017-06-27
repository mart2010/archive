"""Module to generate DEM and Image cropping
This uses GDAL programs to manage GIS file and GIS-related operations

"""
import datetime
import subprocess
from gdalconst import *
import gdal
import subprocess
import argparse
import ConfigParser
import os


class Point(object):
    """For holding point coordinate in any arbitrary format
    """
    def __init__(self, x, y):
        # for now we use Decimal degrees (ex. +123.3232,+45.4324)
        self.X = x
        self.Y = y

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
        if self.Y >= 0:
            s = 'N' + '%02d' % self.Y
        else:
            s = 'S' + '%02d' % abs(self.Y - 1)
        if self.X >= 0:
            s += 'E' + '%03d' % self.X
        else:
            s += 'W' + '%03d' % abs(self.X - 1)
        return s

    def __str__(self):
        return 'X:' + str(self.X) + ', Y:' + str(self.Y)


class Rectangle(object):
    """Rectangle described by 2 Points
    """
    def __init__(self, ll=None, ur=None, ul=None, lr=None):
        if ll != None and ur != None:
            self.pointll = Point(ll.X, ll.Y)
            self.pointur = Point(ur.X, ur.Y)
            self.pointul = Point(ll.X, ur.Y)
            self.pointlr = Point(ur.X, ll.Y)
        elif ul != None and lr != None:
            self.pointul = Point(ul.X, ul.Y)
            self.pointlr = Point(lr.X, lr.Y)
            self.pointll = Point(ul.X, lr.Y)
            self.pointur = Point(lr.X, ul.Y)
        else:
            raise ValueError('Rectangle has wrong pair of points')

        if (self.pointul.X > self.pointlr.X or
            self.pointul.Y < self.pointlr.Y):
            raise ValueError("Rectangle definition invalid: " + str(self))

    def lowerLeft(self):
        return self.pointll

    def upperRight(self):
        return self.pointur

    def upperLeft(self):
        return self.pointul

    def lowerRight(self):
        return self.pointlr

    def getTileLon(self):
        """Return tiles longitude coverage, ex. [-23,-22]
        """
        llx = int(self.pointll.X)
        urx = int(self.pointur.X)
        return [x for x in range(llx, urx + 1)]

    def getTileLat(self):
        """Return tiles latitude coverage, ex. [45, 46, 47]
        """
        lly = int(self.pointll.Y)
        ury = int(self.pointur.Y)
        return [y for y in range(lly, ury + 1)]

    def getTileLatDesc(self):
        """Return descending order: north to south, ex. [47, 46, 45]
        """
        lly = int(self.pointll.Y)
        ury = int(self.pointur.Y)
        return [y for y in range(ury, lly - 1, -1)]

    # for now, hardcode 'hgt' file extension
    def getHgtTilesName(self):
        n = []
        for ty in self.getTileLat():
            for tx in self.getTileLon():
                tileP = Point(tx, ty)
                n.append(tileP.getTileName() + ".hgt")
        return n


    def __str__(self):
        return 'Upper-left (' + str(self.pointul) + '), Lower-right (' + str(self.pointlr) + ')'


def name_gen_file(prefix, m, postfix):
    """Returns a filename derived from Rectangle coordinate"""
    if isinstance(m, Rectangle):
        t = "_%.4f-%.4f--%.4f-%.4f_" % (m.upperLeft().X, m.upperLeft().Y, m.lowerRight().X, m.lowerRight().Y)
    elif isinstance(m, str):
        t = m
    else:
        ValueError("Wrong argument type: %s" %type(m))
    return prefix + t + postfix


def process_input(fileInput, cmd, deleteInput):
    """Execute OS-level program normally operating on fileInput as specified in cmd """
    print "About to execute...: " + " ".join(cmd)
    #this checks exit code and raise CalledProcessError if code != 0
    subprocess.check_call(cmd)
    if deleteInput:
        os.remove(fileInput)
    return 



def merge_crop_files(zone):
    """ Use gdal_merge to merge and crop files found in zone,
        this requires Geotif format when subsetting the input files
        Return generated file output 
    """

    cmd = []
    cmd.append(ENV_PARAMS["gdalpath"] + 'gdal_merge.py')

    outfilename = name_gen_file("Dem", zone, "crop.tif")
    outfilepath = ENV_PARAMS["outpath"] + outfilename
    cmd.append('-o')
    cmd.append(outfilepath)

    ulx, uly, lrx, lry = str(zone.upperLeft().X), str(zone.upperLeft().Y), \
                         str(zone.lowerRight().X), str(zone.lowerRight().Y)
    cmd.append('-ul_lr')
    cmd.append(ulx)
    cmd.append(uly)
    cmd.append(lrx)
    cmd.append(lry)

    for t in zone.getHgtTilesName():
        fileD = ENV_PARAMS["dempath"] + t
        if not os.path.isfile(fileD):
            raise ValueError("Dem file '%s' is missing, stop execution!" % fileD)
        cmd.append(fileD)

    process_input(None, cmd, False)
    return outfilename


def reproject_file(inputFilename, deleteInput, targetSrs, sourceSRS= None):
    """ Use gdalwarp to reproject DEM raster.  Srs can be detailed ("+proj=utm +zone=32 +datum=WGS_1984 +units=m")
        or using EPSG equivalent ("EPSG:32632")
        Familiar EPSG are:
            - EPSG:4326 --> lat/long unprojected Geo coord system 
            - EPSG:32632 --> UTM zone 32 (Swiss)
            
    """

    cmd = []
    cmd.append(ENV_PARAMS["gdalpath"] + 'gdalwarp')

    # could add these, but source'info already included inside the GeoTiff
    # cmd.append('-s_srs')
    cmd.append('-t_srs')
    # harccode values for now, UTM zone 32 (swiss)
    cmd.append("EPSG:32632")
    # input file    
    cmd.append(ENV_PARAMS["outpath"] + inputFilename)
    mid = inputFilename[inputFilename.find("_"):inputFilename.rfind("_")]
    outfilename = name_gen_file("Proj", mid, "UTM.tif")
    # output file
    cmd.append(ENV_PARAMS["outpath"] + outfilename)

    process_input(ENV_PARAMS["outpath"] + inputFilename, cmd, deleteInput)
    return outfilename


def convert_to_raster(inputFilename, outformat, deleteInput):
    """Convert with gdal_translate. Ex. of raster outformat:
        AAIGrid (Arc/Info Ascii grid), GTiff (default), PNG,
        XYZ, USGSDEM (USGS Ascii Dem), SRTMHGT (SRTM Hgt)
    """
    cmd = []
    cmd.append(ENV_PARAMS["gdalpath"] + 'gdal_translate')
    cmd.append('-of')
    cmd.append(outformat)
    #source 
    cmd.append(ENV_PARAMS["outpath"] + inputFilename)
    #dest raster
    destfile = inputFilename[0:inputFilename.rindex(".")] + "_" + outformat
    destpath = ENV_PARAMS["outpath"] + destfile
    cmd.append(destpath)

    process_input(ENV_PARAMS["outpath"] + inputFilename, cmd, deleteInput)
    return destfile


def csv_to_SHP(inputFilename, deleteInput):
    """Convert CSV (assumed format "x_coord; y_coord; z_elev") into a Shapefile ('ESRI Shapefile') using ogr2ogr.
       Assummption: CSV points are unprojected lat/long (WGS84).
       Ex of vrt file (for csv source lat, lon, name):
        <OGRVRTDataSource>
            <OGRVRTLayer name="test">
                <SrcDataSource>test.csv</SrcDataSource>
                <GeometryType>wkbPoint</GeometryType>
                <LayerSRS>WGS84</LayerSRS>
                <GeometryField encoding="PointFromColumns" x="long" y="lat" />
            </OGRVRTLayer>
        </OGRVRTDataSource>
       
    """
    layer = inputFilename[:inputFilename.rindex(".csv")]
    shpFilename = layer + ".shp"


    # First create a virtual dataset vrt file
    vrtFile = ENV_PARAMS["outpath"] + layer + "_temp.vrt"
    vrt = open(vrtFile,'w')
    vrt.write('<OGRVRTDataSource><OGRVRTLayer name="%s">\n' % layer)
    vrt.write('\t<SrcDataSource>%s</SrcDataSource>\n' % (ENV_PARAMS["outpath"] + inputFilename))
    vrt.write('\t<GeometryType>wkbPoint</GeometryType>\n')
    vrt.write('\t<LayerSRS>WGS84</LayerSRS>\n')
    vrt.write('\t<GeometryField encoding="PointFromColumns" x="x_coord" y="y_coord" z="z_elev"/>\n')
    vrt.write('</OGRVRTLayer></OGRVRTDataSource>')
    vrt.close()

    # Create the command
    cmd = []
    cmd.append("ogr2ogr")
    cmd.append("-f")
    cmd.append("ESRI Shapefile")
    # with SHP, dataset is meant to be a directory
    cmd.append(ENV_PARAMS["outpath"] + "shp_" + layer)
    cmd.append(vrtFile)
    cmd.append("-overwrite")

    process_input(ENV_PARAMS["outpath"] + "shp_" + layer, cmd, False)
    # cleanup vrt temp file
    #os.remove(vrtFile)


def gridAscii_to_csv(fileInput, deleteInput):
    infile = open(ENV_PARAMS["outpath"] + fileInput,'r')
    ncols = int(infile.readline().split()[1])
    nrows = int(infile.readline().split()[1])
    xll = float(infile.readline().split()[1])
    yll = float(infile.readline().split()[1])
    csize = float(infile.readline().split()[1])
    yul = yll + csize*nrows

    # use same name but a 'csv' extension
    csvFile = fileInput[:fileInput.rindex(".asc")] + ".csv"
    outfile = open(ENV_PARAMS["outpath"] + csvFile,'w')
    outfile.write("x_coord; y_coord; z_elev\n")
    
    for r in range(nrows):
        row = infile.readline().split()
        for c in range(ncols):
            x = xll + csize/2 + (csize*c)
            y = yul - csize/2 - (csize*r)
            outfile.write("%f;%f;%s\n" %(x, y, row[c]))

    outfile.close()
    infile.close()
    if deleteInput:
        os.remove(ENV_PARAMS["outpath"] + fileInput)

    return csvFile



def _read_args():
    parser = argparse.ArgumentParser()
    #Mandatory positional arguments
    parser.add_argument("UpperLeftLongitude", type=float, help="Coordinates in Degree decimal")
    parser.add_argument("UpperLeftLatitude", type=float)
    parser.add_argument("LowerRightLongitude", type=float)
    parser.add_argument("LowerRightLatitude", type=float)

    #Optional argument for overwrite config file
    parser.add_argument("-s","--dempath")
    parser.add_argument("-o","--outpath")
    # etc..
    #parser.add_argument("-on","--outputFilename",)

    args = parser.parse_args()
    return args


ENV_PARAMS = {}

def setup_env(configFile = 'param.conf'):
    """Load up environment variables in ENV_PARAMS often required by functions.
       By default read file 'param.conf' located in path but can be changed by calling code
    """
    conf = ConfigParser.SafeConfigParser()
    conf.read(configFile)

    ENV_PARAMS["gdalpath"] = conf.get("Gdal lib","location")
    ENV_PARAMS["dempath"] = conf.get("Path","dem_source")
    ENV_PARAMS["outpath"] = conf.get("Path","gen_output")
    # other dep to be used: blender, java lib, source data repos, etc..



def main():
    """ This runs complete workflow:
        1- Load the environment var: gdal, dirs, blender(if I decide to call blender from script: blender --background --python myscript.py)
           data source, temp dir, target dir (PARAMS)
        2- Generate the DEM from input zone coordinates
        3- Generate the Imagery for same zone
        4- Generate 3D mesh (from blender)

    """
    #### Read arguments
    args = _read_args()

    #### Setup config params 
    setup_env()

    ### Get the zone of interest 
    ul = Point(args.UpperLeftLongitude, args.UpperLeftLatitude)
    lr = Point(args.LowerRightLongitude, args.LowerRightLatitude)
    zone = Rectangle(ul= ul, lr= lr)
    
    #possibly overwrite global env var
    if args.dempath != None: ENV_PARAMS["dempath"] = args.dempath  
    if args.outpath != None: ENV_PARAMS["outpath"] = args.outpath
    
    print ENV_PARAMS
    print zone
    
    #### Setup logging
    

    #### Step Merge and Crop DEM
    
    filetif = merge_crop_files(zone)
    print "File generated =" + filetif

    #### for testing purpose... generate easy to read ascii
    destfile = gridAscii_to_csv(filetif, False)
    print "File generated =" + destfile

    #### Step Reproject DEM into UTM (to check same as Landsat imagery)
    #TODO: to test
    projectfile = reproject_file(filetif, 'todo')
    print "File with proper projection is" + projectfile


# activate doctest when module executed in shell: 'python module.py'
if __name__ == "__main__":
    import doctest
    doctest.testmod()
    main()
