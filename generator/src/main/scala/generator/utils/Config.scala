package generator.utils

/**
  * Config case class implemented for the user to be able to specify certain styling details in the generated PDF
  *
  * @param color     - The color of the text to be displayed. Ex:. "red"; "green"; "#000"
  * @param fontSize      - The size of the text to be displayed. Ex:. "10"; "20.2"
  * @param textAlignment - The alignment the displayed text should take. Ex:. "center"; "justify"
  * @param fontFamily    - The desired font to be used in the displayed text. Ex:. "Arial"; "Times New Roman"
  * @param fontWeight    - The desired font weight to be used in the displayed text. Ex:. "bold"; "italic"
  */
case class Config(
                   color: String = "",
                   fontSize: String = "",
                   textAlignment: String = "",
                   fontFamily: String = "",
                   fontWeight: String = ""
                 ) {
  //  require(isAllDigits(fontSize))
  //
  //  private def isAllDigits(fontSize: String): Boolean = {
  //    if (fontSize.nonEmpty) {
  //      try {
  //        fontSize.toDouble; true
  //      } catch {
  //        case _: Exception => false
  //      }
  //    } else true //In case fontSize is "" (default value) let it pass
  //  }
}
