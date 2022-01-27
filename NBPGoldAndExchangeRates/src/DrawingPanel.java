import app.repository.Rate;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawingPanel extends JPanel {
    private final int margin = 30;
    List<ExchangeData> exchangesRates;
    LocalDate start;
    LocalDate end;

    public DrawingPanel() {
        start = LocalDate.now().minusDays(1);
        end = LocalDate.now();
        exchangesRates = new ArrayList<ExchangeData>();
    }

    private LocalDate getDate(int x) {
        int days = (int) Duration.between(start.atStartOfDay(), end.atStartOfDay()).toDays();
        return start.plusDays((long) (((double) days / (double) getDrawWidth()) * (double) x));
    }

    private int getDrawWidth() {
        int width = this.getWidth();
        return width - margin;
    }

    private int getDrawHeight() {
        int height = this.getHeight();
        return height - margin;
    }

    void draw(List<ExchangeData> exchangesRates, LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
        this.exchangesRates = exchangesRates;
        repaint();
    }

    private double getMaxVal(List<Rate> values) {
        double max = values.get(0).getValue();
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i).getValue() > max) {
                max = values.get(i).getValue();
            }
        }
        return max;
    }

    private double getMinVal(List<Rate> values) {
        double min = values.get(0).getValue();
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i).getValue() < min) {
                min = values.get(i).getValue();
            }
        }
        return min;
    }

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int days = 0;
        try {
            days = (int) Duration.between(start.atStartOfDay(), end.atStartOfDay()).toDays();
        } catch (Exception exception) {
            System.out.print(exception);
        }
        double windowWidth = this.getDrawWidth();
        double windowHeight = this.getDrawHeight();
        if (days < 2 || exchangesRates.isEmpty()) return;
        Map<String, List<Point>> exchanges = new HashMap<String, List<Point>>();
        double maxVal = exchangesRates.get(0).rates.get(0).getValue();
        double minVal = exchangesRates.get(0).rates.get(0).getValue();
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

        int exIndex;
        double yScale = (windowHeight - 20) / (maxVal - minVal);

        for (ExchangeData exchangeRates : exchangesRates) {
            exIndex = 1;
            exchanges.get(exchangeRates.code)
                    .add(new Point(0, windowHeight - (yScale * (exchangeRates.rates.get(0).getValue() - minVal) + 10)));
            double avgVal = exchangeRates.rates.get(exIndex).getValue();
            int count = 1;
            for (int i = 1; i < windowWidth; i++) {
                LocalDate date = getDate(i);
                Rate exchangeRate = exchangeRates.rates.get(exIndex);
                LocalDate exchangeDate = exchangeRate.getDate();
                if (exchangeDate.isBefore(date)) {
                    exIndex++;
                    avgVal /= count;
                    exchanges.get(exchangeRates.code)
                            .add(new Point(i - 1, windowHeight - (yScale * (avgVal - minVal) + 10)));
                    avgVal = 0;
                    count = 0;
                    Rate localRate = exchangeRates.rates.get(exIndex);
                    while (exchangeRates.rates.size() > exIndex && localRate.getDate().isBefore(date)) {
                        localRate = exchangeRates.rates.get(exIndex++);
                        avgVal += exchangeRate.getValue();
                        count++;
                    }
                    avgVal += exchangeRate.getValue();
                    count++;
                } else {
                    avgVal += exchangeRate.getValue();
                    count++;
                }

            }
            exchanges.get(exchangeRates.code)
                    .add(new Point(windowWidth, windowHeight - (yScale * (exchangeRates.rates.get(exchangeRates.rates.size() - 1).getValue() - minVal) + 10)));
        }

        drawPlot((Graphics2D) g, exchangesRates, exchanges, windowWidth, windowHeight, minVal, maxVal);
    }

    private void drawPlot(Graphics2D graphics, List<ExchangeData> exchangesRates, Map<String, List<Point>> exchanges, double windowWidth, double windowHeight, double minVal, double maxVal) {
        graphics.setColor(Color.gray);
        for (int i = 0; i < windowHeight; i += 10) {
            graphics.drawLine(margin, i, (int) windowWidth + margin, i);
        }
        for (int i = margin; i < windowWidth + margin; i += 10) {

        }

        for (int i = margin; i < windowWidth + margin; i += 10) {
            if ((i - margin - 30) % 90 == 0) {
                graphics.setColor(new Color(255, 150, 150));
            } else {
                graphics.setColor(Color.gray);
            }
            graphics.drawLine(i, (int) windowHeight, i, 0);
        }

        graphics.setStroke(new BasicStroke(2));
        graphics.setColor(Color.black);

        for (ExchangeData exchangeRates : exchangesRates) {
            List<Point> points = exchanges.get(exchangeRates.code);
            graphics.setColor(exchangeRates.color);
            for (int i = 1; i < points.size(); i++) {
                graphics.drawLine((int) points.get(i - 1).x + margin, (int) points.get(i - 1).y, (int) points.get(i).x + margin, (int) points.get(i).y);
            }
        }
        graphics.setColor(Color.black);
        graphics.setBackground(Color.white);
        graphics.drawString(df.format(maxVal), 3, 10);
        graphics.drawString(df.format(minVal), 3, (int) (windowHeight - 10));
        double midScal = (maxVal - minVal) / ((windowHeight - 20) / 20);
        double midVal = maxVal - midScal;
        for (int i = 30; i < windowHeight - 30; i += 20) {
            graphics.drawString(df.format(midVal), 3, i);
            midVal -= midScal;
        }

        for (int i = margin; i < windowWidth - margin - 10; i += 90) {
            graphics.drawString(getDate(i - margin + 30).toString(), i, (int) (windowHeight + margin / 2));
        }

        for (int i = 0; i < windowHeight; i += 10) {
            graphics.drawLine(margin, i, margin + 2, i);
        }

        for (int i = margin; i < windowWidth + margin; i += 10) {
            if ((i - margin - 30) % 90 == 0) {
                graphics.setColor(Color.red);
            } else {
                graphics.setColor(Color.black);
            }
            graphics.drawLine(i, (int) windowHeight - 2, i, (int) windowHeight);
        }
    }
}
