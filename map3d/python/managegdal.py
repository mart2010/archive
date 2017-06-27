__author__ = 'mouellet'

import osgeo
import gdal
from gdalconst import *
import struct

# by default, gdal does not raise Exception, following required to enable proper behavior
gdal.UseExceptions()

hgtfile = '/Users/mouellet/dev/map3d/dem_sample/alps_hgt/N46E007.hgt'

dataset = gdal.Open(hgtfile, GA_ReadOnly)


print 'Driver: ', dataset.GetDriver().ShortName, '/', \
    dataset.GetDriver().LongName
print 'Size is ', dataset.RasterXSize, 'x', dataset.RasterYSize, 'y', dataset.RasterCount
print 'Projection is ', dataset.GetProjection()
print 'Raster count is ', dataset.RasterCount

# get the affine transformation
geotransform = dataset.GetGeoTransform()
if not geotransform is None:
    print 'Origin = (', geotransform[0], ',', geotransform[3], ')'
    print 'Pixel Size = (', geotransform[1], ',', geotransform[5], ')'

# the GeoTransform is the affine transformation that convert the raster grid (x,y) coordinate of dataset into lat/long
# if we want to do the opposite, we must invert the affine transformation
# the inverse in GDAL can be obtained:
success, tInverse = gdal.InvGeoTransform(geotransform)
if not success:
    print "No Inverse affine transfor exist for " + geotransform

# NOW the lat/long to dataset x,y coordinate conversion is done:
x, y = gdal.ApplyGeoTransform(tInverse, 7.5, 46.5)
print "Grid x,y Coordinate at center of tile: ", str(x) + ", " + str(y)
lx, ly = gdal.ApplyGeoTransform(geotransform,x,y)
print "Reversing to the original lat/long:" + str(lx) + ", " + str(ly)

t = gdal.ApplyGeoTransform(tInverse, 7.0, 46.0)
print "Yes...The ll corner of tile should be at (0.5,3600.5)" + str(t[0]) + ", " + str(t[1])

print "VERSION :" + gdal.VersionInfo()

band = dataset.GetRasterBand(1)

print 'Block size is ', band.GetBlockSize()
print 'X size is ', band.XSize
print 'Y size is ', band.YSize
print 'Data type is ', band.DataType


#fmt = "<" + ("h" * band.XSize)
fmt = "<" + str(band.XSize) + "H"

totHeight = 0
for y in range(band.YSize):
    #ReadRaster(xoff, yoff, xsize (#of cells to read), ysize (#of lines to read), buf_xsize, buf_ysize, buf_type (pixel-type)
    #buf_type is given by band.DataType (constant defining how many bytes per cell and how to interpret )
    scanline = band.ReadRaster(0, y, band.XSize, 1, band.XSize, 1, band.DataType )
    values = struct.unpack(fmt, scanline)

    print "Value 1: " + str(values[0]) + ", Value2000: " + str(values[1999])
    #for value in values:
    #    print "Elevation is" + str(value)



