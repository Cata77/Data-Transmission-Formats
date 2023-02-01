# Data Transmission Formats Java Project
Users can search the internet for free 3D model data files of their interest (e.g. famous buildings). We would like to help them to view these model files
mapped to 2D. The idea is that an application reads the 3D json file and converts it to SVG.
SVG files can be viewed with a web browser

## Functionaliy
1. Application reads 3D model json file that contains triangles shapes.
2. Application rotates the triangles, maps them to 2D and moves them to the visible portion of the screen.
3. Write the triangles as polygons into an SVG file

## Sample Input
The input can be found in the ```input``` folder.

This is a tetrahedron shape (formatting can be strange; reason is to fit on one page):

```
[
  [
    {
      "x" : -100,
      "y" : 100,
      "z" : 0
    },
    {
      "x" : 100,
      "y" : 100,
      "z" : -100
    },
    {
      "x" : 0,
      "y" : -100,
      "z" : 0
    }
  ],
  [
    {
      "x" : 100,
      "y" : 100,
      "z" : -100
    },
    {
      "x" : 0,
      "y" : 100,
      "z" : 100
    },
    {
      "x" : 0,
      "y" : -100,
      "z" : 0
    }
  ],
  [
    {
      "x" : -100,
      "y" : 100,
      "z" : -100
    },
    {
      "x" : 0,
      "y" : 100,
      "z" : 100
    },
    {
      "x" : 0,
      "y" : -100,
      "z" : 0
    }
  ],
  [
    {
      "x" : -100,
      "y" : 100,
      "z" : -100
    },
    {
      "x" : 100,
      "y" : 100,
      "z" : -100
    },
    {
      "x" : 0,
      "y" : 100,
      "z" : 100
    }
  ]
]
```
The tetrahedron rotation angles on each of the axles are (values are given in radian):   

- X axis: -0.5

- Y axis: 1

- Z axis: 0.5

## Sample Output 
The output can be found in the ```output``` folder.

This is an output example:

```html
<?xml
version="1.0" ?>
<html>
  <body>
    <svg width="1000" height="1000">
      <polygon points="0.0,11.967310893242441 131.05818858679152,192.82522360138182 244.19152869049444,0.0" style="fill:white;stroke:black;stroke-width:0.1"></polygon>
      <polygon points="94.83197635580758,63.774255693227595 131.05818858679152,192.82522360138182
244.19152869049444,0.0" style="fill:white;stroke:black;stroke-width:0.1"></polygon>
      <polygon points="0.0,11.967310893242441 94.83197635580758,63.774255693227595 244.19152869049444,0.0" style="fill:white;stroke:black;stroke-width:0.1"></polygon>
      <polygon points="0.0,11.967310893242441 94.83197635580758,63.774255693227595
131.05818858679152,192.82522360138182" style="fill:white;stroke:black;stroke-width:0.1"></polygon>
    </svg>
  </body>
</html>
```

Result displayed on screen with a web browser:

<img width="191" alt="image" src="https://user-images.githubusercontent.com/63099068/216052242-fd9e7cff-a562-43fa-88f8-75e9a52ac6ae.png">

## Triangle Rotation
Rotate triangles by rotating their 3D points with this formula: rotation matrix * 3D point

  1. Example for X axis rotation:
    
  <img width="365" alt="image" src="https://user-images.githubusercontent.com/63099068/216052600-58810bff-ca04-4cfd-8ed3-5a39046e8b00.png">
  
  2. Matrix multiplication: https://en.wikipedia.org/wiki/Matrix_multiplication

### Rotation Matrices
- X axis rotation matrix:

<img width="280" alt="image" src="https://user-images.githubusercontent.com/63099068/216053794-be42d417-d38d-4560-a7b2-7f944903c53c.png">

- Y axis rotation matrix:

<img width="278" alt="image" src="https://user-images.githubusercontent.com/63099068/216053918-6d4d7f87-e922-4032-b04e-7c79f8631eea.png">

- Z axis rotation matrix:

<img width="286" alt="image" src="https://user-images.githubusercontent.com/63099068/216054017-7c3b8cf8-35bd-4c44-abae-7199ec49a555.png">


### Rotation example
The following example demonstrates the rotation of the first 3D point from the tetrahedron on the X, Y and then Z axis using the formulas and rotation
matrices given above.

Point coordinates:
- x = -100

- y = 100

- z = -100

The rotation angles (in radian) on each of the axles:

- X = -0.5
- Y = 1
- Z = 0.5

Step 1: Rotate the point on the X axis by -0.5 radian using the given formula (with detailed matrix multiplication):

<img width="427" alt="image" src="https://user-images.githubusercontent.com/63099068/216054633-5cc53ab1-3fb9-445a-8669-d48798211c1d.png">

Step 2: Rotating the resulting point on the Y axis by 1 radian:

<img width="359" alt="image" src="https://user-images.githubusercontent.com/63099068/216054730-50874806-10ea-468c-b141-5cdbdc40efa8.png">

Step 3: Finally rotating the point on the Z axis by 0.5 radian will result in the following:

<img width="347" alt="image" src="https://user-images.githubusercontent.com/63099068/216054800-666675e9-3197-4704-9558-4ba39723bfd6.png">

**Notes**: The demonstrated algorithm rotated only one point once on each of the three axles. This algorithm has to be applied on all points of each triangle.
This means that all three points of a triangle have to be rotated three times













