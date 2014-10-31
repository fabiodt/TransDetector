package metalanguage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GoogleAPI {
	
	// search returns TRUE if the query dosn't get any result, so it is a random name
	public static boolean search(String query) throws IOException {
		URL url;
		
		try {
			url = new URL(address + query);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			
			String str;
			
			while ((str = in.readLine()) != null) {
				
				Results rs ;
				rs = new Results(str);
				
				if ( rs.exist() )
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			
			in.close();
		}
		catch (Exception e) {
			System.err.println("Internet connection error");
			return false;
		}
		
		return false;
	}

	public static void waitfor() {
		// The algorithm will wait 6 seconds among researches. This is due to Google policies about automatic queries.
		// If there are any problem, it's possible to increase the interval... or buy searches from Google.
		try {Thread.sleep(6000);}
		catch (Exception e) {}
	}
	
	
	private static String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";;

	
	private static class Results {
		   public Results (String str) { 
		                this.str = str ; 
		   }

		   public boolean exist() {
			   if (str == null )
			   {
				   return false;
			   }

			   return  str.contains("GsearchResultClass") ;
		   }


		   private String str;
	}


}

