package generator

import java.io._
import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.tool.xml.XMLWorkerHelper
import org.json4s._
import FileHandler._
import org.json4s.native.JsonMethods._
import scalatags.Text.all._

/**
  * Singleton object that implements the functions regarding the pdf file generation
  */
object Generator {

  /**
    * Method that receives a Map[String,Any] and converts it into a HTML file, and then into a PDF file.
    * This method overload implements the user decision to not send any additional CSS
    *
    * @param jsonMap - The Map to be converted into a PDF document
    * @return a Boolean saying if the conversion from JSON to PDF was successful or not
    */
  def convertMapToPDF(jsonMap: Map[String, Any]): Boolean = {
    val htmlURI = writeHTML(Some(jsonMap)).getOrElse("")
    convertHTMLToPDF(htmlURI)
  }

  /**
    * Method that receives a Map[String,Any] and converts it into a HTML file, and then into a PDF file.
    * This method overload implements the user decision to send an additional CSS file to be included in the HTML file
    *
    * @param jsonMap - The Map to be converted into a PDF document
    * @param cssFile - The additional CSS file to be included in the HTML file
    * @return a Boolean saying if the conversion from JSON to PDF was successful or not
    */
  def convertMapToPDF(jsonMap: Map[String, Any], cssFile: File): Boolean = {
    val htmlURI = writeHTML(Some(jsonMap), cssFile).getOrElse("")
    convertHTMLToPDF(htmlURI)
  }

  /**
    * Method that receives a Map[String,Any] and converts it into a HTML file, and then into a PDF file.
    * This method overload implements the user decision to send an additional String containing the desired CSS to be included
    *
    * @param jsonMap   - The Map to be converted into a PDF document
    * @param cssString - The additional String containing the the CSS to be included in the HTML file
    * @return a Boolean saying if the conversion from JSON to PDF was successful or not
    */
  def convertMapToPDF(jsonMap: Map[String, Any], cssString: String): Boolean = {
    val htmlURI = writeHTML(Some(jsonMap), cssString).getOrElse("")
    convertHTMLToPDF(htmlURI)
  }

  /**
    * Method that receives a Map[String,Any] and converts it into an HTML file, and then into a PDF file.
    * This method overload implements the user decision to send an additional config file specifying simple styling details to be implemented
    *
    * @param jsonMap - The Map to be converted into a PDF document
    * @param config  - The config specifying simple styling details to be implemented in the PDF conversion
    * @return a Boolean saying if the conversion from JSON to PDF was successful or not
    */
  def convertMapToPDF(jsonMap: Map[String, Any], config: Config): Boolean = {
    val htmlURI = writeHTML(Some(jsonMap), config).getOrElse("")
    convertHTMLToPDF(htmlURI)
  }

  /**
    * Method that receives a JSON string and parses it into a Map[String,Any]
    *
    * @param json - The JSON string to be parsed
    * @return a Map[String,Any] with the information parsed from the JSON
    */
  def convertJSONtoMap(json: String): Option[Map[String, Any]] = {
    try {
      Some(parse(json).values.asInstanceOf[Map[String, Any]])
    } catch {
      case e: Exception => e.printStackTrace(); None
    }
  }

  /**
    * Method that receives a JSON string parses it, converts the result into an HTML and then into a PDF file
    * This method overload implements the user decision to not send any additional CSS
    *
    * @param json - The Json string to be converted into a PDF document
    * @return a Boolean saying if the conversion from JSON to PDF was successful or not
    */
  def convertJSONtoPDF(json: String): Boolean = {
    val jsonMap = convertJSONtoMap(json)
    val htmlURI = writeHTML(jsonMap).getOrElse("")
    convertHTMLToPDF(htmlURI)

  }

  /**
    * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
    * This method overload implements the user decision to send an additional CSS file to be included in the HTML file
    *
    * @param json    - The Json string to be converted into a PDF document
    * @param cssFile - The additional CSS file to be included in the HTML file
    * @return a Boolean saying if the conversion from JSON to PDF was successful or not
    */
  def convertJSONtoPDF(json: String, cssFile: File): Boolean = {
    val jsonMap = convertJSONtoMap(json)
    val htmlURI = writeHTML(jsonMap, cssFile).getOrElse("")
    convertHTMLToPDF(htmlURI)
  }

  /**
    * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
    * This method overload implements the user decision to send an additional String containing the desired CSS to be included
    *
    * @param json      - The Json string to be converted into a PDF document
    * @param cssString - The additional String containing the the CSS to be included in the HTML file
    * @return a Boolean saying if the conversion from JSON to PDF was successful or not
    */
  def convertJSONtoPDF(json: String, cssString: String): Boolean = {
    val jsonMap = convertJSONtoMap(json)
    val htmlURI = writeHTML(jsonMap, cssString).getOrElse("")
    convertHTMLToPDF(htmlURI)
  }

  /**
    * Method that receives a Map[String,Any] and converts it into an HTML file, and then into a PDF file.
    * This method overload implements the user decision to send an additional config file specifying simple styling details to be implemented
    *
    * @param json   - The JSON string to be converted into a PDF document
    * @param config - The config specifying simple styling details to be implemented in the PDF conversion
    * @return a Boolean saying if the conversion from JSON to PDF was successful or not
    */
  def convertJSONtoPDF(json: String, config: Config): Boolean = {
    val jsonMap = convertJSONtoMap(json)
    val htmlURI = writeHTML(jsonMap, config).getOrElse("")
    convertHTMLToPDF(htmlURI)
  }

  /**
    * Method that implements the conversion from a HTML file to a PDF file.
    * This method is called by all convertJSONtoPDF overloads
    *
    * @param htmlURI - The HTML URI specifying where the this document is placed
    * @return a Boolean saying if the conversion from HTML to PDF was successful or not
    */
  private def convertHTMLToPDF(htmlURI: String): Boolean = {
    if (htmlURI.nonEmpty) {
      try {
        val document = new Document()
        val writer = PdfWriter.getInstance(document, new FileOutputStream("html.pdf")) //TODO change destination folder to something else
        document.open()
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(htmlURI))
        document.close()
        cleanHTMLDir()
        true
      } catch {
        case e: Exception => e.printStackTrace(); false
      }
    } else false
  }

  /**
    * Method that calls createHTML. This method overload implements the user decision of not sending additional CSS
    *
    * @param jsonMap - The JSON string to be parsed
    * @return An Option wrapping the URI of the created HTML file. Returns None in case of exception when parsing the JSON String
    */
  private def writeHTML(jsonMap: Option[Map[String, Any]]): Option[String] = {
    createHtml(jsonMap)
  }

  /**
    * Method that calls createHTML. This method overload implements the user decision to send an additional CSS file
    *
    * @param jsonMap - The JSON string to be parsed
    * @param cssFile - the URI of the created html file
    * @return An Option wrapping the URI of the created HTML file. Returns None in case of exception when parsing the JSON String
    */
  private def writeHTML(jsonMap: Option[Map[String, Any]], cssFile: File): Option[String] = {
    val bufferedSourceOption = loadCSSFile(cssFile)
    bufferedSourceOption match {
      case Some(bufferedSource) =>
        val cssStr = bufferedSource.getLines.mkString
        createHtml(jsonMap, cssStr)
      case _ => None
    }
  }

  /**
    * Method that calls createHTML.
    * This method overload implements the user decision to send an additional String containing the desired CSS
    *
    * @param jsonMap   - The JSON string to be parsed
    * @param cssString - The String containing the desired CSS to be included in the HTML file
    * @return An Option wrapping the URI of the created HTML file. Returns None in case of exception when parsing the JSON String
    */
  private def writeHTML(jsonMap: Option[Map[String, Any]], cssString: String): Option[String] = {
    createHtml(jsonMap, cssString)
  }

  /**
    * Method that calls createHTML.
    * This method overload implements the user decision to send an additional Generator.Config object containing simple styling details
    *
    * @param jsonMap - The JSON string to be parsed
    * @param config  - The Generator.Config object containing simple styling details
    * @return An Option wrapping the URI of the created HTML file. Returns None in case of exception when parsing the JSON String
    */
  private def writeHTML(jsonMap: Option[Map[String, Any]], config: Config): Option[String] = {
    val cssString =
      "body{" +
        "font-weight: " + config.fontWeight + ";" +
        " color: " + config.textColor + ";" +
        " font-family: " + config.fontFamily + ";" +
        " text-align: " + config.textAlignment + ";" +
        "font-size: " + config.fontSize + "pt;" +
        "}"
    createHtml(jsonMap, cssString)
  }

  /**
    * Method that implement's the creation and witting of the html file generated from the passed JSON string
    * This method is used by all writeHTML overloads
    *
    * @param jsonMapOpt - An Option wrapping the JSON-Map object
    * @param cssString  - A String containing all the desired CSS to be included in the HTML (to then be transformed to pdf)
    * @return An Option wrapping the URI of the created HTML file. Returns None in case of exception when parsing the JSON String
    */
  private def createHtml(jsonMapOpt: Option[Map[String, Any]], cssString: String = ""): Option[String] = {
    jsonMapOpt match {
      case Some(jsonMap) =>
        val kvParagraph = jsonMap.map { case (k, v) => p(k + " : " + v) }.toList

        val htmlString =
          if (cssString.isEmpty) html(head(), body(kvParagraph)).toString //generate html code with no css
          else {
            val str = html(head(), body(kvParagraph)).toString
            val (left, right) = str.splitAt(12) //split html string at index 12, right in between the <head> tag
            s"$left <style> $cssString </style> $right" //creates the desired HTML code with a <style> tag containing the user-sent css
          }

        val dir = new File("./target/htmlPages")
        if (!dir.exists) dir.mkdirs
        val filePath = "./target/htmlPages/" + System.nanoTime() + ".html"
        val pw = new PrintWriter(new File(filePath)) //prints the HTML code to the html file
        pw.write(htmlString)
        pw.close()
        Some(filePath)
      case None => None
    }
  }

  /**
    * Method that deletes all the files in the htmlPages directory
    */
  private def cleanHTMLDir() {
    val dir = new File("./target/htmlPages")
    if (dir.exists) {
      val files = dir.listFiles.filter(_.isFile).toList
      files.foreach(_.delete)
    }
  }

}


