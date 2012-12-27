package org.systemgo.codegen.swt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * 扩展label，用于制作对话框的说明头
 * 
 * @author gaoht
 * 
 */
public class Title {

	private Composite container;

	public Title(Composite parent, int style) {
		container = new Composite(parent, style);
	}

	private Label title;

	private Label ti;

	private Label image;

	public void setTitle(String title) {
		this.title = new Label(container, SWT.LEFT);
		this.title.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));
		FontData newFontData = this.title.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(12);
		Font newFont = new Font(container.getShell().getDisplay(), newFontData);
		this.title.setFont(newFont);
		this.title.setText(title);
	}

	public void setTitleInstruct(String ti) {

	}

	public void setImage(String imagePath) {

	}
	
	private void setLayout(){
		if(title==null){
			setTitle("");
		}
		if(ti==null){
			setTitleInstruct("");
		}
		if(image==null){
			setImage("");
		}
		
		FormData titleData = new FormData();
		titleData.width = 400;
		titleData.height = 50;
		titleData.top = new FormAttachment(container, 0, SWT.TOP);
		titleData.left = new FormAttachment(container, 0, SWT.LEFT);
		titleData.right = new FormAttachment(container, 0, SWT.RIGHT);
		title.setLayoutData(titleData);
	}

}
