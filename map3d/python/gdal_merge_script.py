#!/bin/bash 
# script t illustrate the usage of merge: can merge and clip in one step!!
p='/Library/Frameworks/GDAL.framework/Versions/1.11/Programs'
f='/Users/mouellet/dev/map3d/dem_sample/alps_hgt'
python $p/gdal_merge.py -o merge_hgt.tif $f/N46E007.hgt $f/N46E008.hgt

