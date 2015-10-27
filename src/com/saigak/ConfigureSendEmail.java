package com.saigak;

import com.sun.mail.smtp.SMTPMessage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by root on 10/23/15.
 */
public class ConfigureSendEmail extends JDialog {

    private JButton send;
    private JButton cancel;
    private JTextField companyName;
    private JTextField subject;
    private JFileChooser fileChooser;
    private JButton openFile;
    private JPanel panel;
    private JLabel countEmail;
    private java.util.List<String> emails = new ArrayList<>();
    private JProgressBar progressBar;

    private String html;

    public ConfigureSendEmail(Frame owner) throws HeadlessException {
        super(owner);
        setSize(250, 280);
        setLocation(400, 200);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        initUIComponent();
        addEvents();

        add(panel, BorderLayout.CENTER);
        setResizable(false);
    }

    private void initUIComponent() {

        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 10, 10);
        panel = new JPanel(flowLayout);

        send = new JButton("Send");
        send.setPreferredSize(new Dimension(223, 24));
        send.setEnabled(false);
        companyName = new JTextField(20);
        subject = new JTextField(20);
        companyName.setPreferredSize(new Dimension(200, 20));
        subject.setPreferredSize(new Dimension(200, 20));

        fileChooser = new JFileChooser();
        openFile = new JButton("Open file");
        openFile.setPreferredSize(new Dimension(223, 24));

        countEmail = new JLabel("Valid emails number: " + 0);
        progressBar = new JProgressBar(0, emails.size());
        progressBar.setPreferredSize(new Dimension(223, 24));
        progressBar.setValue(0);
        progressBar.setVisible(false);
        cancel = new JButton("Close");
        cancel.setPreferredSize(new Dimension(223, 24));
        panel.add(new JLabel("Sender name:"));
        panel.add(companyName);
        panel.add(new JLabel("Subject:"));
        panel.add(subject);
        panel.add(countEmail);
        panel.add(openFile);
        panel.add(send);
        panel.add(cancel);
        panel.add(progressBar);
    }

    private void addEvents() {
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (companyName.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Please, set the sender name", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (subject.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Please, set the subject", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                EmailUtils email = new EmailUtils();

//                System.out.println("Send email" + html);
                try {
                    MimeMultipart mimeMultipart = email.buildMultipart(html);
                    SMTPMessage m = new SMTPMessage(email.buildGoogleSession());
                    m.setFrom(new InternetAddress("mail@companyxyz.com", companyName.getText()));
                    m.setContent(mimeMultipart);
                    m.setSubject(subject.getText());
//                            progressBar.setMaximum(600);

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            boolean isValieEnd = true;
                            progressBar.setVisible(true);
//                            progressBar.setIndeterminate(true);
                            send.setEnabled(false);
                            openFile.setEnabled(false);
                            cancel.setEnabled(false);
                            setSize(250, 320);
                            try {
                                for (String s : emails) {
                                    email.send(m, s);
                                    progressBar.setValue(progressBar.getValue() + 1);
                                }
                                JOptionPane.showMessageDialog(panel, "All send completed!", "Information", JOptionPane.INFORMATION_MESSAGE);

                            } catch (MessagingException e1) {
                                e1.printStackTrace();
                                JOptionPane.showMessageDialog(panel, "Error when send email..", "Error", JOptionPane.ERROR_MESSAGE);
                                isValieEnd = false;
                            } finally {
                                emails.clear();
//                                        if(!isValieEnd) {
                                countEmail.setText("Valid emails number: " + 0);
                                setSize(250, 280);
                                progressBar.setValue(0);
//                                progressBar.setIndeterminate(false);
                                progressBar.setVisible(false);
                                send.setEnabled(false);
                                openFile.setEnabled(true);
                                cancel.setEnabled(true);
                                if (isValieEnd) {
                                    dispose();
                                }
                            }
//                                    }
                        }
                    };

                    new Thread(runnable).start();


                } catch (MessagingException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Error when send email..", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Error when send email..", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(panel, "Error when send email..", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        emails = ReadFileUtils.readEmails(file);

                        if (!emails.isEmpty()) {
                            send.setEnabled(true);
                            countEmail.setText("Valid emails number: " + emails.size());
                            progressBar.setMaximum(emails.size());
                        } else {
                            send.setEnabled(false);
                            JOptionPane.showMessageDialog(panel, "Nothing to send", "Error", JOptionPane.ERROR_MESSAGE);
                        }


                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(panel, "Could not open file", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(panel, "Could not open file", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (InvalidFormatException e1) {
                        JOptionPane.showMessageDialog(panel, "Could not open file", "Error", JOptionPane.ERROR_MESSAGE);
                        e1.printStackTrace();
                    }
                } else {
                }
            }
        });

    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
