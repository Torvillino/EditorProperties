package es.com.vtools.business.model;

public class TreeObject {
	
	private Type type = null;
	private String text;
	
	// Identificador de la organizacion
	private String keyOrg;
	
	// Identificador de la propiedad solo la ultima palabra
	private String propertieName;
	
	// Identificador de la propiedad
	private String propertieKey;
	
	// Valor de la propiedad
	private String propertieValue;
	
	// Propiedad que indica si es cifrada o no
	private boolean isCypher = false;
		
	// Padre
	private TreeParent parent;
	private TreeIcon icon;
	
	public TreeObject() {}
	
	public TreeObject(String text) {
		this.text = text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	public String getText() {
		return text;
	}
	public void setParent(TreeParent parent) {
		this.parent = parent;
	}
	public TreeParent getParent() {
		return parent;
	}
	public String toString() {
		return getText();
	}
	public <T> T getAdapter(Class<T> key) {
		return null;
	}
	public TreeIcon getIcon() {
		return icon;
	}
	public void setIcon(TreeIcon icon) {
		this.icon = icon;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getPropertieName() {
		return propertieName;
	}
	public void setPropertieName(String propertieName) {
		this.propertieName = propertieName;
	}
	public String getPropertieValue() {
		return propertieValue;
	}
	public void setPropertieValue(String propertieValue) {
		this.propertieValue = propertieValue;
	}
	public String getPropertieKey() {
		return propertieKey;
	}
	public void setPropertieKey(String propertieKey) {
		this.propertieKey = propertieKey;
	}
	public boolean isCypher() {
		return isCypher;
	}
	public void setCypher(boolean isCypher) {
		this.isCypher = isCypher;
	}

	public String getKeyOrg() {
		return keyOrg;
	}

	public void setKeyOrg(String keyOrg) {
		this.keyOrg = keyOrg;
	}
}
