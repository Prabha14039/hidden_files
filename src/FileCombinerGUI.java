import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class FileCombinerGUI extends JFrame {

    private JTextField outputFilePathField;
    private JTextArea outputArea;
    private JProgressBar progressBar;

    public FileCombinerGUI() {
        setTitle("File Combiner");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create UI elements
        JLabel outputLabel = new JLabel("Output File Path:");
        outputFilePathField = new JTextField(20);
        JButton browseButton = new JButton("Browse...");
        JButton combineButton = new JButton("Combine Files");
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Create progress bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);

        // Add ActionListener to the browse button
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectOutputFile();
            }
        });

        // Add ActionListener to the combine button
        combineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                combineFiles();
            }
        });

        // Layout setup
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(outputLabel, gbc);

        gbc.gridx = 1;
        add(outputFilePathField, gbc);

        gbc.gridx = 2;
        add(browseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(combineButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        add(progressBar, gbc);
    }

    private void selectOutputFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Output File");
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            outputFilePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void combineFiles() {
        String outputFilePath = outputFilePathField.getText();

        if (outputFilePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide the output file path.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Clear the output area
        outputArea.setText("");

        // Call the combine method with the correct parameters
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    FileCombiner.combineFiles(outputFilePath, outputArea, progressBar);
                } catch (IOException e) {
                    e.printStackTrace();
                    outputArea.append("Error during file combining.\n");
                }
                return null;
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(FileCombinerGUI.this, "File combining completed.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileCombinerGUI gui = new FileCombinerGUI();
            gui.setVisible(true);
        });
    }
}

