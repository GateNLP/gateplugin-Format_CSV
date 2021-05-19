/*
 * CSVStreamingOutputHandler.java
 * 
 * Copyright (c) 2015, The University of Sheffield. See the file COPYRIGHT.txt
 * in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * licenced under the GNU Library General Public License, Version 2, June 1991
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 * 
 * Mark A. Greenwood, 5/08/2015
 */

package gate.cloud.io.csv;

import static gate.cloud.io.IOConstants.PARAM_ENCODING;
import static gate.cloud.io.IOConstants.PARAM_FILE_EXTENSION;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Utils;
import gate.cloud.batch.DocumentID;
import gate.cloud.io.AbstractOutputHandler;
import gate.cloud.io.file.StreamingFileOutputHelper;
import gate.util.GateException;

/**
 * GCP output handler that writes CSV files.
 */
public class CSVStreamingOutputHandler extends AbstractOutputHandler {

  public static final String PARAM_SEPARATOR_CHARACTER = "separator";

  public static final String PARAM_QUOTE_CHARACTER = "quote";

  public static final String PARAM_COLUMNS = "columns";

  public static final String PARAM_COLUMN_HEADERS = "columnHeaders";

  public static final String PARAM_ANNOTATION_SET_NAME = "annotationSetName";

  public static final String PARAM_ANNOTATION_TYPE = "annotationType";
  
  public static final String PARAM_CONTAINED_ONLY = "containedOnly";

  protected char separatorChar;

  protected char quoteChar;

  protected String annotationSetName, annotationType;

  /**
   * Column specifications, either .docFeature, annType (= string
   * under annotation) or annType.feature.
   */
  protected String[] columns;
  
  /**
   * Column headings to be written as the first line of each CSV
   * file chunk.  If null, no header row will be written.
   */
  protected String[] columnHeaders;
  
  protected boolean containedOnly;
  
  protected String encoding;
  
  private StreamingFileOutputHelper<String[], ICSVWriter> helper;
  
  public CSVStreamingOutputHandler() {
    helper = new StreamingFileOutputHelper<String[], ICSVWriter>(
        new String[0],
        // opening a new chunk - wrap the stream in a CSVWriter
        stream -> {

          RFC4180Parser parser = new RFC4180ParserBuilder()
            .withSeparator(separatorChar)
            .withQuoteChar(quoteChar).build();

          ICSVWriter w = new CSVWriterBuilder(new OutputStreamWriter(stream, encoding))
            .withParser(parser)
            .withLineEnd(ICSVWriter.RFC4180_LINE_END)
            .build();

          if(columnHeaders != null) w.writeNext(columnHeaders,false);
          return w;
        },
        // write an item
        (ICSVWriter w, String[] item) -> {
          w.writeNext(item,false);
          w.flush();
        },
        (String[] item) -> {
          // approximate the *bytes* written as total number of
          // *characters* in the column values plus 2n quotes and
          // n-1 commas and 1 newline - this is an over-estimate
          // if there are lots of null column values but it's
          // not critical to be super-accurate here anyway
          return Arrays.stream(item).mapToInt(v -> (v == null ? 0 : v.length())).sum() + 3*item.length;
        });
  }

  @Override
  protected void configImpl(Map<String, String> configData) throws IOException,
      GateException {

    if(!configData.containsKey(PARAM_FILE_EXTENSION)) {
      // set the extension to csv if nothing is provided
      configData.put(PARAM_FILE_EXTENSION, ".csv");
    }

    // configuration params for the CSV document output
    encoding =
        configData.containsKey(PARAM_ENCODING)
            ? configData.get(PARAM_ENCODING)
            : "UTF-8";

    separatorChar =
        configData.containsKey(PARAM_SEPARATOR_CHARACTER) ? configData.get(
            PARAM_SEPARATOR_CHARACTER).charAt(0) : CSVWriter.DEFAULT_SEPARATOR;

    quoteChar =
        configData.containsKey(PARAM_QUOTE_CHARACTER)
            ? configData.get(PARAM_QUOTE_CHARACTER).charAt(0)
            : CSVWriter.DEFAULT_QUOTE_CHARACTER;

    // the details of the columns to output
    columns = configData.get(PARAM_COLUMNS).split(",\\s*");
    
    // column headers - if unspecified no headers will be written
    if(configData.containsKey(PARAM_COLUMN_HEADERS)) {
      columnHeaders = configData.get(PARAM_COLUMN_HEADERS).split(",\\s*");
      if(columnHeaders.length != columns.length) {
        throw new GateException("columns and columnHeaders must be lists of the same length");
      }
    }

    // the annotation set to read annotations from
    annotationSetName = configData.get(PARAM_ANNOTATION_SET_NAME);

    // the annotation type to treat as a document, can be null
    annotationType = configData.get(PARAM_ANNOTATION_TYPE);

    // if the annotationType param is empty then nullify the variable so we work
    // at the document level as if the param was missing
    if(annotationType != null && annotationType.trim().equals(""))
      annotationType = null;
    
    // should we only look at annotations contained within the annotationType or
    // do we allow overlapping ones as well?
    containedOnly =
        configData.containsKey(PARAM_CONTAINED_ONLY) ? Boolean
            .parseBoolean(configData.get(PARAM_CONTAINED_ONLY)) : true;
            
    helper.config(configData);
  }

  @Override
  protected void outputDocumentImpl(Document document, DocumentID documentId)
      throws IOException, GateException {

    // create an array to hold the column data

    if(annotationType == null) {
      // if we are producing one row per document then....

      String[] data = new String[columns.length];
      for(int i = 0; i < columns.length; ++i) {
        // get the data for each column
        data[i] = Objects.toString(getValue(columns[i], document, null));
      }
      
      helper.sendItem(data);
    } else {
      // we are producing one row per annotation so find all the annotations of
      // the correct type to treat as documents
      List<Annotation> sorted =
          Utils.inDocumentOrder(document.getAnnotations(annotationSetName).get(
              annotationType));

      for(Annotation annotation : sorted) {
        // for each of the annotations....
        String[] data = new String[columns.length];
        for(int i = 0; i < columns.length; ++i) {
          // get the data for each column
          data[i] = Objects.toString(getValue(columns[i], document, annotation));
        }

        // write the row to the ouput
        helper.sendItem(data);
      }
    }
  }

  /**
   * Get the value from the document given the column key
   */
  private Object getValue(String key, Document document, Annotation within) {

    // split the key on the first .
    String[] parts = key.split("\\.", 2);

    if(key.startsWith(".")) {
      // keys that start with a . are references to document features
      return document.getFeatures().get(parts[1]);
    } else {
      // if it's not a document feature then it refers to an annotation

      // special case - if it's foo or foo.bar and "within" is itself a "foo"
      // just use the "within" annotation itself
      if(within != null && parts[0].equals(within.getType())) {
        return (parts.length == 1) ? Utils.stringFor(document, within) : within.getFeatures().get(parts[1]);
      }
      
      // get all annotations of the correct type (the bit before the .)
      AnnotationSet annots =
          document.getAnnotations(annotationSetName).get(parts[0]);

      if(within != null) {
        // if we have been provided with an annotation to limit the search then
        // get just those either....
        
        if (containedOnly) {
          // contained within the annotation
          annots = Utils.getContainedAnnotations(annots, within);
        }
        else {
          // or partially overlapping with it
          annots = Utils.getOverlappingAnnotations(annots, within);
        }
      }

      // if there are no annotations then we can quit
      if(annots.size() == 0) return null;

      // get the first annotation that matches
      Annotation annotation = Utils.inDocumentOrder(annots).get(0);

      // if we just want the annotation then return it's document content
      if(parts.length == 1) return Utils.stringFor(document, annotation);

      // the key references a feature so return that
      return annotation.getFeatures().get(parts[1]);
    }
  }
  
  @Override
  public void init() throws IOException, GateException {
    helper.init();
  }

  @Override
  public void close() throws IOException, GateException {
    helper.close();
  }
}
