import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import java.io.IOException;

public class FileProcessorGUI extends JFrame {

    private JTextField inputFilePathField;
    private JTextField outputFilePathField;
    private JTextArea outputArea;

    public FileProcessorGUI() {
        setTitle("File Processor");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create UI elements
        JLabel inputLabel = new JLabel("Input File Path:");
        inputFilePathField = new JTextField(20);
        JButton browseButton = new JButton("Browse...");
        JButton processButton = new JButton("Process File");
        JLabel outputLabel = new JLabel("Output File Path:");
        outputFilePathField = new JTextField(20);
        JButton combineButton = new JButton("Combine Files");
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Add ActionListener to the browse button
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFile();
            }
        });

        // Add ActionListener to the process button
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processFile();
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
        add(inputLabel, gbc);

        gbc.gridx = 1;
        add(inputFilePathField, gbc);

        gbc.gridx = 2;
        add(browseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(processButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(outputLabel, gbc);

        gbc.gridx = 1;
        add(outputFilePathField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(combineButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        add(scrollPane, gbc);
    }

    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            inputFilePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void processFile() {
        String inputFilePath = inputFilePathField.getText();
        if (inputFilePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide the input file path.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Clear the output area
        outputArea.setText("");

        // Call the processFile method from Main class
        try {
            Main.processFile(inputFilePath, outputArea);
        } catch (IOException e) {
            e.printStackTrace();
            outputArea.append("Error during file processing.\n");
        }

        JOptionPane.showMessageDialog(this, "File processing completed.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void combineFiles() {
        String outputFilePath = outputFilePathField.getText();
        if (outputFilePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide the output file path.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Clear the output area
        outputArea.setText("");

        // Call the combineFiles method from Main class
        Main.combineFiles(outputFilePath, outputArea);

        JOptionPane.showMessageDialog(this, "File combining completed.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileProcessorGUI().setVisible(true);
            }
        });
    }
}

