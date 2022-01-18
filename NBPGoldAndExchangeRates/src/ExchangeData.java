import app.repository.Rate;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeData {
    ExchangeData(String code,Color color) {
        this.code = code;
        this.color = color;
        rates = new ArrayList<Rate>();
    }

    public Color color;
    public String code;
    public List<Rate> rates;
}
