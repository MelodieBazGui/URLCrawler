package stryckyzzzComponents;

import javax.swing.*;

import crawlerUtils.LinkExtractor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StryckyzzzFilterPanel extends JPanel {

    private static final long serialVersionUID = -5636815545495224709L;
    private JTextArea customFilterArea;
    private DefaultListModel<String> selectedFilterModel;
    private JList<String> selectedFilterList;
    private JPanel topPanel;
    private JPanel customFilterPanel;
    private JScrollPane scrollPane;
    private JPanel selectedFilterPanel;
    private JScrollPane scrollPane_1;

    public StryckyzzzFilterPanel() {
        setLayout(new BorderLayout());
        initComponents(LinkExtractor.getLinks());
    }

    private void initComponents(List<String> items) {
        customFilterPanel = new JPanel(new BorderLayout());
        selectedFilterPanel = new JPanel(new BorderLayout());

        customFilterArea = new JTextArea(3, 20);
        JButton applyCustomFilterButton = new JButton("Apply Custom Filter");
        applyCustomFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customText = customFilterArea.getText().trim();
                if (!customText.isEmpty()) {
                    for (String filter : customText.split("\\n")) {
                        addFilter(filter.trim());
                    }
                }
            }
        });

        customFilterPanel.setBorder(BorderFactory.createTitledBorder("Custom Filters"));
        scrollPane = new JScrollPane(customFilterArea);
        customFilterPanel.add(scrollPane, BorderLayout.CENTER);
        customFilterPanel.add(applyCustomFilterButton, BorderLayout.SOUTH);

        selectedFilterModel = new DefaultListModel<>();
        selectedFilterList = new JList<>(selectedFilterModel);
        selectedFilterPanel.setBorder(BorderFactory.createTitledBorder("Selected Filters"));
        scrollPane_1 = new JScrollPane(selectedFilterList);
        selectedFilterPanel.add(scrollPane_1, BorderLayout.CENTER);

        topPanel = new JPanel(new BorderLayout());
        topPanel.add(customFilterPanel, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);
        add(selectedFilterPanel, BorderLayout.CENTER);
    }

    private void addFilter(String filter) {
        if (!filter.isEmpty() && !selectedFilterModel.contains(filter)) {
            selectedFilterModel.addElement(filter);
        }
    }

    public List<String> getSelectedFilters() {
        return Collections.list(selectedFilterModel.elements());
    }
    
    public static void filterComponentsByText(JPanel panel, List<String> keywords) {
    	String regex = String.join("|", keywords);
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    	
        for (Component c : panel.getComponents()) {
        	if(c.getClass().getCanonicalName().contains("Jbutton")) {
        		if (!pattern.matcher(((JButton) (c)).getText()).find()) {
        			panel.remove(c);
        		};
        	};
        }
        panel.revalidate();
        panel.repaint();
    }
}