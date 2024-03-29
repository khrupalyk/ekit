/*
GNU Lesser General Public License

ImageDialog
Copyright (C) 2003 Howard Kistler & other contributors

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package com.hexidec.ekit.component;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

public class ImageDialog extends JDialog implements ActionListener
{
	/** <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= -6443353902954507446L;

	private static final String[] BORDERSIZES = new String[]
	{
		"none",
		"1",
		"2",
		"3",
		"4",
		"5",
		"6",
		"7",
		"8",
		"9",
		"10"
	};

	private static final String[] WRAPS = new String[]
	{
		"none",
		"left",
		"right",
		"top",
		"middle",
		"bottom"
	};

	private ExtendedHTMLEditorKit imageHtmlKit;
	private HTMLDocument imageHtmlDoc;
	private JList wrapJList;
	private JList borderSizeJList;
	private JList imagesJList;
	private JTextField imageAltText;
	private JTextField imageWidth;
	private JTextField imageHeight;
	private JEditorPane previewPane;

	private String   imageDir;
	private String[] imageList;
	private String   previewImage;
	private String   selectedImage;

	/**
	 * Creates and displays a FontSelectorDialog.
	 * @param parent the parent Frame or Dialog of the FontSelectorDialog
	 * @param imageDir the directory where to find the images
	 * @param imageList a list of available images
	 * @param title the dialog title
	 * @param modal whether the dialog should be modal
	 * @return the ImageDialog instance
	 */
	public static ImageDialog open(Window parent, String imageDir, String[] imageList, String title, boolean modal)
	{
		ImageDialog me;
		if (parent instanceof Frame)
		{
			me = new ImageDialog((Frame) parent, imageDir, imageList, title, modal);
		}
		else if (parent instanceof Dialog)
		{
			me = new ImageDialog((Dialog) parent, imageDir, imageList, title, modal);
			me.pack();
			me.setVisible(true);
		}
		else
		{
			throw new IllegalArgumentException("parent must be a Frame or a Dialog");
		}
		return me;
	}

	private ImageDialog(Dialog parent, String imageDir, String[] imageList, String title, boolean modal)
	{
		super(parent, title, modal);
		init(imageDir, imageList);
	}

	/**
	 * @deprecated use {@link #open(Window, String, String[], String, boolean)} instead
	 */
	// TODO reduce visibility to private in next release
	public ImageDialog(Frame parent, String imageDir, String[] imageList, String title, boolean modal)
	{
		super(parent, title, modal);
		init(imageDir, imageList);
		pack();
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("apply"))
		{
			ListSelectionModel sm = imagesJList.getSelectionModel();
			if(sm.isSelectionEmpty())
			{
				SimpleInfoDialog.show(getOwner(), "Error", true, "No image selected", SimpleInfoDialog.ERROR);
				imagesJList.requestFocus();
			}
			else
			{
				if(validateControls())
				{
					previewSelectedImage();
				}
			}
		}	
		if(e.getActionCommand().equals("save"))
		{
			ListSelectionModel sm = imagesJList.getSelectionModel();
			if(sm.isSelectionEmpty())
			{
				SimpleInfoDialog.show(getOwner(), "Error", true, "No image selected", SimpleInfoDialog.ERROR);
				imagesJList.requestFocus();
			}
			else
			{
				if(validateControls())
				{
					previewSelectedImage();
					selectedImage = previewImage;
					hide();
				}
			}
		}
		else if(e.getActionCommand().equals("cancel"))
		{
			hide();
		}
	}

	public void init(String imageDir, String[] imageList)
	{
		this.imageDir = imageDir;
		this.imageList = imageList;

		selectedImage = "";
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		//setBounds(100,100,500,300);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		imagesJList = new JList(imageList);
		imagesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		imagesJList.clearSelection();
		ListSelectionModel lsm = imagesJList.getSelectionModel();

		/* Create the editor kit, document, and stylesheet */
		previewPane = new JEditorPane();
		previewPane.setEditable(false);
		imageHtmlKit = new ExtendedHTMLEditorKit();
		imageHtmlDoc = (HTMLDocument)(imageHtmlKit.createDefaultDocument());
		imageHtmlKit.setDefaultCursor(new Cursor(Cursor.TEXT_CURSOR));
		previewPane.setCaretPosition(0);
		//PreviewPane.getDocument().addDocumentListener(this);
		//StyleSheet styleSheet = ImageHtmlDoc.getStyleSheet();
		//ImageStyleSheet = styleSheet;
		lsm.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e)
			{
				if(!e.getValueIsAdjusting() && validateControls())
				{
					previewSelectedImage();
				}
			}
				
		});

		JScrollPane imageScrollPane = new JScrollPane(imagesJList);
		imageScrollPane.setPreferredSize(new Dimension(200,250));
		imageScrollPane.setMaximumSize(new Dimension(200,250));
		imageScrollPane.setAlignmentX(LEFT_ALIGNMENT);
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
		centerPanel.add(imageScrollPane);
		centerPanel.setBorder(BorderFactory.createTitledBorder("Server Images"));

		/* Set up the text pane */
		previewPane.setEditorKit(imageHtmlKit);
		previewPane.setDocument(imageHtmlDoc);
		previewPane.setMargin(new Insets(4, 4, 4, 4));
		JScrollPane previewViewport = new JScrollPane(previewPane);
		previewViewport.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		previewViewport.setPreferredSize(new Dimension(250,250));
		centerPanel.add(previewViewport); 

		JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
		JPanel altPanel = new JPanel();
		altPanel.setLayout(new BoxLayout(altPanel, BoxLayout.X_AXIS));
		altPanel.add(Box.createHorizontalStrut(10));
		JLabel imageAltTextLabel = new JLabel("Alternate Text:", SwingConstants.LEFT);	  
		altPanel.add(imageAltTextLabel);

		imageAltText = new JTextField("");
		imageAltText.addActionListener(this);
		imageAltText.setPreferredSize(new Dimension(300,25));
		imageAltText.setMaximumSize(new Dimension(600,25));
		altPanel.add(imageAltText);
		altPanel.add(Box.createHorizontalStrut(10));
		controlsPanel.add(altPanel);
		controlsPanel.add(Box.createVerticalStrut(5));

		JPanel dimPanel = new JPanel();
		dimPanel.setLayout(new BoxLayout(dimPanel, BoxLayout.X_AXIS));
		dimPanel.add(Box.createHorizontalStrut(10));
		JLabel imageWidthLabel = new JLabel("Width:", SwingConstants.LEFT);	  
		dimPanel.add(imageWidthLabel);
		imageWidth = new JTextField("");
		imageWidth.setPreferredSize(new Dimension(40,25));
		imageWidth.setMaximumSize(new Dimension(40,25));
		dimPanel.add(imageWidth);
		JLabel imageWidthPixels = new JLabel("pix", SwingConstants.LEFT);	  
		imageWidthPixels.setPreferredSize(new Dimension(20,10));
		dimPanel.add(imageWidthPixels);
		dimPanel.add(Box.createHorizontalStrut(10));
		JLabel imageHeightLabel = new JLabel("Height:", SwingConstants.LEFT);	  
		dimPanel.add(imageHeightLabel);
		imageHeight = new JTextField("");
		imageHeight.setPreferredSize(new Dimension(40,25));
		imageHeight.setMaximumSize(new Dimension(40,25));
		dimPanel.add(imageHeight);
		JLabel imageHeightPixels = new JLabel("pix", SwingConstants.LEFT);	  
		imageHeightPixels.setPreferredSize(new Dimension(20,10));
		dimPanel.add(imageHeightPixels);
		dimPanel.add(Box.createHorizontalStrut(10));

		JLabel wrapLabel = new JLabel("Wrap:", SwingConstants.LEFT);
		dimPanel.add(wrapLabel);
		wrapJList = new JList(WRAPS);
		wrapJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		wrapJList.getSelectionModel().setSelectionInterval(0,0);
		JScrollPane wrapScrollPane = new JScrollPane(wrapJList);
		wrapScrollPane.setAlignmentX(LEFT_ALIGNMENT);
		wrapScrollPane.setPreferredSize(new Dimension(80,40));
		wrapScrollPane.setMaximumSize(new Dimension(80,100));
		dimPanel.add(wrapScrollPane);
		controlsPanel.add(dimPanel);

		/*
		JPanel borderPanel = new JPanel();
		JLabel borderStyleLabel = new JLabel("Style:", SwingConstants.LEFT);
		borderPanel.add(borderStyleLabel);
		BorderList = new JList(Borders);
		BorderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		BorderList.getSelectionModel().setSelectionInterval(0,0);
		JScrollPane borderScrollPane = new JScrollPane(BorderList);
		borderScrollPane.setAlignmentX(LEFT_ALIGNMENT);
		borderScrollPane.setPreferredSize(new Dimension(80,40));
		borderScrollPane.setMaximumSize(new Dimension(80,100));
		borderPanel.add(borderScrollPane);
		borderPanel.add(Box.createHorizontalStrut(5));
		*/

		dimPanel.add(Box.createHorizontalStrut(5));
		JLabel borderSizeLabel = new JLabel("Border Size:", SwingConstants.LEFT);
		dimPanel.add(borderSizeLabel);
		borderSizeJList = new JList(BORDERSIZES);
		borderSizeJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		borderSizeJList.getSelectionModel().setSelectionInterval(0,0);
		JScrollPane borderSizeScrollPane = new JScrollPane(borderSizeJList);
		borderSizeScrollPane.setAlignmentX(LEFT_ALIGNMENT);
		borderSizeScrollPane.setPreferredSize(new Dimension(80,40));
		borderSizeScrollPane.setMaximumSize(new Dimension(80,100));
		dimPanel.add(borderSizeScrollPane);
		dimPanel.add(Box.createHorizontalStrut(10));
		dimPanel.add(Box.createVerticalStrut(10));

		/*
		JLabel borderColorLabel = new JLabel("Color:", SwingConstants.LEFT);
		borderPanel.add(borderColorLabel);
		BorderColorList = new JList(BorderColors);
		BorderColorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane borderColorScrollPane = new JScrollPane(BorderColorList);
		borderColorScrollPane.setAlignmentX(LEFT_ALIGNMENT);
		borderColorScrollPane.setPreferredSize(new Dimension(80,40));
		borderPanel.add(borderColorScrollPane);
		controlsPanel.add(borderPanel);
		*/

		JPanel buttonPanel= new JPanel();
		buttonPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		JButton applyButton = new JButton("Apply");
		applyButton.setActionCommand("apply");
		applyButton.addActionListener(this);

		JButton saveButton = new JButton("Accept");
		saveButton.setActionCommand("save");
		saveButton.addActionListener(this);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);

		buttonPanel.add(applyButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		contentPane.add(centerPanel);
		contentPane.add(controlsPanel);
		contentPane.add(buttonPanel);
    }

    void previewSelectedImage()
    {
		ListSelectionModel sm = imagesJList.getSelectionModel();
		if(!sm.isSelectionEmpty())
		{
			String theImage = imageList[sm.getMinSelectionIndex()];
			try
			{
				// Clear the preview area
				previewPane.setText("");
				StringBuffer attrString = new StringBuffer();
				if(!imageHeight.getText().equals(""))
				{
					attrString.append(" HEIGHT=\"" + imageHeight.getText() + '"');
				}
				if(!imageWidth.getText().equals(""))
				{
					attrString.append(" WIDTH=\"" + imageWidth.getText() + '"');
				}
				if(!imageAltText.getText().equals(""))
				{
					attrString.append(" ALT=\"" + imageAltText.getText() + '"');
				}
				if(!wrapJList.getSelectionModel().isSelectionEmpty())
				{
					String theWrap = WRAPS[wrapJList.getSelectionModel().getMinSelectionIndex()];
					if(!theWrap.equals("none"))
					{
						attrString.append(" ALIGN=\"" + theWrap + '"');
					}
				}
				/*
				if(!BorderList.getSelectionModel().isSelectionEmpty())
				{
					String theBorder = Borders[BorderList.getSelectionModel().getMinSelectionIndex()];
					if(!theBorder.equals("none"))
					{
				*/
				String borderSize = null;
				if(!borderSizeJList.getSelectionModel().isSelectionEmpty())
				{
					borderSize = BORDERSIZES[borderSizeJList.getSelectionModel().getMinSelectionIndex()];
					if(!borderSize.equals("none"))
					{
						attrString.append(" BORDER=" + borderSize);
					}
				}
				else
				{
					borderSize = BORDERSIZES[0];
				}
				/*
						if(!BorderColorList.getSelectionModel().isSelectionEmpty())
						{
							borderColor = BorderColors[BorderColorList.getSelectionModel().getMinSelectionIndex()];						
						}
						else
						{
							borderColor = "gray";
						}
						attrString.append(" STYLE=\"border: " + borderColor + " " + borderSize + "px " + theBorder + "\"");
					}
				}
				*/
				previewImage = "<IMG SRC=" + imageDir + "/" + theImage + " " + attrString.toString() + ">";
				imageHtmlKit.insertHTML(imageHtmlDoc, 0, previewImage, 0, 0, HTML.Tag.IMG);
				repaint();
			}
			catch(Exception ex)
			{
				System.err.println("Exception previewing image");
			}
		}
	}

	boolean validateControls()
	{
		boolean result = true;
		if(!imageWidth.getText().equals(""))
		{
			try
			{
				Integer.parseInt(imageWidth.getText());
			}
			catch (NumberFormatException e)
			{
				result = false;
				SimpleInfoDialog.show(getOwner(), "Error", true, "Image Width is not an integer", SimpleInfoDialog.ERROR);
				imageWidth.requestFocus();
			}
		}
		if( result && !imageHeight.getText().equals(""))
		{
			try
			{
				Integer.parseInt(imageHeight.getText());
			}
			catch (NumberFormatException e)
			{
				result = false;
				SimpleInfoDialog.show(getOwner(), "Error", true, "Image Height is not an integer", SimpleInfoDialog.ERROR);
				imageHeight.requestFocus();
			}
		}
		return result;
	}

    public String getSelectedImage()
    {
	  return selectedImage;
    }	
}
