import numpy
import random
import argparse
import math
import psycopg2
from polyhedron import Vrep, Hrep



def generatePolyhedron(points):
    """Generate Polyhedron associated with the convex-hull 
    of specified list of points (numpy array)  
    """
    v = Vrep(points)
    h = Hrep(v.A, v.b)
    #Check for possible "issues" (TODO: enquire about these)
    # 1) vertex having one or zero adjacent vertex
    # 2) facet have one or zero vertex 
    #
    for fidx in range(len(h.A)):
        if len(h.ininc[fidx])<3:
            raise ValueError("Found facet with 2 or less vertices!")
    for v in range(len(h.adj)):
        if len(h.adj[v])<2:
            raise ValueError("Found vertex with 1 or zero adjacents!")
    return h


def reorderFacetVertices(polyh, facetIdx):
    """ Get vertices ordered corretly for given facet of Polyhedron.
        Library 'cddlib' order vertices sequentially by vertices index.  WKT expects 
        the order to follow the "right-hand" rule (Normal of facet) and the polygon
        to be closed (first vertex repeated at last position) 
    """
    def innerCrossProduct(normal, midVertex, vertexB, vertexC):
        """ vector b (from mid to b) is correctly order when its cross-Product 
            with vector c is along Normal of facet: N-transpose(bXc) > 0, 
            otherwise it is vector c.
        """
        #vector b/c taken from midVertex to vertexB/C
        b = polyh.generators[vertexB] - polyh.generators[midVertex]
        c = polyh.generators[vertexC] - polyh.generators[midVertex]
        normalCross = normal[0]*(b[1]*c[2]-b[2]*c[1]) + \
                      normal[1]*(b[2]*c[0]-b[0]*c[2]) + \
                      normal[2]*(b[0]*c[1]-b[1]*c[0]) 
        if normalCross == 0:
            ValueError("innerCrossProduct cannot be 0") 
        return normalCross

    def findNextAdjacent(vertexPos):
        """Find vertex positioned next using the  "right-hand" rule
        """
        prevVertex = orderVertices[vertexPos-1]
        #first vertex to order, must find direction using crossProduct
        if vertexPos == 1:
            two = list(set(remainingVertices) & set(polyh.adj[prevVertex])) 
            if len(two) != 2:
                ValueError("Expected two adjacents for initial vertex, but got %d" % len(two))
            if innerCrossProduct(polyh.A[facetIdx], prevVertex, two[0], two[1]) > 0:
                return two[0]
            else:
                return two[1]
        else:
            one = list(set(remainingVertices) & set(polyh.adj[prevVertex]))
            if len(one) != 1:
                ValueError("Expected one adjacent vertex, but got %d" % len(one))
            return one[0]

    n_vertices = len(polyh.ininc[facetIdx])
    #remaining vertices (without first position vertex0) 
    remainingVertices = [polyh.ininc[facetIdx][i] for i in range(1,n_vertices)]
    #order list adds first vertex at end of list (close)
    orderVertices = [None]*(n_vertices+1)
    orderVertices[0] = polyh.ininc[facetIdx][0]
    orderVertices[n_vertices] = polyh.ininc[facetIdx][0]
    #order remaining vertices
    for pos in range(1,n_vertices):
        nextAdj = findNextAdjacent(pos)
        remainingVertices.remove(nextAdj)
        orderVertices[pos] = nextAdj
    return orderVertices         


def writeFacetAsWKT(polyh, facetIdx):
    wkt = "(("
    for v in reorderFacetVertices(polyh,facetIdx):
        x = str(polyh.generators[v][0]) + " "
        y = str(polyh.generators[v][1]) + " "
        z = str(polyh.generators[v][2])
        wkt += x + y + z + ", " 
    wkt = wkt[:-2] + "))"
    return wkt

def writeAllAsWKT(polyh):
    f = ""
    for i in range(len(polyh.A)):
        f += writeFacetAsWKT(polyh,i) + ",\n"
    return f[:-2]



sql = """ insert into polyh(name, geom) values(%s, ST_GeomFromEWKT(%s)) """

def loadTable(cur, params):
    #print "insert =  " + sql %(params[0],params[1])
    name = params[0]
    geomWkt = "SRID=0;POLYHEDRALSURFACE( " + writeAllAsWKT(params[1]) + " )"

    try:
        cur.execute(sql, (name,geomWkt))
    except Exception, msg:
        print("Error loading to database:\n%s" % msg)
        return 0
    return cur.rowcount
    

def get_args():
    parser = argparse.ArgumentParser()
    parser.add_argument("-ho", "--host", default="bbpdbsrv03")
    parser.add_argument("-p", "--port", default="5432")
    parser.add_argument("-d", "--db", default="pspat" )
    parser.add_argument("-u", "--user", default="pspat")
    return parser.parse_args()


def main():
    args = get_args()
    dbconn = None
    cur = None
    #TODO getpass.getpass to handle password (for now rely on .pgpass)
    try:
        dbconn = psycopg2.connect(host=args.host, database=args.db, user=args.user)
        cur = dbconn.cursor()
        #for now, simply auto-commit
        dbconn.set_isolation_level(0)
    except Exception, msg:
            print("Error connecting to database:\n%s" % msg)
    #call fcts to load data passing the cursor object
    #loadRandomPolyh_Db(cur, 10, 10 ,10)
    loadRandomPolyh_Db(cur, 0, 1, 0, off=(5000,5000,5000))
   
    #TODO: clean-up code for DB-related steps
    dbconn.commit()
    if cur != None:
        cur.close()
    if dbconn != None:
        dbconn.close()
    


def loadRandomPolyh_Db(cur, nsphere=1, nrect=1, ncloud=1, off=None):
    
    for i in range(nsphere+nrect+ncloud):
        geom = None
        if off == None:
            offset = (random.random()*10000, random.random()*10000, random.random()*10000)
        else:
            offset = off
        if 0 < i < nsphere:
            r = random.random()*100
            geom = sphere(10,r,offset)
        elif nsphere <= i < nrect+nsphere:
            size = (random.random()*10,random.random()*10,random.random()*10)
            size = (1000,1000,1000)
            geom = rect(offset, size, i%2 == 0)
        else:
            minMax = (1000,2000)
            n = 100
            geom = cloud(n,minMax)
        #for now skip when polyhedron has issues
        if geom[1] == None:
            continue
        
        #load to DB
        rec = loadTable(cur, geom)
        if rec == 1:
           print "Successfully loaded '" +geom[0]+"'"
        else:
            print "no record was loaded!"



#####convenient fcts to generate polyhdron out of points#############


#cloud of 3-D points within specified min-max range
def cloud(n, minMax):
    polyh = None
    pts =  numpy.random.uniform(minMax[0],minMax[1],(n,3))
    name = "Noshape from %d pts (mean=%.5f)" %(n, pts.mean())
    try:
        polyh = generatePolyhedron(pts)
    except ValueError, msg:
        print("Issues with the generated polyhedron: %s" %msg)
    return (name, polyh)

#points at arete of 3-D rectangle 
#or with added point on roof for house
def rect(off, size, house=False):
    x,y,z = float(off[0]), float(off[1]), float(off[2])
    dx,dy,dz = float(size[0]), float(size[1]), float(size[2])
    pts, name = None, None
    if not house:
        name = "Rect(%.3f,%.3f,%.3f) of size=(%.3f,%.3f,%.3f)" %(x,y,z,dx,dy,dz)
        pts = numpy.array([[x,y,z],[x+dx,y,z],[x,y+dy,z],[x,y,z+dz],
                        [x+dx,y+dy,z],[x+dx,y,z+dz],[x,y+dy,z+dz],[x+dx,y+dy,z+dz]])
    else:
        name = "House(%.3f,%.3f,%.3f) of size=(%.3f,%.3f,%.3f)" %(x,y,z,dx,dy,dz)
        pts = numpy.array([[x,y,z],[x+dx,y,z],[x,y+dy,z],[x,y,z+dz],[x+dx,y+dy,z],[x+dx,y,z+dz],
                        [x,y+dy,z+dz],[x+dx,y+dy,z+dz],[x+dx/2,y+dy/2,z+dz*2]])
    try:
        polyh = generatePolyhedron(pts)
    except ValueError, msg:
        print("Issues with the generated polyhedron: %s" %msg)
    return (name,polyh)

        
#n points located on the surface of the sphere with rayon=r.
def sphere(n,r, offset):
    pts = numpy.zeros(shape=(n,3))
    for i in range(n):
        a1 = random.random()*2*math.pi
        x = random.random()*(math.cos(a1)*r)
        y = random.random()*(math.sin(a1)*r)
        z = random.choice((-1,1)) * math.sqrt(r**2 - x**2 - y**2)
        pt = [x+offset[0],y+offset[1],z+offset[2]]
        pts[i] = pt
    name = "Sphere(%.3f,%.3f,%.3f) with radius=%.3f" %(offset[0],offset[1],offset[2],r)
    polyh = None
    try:
        polyh = generatePolyhedron(pts)
    except ValueError, msg:
        print("Issues with the generated polyhedron: %s" %msg)
    return (name, polyh)

       

###### convenient fct to generate simple WKT text form directly #########

def facet(startPt, size, plane):
    """ startPt is lowerLeft corner when looking toward facet
        plane defines which facet to generate: "z" (planeZ) ,"y", "-z" (opposite of planeZ)
    """
    def fmt(c0, c1, c2, c3):
        c0s = '' + str(c0[0]) + ' '  + str(c0[1]) + ' ' + str(c0[2])
        c1s = '' + str(c1[0]) + ' '  + str(c1[1]) + ' ' + str(c1[2])
        c2s = '' + str(c2[0]) + ' '  + str(c2[1]) + ' ' + str(c2[2])
        c3s = '' + str(c3[0]) + ' '  + str(c3[1]) + ' ' + str(c3[2])
        return '((' + c0s + ', '+ c1s + ', ' + c2s + ', ' + c3s + ', ' +c0s + '))'

    pt0 = startPt
    if plane == 'z':
        pt1 = (pt0[0]+size,pt0[1],pt0[2])
        pt2 = (pt0[0]+size,pt0[1]+size,pt0[2])
        pt3 = (pt0[0],pt0[1]+size,pt0[2])
    elif plane == '-z':
        pt1 = (pt0[0]-size,pt0[1],pt0[2])
        pt2 = (pt0[0]-size,pt0[1]+size,pt0[2])
        pt3 = (pt0[0],pt0[1]+size,pt0[2])
    elif plane == 'x':
        pt1 = (pt0[0],pt0[1],pt0[2]-size)
        pt2 = (pt0[0],pt0[1]+size,pt0[2]-size)
        pt3 = (pt0[0],pt0[1]+size,pt0[2])
    elif plane == '-x':
        pt1 = (pt0[0],pt0[1],pt0[2]+size)
        pt2 = (pt0[0],pt0[1]+size,pt0[2]+size)
        pt3 = (pt0[0],pt0[1]+size,pt0[2])
    elif plane == 'y':
        pt1 = (pt0[0]+size,pt0[1],pt0[2])
        pt2 = (pt0[0]+size,pt0[1],pt0[2]-size)
        pt3 = (pt0[0],pt0[1],pt0[2]-size)
    elif plane == '-y':
        pt1 = (pt0[0]+size,pt0[1],pt0[2])
        pt2 = (pt0[0]+size,pt0[1],pt0[2]+size)
        pt3 = (pt0[0],pt0[1],pt0[2]+size)
    return fmt(pt0,pt1,pt2,pt3)

        
def cubeAsEWKT(minC, maxC):
    """ Returns cube in EWKT format
        minC is tupple listing lowest coordinate of cube: (x-min,y-min,z-min)
       maxC is tupple listing hightest coordinate of cube: (x-max,y-max,z-max)
    """
    xmin, ymin, zmin = minC
    xmax, ymax, zmax = maxC
    size = xmax - xmin
    facets = []
    facets.append(facet((xmin,ymin,zmax),size,"z"))
    facets.append(facet((xmax,ymin,zmin),size,"-z"))
    facets.append(facet((xmax,ymin,zmax),size,"x"))
    facets.append(facet((xmin,ymin,zmin),size,"-x"))
    facets.append(facet((xmin,ymax,zmax),size,"y"))
    facets.append(facet((xmin,ymin,zmin),size,"-y"))
    return '\n'.join(facets)





if __name__ == "__main__":
    # main()
    pass


