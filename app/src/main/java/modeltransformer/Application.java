package modeltransformer;

import modeltransformer.rotation.Angle;
import modeltransformer.transformer.TriangleTransformer;

public class Application {
    private static final String EIFFEL_JSON = "input/eiffel.json";
    private static final String EIFFEL_HTML = "output/eiffel.html";
    private static final Angle EIFFEL_ANGLE = new Angle(1.8, -0.5, 0);
    private static final String TETRAHEDRON_JSON = "input/tetrahedron.json";
    private static final String TETRAHEDRON_HTML = "output/tetrahedron.html";
    private static final Angle TETRA_ANGLE = new Angle(-0.5, 1, 0.5);

    public static void main(String[] args) {
        Application application = new Application();
        application.transform();
    }

    private void transform() {
        TriangleTransformer triangleTransformer = new TriangleTransformer();
        triangleTransformer.transform(TETRAHEDRON_JSON, TETRAHEDRON_HTML, TETRA_ANGLE);
        //triangleTransformer.transform(EIFFEL_JSON, EIFFEL_HTML, EIFFEL_ANGLE);
    }
}
