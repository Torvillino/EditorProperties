package es.com.vtools.business.model;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class PropertieDialog extends TitleAreaDialog {

  private Text newTextPropertie;
  private String newPropertie;
  private String oldPropertie;
  private String oldValue;

  public PropertieDialog(Shell parentShell, String oldPropertie, String oldValue) {
	  super(parentShell);
	  this.oldPropertie = oldPropertie;
	  this.oldValue = oldValue;
  }

  @Override
  public void create() {
    super.create();
    setTitle("Modificación");
    setMessage("Nuevo valor para la propiedad " + oldPropertie, IMessageProvider.INFORMATION);
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    GridLayout layout = new GridLayout(2, false);
    container.setLayout(layout);
    createNewPropertie(container);
    return area;
  }

  private void createNewPropertie(Composite container) {
    Label lbtLastName = new Label(container, SWT.NONE);
    lbtLastName.setText("Nuevo valor");
    
    GridData dataNewPropertiee = new GridData();
    dataNewPropertiee.grabExcessHorizontalSpace = true;
    dataNewPropertiee.horizontalAlignment = GridData.FILL;
    newTextPropertie = new Text(container, SWT.BORDER);
    newTextPropertie.setLayoutData(dataNewPropertiee);
  }



  @Override
  protected boolean isResizable() {
    return true;
  }

  // save content of the Text fields because they get disposed
  // as soon as the Dialog closes
  private void saveInput() {
	  newPropertie = newTextPropertie.getText();
  }

  @Override
  protected void okPressed() {
    saveInput();
    super.okPressed();
  }

  public String getNewPropertie() {
    return newPropertie;
  }
}