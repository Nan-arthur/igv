/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2007-2015 Broad Institute
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/*
 * GenomeSelectionDialog.java
 *
 * Created on November 8, 2007, 3:51 PM
 */

package org.broad.igv.ui.commandbar;

import org.broad.igv.feature.genome.GenomeListItem;
import org.broad.igv.ui.util.IGVMouseInputAdapter;
import org.broad.igv.ui.util.UIUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Dialog box for selecting genomes. User can choose from a list,
 * which is filtered according to text box input
 */
public class GenomeSelectionDialog extends org.broad.igv.ui.IGVDialog {

    private JPanel dialogPane;
    private JPanel contentPanel;
    private JPanel filterPanel;
    private JLabel label1;
    private JTextField genomeFilter;
    private JScrollPane scrollPane1;
    private JList<GenomeListItem> genomeList;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    private boolean isCanceled = true;

    private GenomeListItem selectedValue = null;
    private List<GenomeListItem> allListItems;
    private DefaultListModel genomeListModel;

    /**
     * @param parent
     */
    public GenomeSelectionDialog(java.awt.Frame parent, Collection<GenomeListItem> inputListItems) {
        super(parent);
        initComponents();
        initData(inputListItems);
    }

    private void initData(Collection<GenomeListItem> inputListItems) {
        this.allListItems = new ArrayList<>(inputListItems);
        String filterText = genomeFilter.getText().trim().toLowerCase();
        rebuildGenomeList(filterText);
    }

    /**
     * Filter the list of displayed genomes with the text the user entered.
     */
    private void rebuildGenomeList(String filterText) {
        if (genomeListModel == null) {
            genomeListModel = new DefaultListModel();
            UIUtilities.invokeOnEventThread(() -> genomeList.setModel(genomeListModel));
        }
        genomeListModel.clear();
        filterText = filterText.toLowerCase().trim();
        for (GenomeListItem listItem : allListItems) {
            if (listItem.getDisplayableName().toLowerCase().contains(filterText)) {
                genomeListModel.addElement(listItem);
            }
        }
    }

    /**
     * If a genome is single clicked, we just store the selection.
     * When a genome is double clicked, we treat that as the user
     * wanting to load the genome.
     *
     * @param e
     */
    private void genomeListMouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            okButtonActionPerformed(null);
        }
    }

    private void genomeEntryKeyReleased(KeyEvent e) {
        rebuildGenomeList(genomeFilter.getText());
    }

    public GenomeListItem getSelectedValue() {
        return selectedValue;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        isCanceled = true;
        selectedValue = null;
        setVisible(false);
        dispose();
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        isCanceled = false;
        selectedValue = genomeList.getSelectedValue();
        setVisible(false);
        dispose();
    }


    private void initComponents() {
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        filterPanel = new JPanel();
        label1 = new JLabel();
        genomeFilter = new JTextField();
        scrollPane1 = new JScrollPane();
        genomeList = new JList();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setModal(true);
        setTitle("Hosted Genomes");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setPreferredSize(new Dimension(350, 500));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));


                //======== filterPanel ========
                {
                    filterPanel.setMaximumSize(new Dimension(2147483647, 28));
                    filterPanel.setLayout(new GridBagLayout());
                    ((GridBagLayout) filterPanel.getLayout()).columnWidths = new int[]{0, 0, 0};
                    ((GridBagLayout) filterPanel.getLayout()).rowHeights = new int[]{0, 0};
                    ((GridBagLayout) filterPanel.getLayout()).columnWeights = new double[]{1.0, 1.0, 1.0E-4};
                    ((GridBagLayout) filterPanel.getLayout()).rowWeights = new double[]{1.0, 1.0E-4};

                    //---- label1 ----
                    label1.setText("Filter:");
                    label1.setLabelFor(genomeFilter);
                    label1.setRequestFocusEnabled(false);
                    filterPanel.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                            new Insets(0, 0, 0, 0), 0, 0));

                    //---- genomeFilter ----
                    genomeFilter.setToolTipText("Filter genome list");
                    genomeFilter.setPreferredSize(new Dimension(220, 28));
                    genomeFilter.setMinimumSize(new Dimension(180, 28));
                    genomeFilter.setAlignmentX(0.0F);
                    genomeFilter.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            genomeEntryKeyReleased(e);
                        }
                    });
                    filterPanel.add(genomeFilter, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                }
                contentPanel.add(filterPanel);

                //======== scrollPane1 ========
                {

                    //---- genomeList ----
                    genomeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    genomeList.addMouseListener(new IGVMouseInputAdapter() {
                        @Override
                        public void igvMouseClicked(MouseEvent e) {
                            genomeListMouseClicked(e);
                        }
                    });
                    scrollPane1.setViewportView(genomeList);
                }
                contentPanel.add(scrollPane1);

            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 85, 80};
                ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        okButtonActionPerformed(e);
                    }
                });
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cancelButtonActionPerformed(e);
                    }
                });
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
    }// </editor-fold>//GEN-END:initComponents


}
