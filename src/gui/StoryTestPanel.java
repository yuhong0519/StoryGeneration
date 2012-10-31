/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StoryLearnPanel.java
 *
 * Created on May 1, 2011, 3:25:11 PM
 */
package gui;

import javax.swing.JSlider;
import cfcontrol.*;

/**
 *
 * @author Peng
 */
public class StoryTestPanel extends javax.swing.JPanel {
    /** Story controller */
    StoryTestControl story;

    /** Creates new form StoryLearnPanel */
    public StoryTestPanel() {
        story = new StoryTestControl();
        
        /* Data initialization goes here */
        /* End initialization */
        
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        StoryPanel = new javax.swing.JScrollPane();
        StoryTextArea = new javax.swing.JTextArea();
        NextButton = new javax.swing.JButton();
        PrevButton = new javax.swing.JButton();
        StoryRating = new javax.swing.JSlider();
        RatingDescription = new javax.swing.JLabel();
        StoryPanelDescription = new javax.swing.JLabel();

        StoryPanel.setAutoscrolls(true);

        StoryTextArea.setBackground(new java.awt.Color(204, 204, 204));
        StoryTextArea.setColumns(100);
        StoryTextArea.setEditable(false);
        StoryTextArea.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        StoryTextArea.setLineWrap(true);
        StoryTextArea.setRows(7);
        StoryTextArea.setText(story.showStory());
        StoryTextArea.setWrapStyleWord(true);
        StoryPanel.setViewportView(StoryTextArea);

        NextButton.setText("Next");
        NextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NextButtonActionPerformed(evt);
            }
        });

        PrevButton.setText("Previous");
        PrevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrevButtonActionPerformed(evt);
            }
        });

        StoryRating.setMajorTickSpacing(1);
        StoryRating.setMaximum(5);
        StoryRating.setMinimum(1);
        StoryRating.setPaintLabels(true);
        StoryRating.setPaintTicks(true);
        StoryRating.setSnapToTicks(true);
        StoryRating.setToolTipText("Rate the story from 1-5");
        StoryRating.setValue(story.getRating());
        StoryRating.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                StoryRatingStateChanged(evt);
            }
        });

        RatingDescription.setText("Rating");

        StoryPanelDescription.setText("Story " + story.currentStoryNumber());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(194, 194, 194)
                        .addComponent(PrevButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(StoryRating, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(NextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(StoryPanelDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(StoryPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1061, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(403, 403, 403)
                        .addComponent(RatingDescription)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(StoryPanelDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StoryPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                .addGap(13, 13, 13)
                .addComponent(RatingDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PrevButton)
                    .addComponent(NextButton)
                    .addComponent(StoryRating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void NextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NextButtonActionPerformed
        // TODO add your handling code here:
        story.advanceStory();
        StoryPanelDescription.setText("Story " + story.currentStoryNumber());
        StoryTextArea.setText(story.showStory());
        if(story.endOfStory()){
            StoryTextArea.append("THE END.");
        }
        StoryRating.setValue(story.getRating());
}//GEN-LAST:event_NextButtonActionPerformed

    private void PrevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrevButtonActionPerformed
        // TODO add your handling code here:
        story.backtrackStory();
        StoryPanelDescription.setText("Story " + story.currentStoryNumber());
        StoryTextArea.setText(story.showStory());
        StoryRating.setValue(story.getRating());
}//GEN-LAST:event_PrevButtonActionPerformed

    private void StoryRatingStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_StoryRatingStateChanged
        // TODO add your handling code here:
        JSlider src = (JSlider)evt.getSource();
        if (!src.getValueIsAdjusting()) {
            story.setRating((int)src.getValue());
            System.out.println("Rating changed to: " + story.getRating());
        }
}//GEN-LAST:event_StoryRatingStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton NextButton;
    private javax.swing.JButton PrevButton;
    private javax.swing.JLabel RatingDescription;
    private javax.swing.JScrollPane StoryPanel;
    private javax.swing.JLabel StoryPanelDescription;
    private javax.swing.JSlider StoryRating;
    private javax.swing.JTextArea StoryTextArea;
    // End of variables declaration//GEN-END:variables
}