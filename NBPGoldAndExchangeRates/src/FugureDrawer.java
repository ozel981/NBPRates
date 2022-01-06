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

    public FugureDrawer(JTextField startDate, JTextField endDate, CheckListItem[] exchanges) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.exchanges = exchanges;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LocalDate start = LocalDate.parse(startDate.getText());
        LocalDate end = LocalDate.parse(endDate.getText());
        if (end.isAfter(LocalDate.now())) {
            end = LocalDate.now();
            endDate.setText(end.toString());
        }
        if (start.isAfter(end)) {
            start = end;
            startDate.setText(start.toString());
        }
        List<ExchangeData> exchangesRates = new ArrayList<ExchangeData>();
        for (CheckListItem exchange : exchanges) {
            if (exchange.isSelected()) {
                ExchangeData exchangeRate = new ExchangeData(exchange.toString());
                for (LocalDate date = start.plusYears(1); date.isBefore(end); date = date.plusYears(1)) {
                    try {
                        exchangeRate.rates.addAll(repository.getExchangeRate(exchange.toString(), date.minusYears(1).toString(), date.toString()));
                    } catch (Exception excpetion) {
                        excpetion.printStackTrace();
                    }
                }
                exchangesRates.add(exchangeRate);
            }
        }
    }
}
