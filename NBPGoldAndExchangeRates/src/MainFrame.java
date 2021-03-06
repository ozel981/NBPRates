import app.repository.Repository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    public MainFrame(String title) {
        super(title);
        initFrame();
    }

    private final double weightxy = 0.2;
    private JPanel mainPanel;
    private DrawingPanel drawingPanel;
    private Repository repository = new Repository("http://api.nbp.pl/api");
    private JList list = null;
    private CheckListItem[] exchanges;
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
        this.setMinimumSize(new Dimension(400,400));
        this.setVisible(true);

        this.add(mainPanel, BorderLayout.CENTER);
    }

    private void initCanvas() {
        GridBagConstraints c = new GridBagConstraints();
        drawingPanel = new DrawingPanel();
        drawingPanel.setBackground(Color.white);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;

        mainPanel.add(drawingPanel, c);


    }

    private void initDateRange() {
        GridBagConstraints c = new GridBagConstraints();
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = weightxy;
        c.gridx = 0;
        c.gridy = 6;
        mainPanel.add(panel, c);
        startDate = new JTextField(10);
        endDate = new JTextField(10);
        panel.add(new JLabel("od: "));
        panel.add(startDate);
        panel.add(new JLabel("od: "));
        panel.add(endDate);
        panel.add(new JLabel("minimalna data 2013-01-01"));
        panel.add(new JLabel("             Cena z??ota za 0.01 g"));

    }

    private void initExchangesList() {
        GridBagConstraints c = new GridBagConstraints();
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = weightxy;
        c.weighty = 1;
        c.gridx = 6;
        c.gridy = 0;
        mainPanel.add(panel, c);

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));


        try {
            List<CheckListItem> itemList = new ArrayList<CheckListItem>();
            itemList.add(new CheckListItem("GOLD"));
            for (String exchange : repository.getExchanges()) {
                itemList.add(new CheckListItem(exchange));
            }
            exchanges = itemList.toArray(new CheckListItem[0]);
            list = new JList(exchanges);
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
        panel.setBackground(Color.white);
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
        button.addActionListener(new RatesGetter(startDate,endDate,exchanges,drawingPanel));
        panel.add(button, gbc);
    }
}

