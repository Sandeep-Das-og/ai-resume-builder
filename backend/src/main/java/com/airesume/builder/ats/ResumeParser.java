package com.airesume.builder.ats;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component
public class ResumeParser {
  private final AtsProperties properties;
  private final Tika tika = new Tika();

  public ResumeParser(AtsProperties properties) {
    this.properties = properties;
  }

  public String parse(InputStream inputStream) throws IOException {
    BodyContentHandler handler = new BodyContentHandler(properties.maxParsedChars());
    AutoDetectParser parser = new AutoDetectParser();
    ParseContext context = new ParseContext();
    try {
      parser.parse(inputStream, handler, new org.apache.tika.metadata.Metadata(), context);
      return handler.toString();
    } catch (SAXException | TikaException e) {
      throw new IOException("Failed to parse resume.", e);
    }
  }

  public String detectContentType(byte[] bytes) throws IOException {
    return tika.detect(bytes);
  }
}
