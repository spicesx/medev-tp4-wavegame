package jeudedame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Point2DTest {

    @Test
    public void testConstructeursEtGetters() {
        Point2D p1 = new Point2D(1, 2);
        assertEquals(1, p1.getX());
        assertEquals(2, p1.getY());

        Point2D p2 = new Point2D();
        assertEquals(0, p2.getX());
        assertEquals(0, p2.getY());

        Point2D p3 = new Point2D(p1);
        assertEquals(1, p3.getX());
        assertEquals(2, p3.getY());
    }

    @Test
    public void testSetters() {
        Point2D p = new Point2D(0, 0);
        p.setX(5);
        p.setY(10);
        assertEquals(5, p.getX());
        assertEquals(10, p.getY());
        
        p.setPosition(3, 4);
        assertEquals(3, p.getX());
        assertEquals(4, p.getY());
    }

    @Test
    public void testTranslate() {
        Point2D p = new Point2D(1, 1);
        p.translate(2, 3);
        assertEquals(3, p.getX());
        assertEquals(4, p.getY());
    }

    @Test
    public void testDistance() {
        Point2D p1 = new Point2D(0, 0);
        Point2D p2 = new Point2D(3, 4);
        assertEquals(5.0, p1.distance(p2), 0.0001);
        assertEquals(-1, p1.distance(null));
    }

    @Test
    public void testEqualsAndHashCode() {
        Point2D p1 = new Point2D(1, 1);
        Point2D p2 = new Point2D(1, 1);
        Point2D p3 = new Point2D(2, 2);

        assertEquals(p1, p1); // Same object
        assertEquals(p1, p2); // Same values
        assertNotEquals(p1, p3); // Different values
        assertNotEquals(p1, null);
        assertNotEquals(p1, "NotAPoint");
        
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testToString() {
        Point2D p = new Point2D(5, 7);
        assertEquals("[5;7]", p.toString());
    }
}
