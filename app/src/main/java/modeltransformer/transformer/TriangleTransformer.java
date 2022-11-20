package modeltransformer.transformer;

import jakarta.json.Json;
import jakarta.json.stream.JsonParser;
import modeltransformer.rotation.Angle;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TriangleTransformer {
    private Point point;
    private Triangle triangle;
    private List<Point> points = new ArrayList<>();
    private List<Triangle> triangles = new ArrayList<>();

    public void transform(String inputFilePath, String outputFilePath, Angle angle) {
        File file = new File(inputFilePath);
        List<Integer> list = new ArrayList<>();
        int counter = 0;
        try {
            FileReader fileReader = new FileReader(file);
            try (JsonParser jsonParser = Json.createParser(fileReader)){
                while (jsonParser.hasNext()) {
                    JsonParser.Event event = jsonParser.next();
                    if (event == JsonParser.Event.VALUE_NUMBER) {
                        list.add(jsonParser.getInt());
                        counter++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (counter % 3 == 0) {
            for (int i = 0; i < list.size(); i++) {
                point = new Point();
                point.setX(list.get(i++));
                point.setY(list.get(i++));
                point.setZ(list.get(i));
                points.add(point);
            }

            points.forEach(tr -> rotateMatrix(tr, angle));

            double xMin = points.stream().mapToDouble(Point::getX).min().orElse(Double.MAX_VALUE);
            if (xMin < 0)
                for (Point tr : points)
                    tr.setX(tr.getX() + Math.abs(xMin));

            double yMin = points.stream().mapToDouble(Point::getY).min().orElse(Double.MAX_VALUE);
            if (xMin < 0)
                for (Point tr : points)
                    tr.setY(tr.getY() + Math.abs(yMin));

            for (int i = 0; i < points.size(); i++) {
                triangle = new Triangle();
                double zAverage = 0;
                zAverage += points.get(i).getZ();
                triangle.setPoint1(points.get(i++));
                zAverage += points.get(i).getZ();
                triangle.setPoint2(points.get(i++));
                zAverage += points.get(i).getZ();
                triangle.setPoint3(points.get(i));
                triangle.setzAverage(zAverage / 3);
                triangles.add(triangle);
            }
            triangles = triangles.stream().sorted(Comparator.comparing(Triangle::getzAverage).reversed()).collect(Collectors.toList());
            createDocument(triangles, outputFilePath);
        }
    }

    public static void rotateMatrix(Point point, Angle angle) {
        double[][] matrixA = {{point.getX()},{point.getY()},{point.getZ()}};
        double[][] xMatrix = {{1,0,0},
                           {0,Math.cos(angle.getAngleX()),-Math.sin(angle.getAngleX())},
                           {0,Math.sin(angle.getAngleX()),Math.cos(angle.getAngleX())}};
        double[][] xAuxMatrix = new double[3][1];

        for(int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                xAuxMatrix[i][0] += xMatrix[i][j] * matrixA[j][0];

        double[][] yMatrix = {{Math.cos(angle.getAngleY()),0,Math.sin(angle.getAngleY())},
                              {0,1,0},
                              {-Math.sin(angle.getAngleY()),0,Math.cos(angle.getAngleY())}};
        double[][] yAuxMatrix = new double[3][1];

        for(int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                yAuxMatrix[i][0] += yMatrix[i][j] * xAuxMatrix[j][0];

        double[][] zMatrix = {{Math.cos(angle.getAngleZ()),-Math.sin(angle.getAngleZ()),0},
                              {Math.sin(angle.getAngleZ()),Math.cos(angle.getAngleZ()),0},
                              {0,0,1}};
        double[][] zAuxMatrix = new double[3][1];

        for(int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                zAuxMatrix[i][0] += zMatrix[i][j] * yAuxMatrix[j][0];

        point.setX(zAuxMatrix[0][0]);
        point.setY(zAuxMatrix[1][0]);
        point.setZ(zAuxMatrix[2][0]);
    }

    public static void createDocument(List<Triangle> triangles, String filename) {
        try {
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(filename));

            writer.writeStartDocument();
            writer.writeStartElement("html");
            writer.writeStartElement("body");
            writer.writeStartElement("svg ");
            writer.writeAttribute("width","1000");
            writer.writeAttribute("height","1000");
            for (Triangle tr : triangles) {
                writer.writeStartElement("polygon");
                String p1x = Double.toString(tr.getPoint1().getX());
                String p1y = Double.toString(tr.getPoint1().getY());
                String p2x = Double.toString(tr.getPoint2().getX());
                String p2y = Double.toString(tr.getPoint2().getY());
                String p3x = Double.toString(tr.getPoint3().getX());
                String p3y = Double.toString(tr.getPoint3().getY());
                writer.writeAttribute("points", p1x + "," + p1y + " " + p2x + "," + p2y + " " +p3x + "," + p3y);
                writer.writeAttribute("style", "fill:white;stroke:black;stroke-width:0.1");
                writer.writeEndElement();
            }
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer nr = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
