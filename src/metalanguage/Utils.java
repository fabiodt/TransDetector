package metalanguage;

import java.io.*;
import java.util.*;

public class Utils {
	
	
	public static BufferedReader readFile(String fileName) {
		BufferedReader in = null;
		try {
			in = new BufferedReader ( new FileReader(fileName));
		}
		catch (Exception e0) {
			System.out.println("File not found...");
			System.exit(1);
		}
		
		return in;
	}
	
	@SuppressWarnings("resource")
	public static Vector<String> readFileToVector(String fileName) throws IOException {
		Vector<String> lines = new Vector<String>();
		String input;
		BufferedReader in = null;
		try {
			in = new BufferedReader ( new FileReader(fileName));
		}
		catch (Exception e0) {
			System.out.println("File not found...");
			System.exit(1);
		}
		
		for (input = in.readLine(); input !=null ; input = in.readLine() ) 
		{
			//System.out.println(input);
			lines.addElement( input )	;
		}
		
		return lines;
	}
	
	public static void stampLines(Vector<String> lines) {
		int i;
		for ( i=0; i<lines.size(); i++ )
		{
			System.out.println(lines.elementAt(i));
		}
	}
	
	
	
	public static boolean isNumber(char ch) {
		
		if ( ch >= '0' && ch <= '9' ) {
			return true;
		}
		return false;
	}
	public static boolean isLetter(char ch) {
		// Underscore will be consider a letter
		if ( ( ch >= 'a' && ch <= 'z' ) || ( ch >= 'A' && ch <= 'Z') || ( ch == '_') ) {
			return true;
		}
		return false;
	}
	public static boolean isLogic(char ch) {
		
		if (  ch == '!' ||  ch == '&' || ch == '<' ||  ch == '>' ||  ch == '|' ||  ch == '=' ) {
			return true;
		}
		return false;
	}
	public static boolean isEqual(char ch) {
		
		if ( ch == '=' ) {
			return true;
		}
		return false;
	}
	public static boolean isLexicon(char ch) {
		
		if (  ch == '"' ||  ch == '\'' || ch == '.' ||  ch == '>' ||
			  ch == ';' ||  ch == '\'' || ch == '.' ||  ch == '>') {
			return true;
		}
		return false;
	}
	public static boolean isOperator(char ch) {
		
		if (  ch == '+' ||  ch == '-' || ch == '*' ||  ch == '%' ||  ch == '/' ) {
			return true;
		}
		return false;
	}
	public static boolean isParenthesis(char ch) {
		
		if (  ch == '(' ||  ch == ')' || ch == '[' ||  ch == ']' ||  ch == '{' ||  ch == '}' ) {
			return true;
		}
		return false;
	}

}