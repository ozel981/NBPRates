import java.awt.*;

public class CheckListItem {

    static int r = 54;
    static int g = 54;
    static int b = 54;
    private final int colorRange = 160;
    private String label;
    private Color color;
    private boolean isSelected = false;

    public CheckListItem(String label) {
        CheckListItem.r += 43;
        if (CheckListItem.r > colorRange) {
            CheckListItem.r %= colorRange;
            CheckListItem.g += 55;
            if (CheckListItem.g > colorRange) {
                CheckListItem.g %= colorRange;
                CheckListItem.b += 67;
                CheckListItem.b %= colorRange;
            }
        }
        this.label = label;
        this.color = new Color((r % colorRange) + 54, (g % colorRange) + 54, (b % colorRange) + 54);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return label;
    }
}
