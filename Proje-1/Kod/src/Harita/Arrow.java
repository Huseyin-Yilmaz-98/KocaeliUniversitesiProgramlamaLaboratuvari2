package Harita;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class Arrow extends JPanel {
    double egim;
    float x1, x2, y1, y2;
    Path2D.Double ok;

    Arrow(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        ok = okYarat();
        setBounds(0, 41, 1200, 613);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3));
        //Egim tanjant kullanilarak hesaplanir
        egim = Math.atan2(y2 - y1, x2 - x1);
        AffineTransform at = AffineTransform.getTranslateInstance((int) ((x1 + x2) / 2), (int) ((y1 + y2) / 2));
        at.rotate(egim);
        at.scale(1.9, 2.5);
        Shape shape = at.createTransformedShape(ok);
        g2.setPaint(Color.BLUE);
        g2.draw(shape);

    }

    private Path2D.Double okYarat() {
        double uzunluk = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) / 2;
        int barb = 8;
        double aci = Math.toRadians(13);
        Path2D.Double path = new Path2D.Double();
        path.moveTo(-uzunluk / 2, 0);
        path.lineTo(uzunluk / 2, 0);
        double x = uzunluk / 2 - barb * Math.cos(aci);
        double y = barb * Math.sin(aci);
        path.lineTo(x, y);
        x = uzunluk / 2 - barb * Math.cos(-aci);
        y = barb * Math.sin(-aci);
        path.moveTo(uzunluk / 2, 0);
        path.lineTo(x, y);
        return path;
    }
}


