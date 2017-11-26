import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

import org.jpl7.*;

public class GUI {
    private static int questionTracker = 0;
    private Query query = new Query();
    private JFrame frame;
    private JLabel lblQuestion;
    private JPanel option;
    private JPanel panelShowAll;
    private JPanel panelCfmCancel;

    public static void main(String[] args) {
        new GUI();
        new WrapLayout();
    }

    private GUI() {
        frame = new JFrame();
        frame.setTitle("Subway Order System");
        frame.pack();
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        ImageIcon icImg = new ImageIcon("images/SubwayIcon.png");
        frame.setIconImage(icImg.getImage());

        JPanel panelBanner = new JPanel();
        frame.getContentPane().add(panelBanner, BorderLayout.NORTH);
        ImageIcon subwayBanner = new ImageIcon("images/SubwayBanner.png");
        JLabel lblBanner = new JLabel(subwayBanner);
        panelBanner.add(lblBanner);

        JPanel panelQuestion = new JPanel();
        frame.getContentPane().add(panelQuestion, BorderLayout.CENTER);
        panelQuestion.setLayout(new BorderLayout(0, 0));

        JPanel question_P = new JPanel();
        panelQuestion.add(question_P, BorderLayout.NORTH);

        lblQuestion = new JLabel("Question");
        lblQuestion.setFont(lblQuestion.getFont().deriveFont(20f));
        question_P.add(lblQuestion);

        JPanel panelShowAllOption = new JPanel();
        panelQuestion.add(panelShowAllOption, BorderLayout.CENTER);
        panelShowAllOption.setLayout(new BorderLayout(0, 0));

        panelShowAll = new JPanel();
        panelShowAll.setPreferredSize(new Dimension(200, 200));
        panelShowAllOption.add(panelShowAll, BorderLayout.CENTER);

        JLabel lblShowAll = new JLabel("SHOW ALL");
        panelShowAll.add(lblShowAll);
        lblShowAll.setVisible(false);

        JPanel panelOptionCfmBody = new JPanel();
        panelShowAllOption.add(panelOptionCfmBody, BorderLayout.NORTH);
        panelOptionCfmBody.setLayout(new BorderLayout(0, 0));

        JPanel panelOption = new JPanel();
        panelOptionCfmBody.add(panelOption, BorderLayout.NORTH);
        panelOption.setLayout(new BorderLayout(0, 0));

        option = new JPanel();
        panelOption.add(option);
        option.setLayout(new WrapLayout(1, 1, 1));

        panelCfmCancel = new JPanel();
        panelOptionCfmBody.add(panelCfmCancel);
        panelCfmCancel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        Question();
    }

    private void btnChoice(String question, ArrayList<Term> query, String userChoice) {
        lblQuestion.setText(question);  // Label for the question

        // Remove and repaint the GUI
        option.removeAll();
        option.repaint();
        option.revalidate();

        // Dynamic Button Create
        for (Term aQuery : query) {
            String buttonName = aQuery.toString();
            JButton btnNewButton = new JButton(buttonName);
            btnNewButton.setFont(btnNewButton.getFont().deriveFont(20f));
            option.add(btnNewButton);

            btnNewButton.addActionListener(e -> {
                // Get the text of the button option
                String selected = btnNewButton.getText();
                AppendToFile(userChoice, selected);

                Question();
            });
        }

        frame.setVisible(true);  // Make the dynamic GUI visible
    }

    private void chkChoice(String question, ArrayList<Term> query, String userChoice) {
        lblQuestion.setText(question);  // Label for the question

        // Remove and repaint the GUI
        option.removeAll();
        option.repaint();
        option.revalidate();
        panelCfmCancel.removeAll();
        panelCfmCancel.repaint();
        panelCfmCancel.revalidate();

        // Added a confirm for all selected items
        JButton btnConfirm = new JButton("Confirm");
        btnConfirm.setFont(btnConfirm.getFont().deriveFont(20f));
        panelCfmCancel.add(btnConfirm);
        btnConfirm.addActionListener(e -> {
            // For all the checkbox component at the "option" panel, check if selected
            int checkBoxCounter = 0;
            for (Component checkBox : option.getComponents()) {
                if (checkBox instanceof JCheckBox) {
                    if (((JCheckBox) checkBox).isSelected()) {
                        checkBoxCounter++;

                        // Get the user selected option and append it to file
                        String selected = (((JCheckBox) checkBox).getText());
                        AppendToFile(userChoice, selected);
                    }
                }
            }
            if (checkBoxCounter == 0)
                AppendToFile(userChoice);

            Question();
        });

        // Dynamic CheckBox Create
        for (Term aQuery : query) {
            String checkBoxName = aQuery.toString();
            JCheckBox chckbxNewCheckBox = new JCheckBox(checkBoxName);
            chckbxNewCheckBox.setFont(chckbxNewCheckBox.getFont().deriveFont(20f));
            option.add(chckbxNewCheckBox);
        }

        frame.setVisible(true);  // Make the dynamic GUI visible
    }

    // This method Print the order of user
    private void printOrder(String question, ArrayList<Term> showAll) {
        lblQuestion.setText(question);  // Label for the question

        // Remove and repaint the GUI
        option.removeAll();
        option.repaint();
        option.revalidate();
        panelCfmCancel.removeAll();
        panelCfmCancel.repaint();
        panelCfmCancel.revalidate();

        // Set up the Text Area
        JTextArea showOrder = new JTextArea();
        showOrder.setLineWrap(true);

        JScrollPane scroll = new JScrollPane(showOrder, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension(200, 200));
        Font font = showOrder.getFont();
        showOrder.setFont(font.deriveFont(Font.BOLD, 13f));
        showOrder.setEditable(false);

        // Initialize the String
        String orders;
        StringBuilder combine = new StringBuilder();
        for (Term aShow_all : showAll) {
            if (aShow_all.toString().equals("0"))
                continue;

            orders = aShow_all.toString();
            combine.append(orders).append("\n");
        }

        // Set value to the Text Area
        showOrder.setText(combine.toString());
        panelShowAll.add(scroll);
        panelShowAll.setVisible(true);
        frame.setVisible(true);
    }

    // Append No option available to prolog
    private void AppendToFile(String userChoice) {
        if (userChoice == null)
            return;

        final String saveFile = "src/assignment.pl";
        File f = new File(saveFile);
        String appendText = userChoice + "(0).\n";
        try {
            // Check for file existence
            if (!f.exists())
                System.out.println(saveFile + " does not exist!");

            FileWriter fw = new FileWriter(f, true);

            // BufferedWriter writer give better performance
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(appendText);
            System.out.println(appendText);
            bw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // Append User chosen option to prolog
    private void AppendToFile(String userChoice, String selected) {
        if (userChoice == null)
            return;

        final String saveFile = "src/assignment.pl";
        File f = new File(saveFile);

        String appendText = userChoice + "(" + selected + ").\n";
        try {
            // Check for file existence
            if (!f.exists())
                System.out.println(saveFile + " does not exist!");

            FileWriter fw = new FileWriter(f, true);

            // BufferedWriter writer give better performance
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(appendText);
            System.out.println(appendText);
            bw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // This is the Question Method
    private void Question() {
        questionTracker++;

        switch (questionTracker) {
            case 1:
                String question = "What meal do you like?";
                ArrayList<Term> query = this.query.query("ask_meal(X)");
                String userChoice = "chosen_meal";
                btnChoice(question, query, userChoice);
                break;

            case 2:
                question = "What bread do you like?";
                query = this.query.query("ask_bread(X)");
                userChoice = "chosen_bread";
                btnChoice(question, query, userChoice);
                break;

            case 3:
                question = "What meat do you like?";
                query = this.query.query("ask_meats(X)");
                userChoice = "chosen_meat";
                if (query == null) {
                    AppendToFile(userChoice);
                    Question();
                    break;
                }
                btnChoice(question, query, userChoice);
                break;

            case 4:
                question = "What veggies do you like?";
                query = this.query.query("ask_veggies(X)");
                userChoice = "chosen_veggie";
                if (query == null) {
                    AppendToFile(userChoice);
                    Question();
                    break;
                }
                chkChoice(question, query, userChoice);
                break;

            case 5:
                question = "What sauces do you like?";
                query = this.query.query("ask_sauces(X)");
                userChoice = "chosen_sauce";
                if (query == null) {
                    AppendToFile(userChoice);
                    Question();
                    break;
                }
                chkChoice(question, query, userChoice);
                break;

            case 6:
                question = "What topups do you like?";
                query = this.query.query("ask_topups(X)");
                userChoice = "chosen_topup";
                if (query == null) {
                    AppendToFile(userChoice);
                    Question();
                    break;
                }
                chkChoice(question, query, userChoice);
                break;

            case 7:
                question = "What sides do you like?";
                query = this.query.query("ask_sides(X)");
                userChoice = "chosen_side";
                if (query == null) {
                    AppendToFile(userChoice);
                    Question();
                    break;
                }
                chkChoice(question, query, userChoice);
                break;

            default:
                ArrayList<Term> show_all = new ArrayList<>();
                question = "Here are your order";
                query = this.query.query("show_meal(X)");
                show_all.addAll(query);

                query = this.query.query("show_bread(X)");
                show_all.addAll(query);

                query = this.query.query("show_meat(X)");
                show_all.addAll(query);

                query = this.query.query("show_veggie(X)");
                show_all.addAll(query);

                query = this.query.query("show_sauce(X)");
                show_all.addAll(query);

                query = this.query.query("show_topups(X)");
                show_all.addAll(query);

                query = this.query.query("show_sides(X)");
                show_all.addAll(query);

                System.out.println(question);
                System.out.println(show_all);
                printOrder(question, show_all);
        }
    }
}