package com.saigak;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

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
        System.out.println(jtpMain.getText());
        try {
            System.out.println("------------------------------------------------------------------------------");
            System.out.println(new String(jtpMain.getText().getBytes("Cp866")));
            System.out.println("------------------------------------------------------------------------------");
//            System.out.println(new String(jtpMain.getText().getBytes("UTF-8")));
            dialog.setHtml(new String(jtpMain.getText().getBytes("Cp866")));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
    }
}
