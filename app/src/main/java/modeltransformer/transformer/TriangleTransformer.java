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
    private final List<Point> points = new ArrayList<>();
    private List<Triangle> triangles = new ArrayList<>();

    public void transform(String inputFilePath, String outputFilePath, Angle angle) {
        List<Integer> listNumbers = new ArrayList<>();
        readFile(inputFilePath, listNumbers);

        for (int i = 0; i < listNumbers.size(); i = i + 3)
            createPoint(listNumbers, i);

        points.forEach(pt -> rotateMatrix(pt, angle));

        double xMin = points.stream().mapToDouble(Point::getX).min().orElse(Double.MAX_VALUE);
        if (xMin < 0)
            points.forEach(point -> point.setX(point.getX() + Math.abs(xMin)));

        double yMin = points.stream().mapToDouble(Point::getY).min().orElse(Double.MAX_VALUE);
        if (yMin < 0)
            points.forEach(point -> point.setY(point.getY() + Math.abs(yMin)));

        for (int i = 0; i < points.size(); i = i + 3)
            createTriangle(i);

        triangles = triangles.stream().sorted(Comparator.comparing(Triangle::getzAverage).reversed()).collect(Collectors.toList());
        createDocument(triangles, outputFilePath);
    }

    private static void readFile(String inputFilePath, List<Integer> listNumbers) {
        File file = new File(inputFilePath);
        try {
            FileReader fileReader = new FileReader(file);
            try (JsonParser jsonParser = Json.createParser(fileReader)) {
                while (jsonParser.hasNext()) {
                    JsonParser.Event event = jsonParser.next();
                    if (event == JsonParser.Event.VALUE_NUMBER) {
                        listNumbers.add(jsonParser.getInt());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createTriangle(int i) {
        int index = i;
        Triangle triangle = new Triangle();
        double zAverage = 0;
        zAverage += points.get(i).getZ();
        triangle.setPoint1(points.get(index++));
        zAverage += points.get(i).getZ();
        triangle.setPoint2(points.get(index++));
        zAverage += points.get(i).getZ();
        triangle.setPoint3(points.get(index));
        triangle.setzAverage(zAverage / 3);
        triangles.add(triangle);
    }

    private void createPoint(List<Integer> list, int i) {
        int index = i;
        Point point = new Point();
        point.setX(list.get(index++));
        point.setY(list.get(index++));
        point.setZ(list.get(index));
        points.add(point);
    }

    public static void rotateMatrix(Point point, Angle angle) {
        double[][] matrixA = {{point.getX()}, {point.getY()}, {point.getZ()}};
        double[][] xMatrix = {{1, 0, 0},
                {0, Math.cos(angle.angleX()), -Math.sin(angle.angleX())},
                {0, Math.sin(angle.angleX()), Math.cos(angle.angleX())}};
        double[][] xAuxMatrix = new double[3][1];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                xAuxMatrix[i][0] += xMatrix[i][j] * matrixA[j][0];

        double[][] yMatrix = {{Math.cos(angle.angleY()), 0, Math.sin(angle.angleY())},
                {0, 1, 0},
                {-Math.sin(angle.angleY()), 0, Math.cos(angle.angleY())}};
        double[][] yAuxMatrix = new double[3][1];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                yAuxMatrix[i][0] += yMatrix[i][j] * xAuxMatrix[j][0];

        double[][] zMatrix = {{Math.cos(angle.angleZ()), -Math.sin(angle.angleZ()), 0},
                {Math.sin(angle.angleZ()), Math.cos(angle.angleZ()), 0},
                {0, 0, 1}};
        double[][] zAuxMatrix = new double[3][1];

        for (int i = 0; i < 3; i++)
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
            writer.writeAttribute("width", "1000");
            writer.writeAttribute("height", "1000");
            for (Triangle tr : triangles) {
                writer.writeStartElement("polygon");
                String p1x = Double.toString(tr.getPoint1().getX());
                String p1y = Double.toString(tr.getPoint1().getY());
                String p2x = Double.toString(tr.getPoint2().getX());
                String p2y = Double.toString(tr.getPoint2().getY());
                String p3x = Double.toString(tr.getPoint3().getX());
                String p3y = Double.toString(tr.getPoint3().getY());
                writer.writeAttribute("points", p1x + "," + p1y + " " + p2x + "," + p2y + " " + p3x + "," + p3y);
                writer.writeAttribute("style", "fill:white;stroke:black;stroke-width:0.1");
                writer.writeEndElement();
            }
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
