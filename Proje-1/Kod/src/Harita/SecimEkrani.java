package Harita;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecimEkrani extends JFrame {
    Main obje;
    JPanel secileceklerPanel;
    DefaultListModel secileceklerList = new DefaultListModel();
    JList secileceklerKutusu;
    JPanel secilmislerPanel;
    DefaultListModel secilmislerList = new DefaultListModel();
    JList secilmislerKutusu;
    ActionListener tamamDugmesiListener;
    ActionListener eklemeDugmesiListener;
    ActionListener silmeDugmesiListener;
    JLabel bildirim = new JLabel();

    SecimEkrani(Main obje) {
        this.obje = obje;
        listenerlariTanimla();

        for (int i = 1; i < obje.graf.sehirAdlari.length; i++) {
            secileceklerList.addElement(obje.graf.sehirAdlari[i]);
        }

        secileceklerKutusu = new JList(secileceklerList);
        secileceklerKutusu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        secilmislerKutusu = new JList(secilmislerList);
        secilmislerKutusu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        secileceklerPanel = panelOlustur(20, 60, 191, 351, scrollOlustur(secileceklerKutusu, 0, 0, 191, 351));
        secilmislerPanel = panelOlustur(310, 60, 191, 351, scrollOlustur(secilmislerKutusu, 0, 0, 191, 351));

        setSize(535, 563);
        setLocationRelativeTo(null);
        setLayout(null);
        add(secilmislerPanel);
        add(secileceklerPanel);

        add(dugmeOlustur("+", 240, 130, 41, 41, eklemeDugmesiListener));
        add(dugmeOlustur("-", 240, 280, 41, 41, silmeDugmesiListener));
        add(dugmeOlustur("Tamam", 210, 470, 91, 31, tamamDugmesiListener));

        bildirim.setBounds(0, 420, 510, 32);
        bildirim.setHorizontalAlignment(SwingConstants.CENTER);
        bildirim.setVerticalAlignment(JLabel.CENTER);

        JLabel secilmemisLabel = new JLabel("Eklenecek Şehirler");
        secilmemisLabel.setBounds(20, 40, 191, 20);
        secilmemisLabel.setHorizontalAlignment(SwingConstants.CENTER);
        secilmemisLabel.setVerticalAlignment(JLabel.CENTER);
        add(secilmemisLabel);

        JLabel secilmisLabel = new JLabel("Teslimat Şehirleri");
        secilmisLabel.setBounds(310, 40, 191, 20);
        secilmisLabel.setHorizontalAlignment(SwingConstants.CENTER);
        secilmisLabel.setVerticalAlignment(JLabel.CENTER);

        add(secilmisLabel);
        add(bildirim);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Şehir Seç");
    }

    //verilen koordinatlarla bir JPanel objesi olusturur
    public JPanel panelOlustur(int x, int y, int width, int height, JScrollPane scroll) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(x, y, width, height);
        panel.add(scroll);
        return panel;
    }

    //Verilen koordinatlar ve listenerla bir buton olusturur
    public JButton dugmeOlustur(String text, int x, int y, int width, int height, ActionListener listener) {
        JButton buton = new JButton(text);
        buton.setBounds(x, y, width, height);
        buton.addActionListener(listener);
        return buton;
    }

    //Scroll objesi olusturur
    public JScrollPane scrollOlustur(JList eklenecegiListe, int x, int y, int width, int height) {
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(eklenecegiListe);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(x, y, width, height);
        return scroll;
    }

    //tum listenerlarin tanimlandigi fonksiyon
    public void listenerlariTanimla() {
        //tamam dugmesine tiklandiginda main objesindeki baslatma fonksiyonunu cagirir
        tamamDugmesiListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (secilmislerList.size() == 0) {
                    bildirim.setText("Şehir seçmediniz");
                    return;
                }
                try {
                    obje.baslatma(secilmislerList);
                } catch (Exception t) {
                    System.out.println("Program calistirilamadi");
                }
                dispose(); //Pencereyi yok etmek için.
            }
        };
        //ekleme dugmesi tiklandiginda secilen sehri listeye ekler
        eklemeDugmesiListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (secilmislerList.size() >= 10) {
                    bildirim.setText("10'dan fazla şehir seçilemez.");
                    return;
                }
                if (secileceklerKutusu.getSelectedValue() == null) {
                    bildirim.setText("Seçim yapmadınız.");
                    return;
                }
                bildirim.setText("");
                String secilmis = secileceklerKutusu.getSelectedValue().toString();
                int index = secileceklerList.indexOf(secilmis);
                secileceklerList.remove(index);
                secilmislerList.addElement(secilmis);
            }
        };
        //silme dugmesine tiklandiginda secilen sehri listeden siler
        silmeDugmesiListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (secilmislerList.size() == 0) {
                    bildirim.setText("Listede şehir yok.");
                    return;
                }
                if (secilmislerKutusu.getSelectedValue() == null) {
                    bildirim.setText("Şehir seçiniz.");
                    return;
                }
                bildirim.setText("");
                String secilmis = secilmislerKutusu.getSelectedValue().toString();
                int index = secilmislerList.indexOf(secilmis);
                secilmislerList.remove(index);
                secileceklerList.addElement(secilmis);
            }
        };
    }
}
