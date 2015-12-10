package es.com.vtools.business.model;

public class TreeObject {
	
	private Type type = null;
	private String text;
	private String propertieKey;
	private TreeParent parent;
	private TreeIcon icon;
	
	public TreeObject(String text) {
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
	public String getPropertieKey() {
		return propertieKey;
	}
	public void setPropertieKey(String propertieKey) {
		this.propertieKey = propertieKey;
	}
}
