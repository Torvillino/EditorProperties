package es.com.vtools.business;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import es.com.vtools.business.model.TreeObject;
import es.com.vtools.business.model.TreeParent;
import es.com.vtools.business.model.Type;

public class EditorCore {

	// Properties almacenado
	private static Properties prop;
	private static String path = "";
	
	/**
	 * Metodo que carga el fichero de propiedades
	 * @throws IOException 
	 * 
	 */
	public static void loadProperties(String pathprop){
		path = pathprop;
		try{
			prop = new Properties();
			// Cargamos el fichero properties
			prop.load(new FileInputStream(path));
		}catch(Throwable e){
			System.out.println("Error");
		}
	}
	
	/**
	 * Metodo que recupera el fichero properties, y con las propiedades empieza a montar
	 * un arbol
	 * 
	 */
	public static TreeParent loadStructure(){
		
		// Objecto que contiene el grupo y las propiedades 
		ArrayList<TreeObject> list = new ArrayList<TreeObject>();

		// Recorremos todas las propiedades
		for (Object element : prop.keySet()) {
			// Creamos el objecto
			TreeObject treeObject = new TreeObject();
			
			// Obtenemos la organizacion a traves del primer .
			String keyOrg = element.toString().substring(0, element.toString().indexOf("."));
			
			// Obtenemos el nombre de la propiedad entre el primer y ultimo punto
			String nameProperties = element.toString().substring(element.toString().indexOf(".") + 1, element.toString().length());
			nameProperties+= "=" + prop.getProperty(element.toString());

			// Establecemos los valores
			treeObject.setKeyOrg(keyOrg);
			treeObject.setPropertieKey(element.toString());
			treeObject.setPropertieName(nameProperties);
			treeObject.setText(nameProperties);
			
			// Comprobamos si era propiedad cifrada
			if(keyOrg.toUpperCase().startsWith("X_")){
				treeObject.setKeyOrg(keyOrg.replaceFirst("X_", ""));
				treeObject.setType(Type.CIFR);
			}else{
				treeObject.setType(Type.PROP);
			}
			list.add(treeObject);
		}
		
		// Creamos la organizacion padre
		TreeParent root = new TreeParent("Organization");
		root.setType(Type.ROOT);
		
		Set<String> listOrg = new LinkedHashSet<String>();
		
		// Recuperamos un listado unico
		for (TreeObject treeObject : list) {
			listOrg.add(treeObject.getKeyOrg());
		}
		
		// Creamos subcarpetas por organizaciones
		for (String idOrg : listOrg) {
			TreeParent orgTreeObject = new TreeParent(idOrg);
			orgTreeObject.setType(Type.FOLDER);
			root.addChild(orgTreeObject);
			
			// Creamos los hijos de cada organizacion
			for (TreeObject treeObject : list) {
				if(treeObject.getKeyOrg().equals(idOrg)){
					orgTreeObject.addChild(treeObject);
				}
			}
		}
		
		return root;
	}
	
	/**
	 * Metodo que modifica una propiedad
	 * y lo guarda en el fichero de propiedades
	 * 
	 * @param key
	 * @param value
	 */
	public static void modifyProperty(String key, String value) throws Exception{
		// Cargamos el fichero
		File file = new File(path);          
        try {  
            // Comprobamos si existe  
            if(file.exists()){  
                
            	// Abro un flujo de lectura  
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                StringBuffer lineComplete = new StringBuffer();
                String line = "";

                // Recorremos linea a linea
                while((line=bufferedReader.readLine())!=null) {   
                    
                	// Comprobamos si la linea coincide con la clave
                    if(line.startsWith(key)) {
                    	// En este caso cambiamos el valor por el del nuevo valor
                    	line = key + "=" + value;
                    }   
                    // Almacenamos la linea
                    lineComplete.append(line + "\n");
                }
                
                // Guardamos el fichero con la nueva informacion
    			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"UTF8"));
    			bw.write(lineComplete.toString());
    			bw.close();
                
    			// Deberiamos de guardar la posicion
                
            }else{  
                // Todo excepcion  
            }  
        } catch (Exception ex) {  
            /*Captura un posible error y le imprime en pantalla*/   
             System.out.println(ex.getMessage());  
        }  
	}
	
	/**
	 * Metodo que realiza la recarga del fichero de propiedades
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void reloadProp() throws FileNotFoundException, IOException{
		prop = new Properties();
		prop.load(new FileInputStream(path));
	}
	
	/**
	 * Metodo que crea la estructura inicial del arbol
	 */
	public static TreeParent initStructure(){
		TreeParent root = new TreeParent("Seleccione un fichero");
		return root;
	}
}
