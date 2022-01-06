import java.util.ArrayList;
import java.util.List;

public class ExchangeData {
    ExchangeData(String code) {
        this.code = code;
        rates = new ArrayList<Double>();
    }

    public String code;
    public List<Double> rates;
}
