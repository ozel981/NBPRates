import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawingPanel extends JPanel {
    List<ExchangeData> exchangesRates;
    LocalDate start;
    LocalDate end;

    DrawingPanel() {
        start = LocalDate.now().minusDays(1);
        end = LocalDate.now();
        exchangesRates = new ArrayList<ExchangeData>();
    }

    void draw(List<ExchangeData> exchangesRates, LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
        this.exchangesRates = exchangesRates;
        repaint();
    }

    private double getMaxVal(List<Double> values) {
        double max = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) > max) {
                max = values.get(i);
            }
        }
        return max;
    }

    private double getMinVal(List<Double> values) {
        double min = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) < min) {
                min = values.get(i);
            }
        }
        return min;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int days = 0;
        try {
            days = (int) Duration.between(start.atStartOfDay(), end.atStartOfDay()).toDays();
        } catch (Exception exception) {
            System.out.print(exception);
        }
        double windowWidth = this.getWidth();
        double windowHeight = this.getHeight();
        if (days < 2 || exchangesRates.isEmpty()) return;
        Map<String, List<Point>> exchanges = new HashMap<String, List<Point>>();
        days = exchangesRates.get(0).rates.size();
        double maxVal = exchangesRates.get(0).rates.get(0);
        double minVal = exchangesRates.get(0).rates.get(0);
        for (ExchangeData exchangeRates : exchangesRates) {
            exchanges.put(exchangeRates.code, new ArrayList<Point>());
            double actualMax = getMaxVal(exchangeRates.rates);
            double actualMin = getMinVal(exchangeRates.rates);
            if (actualMax > maxVal) {
                maxVal = actualMax;
            }
            if (actualMin < minVal) {
                minVal = actualMin;
            }
        }

        if (maxVal - minVal < 0.001) return;
        double yScale = (windowHeight - 20) / (maxVal - minVal);

        if (windowWidth >= days) {
            double xInterval = windowWidth / (days - 1);
            double xIndex = 0;
            try {
                for (int i = 0; i < exchangesRates.get(0).rates.size(); i++) {
                    for (ExchangeData exchangeRates : exchangesRates) {
                        exchanges.get(exchangeRates.code)
                                .add(new Point(Math.ceil(xIndex), windowHeight - (yScale * (exchangeRates.rates.get(i) - minVal) + 10)));
                    }
                    xIndex += xInterval;
                }
            } catch (Exception exception) {
                System.out.print(exception);
            }
        } else {
            try {
                double xScale = (days - 2) / (windowWidth - 1);
                double xIndex = 0;
                int xPrev = 0;
                int xActual = 0;
                for (int i = 0; i < windowWidth; i++) {
                    for (ExchangeData exchangeRates : exchangesRates) {
                        double avgVal = 0;
                        for (int j = xPrev; j <= xActual; j++) {
                            if (j < exchangeRates.rates.size())
                                avgVal += exchangeRates.rates.get(j);
                            else {
                                xActual--;
                            }
                        }
                        if (xActual != xPrev) {
                            avgVal /= (xActual - xPrev + 1);
                            exchanges.get(exchangeRates.code)
                                    .add(new Point(i, windowHeight - (yScale * (avgVal - minVal) + 10)));
                        }
                    }
                    xPrev = xActual + 1;
                    xIndex += xScale;
                    xActual = (int) Math.ceil(xIndex);
                }
            } catch (Exception exception) {
                System.out.print(exception);
            }
        }
        Graphics2D graphics = (Graphics2D) g;
        graphics.setStroke(new BasicStroke(2));
        graphics.setColor(Color.black);

        for (ExchangeData exchangeRates : exchangesRates) {
            List<Point> points = exchanges.get(exchangeRates.code);
            graphics.setColor(exchangeRates.color);
            for (int i = 1; i < points.size(); i++) {
                graphics.drawLine((int) points.get(i - 1).x, (int) points.get(i - 1).y, (int) points.get(i).x, (int) points.get(i).y);
            }
        }
    }
}
