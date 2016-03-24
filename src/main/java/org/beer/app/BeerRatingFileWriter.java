package org.beer.app;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeerRatingFileWriter {

	private static final Logger LOG = LoggerFactory.getLogger(BeerRatingFileWriter.class);

	private BeerRatingFileWriter() {
	}

	public static void writeFile(String newFileName, List<String> formattedLines) {

		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFileName), "utf-8"))) {
			for (String line : formattedLines) {
				// LOG.debug("Line to write: " + line);
				writer.write(line + "\n");
			}
		} catch (IOException e) {
			System.out.println("Exception: " + e);
		}
	}
}
