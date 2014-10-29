package metalanguage;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	public Parser( Vector<String> lines ) {
		this.lines = lines;
		int i;
		int nrLine;
		this.variables = new Vector<Variable>();
		strings = new Vector<Variable>();
		Vector<Integer> braces = new Vector<Integer>();
		int startBrace = 0;
		int endBrace = 0;
		
		// All the code lines are analyzed. Every time a '}' is found, it's analyzed its  
		// scope in searching for variables declarations. 
		for ( i=0, nrLine=1; i<this.lines.size(); i++, nrLine++ ) {
					
			if ( this.lines.elementAt(i).contains("{") )
			{
				braces.addElement(nrLine);
			}
			if ( this.lines.elementAt(i).contains("}"))
			{
				endBrace=nrLine;
				startBrace=braces.lastElement();
				braces.remove(braces.indexOf(braces.lastElement()));				
				// Remember that startBrace and endBrace point to the lines starting by 1, not 0!!
				// analyze will parse the 
				analyze(this.lines, startBrace,endBrace,variables);
				analyzeType(this.lines, startBrace,endBrace,variables);
				
	
			}

		} // end for LINES
		
		// The last "analyze" calling will consider all the variables that are outside
        // a {scope}
        analyze(this.lines, 0, this.lines.size(), variables);
        analyzeType(this.lines, startBrace,endBrace, variables);
		
		// Filling the "strings" Vector
		for ( i=0; i<variables.size(); i++ ) {
			if ( variables.elementAt(i).type() != null ) {
				if ( variables.elementAt(i).type().equals("String")  ) {
						strings.addElement( variables.elementAt(i) );
				}
			}
			
		}
		
	}
	
	
	// Methods
	public void stamp() {
		int i;
		for( i=0; i<variables.size(); i++ ) {
			System.out.println(variables.elementAt(i).name());
		}
	}
	public void stampType() {
		int i;
		for( i=0; i<variables.size(); i++ ) {
			System.out.println(variables.elementAt(i).name() +"\t\t"+variables.elementAt(i).type() );
		}
	}
	public void stampStrings() {
		int i;
		for( i=0; i<strings.size(); i++ ) {
			System.out.println( strings.elementAt(i).name()+" = "+strings.elementAt(i).stringValue() );
		}
	}
	
	public int size() {
		return variables.size();
	}
	
	public Variable elementAt(int i) {
		return variables.elementAt(i);
	}
	
	public Vector<Variable> getStrings() {
		return strings;
	}
	
	
	
	
	
	
	private Vector<Variable> variables;
	private Vector<String> lines;
	private Vector<Variable> strings;
	private static void analyze(Vector<String> lines, int startBrace, int endBrace, Vector<Variable> variables ) {
		
		int i ;
		int braceFound;
		
		braceFound = 0;
		// Remember that startBrace and endBrace point to the lines starting by 1, not 0!!
		// That is the reason because k doesn't start from startBrace+1
		//  in fact startBrace doesn't contain the {, but the following line
		for (i=startBrace ; i<lines.size() && i<endBrace ; i++)
		{
			if ( lines.elementAt(i).contains("{") )
			{
				braceFound++;
			}
			if ( lines.elementAt(i).contains("}") )
			{
				braceFound--;
			}
			
			if ( braceFound == 0 )
			{
				//line = lines.elementAt(i);
				String patVar = "var ";
				Pattern p = Pattern.compile(patVar);
				Matcher m = p.matcher(lines.elementAt(i));
				if ( m.find() ) {
					/* pattern explanation:
					 * ((var {1,})		The declaration could start with "var" plus a variable number of spaces
					 * |(,{1} {0,}))	 or with a comma plus a variable number of spaces.
					 * ((\\w{1,}|_{1,})	The name must of the variable contains a variable number of characters or underscores.
					 *  {0,}([;\n,=])	The name must end with a semicolon,a newline, a comma or a new line,
					 *  				 possibly precede by white spaces.
					 * 					
					*/					
					p = Pattern.compile("((var {1,})|(,{1} {0,}))((\\w{1,}|_{1,}) {0,}[;\n,=])");
					Matcher m2 = p.matcher(lines.elementAt(i));
					while ( m2.find() ) {
						//System.out.println(m2.group());
						variables.addElement( new Variable(m2.group(5), i+1, startBrace, endBrace ) );
						
					}
				}
			
			} // end if braceFound
		} // end for k
		
		
	} // end Analyze
	
	// The algorithm has been studied to recognize only string literals and save their value.
	private static void analyzeType( Vector<String> lines, int startBrace, int endBrace, Vector<Variable> variables ) {
		int i, j;
		String string;
		String numeric;
		String array;
		String object;
		Pattern pStr;
		Pattern pNum;
		Pattern pArr;
		Pattern pObj;
		Matcher m;
		
		
		for ( i=0; i<variables.size(); i++ ) {
			/* The algorithm recognizes only string literals. It is possibile, for example to add:
			*  string = variables.elementAt(i).name()+" {0,}={1} {0,}(\"|document.getElementById.{1,}\\.value)";
			*  etc.. to complete the algorithm for all type of strings and so for all kind of variables
			*/
			string = variables.elementAt(i).name()+" {0,}={1} {0,}\"(.*{0,})\"";
			numeric = variables.elementAt(i).name()+" {0,}={1} {0,}\\d";
			array = variables.elementAt(i).name()+" {0,}={1} {0,}\\[";
			object = variables.elementAt(i).name()+" {0,}={1} {0,}\\{";
			pStr = Pattern.compile(string);
			pNum = Pattern.compile(numeric);
			pArr = Pattern.compile(array);
			pObj = Pattern.compile(object);
			for ( j=0; j<lines.size(); j++ ) {
				m = pStr.matcher( lines.elementAt(j) );
				if ( m.find() ) {
					variables.elementAt(i).addType("String");
					variables.elementAt(i).addStringValue(m.group(1));;
					break;
				}
				m = pNum.matcher( lines.elementAt(j) );
				if ( m.find() ) {
					variables.elementAt(i).addType("Numeric");
					break;
				}
				m = pArr.matcher( lines.elementAt(j) );
				if ( m.find() ) {
					variables.elementAt(i).addType("Array");
					break;
				}
				m = pObj.matcher( lines.elementAt(j) );
				if ( m.find() ) {
					variables.elementAt(i).addType("Object");
					break;
				}
			}
			
		}
	}
}







