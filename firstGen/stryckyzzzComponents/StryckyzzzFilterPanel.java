package stryckyzzzComponents;

import javax.swing.*;

import crawlerUtils.LinkExtractor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class StryckyzzzFilterPanel extends JPanel {

    private static final long serialVersionUID = -5636815545495224709L;
    private JTextArea customFilterArea;
    private DefaultListModel<String> selectedFilterModel;
    private JList<String> selectedFilterList;
    private JPanel topPanel;
    private JPanel buttonPanel;
    private JPanel customFilterPanel;
    private JScrollPane scrollPane;
    private JPanel selectedFilterPanel;
    private JScrollPane scrollPane_1;

    public StryckyzzzFilterPanel() {
        setLayout(new BorderLayout());
        initComponents(LinkExtractor.getLinks());
    }

    private void initComponents(List<String> items) {
    	
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        customFilterPanel = new JPanel(new BorderLayout());
        selectedFilterPanel = new JPanel(new BorderLayout());

        List<String> topFilters = getTopFrequentStrings(items, 3);

        for (String filter : topFilters) {
            JButton filterButton = new JButton(filter);
            filterButton.addActionListener(e -> addFilter(filter));
            buttonPanel.add(filterButton);
        }

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
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        topPanel.add(customFilterPanel, BorderLayout.EAST);

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
    
    private List<String> getTopFrequentStrings(List<String> input, int topN) {
        Map<String, Integer> freqMap = new HashMap<>();
        for (String item : input) {
            freqMap.put(item, freqMap.getOrDefault(item, 0) + 1);
        }

        return freqMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}