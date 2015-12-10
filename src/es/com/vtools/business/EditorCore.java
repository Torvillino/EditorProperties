package es.com.vtools.business;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

import es.com.vtools.business.model.TreeObject;
import es.com.vtools.business.model.TreeParent;
import es.com.vtools.business.model.Type;

public class EditorCore {

	// Properties almacenado
	private static Properties prop = new Properties();
	private static String path = "";
	
	/**
	 * Metodo que carga el fichero de propiedades
	 * @throws IOException 
	 * 
	 */
	public static void loadProperties(String pathprop){
		path = pathprop;
		try{
			// Cargamos el fichero properties
			prop.load(new FileInputStream(path));
		}catch(Throwable e){
			System.out.println("Error");
		}
	}
	
	/**
	 * Metodo que crea la estructura en arbol
	 */
	public static TreeParent loadStructure(){
		
		// Objecto que contiene el grupo y las propiedades 
		HashMap<String, HashMap<String, String>> mapKey = new HashMap<String,HashMap<String, String>>();

		// Recorremos todas las propiedades
		for (Object element : prop.keySet()) {
			
			// Obtenemos la organizacion a traves del primer .
			String keyOrg = element.toString().substring(0, element.toString().indexOf("."));
			
			// Obtenemos el nombre de la propiedad entre el primer y ultimo punto
			String keyProperties = element.toString().substring(element.toString().indexOf(".") + 1, element.toString().length());
			
			// Agregamos el valor de la propiedad
			keyProperties+= "=" + prop.getProperty(element.toString());
			
			// Si contiene el grupo agregamos la propiedad
			if(mapKey.containsKey(keyOrg)){
				mapKey.get(keyOrg).put(keyProperties, element.toString());
				
			}else{
				
				// Comprobamos si era propiedad cifrada
				if(keyOrg.toUpperCase().startsWith("X_")){
					keyOrg = keyOrg.toUpperCase().replaceFirst("X_", "");
					// Si no contiene el grupo lo creamos y agregamos la propiedad				
					mapKey.put(keyOrg, new HashMap<String,String>());
					mapKey.get(keyOrg).put("X_" + keyProperties, element.toString());
				}else{
					// Si no contiene el grupo lo creamos y agregamos la propiedad				
					mapKey.put(keyOrg, new HashMap<String,String>());
					mapKey.get(keyOrg).put(keyProperties, element.toString());
				}
			}
		}
		
		// Creamos la organizacion padre
		TreeParent root = new TreeParent("Organization");
		root.setType(Type.ROOT);
		for (Entry<String, HashMap<String, String>> mapElement : mapKey.entrySet()) {
			
			// Creamos la organizacion
			TreeParent treeParent = new TreeParent(mapElement.getKey());
			treeParent.setType(Type.FOLDER);
			
			// Por cada propiedad la almacenamos en la organizacion
			for (String iterable_element : mapElement.getValue().keySet()) {
				
				TreeObject treeObj = new TreeObject(iterable_element);
				// Comprobamos el tipo sabiendo si es de cifrado
				if(iterable_element.toUpperCase().startsWith("X_")){
					treeObj.setType(Type.CIFR);
				}else{
					treeObj.setType(Type.PROP);
				}
				
				treeObj.setPropertieKey(mapElement.getKey() + "." + iterable_element);
				treeParent.addChild(treeObj);
			}
			
			// Lo agregamos al padre
			root.addChild(treeParent);
		}
		
		return root;
	}
	
	public static void modifyProperty(String key, String value){
		prop.setProperty(key, value);
	}
	
	public static void reloadProp() throws FileNotFoundException, IOException{
		prop.load(new FileInputStream(path));
	}
	
	/**
	 * Metodo que crea la estructura en arbol
	 */
	public static TreeParent initStructure(){
		
		TreeParent root = new TreeParent("Seleccione un fichero");
		return root;
	}
}
