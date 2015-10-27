package com.saigak;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by root on 23.10.15.
 */
public class SendEventListener implements ActionListener {
    private JTextPane jtpMain;
    private ConfigureSendEmail dialog;

    public SendEventListener(JTextPane jtpMain, Frame owner) {
        this.jtpMain = jtpMain;
        dialog = new ConfigureSendEmail(owner);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dialog.setVisible(true);
        dialog.setHtml(jtpMain.getText());
    }
}
