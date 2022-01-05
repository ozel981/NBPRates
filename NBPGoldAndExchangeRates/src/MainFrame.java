import app.repository.Repository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {
    MainFrame(String title) {
        super(title);
        initFrame();
    }

    private double weightxy = 0.2;
    private int count = 0;
    private JLabel label;
    private JPanel mainPanel;
    private Repository repository = new Repository("http://api.nbp.pl/api");

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

        JTextField startDate = new JTextField(10);
        JTextField endDate = new JTextField(10);
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

        JList list = null;
        try {
            list = new JList(repository.getExchanges().stream().map((exchange -> new CheckListItem(exchange))).toArray());
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
            try {
                repository.getExchanges();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        panel.add(button, gbc);
    }
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
