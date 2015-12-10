package editorproperties.views;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import es.com.vtools.business.EditorCore;
import es.com.vtools.business.model.PropertieDialog;
import es.com.vtools.business.model.TreeObject;
import es.com.vtools.business.model.TreeParent;
import es.com.vtools.business.model.ViewLabelProvider;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class EditorView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "editorproperties.views.EditorView";

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action selectionFile;
	private Action modifyProperties;
	private Action doubleClickAction;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {
		private TreeParent invisibleRoot;

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		
		public void dispose() {
		}
		
		public Object[] getElements(Object parent) {
			
			if (parent.equals(getViewSite())) {
				if (invisibleRoot==null) initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}
		
		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject)child).getParent();
			}
			return null;
		}
		
		public Object [] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent)parent).getChildren();
			}
			return new Object[0];
		}
		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent)parent).hasChildren();
			return false;
		}
		
		/**
		 * Inicializacion del componente
		 */
		private void initialize() {
			invisibleRoot = new TreeParent("");
			invisibleRoot.addChild(EditorCore.initStructure());
		}
		
		/**
		 *  Carga de una nueva estructura
		 */
		public void changeRoot(){
			invisibleRoot = new TreeParent("");
			invisibleRoot.addChild(EditorCore.loadStructure());
		}
	}

	/**
	 * The constructor.
	 */
	public EditorView() {}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "EditorProperties.viewer");
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				EditorView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(modifyProperties);
		manager.add(new Separator());
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(selectionFile);
		manager.add(modifyProperties);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(selectionFile);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		selectionFile = new Action() {
			public void run() {
			
//				IWorkbench wb = PlatformUI.getWorkbench();
//				IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
//				IWorkbenchPage page = window.getActivePage();
//				IEditorPart editor = page.getActiveEditor();
//				IEditorInput input = editor.getEditorInput();

				//showMessage("Action 1 executed");
				FileDialog dialog = new FileDialog(viewer.getControl().getShell(), SWT.OPEN);
				dialog.setFilterExtensions(new String [] {"*.properties"});
				dialog.setFilterPath("D:\\");
				EditorCore.loadProperties(dialog.open());
				ViewContentProvider viewContentProvider = new ViewContentProvider();
				viewContentProvider.changeRoot();
				viewer.setContentProvider(viewContentProvider);
				
			}
		};
		selectionFile.setText("Seleccione un fichero");
		selectionFile.setToolTipText("Seleccione un fichero");
		selectionFile.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER));
		
		
		// Accion para modificar las propiedades
		modifyProperties = new Action() {
			public void run() {		
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
                TreeObject treeObject = (TreeObject)selection.getFirstElement();
                PropertieDialog dialog = new PropertieDialog(viewer.getControl().getShell(), treeObject.getText(), null);
                dialog.create();
                if (dialog.open() ==  Window.OK) {
                	EditorCore.modifyProperty(treeObject.getPropertieKey(), dialog.getNewPropertie());
                	try {
						EditorCore.reloadProp();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
			}
		};
		
		modifyProperties.setText("Modificar");
		modifyProperties.setToolTipText("Modifique una propiedad");
		modifyProperties.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_ETOOL_CLEAR));
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	

//	private ISelectionListener mylistener = new ISelectionListener() {
//        public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
//	        if (selection instanceof IStructuredSelection) {
//	            	System.out.println("hola");
//	            }
//	        }
//    };
}
