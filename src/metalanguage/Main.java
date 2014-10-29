package metalanguage;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) {

		int transcriptaseIndex;
		String fileName = "testTranscriptase.js";
		try {
			Vector<String> lines = Utils.readFileToVector(fileName);
			Parser variables = new Parser(lines);

			transcriptaseIndex = 0;
			
			// Instruction Permutation 
			if ( instructionPermutation( variables ) == true ) {
				System.out.println("Instruction Permutation: FOUND");
				transcriptaseIndex++;
			}
			else {
				System.out.println("Instruction Permutation: NOT found");
			}
			
			// Name Randomization
			if ( nameRandomization( variables ) ) {
				System.out.println("Name Randomization: FOUND");
				transcriptaseIndex++;
			}
			else {
				System.out.println("Name Randomization: NOT found");
			}
			
			// Meta-Language Symbols
			if ( metaLanguageSymbols( variables ) ) {
				System.out.println("Meta-Language Symbols: FOUND");
				transcriptaseIndex++;
			}
			else {
				System.out.println("Meta-Language Symbols: NOT found");
			}
			
			// Function Insertion
			if ( functionInsertion( lines ) ) {
				System.out.println("Function Insertion: FOUND");
				transcriptaseIndex++;
			}
			else {
				System.out.println("Function Insertion: NOT found");
			}
			
			
			switch (transcriptaseIndex) {
            case 4: 
                System.out.println("\nTRANSCRIPTASE detected!!");
                break;
            case 3: 
            	System.out.println("\nHigh probability of TRANSCRIPTASE!\n");
                break;
            case 2: 
            	System.out.println("\nPossible presence of Transcriptase\n");
                break;
            case 1: 
            	System.out.println("\nWeak possibility of Transcriptase\n");
                break;
            default:
            	System.out.println("\nThe file is Transcriptase free\n");
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
	public static boolean instructionPermutation ( Parser variables ) {
		int i;
		
		Vector<Variable> strings = variables.getStrings();
		
		String pattern = "\\W\\d{1,}\\W\\d{1,}\\W((\\d{1,}\\W)?){1,}";
		
		Pattern p = Pattern.compile(pattern);
		
		for ( i=0; i<strings.size(); i++ ) {
			String line = strings.elementAt(i).stringValue();
			int count = 0;
			int matchingLength;
			int stringLength = line.length();
			float percentageSymbolsChars = 0;
			
			Matcher m = p.matcher( line );
			
			count=0;
			matchingLength=0;
			while ( m.find() ) {
				// It shows the found meta-language
				//System.out.println( m.group() );
				matchingLength += m.group().length();
				count++;
			}
			
			percentageSymbolsChars = ((float)matchingLength/(float)stringLength)*100;
			
			/*
			System.out.printf(  "\nMeta-Language symbols found: "+count
								+"\nTotal symbols characters: "+matchingLength
								+"\nTotal string characters: "+stringLength
								+"\nPercentage: %.2f%%\n\n",percentageSymbolsChars
								);
			*/
			
			if ( percentageSymbolsChars > 10 ) {
				return true;
			}
		}
		return false;
	}

	
	public static boolean nameRandomization ( Parser variables ) throws IOException {
		
		int i;
		int nameRandomization ;
		Random randomGenerator = new Random();
		
		final int nrAnalyzedVar = 3; // Number of analyzed variables
		final int minVarLength = 5; // Minimum variable length 
		final int minNrOfVariables = 3; // If the file contains less than this value of variables, is considered legit
		
		nameRandomization = 0;
			
	    if ( variables.size() < minNrOfVariables ) {
	        return false;
	    }
		
		for ( i=0; i<nrAnalyzedVar; i++ ) {
                    if ( variables.size() > 0 ) {
			int randomIndex = randomGenerator.nextInt(variables.size());
			
			if ( variables.elementAt(i).name().length() >= minVarLength )
			{
				if (GoogleAPI.search(variables.elementAt(randomIndex).name()) ) {
					nameRandomization++;
					if ( i<(nrAnalyzedVar-1) ) {
						GoogleAPI.waitfor();
					}
				}
			}
                    }
                    else {
                        break;
                    }
				
		}
		
		if ( nameRandomization ==  nrAnalyzedVar ) {
			return true; 
		}
		else {
			return false;
		}
	}

	
	public static boolean metaLanguageSymbols ( Parser variables ) {
		int i;
		Vector<Variable> strings = variables.getStrings();
		/*
		 * Pattern explanation:
		 * ([^\\s\\w])		The first character must be a symbol, neither a whitespace, nor a letter/digit 
		 * (.)				The second character can be everything
		 * \\w{1,}			Between the Meta-Language Symbols there could be any digit or letter 
		 * (\\2)			The character must be equal to the second one 
		 * (\\1)			The last symbol must be of the same of the first one.
		 */
		String pattern = "([^\\s\\w])(.)[\\w]{1,}(\\2)(\\1)";
		Pattern p = Pattern.compile(pattern);
		
		for ( i=0; i<strings.size(); i++ ) {
			Matcher m = p.matcher( strings.elementAt(i).stringValue() );
			while ( m.find() ) {
				//System.out.println("Meta Language Symbol: ."+m.group()+"." );
				return true;
			}
		}
		return false;
		
	}

	
	public static boolean functionInsertion ( Vector<String> lines ) {

		int i,j;
		int nrBraces;
		boolean firstBrace;
		int startBrace=0;
		int lastBrace=0;
		int countReturns=0;
		String returnVar = null;
		int k;
		
		String minOperators = "3"; // Minimum number of operators in the definition of the return value to consider it a fake
		
		String pattern = "function {1,}\\w{1,}(.{0,})";
		//String patternIn = "{";
		Pattern pFun = Pattern.compile(pattern);
		
		
		nrBraces=0;
		countReturns = 0;
		firstBrace = true;
		for ( i=0; i<lines.size(); i++ ) {
			Matcher m = pFun.matcher( lines.elementAt(i) );
			if ( m.find() ) {
				j=i;
				// The while will analyze the scope of the function, save the position of the start and end brackets
				// and save in retVar the name of the returning function
				while ( j<lines.size() ) {
					if ( lines.elementAt(j).contains("{") ) {
						if ( firstBrace == true ) {
							startBrace = j;
						}
						nrBraces++;
						firstBrace = false;
						// If the first brace is already been caught (firstBrace=false),
						// the algorithm will exit as soon the nrBraces returns to 0
					} else 	if ( lines.elementAt(j).contains("}") ) {
						nrBraces--;
					}
					
					if ( (nrBraces == 0)  &&  (firstBrace == false) ) {
						lastBrace = j;
						i = j; // it jumps the function 
						firstBrace = true;
						break;
					}
					else {
						// we are within the function yet, we have to analyze the function content
						// row by row
						Pattern pRet = Pattern.compile("return {1,}(\\w{1,})[;\\n]");
						Matcher mRet = pRet.matcher(lines.elementAt(j) );
						if ( mRet.find() ) {
							countReturns++;
							returnVar = mRet.group(1);
							//System.out.println("return: "+returnVar);
							// if it's return 3+43?
						}
					}
					
					j++;
				} // end While j
				
				//System.out.println("\nStart Brace: "+startBrace+"\nReturn: "+returnVar+"\nLastBrace: "+lastBrace);
				
				
				if ( countReturns == 1 ) {
					// If we have more than one return, we'll consider the function as legit 

					k = startBrace;
					// This while will analyze the function searching for the value of the returning variable.
					// If this value is obtain by arithmetic operations (at least three operators) the function
					// will be considered a fake.
					while ( k<=lastBrace ) {
						if ( returnVar == null || returnVar.equals("null")) {
							break;
						}


						Pattern pVal = Pattern.compile(returnVar+" {0,}={1} {0,}(.{1,})[;\\n]");
						Matcher mVal = pVal.matcher(lines.elementAt(k));
						if ( mVal.find() ) {
							//System.out.println( "Operation: "+mVal.group(1) );
							// The pattern will search for minOperators operators within the return value.
							// It is obviously possible to integrate pOp within pVal, but due to the complexity 
							// I preferred to keep them separated. 
							Pattern pOp = Pattern.compile("(([^\\*\\+\\-\\%]{1,}\\*[^\\*\\+\\-\\%]{0,})"
															+"|([^\\*\\+\\-\\%]{0,}\\+[^\\*\\+\\-\\%]{0,})"
															+"|([^\\*\\+\\-\\%]{0,}\\-[^\\*\\+\\-\\%]{0,})"
															+"|([^\\*\\+\\-\\%]{0,}\\%[^\\*\\+\\-\\%]{0,})){"
															+minOperators+",}");
							Matcher mOp = pOp.matcher(mVal.group(1));
							if ( mOp.find() ) {
								return true;
							}
						}
						
						k++;
					}
				}
				countReturns = 0;
			} // end if function
		}// end for i
		
		
		return false;
	}
	
		
	
}
