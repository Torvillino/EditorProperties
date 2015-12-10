package es.com.vtools.business.model;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class ViewLabelProvider extends StyledCellLabelProvider  {

	public String getText(Object obj) {
		return obj.toString();
	}
	
	public Image getImage(Object obj) {
		
		//"icons/editor.png"
		TreeObject treeObj = (TreeObject) obj;
		String imageKey = "";
		if(treeObj.getType() != null){
			switch (treeObj.getType()) {
				case ROOT:
					imageKey = ISharedImages.IMG_ETOOL_HOME_NAV;
					break;
					
				case FOLDER:
					imageKey = ISharedImages.IMG_OBJ_FOLDER;
					break;
		
				case PROP:
					imageKey = ISharedImages.IMG_OBJ_ELEMENT;
					break;
					
				case CIFR:
					imageKey = ISharedImages.IMG_OBJ_ADD;
					break;
					
				case TITLE:
					imageKey = ISharedImages.IMG_ELCL_SYNCED_DISABLED;
					break;
				
				default:
					imageKey = ISharedImages.IMG_ETOOL_HOME_NAV;
					break;
			}
		}else{
			imageKey = ISharedImages.IMG_ETOOL_HOME_NAV;
		}
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
	
	/**
	 * Me todo que ejecutara la actualizacion de la tabla
	 * 
	 */
	public void update(ViewerCell cell) {
		Object obj = cell.getElement();
		cell.setText(getText(obj));
		cell.setImage(getImage(obj));
		
		// Si contine un igual es una propertie
		if(obj.toString().contains("=")){
			RGB rgb = new RGB(181, 140, 66);
			org.eclipse.swt.graphics.Color color = new Color(null, rgb);
			StyleRange myStyledRange = 
					new StyleRange(
							obj.toString().indexOf("=") + 1, obj.toString().length(), 
							color, null); 
			
			StyleRange[] range = { myStyledRange };
			cell.setStyleRanges(range);
		}
	}
}