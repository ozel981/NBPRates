import app.repository.Repository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    MainFrame(String title) {
        super(title);
        initFrame();
    }

    private final double weightxy = 0.2;
    private JPanel mainPanel;
    private Repository repository = new Repository("http://api.nbp.pl/api");
    private JList list = null;
    private CheckListItem[] checkList;
    private JTextField startDate;
    private JTextField endDate;

    private void initFrame() {
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.BLUE);

        initCanvas();
        initExchangesList();
        initDateRange();
        initButton();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setVisible(true);

        this.add(mainPanel, BorderLayout.CENTER);
    }

    private void initCanvas() {
        GridBagConstraints c = new GridBagConstraints();
        DrawingPanel panel = new DrawingPanel();
        panel.setBackground(Color.magenta);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(panel, c);


    }

    private void initDateRange() {
        GridBagConstraints c = new GridBagConstraints();
        JPanel panel = new JPanel();
        panel.setBackground(Color.yellow);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = weightxy;
        c.gridx = 0;
        c.gridy = 6;
        mainPanel.add(panel, c);
        startDate = new JTextField(10);
        endDate = new JTextField(10);
        panel.add(startDate);
        panel.add(endDate);

    }

    private void initExchangesList() {
        GridBagConstraints c = new GridBagConstraints();
        JPanel panel = new JPanel();
        panel.setBackground(Color.green);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = weightxy;
        c.weighty = 1;
        c.gridx = 6;
        c.gridy = 0;
        mainPanel.add(panel, c);

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));


        try {
            List<CheckListItem> itemList = new ArrayList<CheckListItem>();
            for (String exchange : repository.getExchanges()) {
                itemList.add(new CheckListItem(exchange));
            }
            checkList = itemList.toArray(new CheckListItem[0]);
            list = new JList(checkList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        list.setCellRenderer(new CheckListRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(10);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                JList list = (JList) event.getSource();
                int index = list.locationToIndex(event.getPoint());// Get index of item
                // clicked
                CheckListItem item = (CheckListItem) list.getModel()
                        .getElementAt(index);
                item.setSelected(!item.isSelected()); // Toggle selected state
                list.repaint(list.getCellBounds(index, index));// Repaint cell
            }
        });


        JScrollPane listScroller = new JScrollPane(list);

        panel.add(Box.createRigidArea(new Dimension(0, 0)));
        panel.add(listScroller);
    }

    private void initButton() {
        GridBagConstraints c = new GridBagConstraints();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.red);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = weightxy;
        c.weighty = weightxy;
        c.gridx = 6;
        c.gridy = 6;
        mainPanel.add(panel, c);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        JButton button = new JButton("Pobierz");
        button.addActionListener((s) -> {
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
            List<ExchangeData> exchanges = new ArrayList<ExchangeData>();
            for (CheckListItem checkElem : checkList) {
                if (checkElem.isSelected()) {
                    ExchangeData exchange = new ExchangeData(checkElem.toString());
                    start.plusYears(1);
                    for (LocalDate date = start; date.isBefore(end); date = date.plusYears(1)) {
                        try {
                            LocalDate startt = date;
                            startt.minusYears(1);
                            exchange.rates.addAll(repository.getExchangeRate(checkElem.toString(), startt.toString(), date.toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    exchanges.add(exchange);
                }
            }
            System.out.println(exchanges);
        });
        panel.add(button, gbc);
    }
}

class ExchangeData {
    ExchangeData(String code) {
        this.code = code;
        rates = new ArrayList<Double>();
    }

    public String code;
    public List<Double> rates;
}

class CheckListItem {

    private String label;
    private boolean isSelected = false;

    public CheckListItem(String label) {
        this.label = label;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return label;
    }
}

class CheckListRenderer extends JCheckBox implements ListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean hasFocus) {
        setEnabled(list.isEnabled());
        setSelected(((CheckListItem) value).isSelected());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setText(value.toString());
        return this;
    }
}
