import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeData {
    ExchangeData(String code,Color color) {
        this.code = code;
        this.color = color;
        rates = new ArrayList<Double>();
    }

    public Color color;
    public String code;
    public List<Double> rates;
}
