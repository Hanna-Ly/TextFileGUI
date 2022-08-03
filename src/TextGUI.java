import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextGUI extends JFrame{
    private JPanel pnlTop;
    private JPanel pnlMiddle;
    private JPanel pnlBottom;
    private JButton btnAdd;
    private JButton btnRead;
    private JTextField txtName;
    private JTextField txtAge;
    private JLabel lblName;
    private JLabel lblAge;
    private String filename = "Info.txt";
    private List<TextGUI> animalData;
    private String name;
    private int age;
    private int number;

    public TextGUI(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
    public TextGUI(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }

    private TextGUI() {

        //Akna loomine
        this.setTitle("TextFileGUI");
        this.setPreferredSize(new Dimension(500,180));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createTopPanel();
        createMiddlePanel();
        createBottomPanel();
        createMainPanel();

        //Vidinad
        dataLabels();
        createTextField();
        setupButton();

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    private void createTopPanel() {
        pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTop.setBackground(Color.lightGray);
    }
    private void createMiddlePanel() {
        pnlMiddle = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlMiddle.setBackground(Color.lightGray);
    }
    private void createBottomPanel() {
        pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBottom.setBackground(Color.lightGray);
    }
    private void createMainPanel() {
        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.lightGray);
        pnlMain.setBorder(new EmptyBorder(25,5,25,5));
        pnlMain.add(pnlTop, BorderLayout.NORTH);
        pnlMain.add(pnlMiddle, BorderLayout.CENTER);
        pnlMain.add(pnlBottom, BorderLayout.SOUTH);
        this.add(pnlMain);
    }
    private void clearFields() {
        txtName.setText(null);
        txtAge.setText(null);
    }
    private void setupButton() {
        btnAdd = new JButton("Lisa faili");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                addDataToFile();
                clearFields();
            }
        });
        btnRead = new JButton("Näita lisatud loomi");
        btnRead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Loe faili nuppu klikiti");
                try {
                    readFromFile();
                    JDialog dialog = new JDialog();
                    createAnimalTable(dialog);
                    dialog.setModal(true);
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);

                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            }
        });
        pnlBottom.add(btnAdd);
        pnlBottom.add(btnRead);
    }
    private void dataLabels() {
        lblName = new JLabel("Sisesta looma nimi (vähemalt 2 tähemärki): ");
        lblAge = new JLabel("Sisesta looma vanus (täisarv numbrina vahemikus 0-100): ");
        pnlTop.add(lblName);
        pnlMiddle.add(lblAge);
    }
    private void createTextField() {
        txtName = new JTextField("", 15);
        txtAge = new JTextField("", 3);
        pnlTop.add(txtName);
        pnlMiddle.add(txtAge);

    }
    private JTextField getTxtName() {return txtName;}
    private JTextField getTxtAge() {return txtAge;}
    private static boolean isInteger(String str) {
        if (str == null || str.isEmpty())
            return false;
        for (char c : str.toCharArray())
            if (c < '0' || c > '9')
                return false;
        return true;
    }
    private void addDataToFile() {
        String txtName = getTxtName().getText();
        String txtAge = getTxtAge().getText();
        if(isInteger(txtAge)) {
            number = Integer.parseInt(txtAge);
        } else {
            JOptionPane.showMessageDialog(this, "Looma vanust ei ole sisestatud");
            number = Integer.parseInt("0");
        }
        String defaultAge = "0";
        int length = txtName.length();
        if(txtName != null && !txtName.isEmpty() && !(length <= 1) && number <= 0 || number > 100) {
            JOptionPane.showMessageDialog(this, "Valesti märgitud vanus, vanuseks määratud \"0\"");
            writeToFile(txtName, defaultAge);
        } else if (txtName != null && !txtName.isEmpty() && !(length <= 1)) {
            writeToFile(txtName, txtAge);
            JOptionPane.showMessageDialog(this, "Andmed lisatud faili");
        } else {
            JOptionPane.showMessageDialog(this, "Looma nime ei ole sisestatud või on sisestatud vähem kui 2 tähemärki");
        }
    }
    private void writeToFile(String txtName, String txtAge) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("Info.txt", true))) {
            String line = txtName + ";" + txtAge;
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            //throw new RuntimeException(e);
            JOptionPane.showMessageDialog(this, "Loomade infoga faili ei ole");
        }
    }
    private boolean readFromFile() throws IOException {
        animalData = new ArrayList<>();
        File f = new File(filename);
        if(f.exists()) {
            if(f.length() > 0) {
                try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
                    for(String line; (line = br.readLine()) != null;) {
                        String name = line.split(";")[0];
                        int age = Integer.parseInt(line.split(";")[1]);
                        animalData.add(new TextGUI(name, age));
                    }
                    return true;
                }
            }
        } else {
            f.createNewFile();
            JOptionPane.showMessageDialog(this, "Loomade fail loodi");
        }
        return false;
    }
    private void createAnimalTable(JDialog dialog) {
        /* Tabeli päise veeru nimed */
        String[] columnNames = new String[] {"Jrk", "Looma Nimi", "Looma Vanus"};
        String[][] data = new String[animalData.size()][3];
        /* animalData'st lisa info data */
        for(int x = 0; x < animalData.size(); x++) {
            data[x][0] = String.valueOf(x+1);
            data[x][1] = animalData.get(x).getName();
            data[x][2] = String.valueOf(animalData.get(x).getAge());
        }
        JTable table = new JTable(data, columnNames);
        dialog.add(new JScrollPane(table));
        dialog.setTitle("Loomade andmed");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TextGUI();
            }
        });
    }
}


