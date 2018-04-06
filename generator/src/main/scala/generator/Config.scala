package generator

/**
  * Config case class implemented for the user to be able to specify certain styling details in the generated PDF
  *
  * @param textColor      - The color of the text to be displayed. Ex:. "red"; "green"; "#000"
  * @param formattingType - The type of that part of the text (ex:. Header, List, Table, Text, etc.)
  * @param fontSize       - The size of the text to be displayed. Ex:. 10; 20.2
  * @param textAlignment  - The alignment the displayed text should take. Ex:. "center"; "justify"
  * @param fontFamily     - The desired font to be used in the displayed text. Ex:. "Arial"; "Times New Roman"
  * @param fontWeight     - The desired font weight to be used in the displayed text. Ex:. "bold"; "italic"
  */
case class Config(
                   formattingType: FormattingType = Text(),
                   textColor: String = "",
                   fontSize: Double = -1.0,
                   textAlignment: String = "",
                   fontFamily: String = "",
                   fontWeight: String = ""
                 )

//TODO maybe add margin, padding, border etc
