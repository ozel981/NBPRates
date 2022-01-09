import app.repository.Repository;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FugureDrawer implements ActionListener {
    private final Repository repository = new Repository("http://api.nbp.pl/api");
    private JTextField startDate;
    private JTextField endDate;
    private CheckListItem[] exchanges;
    private DrawingPanel drawingPanel;

    public FugureDrawer(JTextField startDate, JTextField endDate, CheckListItem[] exchanges, DrawingPanel drawingPanel) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.exchanges = exchanges;
        this.drawingPanel = drawingPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LocalDate start = LocalDate.now().minusDays(10);
        LocalDate end = LocalDate.now();
        try {
            start = LocalDate.parse(startDate.getText());
        } catch (Exception exception) {
            startDate.setText("2013-01-01");
            start = LocalDate.parse(startDate.getText());
        }
        try {
            end = LocalDate.parse(endDate.getText());
        } catch (Exception exception) {
            endDate.setText(LocalDate.now().toString());
            end = LocalDate.now();
        }
        if (end.isAfter(LocalDate.now())) {
            end = LocalDate.now();
            endDate.setText(end.toString());
        }
        if (end.isBefore(LocalDate.of(2013,1,11))) {
            end = LocalDate.of(2013,1,11);
            endDate.setText(end.toString());
        }
        if (start.isBefore(LocalDate.of(2013,1,1))) {
            start = LocalDate.of(2013,1,1);
            startDate.setText("2013-01-01");
        }
        if (start.isAfter(end.minusDays(10))) {
            start = end.minusDays(10);
            startDate.setText(start.toString());
        }
        List<ExchangeData> exchangesRates = new ArrayList<ExchangeData>();
        for (CheckListItem exchange : exchanges) {
            if (exchange.isSelected()) {
                ExchangeData exchangeRate = new ExchangeData(exchange.toString(), exchange.getColor());
                LocalDate date = start.plusYears(1);
                for (; date.isBefore(end); date = date.plusYears(1)) {
                    try {
                        exchangeRate.rates.addAll(repository.getRates(exchange.toString(), date.minusYears(1).toString(), date.toString()));
                    } catch (Exception excpetion) {
                        excpetion.printStackTrace();
                    }
                }
                try {
                    exchangeRate.rates.addAll(repository.getRates(exchange.toString(), date.minusYears(1).toString(), end.toString()));
                } catch (Exception excpetion) {
                    excpetion.printStackTrace();
                }
                exchangesRates.add(exchangeRate);
            }
        }
        drawingPanel.draw(exchangesRates, start, end);
    }
}
