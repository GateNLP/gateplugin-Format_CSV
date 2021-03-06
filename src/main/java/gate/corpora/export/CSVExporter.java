package gate.corpora.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Objects;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.CorpusExporter;
import gate.Document;
import gate.FeatureMap;
import gate.Utils;
import gate.creole.metadata.AutoInstance;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;

@CreoleResource(name = "CSV Exporter",
comment = "Export documents and corpora in CSV format",
tool = true, autoinstances = @AutoInstance, icon = "CSVFile" )
public class CSVExporter extends CorpusExporter {

  private static final long serialVersionUID = -4584400521422513882L;
  
  public CSVExporter() {
    super("Comma Separated Values (CSV)", "csv", "text/csv");
  }

  @RunTime
  @CreoleParameter(defaultValue = "UTF-8", comment = "The encoding used to store the file")
  public void setEncoding(String encoding) { }
  public String getEncoding() { return null; }
  
  @RunTime
  @Optional
  @CreoleParameter(comment = "Produce one row per annotation of this type, or one row per document if this is not set")
  public void setAnnotationType(String annotationType) { }
  public String getAnnotationType() { return null; }
  
  @RunTime
  @Optional
  @CreoleParameter(comment = "The annotation set from which to read annotations")
  public void setAnnotationSetName(String annotationSetName) { }
  public String getAnnotationSetName() { return null; }
  
  @RunTime
  @CreoleParameter(defaultValue = "true", comment = "if true only use annotations strictly within the row annotation, else allow those which partially overlap")
  public void setContainedOnly(Boolean containedOnly) { }
  public Boolean getContainedOnly() { return Boolean.FALSE; }
   
  @RunTime
  @CreoleParameter(defaultValue = "\"")
  public void setQuoteCharacter(String quoteCharacter) { }
  public String getQuoteCharacter() { return null; }

  @RunTime
  @CreoleParameter(defaultValue = ",")
  public void setSeparatorCharacter(String sepCharacter) { }
  public String getSeparatorCharacter() { return null; }

  @RunTime
  @CreoleParameter(comment = "list of columns to produce. these are defined as <Annotation>.<Feature>. If you have just .<Feature> then that is assumed to be a document feature, whereas just <Annotation> is the text under the annotation")
  public void setColumns(List<String> columns) { }
  public List<String> getColumns() { return null; }
  
  @RunTime
  @Optional
  @CreoleParameter(comment = "The values to use for a header row")
  public void setColumnHeaders(List<String> headers) { }
  public List<String> getColumnHeaders() { return null; }
  
  
  @Override
  public void export(Corpus corpus, OutputStream out, FeatureMap options)
      throws IOException {

    ICSVWriter csvWriter = createWriter(out, options);
        
    for (Document doc : corpus) {
      save(doc,csvWriter,options);
    }
  }

  @Override
  public void export(Document doc, OutputStream out, FeatureMap options)
      throws IOException {
    
    ICSVWriter csvWriter = createWriter(out,options);
    save(doc,csvWriter,options);
  }
  
  private ICSVWriter createWriter(OutputStream out, FeatureMap options)
      throws IOException {
    String encoding = options.containsKey("encoding")
        ? (String)options.get("encoding")
        : "UTF-8";

    char separatorChar = options.containsKey("separatorCharacter")
        ? ((String)options.get("separatorCharacter")).charAt(0)
        : CSVWriter.DEFAULT_SEPARATOR;

    char quoteChar = options.containsKey("quoteCharacter")
        ? ((String)options.get("quoteCharacter")).charAt(0)
        : CSVWriter.DEFAULT_QUOTE_CHARACTER;

    RFC4180Parser parser = new RFC4180ParserBuilder()
      .withSeparator(separatorChar)
      .withQuoteChar(quoteChar).build();

    ICSVWriter csvWriter = new CSVWriterBuilder(new OutputStreamWriter(out, encoding))
      .withParser(parser)
      .withLineEnd(ICSVWriter.RFC4180_LINE_END)
      .build();

    @SuppressWarnings("unchecked")
    List<String> headers = (List<String>)options.get("columnHeaders");
    if(headers != null && !headers.isEmpty()) {
      csvWriter.writeNext(headers.toArray(new String[headers.size()]),false);
    }

    return csvWriter;
  }

  private void save(Document document, ICSVWriter csvWriter, FeatureMap options)
      throws IOException {

    String annotationType = (String)options.get("annotationType");
    @SuppressWarnings("unchecked")
    String[] columns =
        ((List<String>)options.get("columns")).toArray(new String[]{});
    String annotationSetName = (String)options.get("annotationSetName");
    Boolean containedOnly = (Boolean)options.get("containedOnly");

    if(annotationType == null) {
      // if we are producing one row per document then....

      String[] data = new String[columns.length];
      for(int i = 0; i < columns.length; ++i) {
        // get the data for each column
        data[i] = Objects.toString(getValue(columns[i], document, null,
            annotationSetName, containedOnly));
      }

      csvWriter.writeNext(data,false);
    } else {
      // we are producing one row per annotation so find all the annotations of
      // the correct type to treat as documents
      List<Annotation> sorted = Utils.inDocumentOrder(
          document.getAnnotations(annotationSetName).get(annotationType));

      for(Annotation annotation : sorted) {
        // for each of the annotations....
        String[] data = new String[columns.length];
        for(int i = 0; i < columns.length; ++i) {
          // get the data for each column
          data[i] = Objects.toString(getValue(columns[i], document, annotation,
              annotationSetName, containedOnly));
        }

        // write the row to the ouput
        csvWriter.writeNext(data,false);
      }
    }

    csvWriter.flush();
  }

  /**
   * Get the value from the document given the column key
   */
  private Object getValue(String key, Document document, Annotation within,
      String annotationSetName, boolean containedOnly) {

    // split the key on any . that appear
    String[] parts = key.split("\\.", 2);

    if(key.startsWith(".")) {
      // keys that start with a . are references to document features
      return document.getFeatures().get(parts[1]);
    } else {
      // if it's not a document feature then it refers to an annotation

      // special case - if it's foo or foo.bar and "within" is itself a "foo"
      // just use the "within" annotation itself
      if(within != null
          && parts[0].equals(within.getType())) { return (parts.length == 1)
              ? Utils.stringFor(document, within)
              : within.getFeatures().get(parts[1]); }

      // get all annotations of the correct type (the bit before the .)
      AnnotationSet annots =
          document.getAnnotations(annotationSetName).get(parts[0]);

      if(within != null) {
        // if we have been provided with an annotation to limit the search then
        // get just those either....

        if(containedOnly) {
          // contained within the annotation
          annots = Utils.getContainedAnnotations(annots, within);
        } else {
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

}
