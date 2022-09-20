import java.util.Scanner; 
import java.io.*;

// Main Class
class Main {

	// Main driver method
	public static void main(String[] args)
		throws IOException
	{

		// Creating two stream
		// one input and other output
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileOutputStream fic = null;

		// Try block to check for exceptions
		try {

			// Initializing both the streams with
			// respective file directory on local machine

			// Custom directory path on local machine
			fis = new FileInputStream(
				"input.txt");

			// Custom directory path on local machine
			fos = new FileOutputStream("output.txt");
			fic = new FileOutputStream("intermediate.txt");

			int c;
            
            Scanner Reader = new Scanner(fis);
            while (Reader.hasNextLine()) {
                String data = Reader.nextLine();
                System.out.println(data);
                
            }
            Scanner mc = new Scanner(fis);
            String data = mc.nextLine();
            while (data == "MACRO"){
                c = fis.read();

				// Writing to output file of the specified
				// directory
				fic.write(c);
			}
			// Condition check
			// Reading the input file till there is input
			// present
			while ((c = fis.read()) != -1) {

				// Writing to output file of the specified
				// directory
				fos.write(c);
			}
			
			// By now writing to the file has ended, so

			// Display message on the console
			System.out.println(
				"copied the file successfully");
		}

		// Optional finally keyword but is good practice to
		// empty the occupied space is recommended whenever
		// closing files,connections,streams
		finally {

			// Closing the streams

			if (fis != null) {

				// Closing the fileInputStream
				fis.close();
			}
			if (fos != null) {

				// Closing the fileOutputStream
				fos.close();
			}
		}
	}
}
