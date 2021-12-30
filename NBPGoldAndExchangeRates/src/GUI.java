import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.*;
import java.nio.file.*;

public class GUI implements ActionListener {

    private int count = 0;
    private JLabel label;
    private JFrame frame;
    private JPanel panel;

    public GUI() {
        frame = new JFrame();
        panel = new JPanel();
        JButton drawButton = new JButton("Pokaż kursy");
        drawButton.addActionListener((val) -> {
            try {
                get("http://api.nbp.pl/api/cenyzlota");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        label = new JLabel("Number of clicks: " + count);

        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(drawButton);
        panel.add(label);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Kursy walut i złota NBP");
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*count++;
        label.setText("Number of clicks: " + count);*/
        try {
            get("http://api.nbp.pl/api/cenyzlota");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void get(String uri) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Reponse" + response.body());
    }
}
