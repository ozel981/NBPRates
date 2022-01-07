import java.awt.*;
import java.util.Random;

public class CheckListItem {

    private String label;
    private Color color;
    private boolean isSelected = false;

    public CheckListItem(String label) {
        Random random = new Random();
        this.label = label;
        this.color = Color.getHSBColor(random.nextFloat() % 0.8f + 0.1f, random.nextFloat() % 0.8f + 0.1f, random.nextFloat() % 0.8f + 0.1f);
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
