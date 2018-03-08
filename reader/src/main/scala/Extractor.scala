import java.io.File
import java.text.Normalizer

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

import scala.util.matching.Regex

object Extractor {

  type Keyword = String

  //A Map of keywords -> regular expressions that were tailored to obtain better results
  val knownRegEx: Map[Keyword, Regex] = Map(
    "name" -> new Regex("(?:name is|I'm|I am)\\s+((?:[A-Z]\\w+\\s*){1,2})"),
    "age" -> "(?:I'm|I am)\\s+(\\d{1,2})".r,
    "mail" -> "((?:[a-z]|[0-9]|\\.|-|_)+@(?:[a-z]|\\.)+(?:pt|com|org|net|uk|co|eu))".r
  )

  /**
    * Method that given a file path (maybe change to a real file) will load that PDF file and read the text from it
    *
    * @param filePath - String containing the URI for the file to be loaded
    * @return A String containing all the text found in the document
    */
  def readPDF(filePath: String): String = {
    val pdf = PDDocument.load(new File(filePath))
    val document = new PDFTextStripper

    val str = Normalizer.normalize(document.getText(pdf), Normalizer.Form.NFD)
      .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
    pdf.close()
    str
  }

  /**
    * Method that will iterate through a list of given keywords and will try to obtain a value for that keyword
    *
    * @param text        - Text from the PDF extracted from readPDF method
    * @param keywords    - List containing all the keywords we want to find values for
    * @param clientRegEx - Optional parameter - If the client already has a predefined Regular Expression for a given key
    *                    use that regular expression instead of ours
    * @return a List containing pairs of Keywords and a list of values found for that keyword
    */
  def getMatchedValues(text: String, keywords: List[String], clientRegEx: Map[String, String] = null): List[(Keyword, Set[String])] = {
    keywords.map(key => {
      if (knownRegEx.contains(key)) //if we already know a good RegEx for this keyword, use it
        (key, knownRegEx(key).findAllIn(text).matchData.map(_.group(1)).toSet)
      else (key, Set("ups")) //to be changed, here we need to manually search for the keywords in the text
    })
  }

  def makeJSONString(listJSON:List[(Keyword,Set[String])]): String ={
    val str = listJSON.map( k =>
      if(k._2.size>1){
        "\""+k._1+"\":\""+k._2.mkString("[",", ","]")+"\""
      }
      else "\""+k._1+"\": \""+k._2.head+"\""
    )
    println(str)
    str.mkString("{",", ","}")
  }

}
