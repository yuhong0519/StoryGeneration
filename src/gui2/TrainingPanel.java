/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TrainingPanel.java
 *
 * Created on Mar 24, 2012, 6:00:51 PM
 */
package gui2;

/**
 *
 * @author Hong Yu
 * 
 */
import java.awt.Color;
import javax.swing.*;
import java.util.*;

public class TrainingPanel extends javax.swing.JPanel {

    /** Creates new form TrainingPanel */
    public TrainingControl tc;
    public int rating = 0;    
    private JApplet ja;
//    private ArrayList<JSlider> jcbA = new ArrayList<JSlider>();
    private ArrayList<JLabel> options = new ArrayList<JLabel>();
    
    private ArrayList<JPanel> optionPanels = new ArrayList<JPanel>();
    
    private final int maxNumOptions = 6;
    private final int maxRatingLevels = 6;
    public ArrayList<JRadioButton> jrbA = new ArrayList<JRadioButton>();
    private boolean showNextMove = false;
    
//    private int nextMove = -1;
    JRadioButton[][] optionRadioA = new JRadioButton[maxNumOptions][maxRatingLevels];
//    private int selectedOption = -1;
    
    private Color backgroundC = new Color(240,240,240);
    private Color greyC = new Color(204,204,204);
    private Color yellowC = new Color(255,255,102);
    private Color darkYelloC = new Color(255,255,0);
    
    public TrainingPanel(JApplet ja) {
        tc = new TrainingControl(ja, this);
        this.ja = ja;
        initComponents();
        jRadioButton6.setVisible(false);

        jrbA.add(jRadioButton1);
        jrbA.add(jRadioButton2);
        jrbA.add(jRadioButton3);
        jrbA.add(jRadioButton4);
        jrbA.add(jRadioButton5);
        jrbA.add(jRadioButton6);
        optionPanels.add(optionP1);
        optionPanels.add(optionP2);
        optionPanels.add(optionP3);
        optionPanels.add(optionP4);
        optionPanels.add(optionP5);
        optionPanels.add(optionP6);
        
        for(int i = 0;i < jrbA.size()-1; i++){
            jrbA.get(i).setBackground(yellowC);
        }
        jPanel1.setBackground(yellowC);
        
        options.add(jLabel3);
        options.add(jLabel4);
        options.add(jLabel5);
        options.add(jLabel6);
        options.add(jLabel7);
        options.add(jLabel8);
        optionRadioA[0][0] = jRadioButton7;
        optionRadioA[0][1] = jRadioButton8;
        optionRadioA[0][2] = jRadioButton9;
        optionRadioA[0][3] = jRadioButton10;
        optionRadioA[0][4] = jRadioButton11;
        optionRadioA[0][5] = jRadioButton12;
        optionRadioA[1][0] = jRadioButton25;
        optionRadioA[1][1] = jRadioButton26;
        optionRadioA[1][2] = jRadioButton27;
        optionRadioA[1][3] = jRadioButton28;
        optionRadioA[1][4] = jRadioButton29;
        optionRadioA[1][5] = jRadioButton30;
        optionRadioA[2][0] = jRadioButton31;
        optionRadioA[2][1] = jRadioButton32;
        optionRadioA[2][2] = jRadioButton33;
        optionRadioA[2][3] = jRadioButton34;
        optionRadioA[2][4] = jRadioButton35;
        optionRadioA[2][5] = jRadioButton36;
        optionRadioA[3][0] = jRadioButton37;
        optionRadioA[3][1] = jRadioButton38;
        optionRadioA[3][2] = jRadioButton39;
        optionRadioA[3][3] = jRadioButton40;
        optionRadioA[3][4] = jRadioButton41;
        optionRadioA[3][5] = jRadioButton42;
        optionRadioA[4][0] = jRadioButton49;
        optionRadioA[4][1] = jRadioButton50;
        optionRadioA[4][2] = jRadioButton51;
        optionRadioA[4][3] = jRadioButton52;
        optionRadioA[4][4] = jRadioButton53;
        optionRadioA[4][5] = jRadioButton54;
        optionRadioA[5][0] = jRadioButton43;
        optionRadioA[5][1] = jRadioButton44;
        optionRadioA[5][2] = jRadioButton45;
        optionRadioA[5][3] = jRadioButton46;
        optionRadioA[5][4] = jRadioButton47;
        optionRadioA[5][5] = jRadioButton48;
        jLabel10.setVisible(false);
        
        for (int i = 0; i < maxNumOptions; i++){
            options.get(i).setVisible(false);
            for(int j = 0; j < 6; j++){
                optionRadioA[i][j].setVisible(false);
            }
        }

    }
    public void disableNext(){
        showNextMove = false;
        jRadioButton1.setEnabled(false);
        jRadioButton2.setEnabled(false); 
        jRadioButton3.setEnabled(false); 
        jRadioButton4.setEnabled(false); 
        jRadioButton5.setEnabled(false); 
    }
    
//    Show items when the same prefix is encountered again
    public void showSame(){
        for(int i = 0; i < jrbA.size()-1; i++){
            jrbA.get(i).setEnabled(false);
            jrbA.get(i).setBackground(backgroundC);
        }
        jPanel1.setBackground(backgroundC);

        int min = 10;
        for(int i = 0; i < tc.getNumOptions(); i++){ 
            if(preferenceArray[i] < min)
                min = preferenceArray[i];            
        }        
        jLabel10.setVisible(true);
        for(int i = 0; i < tc.getNumOptions(); i++){
            options.get(i).setVisible(true);
            optionPanels.get(i).setBackground(yellowC);
            for(int j = 0; j < 5; j++){
                optionRadioA[i][j].setVisible(true);
                optionRadioA[i][j].setBackground(yellowC);
            }
        }        
        if(min > 0){
            for(int i = 0; i < tc.getNumOptions(); i++){
                options.get(i).setBackground(yellowC);
                optionPanels.get(i).setBackground(backgroundC);
                 for(int j = 0; j < 5; j++){
//                    optionRadioA[i][j].setEnabled(false);
                    optionRadioA[i][j].setBackground(backgroundC);
                }
            }            
            jLabel10.setText("Please select your preferred option by clicking it.");
            showNextMove = true;
        }
    }
    
    private void showOptions(){
        for(int i = 0;i < jrbA.size()-1; i++){
            jrbA.get(i).setEnabled(false);
            jrbA.get(i).setBackground(backgroundC);
        }
        jPanel1.setBackground(backgroundC);
        
        jLabel10.setVisible(true);
        DefaultListModel dlm = tc.getListModel();
        for(int i = 0; i < dlm.size(); i++){
            options.get(i).setText(dlm.get(i).toString());
            options.get(i).setVisible(true);
            options.get(i).setBackground(greyC);
            optionPanels.get(i).setBackground(yellowC);
            for(int j = 0; j < 5; j++){
                optionRadioA[i][j].setVisible(true);
                optionRadioA[i][j].setEnabled(true);
                optionRadioA[i][j].setBackground(yellowC);
            }
            
        }
        for(int i = dlm.size(); i < maxNumOptions; i++){
            options.get(i).setText("");
            options.get(i).setVisible(false);
            for(int j = 0; j < 5; j++){
                optionRadioA[i][j].setVisible(false);
            }
        }    
        if(dlm.size() == 1 && dlm.get(0).toString().compareTo("1: Continue.") == 0){
            
            selectOptionPreference(0, 3);
        }
    }
    

    
    int[] preferenceArray = new int[maxNumOptions];

    public void selectOptionPreference(int index, int value){
        tc.setPreferenceCB(index,  value);
        preferenceArray[index] = value;
        
        int min = 10;
        for(int i = 0; i < tc.getNumOptions(); i++){ 
            if(preferenceArray[i] < min)
                min = preferenceArray[i];            
        }
        if(min > 0 && options.get(0).getBackground().getBlue() != 102){
            for(int i = 0; i < tc.getNumOptions(); i++){
//                disable the option radio buttons when finish
                for(int j = 0; j < 5; j++){
//                    optionRadioA[i][j].setEnabled(false);
                    optionRadioA[i][j].setBackground(backgroundC);
                }
                options.get(i).setBackground(yellowC);
                optionPanels.get(i).setBackground(backgroundC);
            }
            showNextMove = true;
            jLabel10.setText("Please select your preferred option by clicking it.");
        }
    }
    
    public void questionResult(int value){
        tc.correctQuestion(value);
    }
    

    QuestionDialog qdd = null;
    private void nextPlotPoint(int nextMove){

        int tp = nextMove;
        if(tp == -1){
            return;
        }
        showNextMove = false;
        for(int i = 0; i < preferenceArray.length; i++){
            preferenceArray[i] = -1;
        }
        
        ArrayList<String> questions = tc.getQuestions();

        tc.setCorrectChoice(tp);
//        Enable the story rating panel
        for(int i = 0;i < jrbA.size()-1; i++){
            jrbA.get(i).setEnabled(true);
            jrbA.get(i).setBackground(yellowC);
        }
        jPanel1.setBackground(yellowC);

        jLabel10.setVisible(false);
        jLabel10.setText("Please rate each option.");

        for(int i = 0; i < tc.getNumOptions(); i++){
            options.get(i).setVisible(false);
            options.get(i).setBackground(greyC);
            for(int j = 0; j < 5; j++){
                optionRadioA[i][j].setVisible(false);
                
            }
        }
        tc.next(tp, rating);

        StoryTextArea.setText(tc.show());
        DefaultListModel dlm = tc.getListModel();

        jRadioButton6.setSelected(true);
        rating = 0;

        for(int i = 0; i < dlm.size(); i++){
            options.get(i).setText(dlm.get(i).toString());
        }
        for(int i = dlm.size(); i < maxNumOptions; i++){
            options.get(i).setText("");
        }

        tc.getPreferenceCB(this, optionRadioA);
        jLabel1.setText(tc.getTitle());
        
        if(questions != null){
            qdd = new QuestionDialog(null, true, this, questions);
            qdd.setVisible(true);            
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        buttonGroup7 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane1 = new javax.swing.JScrollPane();
        StoryTextArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        optionP1 = new javax.swing.JPanel();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        jRadioButton10 = new javax.swing.JRadioButton();
        jRadioButton11 = new javax.swing.JRadioButton();
        jRadioButton12 = new javax.swing.JRadioButton();
        optionP2 = new javax.swing.JPanel();
        jRadioButton25 = new javax.swing.JRadioButton();
        jRadioButton26 = new javax.swing.JRadioButton();
        jRadioButton27 = new javax.swing.JRadioButton();
        jRadioButton28 = new javax.swing.JRadioButton();
        jRadioButton29 = new javax.swing.JRadioButton();
        jRadioButton30 = new javax.swing.JRadioButton();
        optionP3 = new javax.swing.JPanel();
        jRadioButton31 = new javax.swing.JRadioButton();
        jRadioButton32 = new javax.swing.JRadioButton();
        jRadioButton33 = new javax.swing.JRadioButton();
        jRadioButton34 = new javax.swing.JRadioButton();
        jRadioButton35 = new javax.swing.JRadioButton();
        jRadioButton36 = new javax.swing.JRadioButton();
        optionP4 = new javax.swing.JPanel();
        jRadioButton37 = new javax.swing.JRadioButton();
        jRadioButton38 = new javax.swing.JRadioButton();
        jRadioButton39 = new javax.swing.JRadioButton();
        jRadioButton40 = new javax.swing.JRadioButton();
        jRadioButton41 = new javax.swing.JRadioButton();
        jRadioButton42 = new javax.swing.JRadioButton();
        optionP5 = new javax.swing.JPanel();
        jRadioButton49 = new javax.swing.JRadioButton();
        jRadioButton50 = new javax.swing.JRadioButton();
        jRadioButton51 = new javax.swing.JRadioButton();
        jRadioButton52 = new javax.swing.JRadioButton();
        jRadioButton53 = new javax.swing.JRadioButton();
        jRadioButton54 = new javax.swing.JRadioButton();
        optionP6 = new javax.swing.JPanel();
        jRadioButton43 = new javax.swing.JRadioButton();
        jRadioButton44 = new javax.swing.JRadioButton();
        jRadioButton45 = new javax.swing.JRadioButton();
        jRadioButton46 = new javax.swing.JRadioButton();
        jRadioButton47 = new javax.swing.JRadioButton();
        jRadioButton48 = new javax.swing.JRadioButton();

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        setPreferredSize(new java.awt.Dimension(1100, 1300));

        StoryTextArea.setBackground(new java.awt.Color(204, 204, 204));
        StoryTextArea.setColumns(100);
        StoryTextArea.setEditable(false);
        StoryTextArea.setFont(new java.awt.Font("Times New Roman", 0, 20)); // NOI18N
        StoryTextArea.setLineWrap(true);
        StoryTextArea.setRows(7);
        StoryTextArea.setText(tc.show());
        StoryTextArea.setWrapStyleWord(true);
        StoryTextArea.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jScrollPane1.setViewportView(StoryTextArea);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText(tc.getTitle());

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton1.setText("1");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton2.setText("2");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton3.setText("3");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton4.setText("4");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton5);
        jRadioButton5.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton5.setText("5");
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton6);
        jRadioButton6.setText("6");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jRadioButton1)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton2)
                .addGap(2, 2, 2)
                .addComponent(jRadioButton3)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton4)
                .addGap(2, 2, 2)
                .addComponent(jRadioButton5)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton6))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jRadioButton1))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jRadioButton2))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jRadioButton3))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jRadioButton4))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jRadioButton5))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jRadioButton6))
        );

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setText("Your preference rating for the story until now (1 is the worst, 5 is the best):");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setText("Please rate each option:");

        jLabel3.setBackground(new java.awt.Color(204, 204, 204));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("jLabel3");
        jLabel3.setOpaque(true);
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel3MouseExited(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(204, 204, 204));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("jLabel4");
        jLabel4.setOpaque(true);
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel4MouseExited(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(204, 204, 204));
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("jLabel5");
        jLabel5.setOpaque(true);
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel5MouseExited(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(204, 204, 204));
        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("jLabel6");
        jLabel6.setOpaque(true);
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel6MouseExited(evt);
            }
        });

        jLabel7.setBackground(new java.awt.Color(204, 204, 204));
        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("jLabel7");
        jLabel7.setOpaque(true);
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel7MouseExited(evt);
            }
        });

        jLabel8.setBackground(new java.awt.Color(204, 204, 204));
        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("jLabel8");
        jLabel8.setOpaque(true);
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel8MouseExited(evt);
            }
        });

        buttonGroup2.add(jRadioButton7);
        jRadioButton7.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton7.setText("1");
        jRadioButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton7ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButton8);
        jRadioButton8.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton8.setText("2");
        jRadioButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton8ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButton9);
        jRadioButton9.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton9.setText("3");
        jRadioButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton9ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButton10);
        jRadioButton10.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton10.setText("4");
        jRadioButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton10ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButton11);
        jRadioButton11.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton11.setText("5");
        jRadioButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton11ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButton12);
        jRadioButton12.setText("6");

        javax.swing.GroupLayout optionP1Layout = new javax.swing.GroupLayout(optionP1);
        optionP1.setLayout(optionP1Layout);
        optionP1Layout.setHorizontalGroup(
            optionP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionP1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jRadioButton7)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton8)
                .addGap(2, 2, 2)
                .addComponent(jRadioButton9)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton10)
                .addGap(2, 2, 2)
                .addComponent(jRadioButton11)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton12))
        );
        optionP1Layout.setVerticalGroup(
            optionP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionP1Layout.createSequentialGroup()
                .addGroup(optionP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(optionP1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton7))
                    .addGroup(optionP1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton8))
                    .addGroup(optionP1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton9))
                    .addGroup(optionP1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton10))
                    .addGroup(optionP1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton11))
                    .addGroup(optionP1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jRadioButton12)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonGroup3.add(jRadioButton25);
        jRadioButton25.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton25.setText("1");
        jRadioButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton25ActionPerformed(evt);
            }
        });

        buttonGroup3.add(jRadioButton26);
        jRadioButton26.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton26.setText("2");
        jRadioButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton26ActionPerformed(evt);
            }
        });

        buttonGroup3.add(jRadioButton27);
        jRadioButton27.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton27.setText("3");
        jRadioButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton27ActionPerformed(evt);
            }
        });

        buttonGroup3.add(jRadioButton28);
        jRadioButton28.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton28.setText("4");
        jRadioButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton28ActionPerformed(evt);
            }
        });

        buttonGroup3.add(jRadioButton29);
        jRadioButton29.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton29.setText("5");
        jRadioButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton29ActionPerformed(evt);
            }
        });

        buttonGroup3.add(jRadioButton30);
        jRadioButton30.setText("6");

        javax.swing.GroupLayout optionP2Layout = new javax.swing.GroupLayout(optionP2);
        optionP2.setLayout(optionP2Layout);
        optionP2Layout.setHorizontalGroup(
            optionP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionP2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jRadioButton25)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton26)
                .addGap(2, 2, 2)
                .addComponent(jRadioButton27)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton28)
                .addGap(2, 2, 2)
                .addComponent(jRadioButton29)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton30))
        );
        optionP2Layout.setVerticalGroup(
            optionP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionP2Layout.createSequentialGroup()
                .addGroup(optionP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(optionP2Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton25))
                    .addGroup(optionP2Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton26))
                    .addGroup(optionP2Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton27))
                    .addGroup(optionP2Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton28))
                    .addGroup(optionP2Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton29))
                    .addGroup(optionP2Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jRadioButton30)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonGroup4.add(jRadioButton31);
        jRadioButton31.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton31.setText("1");
        jRadioButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton31ActionPerformed(evt);
            }
        });

        buttonGroup4.add(jRadioButton32);
        jRadioButton32.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton32.setText("2");
        jRadioButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton32ActionPerformed(evt);
            }
        });

        buttonGroup4.add(jRadioButton33);
        jRadioButton33.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton33.setText("3");
        jRadioButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton33ActionPerformed(evt);
            }
        });

        buttonGroup4.add(jRadioButton34);
        jRadioButton34.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton34.setText("4");
        jRadioButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton34ActionPerformed(evt);
            }
        });

        buttonGroup4.add(jRadioButton35);
        jRadioButton35.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton35.setText("5");
        jRadioButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton35ActionPerformed(evt);
            }
        });

        buttonGroup4.add(jRadioButton36);
        jRadioButton36.setText("6");

        javax.swing.GroupLayout optionP3Layout = new javax.swing.GroupLayout(optionP3);
        optionP3.setLayout(optionP3Layout);
        optionP3Layout.setHorizontalGroup(
            optionP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionP3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jRadioButton31)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton32)
                .addGap(2, 2, 2)
                .addComponent(jRadioButton33)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton34)
                .addGap(2, 2, 2)
                .addComponent(jRadioButton35)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton36))
        );
        optionP3Layout.setVerticalGroup(
            optionP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionP3Layout.createSequentialGroup()
                .addGroup(optionP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(optionP3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton31))
                    .addGroup(optionP3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton32))
                    .addGroup(optionP3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton33))
                    .addGroup(optionP3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton34))
                    .addGroup(optionP3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton35))
                    .addGroup(optionP3Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jRadioButton36)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonGroup5.add(jRadioButton37);
        jRadioButton37.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton37.setText("1");
        jRadioButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton37ActionPerformed(evt);
            }
        });

        buttonGroup5.add(jRadioButton38);
        jRadioButton38.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton38.setText("2");
        jRadioButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton38ActionPerformed(evt);
            }
        });

        buttonGroup5.add(jRadioButton39);
        jRadioButton39.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton39.setText("3");
        jRadioButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton39ActionPerformed(evt);
            }
        });

        buttonGroup5.add(jRadioButton40);
        jRadioButton40.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton40.setText("4");
        jRadioButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton40ActionPerformed(evt);
            }
        });

        buttonGroup5.add(jRadioButton41);
        jRadioButton41.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton41.setText("5");
        jRadioButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton41ActionPerformed(evt);
            }
        });

        buttonGroup5.add(jRadioButton42);
        jRadioButton42.setText("6");

        javax.swing.GroupLayout optionP4Layout = new javax.swing.GroupLayout(optionP4);
        optionP4.setLayout(optionP4Layout);
        optionP4Layout.setHorizontalGroup(
            optionP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionP4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jRadioButton37)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton38)
                .addGap(2, 2, 2)
                .addComponent(jRadioButton39)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton40)
                .addGap(2, 2, 2)
                .addComponent(jRadioButton41)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton42))
        );
        optionP4Layout.setVerticalGroup(
            optionP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionP4Layout.createSequentialGroup()
                .addGroup(optionP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(optionP4Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton37))
                    .addGroup(optionP4Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton38))
                    .addGroup(optionP4Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton39))
                    .addGroup(optionP4Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton40))
                    .addGroup(optionP4Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton41))
                    .addGroup(optionP4Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jRadioButton42)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonGroup6.add(jRadioButton49);
        jRadioButton49.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton49.setText("1");
        jRadioButton49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton49ActionPerformed(evt);
            }
        });

        buttonGroup6.add(jRadioButton50);
        jRadioButton50.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton50.setText("2");
        jRadioButton50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton50ActionPerformed(evt);
            }
        });

        buttonGroup6.add(jRadioButton51);
        jRadioButton51.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton51.setText("3");
        jRadioButton51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton51ActionPerformed(evt);
            }
        });

        buttonGroup6.add(jRadioButton52);
        jRadioButton52.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton52.setText("4");
        jRadioButton52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton52ActionPerformed(evt);
            }
        });

        buttonGroup6.add(jRadioButton53);
        jRadioButton53.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton53.setText("5");
        jRadioButton53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton53ActionPerformed(evt);
            }
        });

        buttonGroup6.add(jRadioButton54);
        jRadioButton54.setText("6");

        javax.swing.GroupLayout optionP5Layout = new javax.swing.GroupLayout(optionP5);
        optionP5.setLayout(optionP5Layout);
        optionP5Layout.setHorizontalGroup(
            optionP5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionP5Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jRadioButton49)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton50)
                .addGap(2, 2, 2)
                .addComponent(jRadioButton51)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton52)
                .addGap(2, 2, 2)
                .addComponent(jRadioButton53)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton54))
        );
        optionP5Layout.setVerticalGroup(
            optionP5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionP5Layout.createSequentialGroup()
                .addGroup(optionP5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(optionP5Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton49))
                    .addGroup(optionP5Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton50))
                    .addGroup(optionP5Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton51))
                    .addGroup(optionP5Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton52))
                    .addGroup(optionP5Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton53))
                    .addGroup(optionP5Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jRadioButton54)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonGroup7.add(jRadioButton43);
        jRadioButton43.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton43.setText("1");
        jRadioButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton43ActionPerformed(evt);
            }
        });

        buttonGroup7.add(jRadioButton44);
        jRadioButton44.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton44.setText("2");
        jRadioButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton44ActionPerformed(evt);
            }
        });

        buttonGroup7.add(jRadioButton45);
        jRadioButton45.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton45.setText("3");
        jRadioButton45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton45ActionPerformed(evt);
            }
        });

        buttonGroup7.add(jRadioButton46);
        jRadioButton46.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton46.setText("4");
        jRadioButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton46ActionPerformed(evt);
            }
        });

        buttonGroup7.add(jRadioButton47);
        jRadioButton47.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jRadioButton47.setText("5");
        jRadioButton47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton47ActionPerformed(evt);
            }
        });

        buttonGroup7.add(jRadioButton48);
        jRadioButton48.setText("6");

        javax.swing.GroupLayout optionP6Layout = new javax.swing.GroupLayout(optionP6);
        optionP6.setLayout(optionP6Layout);
        optionP6Layout.setHorizontalGroup(
            optionP6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionP6Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jRadioButton43)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton44)
                .addGap(2, 2, 2)
                .addComponent(jRadioButton45)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton46)
                .addGap(2, 2, 2)
                .addComponent(jRadioButton47)
                .addGap(0, 0, 0)
                .addComponent(jRadioButton48))
        );
        optionP6Layout.setVerticalGroup(
            optionP6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionP6Layout.createSequentialGroup()
                .addGroup(optionP6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(optionP6Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton43))
                    .addGroup(optionP6Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton44))
                    .addGroup(optionP6Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton45))
                    .addGroup(optionP6Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton46))
                    .addGroup(optionP6Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jRadioButton47))
                    .addGroup(optionP6Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jRadioButton48)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 843, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 843, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 843, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 843, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 843, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 843, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optionP4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(optionP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(optionP2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(optionP5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(optionP6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(optionP3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(optionP1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(optionP2, 0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(optionP3, 0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(optionP4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(optionP5, 0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(optionP6, 0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1070, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(3160, 3160, 3160))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(474, 474, 474)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 701, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
        rating = 1;
        showOptions();
//        ArrayList<String> tp = new ArrayList<String>();
//        tp.add("1 test.");
//tp.add("1 test.");
//tp.add("1 test.");
//
//        qdd = new QuestionDialog(null, true, this, tp );
//            qdd.setVisible(true);  
            
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
        rating = 2;
        showOptions();
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
        rating = 3;
        showOptions();
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
        rating = 4;
        showOptions();
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        // TODO add your handling code here:
        rating = 5;
        showOptions();
    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void jRadioButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton7ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(0, 1);
    }//GEN-LAST:event_jRadioButton7ActionPerformed

    private void jRadioButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton8ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(0, 2);
    }//GEN-LAST:event_jRadioButton8ActionPerformed

    private void jRadioButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton9ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(0, 3);
    }//GEN-LAST:event_jRadioButton9ActionPerformed

    private void jRadioButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton10ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(0, 4);
    }//GEN-LAST:event_jRadioButton10ActionPerformed

    private void jRadioButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton11ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(0, 5);
    }//GEN-LAST:event_jRadioButton11ActionPerformed

    private void jRadioButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton25ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(1, 1);
    }//GEN-LAST:event_jRadioButton25ActionPerformed

    private void jRadioButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton26ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(1, 2);
    }//GEN-LAST:event_jRadioButton26ActionPerformed

    private void jRadioButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton27ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(1, 3);
    }//GEN-LAST:event_jRadioButton27ActionPerformed

    private void jRadioButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton28ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(1, 4);
    }//GEN-LAST:event_jRadioButton28ActionPerformed

    private void jRadioButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton29ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(1, 5);
    }//GEN-LAST:event_jRadioButton29ActionPerformed

    private void jRadioButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton31ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(2, 1);
    }//GEN-LAST:event_jRadioButton31ActionPerformed

    private void jRadioButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton32ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(2, 2);
    }//GEN-LAST:event_jRadioButton32ActionPerformed

    private void jRadioButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton33ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(2, 3);
    }//GEN-LAST:event_jRadioButton33ActionPerformed

    private void jRadioButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton34ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(2, 4);
    }//GEN-LAST:event_jRadioButton34ActionPerformed

    private void jRadioButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton35ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(2, 5);
    }//GEN-LAST:event_jRadioButton35ActionPerformed

    private void jRadioButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton37ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(3, 1);
    }//GEN-LAST:event_jRadioButton37ActionPerformed

    private void jRadioButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton38ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(3, 2);
    }//GEN-LAST:event_jRadioButton38ActionPerformed

    private void jRadioButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton39ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(3, 3);
    }//GEN-LAST:event_jRadioButton39ActionPerformed

    private void jRadioButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton40ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(3, 4);
    }//GEN-LAST:event_jRadioButton40ActionPerformed

    private void jRadioButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton41ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(3, 5);
    }//GEN-LAST:event_jRadioButton41ActionPerformed

    private void jRadioButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton43ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(5, 1);
    }//GEN-LAST:event_jRadioButton43ActionPerformed

    private void jRadioButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton44ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(5, 2);
    }//GEN-LAST:event_jRadioButton44ActionPerformed

    private void jRadioButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton45ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(5, 3);
    }//GEN-LAST:event_jRadioButton45ActionPerformed

    private void jRadioButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton46ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(5, 4);
    }//GEN-LAST:event_jRadioButton46ActionPerformed

    private void jRadioButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton47ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(5, 5);
    }//GEN-LAST:event_jRadioButton47ActionPerformed

    private void jRadioButton49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton49ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(4, 1);
    }//GEN-LAST:event_jRadioButton49ActionPerformed

    private void jRadioButton50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton50ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(4, 2);
    }//GEN-LAST:event_jRadioButton50ActionPerformed

    private void jRadioButton51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton51ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(4, 3);
    }//GEN-LAST:event_jRadioButton51ActionPerformed

    private void jRadioButton52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton52ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(4, 4);
    }//GEN-LAST:event_jRadioButton52ActionPerformed

    private void jRadioButton53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton53ActionPerformed
        // TODO add your handling code here:
        selectOptionPreference(4, 5);
    }//GEN-LAST:event_jRadioButton53ActionPerformed

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseEntered
        // TODO add your handling code here:
        if(showNextMove){
            jLabel3.setBackground(darkYelloC);
            this.repaint();
        }
    }//GEN-LAST:event_jLabel3MouseEntered

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited
        // TODO add your handling code here:
        if(showNextMove){
            jLabel3.setBackground(yellowC);
            this.repaint();
        }
    }//GEN-LAST:event_jLabel3MouseExited

    private void jLabel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseEntered
        // TODO add your handling code here:
        if(showNextMove){
            jLabel4.setBackground(darkYelloC);
            this.repaint();
        }
    }//GEN-LAST:event_jLabel4MouseEntered

    private void jLabel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseExited
        // TODO add your handling code here:
        if(showNextMove){
            jLabel4.setBackground(yellowC);
            this.repaint();
        }
    }//GEN-LAST:event_jLabel4MouseExited

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
        if (showNextMove) {          
            jLabel3.setBackground(greyC);
            nextPlotPoint(0);
            
        }
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        // TODO add your handling code here:
        if (showNextMove) {     
            jLabel4.setBackground(greyC);
            nextPlotPoint(1);
        }
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseEntered
        // TODO add your handling code here:
        if(showNextMove){
            jLabel5.setBackground(darkYelloC);
            this.repaint();
        }
    }//GEN-LAST:event_jLabel5MouseEntered

    private void jLabel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseExited
        // TODO add your handling code here:
        if(showNextMove){
            jLabel5.setBackground(yellowC);
            this.repaint();
        }
    }//GEN-LAST:event_jLabel5MouseExited

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // TODO add your handling code here:
        if (showNextMove) {  
            jLabel5.setBackground(greyC);
            nextPlotPoint(2);
        }
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseEntered
        // TODO add your handling code here:
        if(showNextMove){
            jLabel6.setBackground(darkYelloC);
            this.repaint();
        }        
    }//GEN-LAST:event_jLabel6MouseEntered

    private void jLabel6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseExited
        // TODO add your handling code here:
        if(showNextMove){
            jLabel6.setBackground(yellowC);
            this.repaint();
        }
    }//GEN-LAST:event_jLabel6MouseExited

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        // TODO add your handling code here:
        if (showNextMove) { 
            jLabel6.setBackground(greyC);
            nextPlotPoint(3);
        }
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseEntered
        // TODO add your handling code here:
        if(showNextMove){
            jLabel7.setBackground(darkYelloC);
            this.repaint();
        }              
    }//GEN-LAST:event_jLabel7MouseEntered

    private void jLabel7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseExited
        // TODO add your handling code here:
        if(showNextMove){
            jLabel7.setBackground(yellowC);
            this.repaint();
        }        
    }//GEN-LAST:event_jLabel7MouseExited

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        // TODO add your handling code here:
        if (showNextMove) {          
            jLabel7.setBackground(greyC);
            nextPlotPoint(4);
        }
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseEntered
        // TODO add your handling code here:
        if(showNextMove){
            jLabel8.setBackground(darkYelloC);
            this.repaint();
        }   
    }//GEN-LAST:event_jLabel8MouseEntered

    private void jLabel8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseExited
        // TODO add your handling code here:
        if(showNextMove){
            jLabel8.setBackground(yellowC);
            this.repaint();
        }    
    }//GEN-LAST:event_jLabel8MouseExited

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        // TODO add your handling code here:
        if (showNextMove) {    
            jLabel8.setBackground(greyC);
            nextPlotPoint(5);
        }
    }//GEN-LAST:event_jLabel8MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea StoryTextArea;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.ButtonGroup buttonGroup7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton11;
    private javax.swing.JRadioButton jRadioButton12;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton25;
    private javax.swing.JRadioButton jRadioButton26;
    private javax.swing.JRadioButton jRadioButton27;
    private javax.swing.JRadioButton jRadioButton28;
    private javax.swing.JRadioButton jRadioButton29;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton30;
    private javax.swing.JRadioButton jRadioButton31;
    private javax.swing.JRadioButton jRadioButton32;
    private javax.swing.JRadioButton jRadioButton33;
    private javax.swing.JRadioButton jRadioButton34;
    private javax.swing.JRadioButton jRadioButton35;
    private javax.swing.JRadioButton jRadioButton36;
    private javax.swing.JRadioButton jRadioButton37;
    private javax.swing.JRadioButton jRadioButton38;
    private javax.swing.JRadioButton jRadioButton39;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton40;
    private javax.swing.JRadioButton jRadioButton41;
    private javax.swing.JRadioButton jRadioButton42;
    private javax.swing.JRadioButton jRadioButton43;
    private javax.swing.JRadioButton jRadioButton44;
    private javax.swing.JRadioButton jRadioButton45;
    private javax.swing.JRadioButton jRadioButton46;
    private javax.swing.JRadioButton jRadioButton47;
    private javax.swing.JRadioButton jRadioButton48;
    private javax.swing.JRadioButton jRadioButton49;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton50;
    private javax.swing.JRadioButton jRadioButton51;
    private javax.swing.JRadioButton jRadioButton52;
    private javax.swing.JRadioButton jRadioButton53;
    private javax.swing.JRadioButton jRadioButton54;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel optionP1;
    private javax.swing.JPanel optionP2;
    private javax.swing.JPanel optionP3;
    private javax.swing.JPanel optionP4;
    private javax.swing.JPanel optionP5;
    private javax.swing.JPanel optionP6;
    // End of variables declaration//GEN-END:variables
}
