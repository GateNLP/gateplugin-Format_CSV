/*
 * CSVStreamingInputHandler.java
 * 
 * Copyright (c) 2015, The University of Sheffield. See the file COPYRIGHT.txt
 * in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * licenced under the GNU Library General Public License, Version 2, June 1991
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 * 
 * Mark A. Greenwood, 27/07/2015
 */

package gate.cloud.io.csv;

import static gate.cloud.io.IOConstants.PARAM_BATCH_FILE_LOCATION;
import static gate.cloud.io.IOConstants.PARAM_ENCODING;
import static gate.cloud.io.IOConstants.PARAM_SOURCE_FILE_LOCATION;
import static gate.cloud.io.IOConstants.VALUE_COMPRESSION_GZIP;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.Map;
import java.util.Set;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvValidationException;

import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.GateConstants;
import gate.cloud.batch.Batch;
import gate.cloud.batch.DocumentID;
import gate.cloud.io.DocumentData;
import gate.cloud.io.IOConstants;
import gate.cloud.io.StreamingInputHandler;
import gate.util.GateException;

public class CSVStreamingInputHandler implements StreamingInputHandler {

  public static final String PARAM_SEPARATOR_CHARACTER = "separator";

  public static final String PARAM_QUOTE_CHARACTER = "quote";

  public static final String PARAM_LABELLED_COLUMNS = "labelledColumns";

  public static final String PARAM_COLUMN = "column";

  public static final String PARAM_TEXT_IS_URL = "textIsURL";
  
  public static final String PARAM_ID = "id";

  private static Logger logger = LoggerFactory
    .getLogger(CSVStreamingInputHandler.class);

  /**
   * Document IDs that are already complete after a previous run of this batch.
   */
  protected Set<String> completedDocuments;

  /**
   * Base directory of the batch.
   */
  protected File batchDir;

  /**
   * The source CSV file from which documents will be streamed.
   */
  protected File srcFile;

  protected CSVReader csvReader;

  protected String encoding;

  protected char separatorChar;

  protected char quoteChar;

  protected long idCounter;

  protected int column;
  
  protected int idColumn = -1;

  protected String[] features;

  protected boolean colLabels;

  protected boolean textIsURL;

  /**
   * Compression applied to the input file. This can be
   * {@link IOConstants#VALUE_COMPRESSION_GZIP} in which case the file will be
   * unpacked using Java's native GZIP support. Any other value is assumed to be
   * a command line to an external command that can accept an additional
   * parameter giving the path to the file and produce the uncompressed data on
   * its standard output, e.g. "lzop -dc" for .lzo compression.
   */
  protected String compression;

  /**
   * External decompression process, if applicable.
   */
  protected Process decompressProcess = null;

  @Override
  public void config(Map<String, String> configData) throws IOException,
    GateException {

    String srcFileStr = configData.get(PARAM_SOURCE_FILE_LOCATION);
    if(srcFileStr == null) {
      throw new IllegalArgumentException("Parameter " +
        PARAM_SOURCE_FILE_LOCATION + " is required");
    } else {
      String batchFileStr = configData.get(PARAM_BATCH_FILE_LOCATION);
      if(batchFileStr != null) {
        batchDir = new File(batchFileStr).getParentFile();
      }
      srcFile = new File(srcFileStr);
      if(!srcFile.isAbsolute()) {
        srcFile = new File(batchDir, srcFileStr);
      }
      if(!srcFile.exists()) { throw new IllegalArgumentException("File \"" +
        srcFile + "\", provided as value for required parameter \"" +
        PARAM_SOURCE_FILE_LOCATION + "\", does not exist!"); }
      if(!srcFile.isFile()) { throw new IllegalArgumentException("File \"" +
        srcFile + "\", provided as value for required parameter \"" +
        PARAM_SOURCE_FILE_LOCATION + "\", is not a file!"); }
    }

    encoding = configData.get(PARAM_ENCODING);
    separatorChar = configData.get(PARAM_SEPARATOR_CHARACTER).charAt(0);
    quoteChar = configData.get(PARAM_QUOTE_CHARACTER).charAt(0);
    colLabels = Boolean.parseBoolean(configData.get(PARAM_LABELLED_COLUMNS));
    column = Integer.parseInt(configData.get(PARAM_COLUMN));
    textIsURL = Boolean.parseBoolean(configData.get(PARAM_TEXT_IS_URL));
    
    if (configData.containsKey(PARAM_ID)) idColumn = Integer.parseInt(configData.get(PARAM_ID));
  }

  @SuppressWarnings("resource")
  @Override
  public void init() throws IOException, GateException {
    InputStream inputStream = null;
    if(compression == null) {
      inputStream = new FileInputStream(srcFile);
    } else if("any".equals(compression)) {
      inputStream = new BufferedInputStream(new FileInputStream(srcFile));
      try {
        inputStream =
          new CompressorStreamFactory()
            .createCompressorInputStream(inputStream);
      } catch(CompressorException e) {
        if(e.getCause() != null) {
          if(e.getCause() instanceof IOException) {
            throw (IOException)e.getCause();
          } else {
            throw new GateException(e.getCause());
          }
        } else {
          // unrecognised signature, assume uncompressed
          logger
            .info("Failed to detect compression format, assuming no compression");
        }
      }
    } else {
      if(VALUE_COMPRESSION_GZIP.equals(compression)) {
        compression = CompressorStreamFactory.GZIP;
      }
      inputStream = new BufferedInputStream(new FileInputStream(srcFile));
      try {
        inputStream =
          new CompressorStreamFactory().createCompressorInputStream(
            compression, inputStream);
      } catch(CompressorException e) {
        if(e.getCause() != null) {
          if(e.getCause() instanceof IOException) {
            throw (IOException)e.getCause();
          } else {
            throw new GateException(e.getCause());
          }
        } else {
          // unrecognised compressor name
          logger
            .info("Unrecognised compression format, assuming external compressor");
          IOUtils.closeQuietly(inputStream);
          // treat compression value as a command line
          ProcessBuilder pb =
            new ProcessBuilder(compression.trim().split("\\s+"));
          pb.directory(batchDir);
          pb.redirectError(Redirect.INHERIT);
          pb.redirectOutput(Redirect.PIPE);
          pb.redirectInput(srcFile);
          decompressProcess = pb.start();
          inputStream = decompressProcess.getInputStream();
        }
      }
    }

    RFC4180Parser parser = new RFC4180ParserBuilder()
      .withSeparator(separatorChar)
      .withQuoteChar(quoteChar).build();

    csvReader = new CSVReaderBuilder(new InputStreamReader(inputStream, encoding))
      .withMultilineLimit(-1)
      .withCSVParser(parser)
      .withKeepCarriageReturn(false)
      .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
      .build();

    try {
		features = (colLabels ? csvReader.readNext() : null);
	} catch (CsvValidationException e) {
		throw new IOException("Unable to read column labels from CSV file", e);
	}

    idCounter = (colLabels ? 1 : 0);

  }

  @Override
  public DocumentData getInputDocument(DocumentID id) throws IOException,
    GateException {

    throw new UnsupportedOperationException(
      "CSVStreamingInputHandler can only operate in streaming mode");
  }

  @Override
  public void startBatch(Batch b) {
    completedDocuments = b.getCompletedDocuments();
    if(completedDocuments != null && completedDocuments.size() > 0) {
      logger.info("Restarting failed batch - " + completedDocuments.size() +
        " documents already processed");
    }
  }

  @Override
  public DocumentData nextDocument() throws IOException, GateException {

    // get the next line from the CSV file
    String[] nextLine;

    try {
		while((nextLine = csvReader.readNext()) != null) {

		  // skip the line if there are less columns than we need to get to the
		  // content
		  if(column >= nextLine.length) continue;

		  // skip the line if the column with the content is empty
		  if(nextLine[column].trim().equals("")) continue;

		  String id = srcFile.getName() + "." + idCounter++;
		  
		  if (idColumn != -1) id = nextLine[idColumn];

		  if(completedDocuments.contains(id)) continue;

		  DocumentID docId = new DocumentID(id);

		  FeatureMap docFeatures = Factory.newFeatureMap();
		  docFeatures.put(GateConstants.THROWEX_FORMAT_PROPERTY_NAME, Boolean.TRUE);

		  if(colLabels) {
		    // copy all the features from the row into a FeatureMap using the
		    // labels from the first line
		    for(int i = 0; i < features.length; ++i) {
		      if(i != column && i < nextLine.length) {
		        docFeatures.put(features[i], nextLine[i]);
		      }
		    }
		  }

		  if (textIsURL && nextLine[column].startsWith("www"))
			  nextLine[column] = "http://"+nextLine[column];

		  FeatureMap docParams = Factory.newFeatureMap();
		  docParams.put(textIsURL ? Document.DOCUMENT_URL_PARAMETER_NAME
		                          : Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME,
		                nextLine[column]);

		  try {
		    Document gateDoc =
		      (Document)Factory.createResource("gate.corpora.DocumentImpl",
		        docParams, docFeatures, id);

		    return new DocumentData(gateDoc, docId);
		  } catch(Exception e) {
		    logger.warn("Error encountered while parsing object with ID " + id +
		      " - skipped", e);
		  }

		}
	} catch (CsvValidationException e) {
		throw new IOException("Unable to read row from CSV file", e);
	}

    return null;
  }

  @Override
  public void close() throws IOException, GateException {
    csvReader.close();
    if(decompressProcess != null) {
      try {
        decompressProcess.waitFor();
      } catch(InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }

  }
}
