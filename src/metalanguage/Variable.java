package metalanguage;

import java.util.Vector;

public class Variable {
	public Variable(String name, int nrLine, int startBrace, int endBrace) {
		this.name = name;
		this.nrLine = nrLine;
		this.startBrace = startBrace;
		this.endBrace = endBrace;
		this.values = new Vector<Integer>();
		this.type = null;
		this.uses = new Vector<Integer>();
		this.strValue = null;
	}
	public Variable(String name, int nrLine ) {
		this.name = name;
		this.nrLine = nrLine;
		this.startBrace = 0;
		this.endBrace = 0;
		this.values = null;
		this.type = null;
		this.uses = new Vector<Integer>();
		this.strValue = null;
	}
	
	
	public String name() {
		return this.name;
	}
	public int nrLine() {
		return this.nrLine;
	}
	public int startBrace() {
		return this.startBrace;
	}
	public int endBrace() {
		return this.endBrace;
	}
	public Vector<Integer> values() {
		return values;
	}
	public String type() {
		return this.type;
	}
	public Vector<Integer> uses() {
		return uses;
	}
	public String stringValue() {
		return strValue;
	}
	
	
	public void addStringValue(String value) {
		this.strValue = value;
	}
	public void addDeclaration(int nrLine) {
		values.addElement(nrLine) ;
	}
	public void addType(String type) {
		this.type=type;
	}
	public void addUse(int nrLine) {
		uses.addElement(nrLine) ;
	}
	
	public boolean isValue(int nrLine) {
		for (int i=0 ; i<values.size(); i++ ) {
			if ( values.elementAt(i) == nrLine ) {
				return true;
			}
		}
		
		return false;
	}
	
	
	private String name;
	private int nrLine;
	private int startBrace;
	private int endBrace;
	private Vector<Integer> values;
	private String type;
	private Vector<Integer> uses;
	private String strValue; 
}