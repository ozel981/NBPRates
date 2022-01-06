import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class DrawingPanel extends JPanel {
    List<ExchangeData> exchangesRates;
    LocalDate start = LocalDate.now();
    LocalDate end = LocalDate.now();
    void draw(List<ExchangeData> exchangesRates,LocalDate start,LocalDate end) {
        this.start = start;
        this.end = end;
        this.exchangesRates = exchangesRates;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int days = (int)Duration.between(start,end).toDays();
        double interval = 1.0;
        if (this.getWidth() > days) {
            interval = this.getWidth() / days;
        } else {
            
        }
        Graphics2D graphics = (Graphics2D) g;
        graphics.setStroke(new BasicStroke(4));
        graphics.setColor(Color.pink);
        Point[] points = {
                new Point(0, this.getSize().height / 2),
                new Point(10, this.getSize().height / 5),
                new Point(20, this.getSize().height / 1.5),
                new Point(30, this.getSize().height / 8),
                new Point(40, this.getSize().height / 3),
                new Point(50, this.getSize().height / 2),
                new Point(60, this.getSize().height / 6),
                new Point(70, this.getSize().height / 4),
                new Point(80, this.getSize().height / 8),
                new Point(90, this.getSize().height / 9),
                new Point(100, this.getSize().height / 10),
                new Point(110, this.getSize().height / 1.2),
                new Point(120, this.getSize().height / 2),
                new Point(130, this.getSize().height / 6)};
        for(int i=1;i<points.length;i++) {
            graphics.drawLine((int)points[i-1].x, (int)points[i-1].y, (int)points[i].x, (int)points[i].y);
        }

        graphics.drawRect(0, 0, this.getSize().width, this.getSize().height);
    }
}
