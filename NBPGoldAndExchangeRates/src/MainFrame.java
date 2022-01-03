import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {
    MainFrame(String title) {
        super(title);
        initFrame();
    }

    private int count = 0;
    private JLabel label;
    private JPanel mainPanel;

    private void initFrame() {
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.BLUE);

        initCanvas();
        initExchangesList();
        initData();
        initButton();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setVisible(true);

        this.add(mainPanel, BorderLayout.CENTER);
    }

    private void initCanvas() {
        GridBagConstraints c = new GridBagConstraints();
        JPanel panel = new JPanel();
        panel.setBackground(Color.magenta);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(panel, c);
    }

    private void initData() {
        GridBagConstraints c = new GridBagConstraints();
        JPanel panel = new JPanel();
        panel.setBackground(Color.yellow);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 0.25;
        c.gridx = 0;
        c.gridy = 6;
        mainPanel.add(panel, c);
    }

    private void initExchangesList() {
        GridBagConstraints c = new GridBagConstraints();
        JPanel panel = new JPanel();
        panel.setBackground(Color.green);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.25;
        c.weighty = 1;
        c.gridx = 6;
        c.gridy = 0;
        mainPanel.add(panel, c);

        JList list = new JList(new CheckListItem[]{new CheckListItem("apple"),
                new CheckListItem("orange"), new CheckListItem("mango"),
                new CheckListItem("orange"), new CheckListItem("mango"),
                new CheckListItem("orange"), new CheckListItem("mango"),
                new CheckListItem("orange"), new CheckListItem("mango"),
                new CheckListItem("orange"), new CheckListItem("mango"),
                new CheckListItem("orange"), new CheckListItem("mango"),
                new CheckListItem("orange"), new CheckListItem("mango"),

                new CheckListItem("paw paw"), new CheckListItem("banana")});
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

        panel.add(listScroller);
    }

    private void initButton() {
        GridBagConstraints c = new GridBagConstraints();
        JPanel panel = new JPanel();
        panel.setBackground(Color.red);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.25;
        c.weighty = 0.25;
        c.gridx = 6;
        c.gridy = 6;
        mainPanel.add(panel, c);
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
