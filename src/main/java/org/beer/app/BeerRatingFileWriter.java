package org.beer.app;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

public class BeerRatingFileWriter {
	public static void writeFile(String newFileName, List<String> formattedLines) {

		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFileName), "utf-8"))) {
			for (String line : formattedLines) {
				writer.write(line + "\n");
			}
		} catch (IOException e) {
			System.out.println("Exception: " + e);
		}

	}
}
