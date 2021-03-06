package parser.extraction;

import parser.utils.Specification;
import scala.Option;
import scala.Some;
import scala.collection.JavaConverters;
import scala.util.matching.Regex;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtractorJava {

    /**
     * Method that given a file path (maybe change to a real file) will load that PDF file and read the text from it
     *
     * @param file - File to be loaded and parsed
     * @return A String containing all the text found in the document. Returns None in case of Exception
     */
    public String readPDF(File file) {
        return readPDF(file, true);
    }

    /**
     * Method that given a file path (maybe change to a real file) will load that PDF file and read the text from it
     *
     * @param file - File to be loaded and parsed
     * @return A String containing all the text found in the document. Returns None in case of Exception
     */
    public String readPDF(File file, Boolean readImages) {
        Option<String> readResult = Extractor.readPDF(file, readImages);
        if (readResult.isDefined())
            return readResult.get();
        else
            throw new NullPointerException(); //TODO maybe change this
    }

    /**
     * Method that will iterate through a list of given keywords and will try to obtain a value for that keyword
     * Method overload representing the users decision to not pass in a Regex map.
     *
     * @param text     - Text in which to look for values for the specified keywords
     * @param keywords - List containing all the keywords we want to find values for
     *                 use that regular expression instead of ours
     * @return List containing pairs of Keywords and a List (non-repeating) of values found for that keyword
     * @throws IllegalArgumentException If the keywords list is empty
     */
    public Map getAllMatchedValues(String text, Map<String, Specification> keywords) throws IllegalArgumentException {
        return getAllMatchedValues(text, keywords, new HashMap<>());
    }

    /**
     * Method that will iterate through a list of given keywords and will try to obtain a value for that keyword
     *
     * @param text        - Text in which to look for values for the specified keywords
     * @param keywords    - List containing all the keywords we want to find values for
     * @param clientRegEx - Optional parameter - If the client already has a predefined Regular Expression for a given key
     *                    use that regular expression instead of ours
     * @return List containing pairs of Keywords and a List (non-repeating) of values found for that keyword
     * @throws IllegalArgumentException If the keywords list is empty
     */
    public Map getAllMatchedValues(String text, Map<String, Specification> keywords, Map<String, String> clientRegEx) throws IllegalArgumentException {
        return getAllMatchedValues(text, keywords, clientRegEx, false);
    }

    /**
     * Method that will iterate through a list of given keywords and will try to obtain a value for that keyword
     *
     * @param text        - Text in which to look for values for the specified keywords
     * @param keywords    - List containing all the keywords we want to find values for
     * @param clientRegEx - Optional parameter - If the client already has a predefined Regular Expression for a given key
     *                    use that regular expression instead of ours
     * @return List containing pairs of Keywords and a List (non-repeating) of values found for that keyword
     * @throws IllegalArgumentException If the keywords list is empty
     */
    public Map getAllMatchedValues(String text, Map<String, Specification> keywords, Map<String, String> clientRegEx, boolean includeDuplicates) throws IllegalArgumentException {
        if (keywords.isEmpty())
            throw new IllegalArgumentException("The list of keywords should not be empty");

        //Create Option wrapper around extracted text
        Option<String> textOpt = (text != null && !text.equals("")) ? Some.apply(text) : Option.apply(null);

        //Convert scala.collection.immutable.List to java.util.List
        scala.collection.immutable.Map result = Extractor.getAllMatchedValues(textOpt, keywordsToScala(keywords), regexToScala(clientRegEx), includeDuplicates);
        return scalaResultToJava(result);
    }

    /**
     * Method that will iterate through a list of given keywords and will try to obtain only the first value it finds for a given
     * keyword, representing a single JSON object.
     * Method overload representing the users decision to not pass a regex Map.
     *
     * @param text     - Text in which to look for values for the specified keywords
     * @param keywords - List containing all the keywords we want to find values for
     * @return A List containing pairs of keywords with a single matched value
     * @throws IllegalArgumentException If the keywords list is empty
     */
    public Map getSingleMatchedValue(String text, Map<String, Specification> keywords) throws IllegalArgumentException {
        return getSingleMatchedValue(text, keywords, new HashMap<>());
    }

    /**
     * Method that will iterate through a list of given keywords and will try to obtain only the first value it finds for a given
     * keyword, representing a single JSON object
     *
     * @param text        - Text in which to look for values for the specified keywords
     * @param keywords    - List containing all the keywords we want to find values for
     * @param clientRegEx - Optional parameter - If the client already has a predefined Regular Expression for a given key
     * @return A List containing pairs of keywords with a single matched value
     * @throws IllegalArgumentException If the keywords list is empty
     */

    public Map getSingleMatchedValue(String text, Map<String, Specification> keywords, Map<String, String> clientRegEx) throws IllegalArgumentException {
        if (keywords.isEmpty())
            throw new IllegalArgumentException("The list of keywords should not be empty");

        Option<String> textOpt = (text != null && !text.equals("")) ? Some.apply(text) : Option.apply(null);
        scala.collection.immutable.Map result = Extractor.getSingleMatchedValue(textOpt, keywordsToScala(keywords), regexToScala(clientRegEx));
        return scalaResultToJava(result);
    }

    /**
     * Method that will iterate through a list of given keywords and will try to obtain a list containing
     * sub-lists that have all keywords and only one value for each of them (representing a single JSON object for each of the sub-lists)
     * Method overload representing the users decision to not pass a regex Map.
     *
     * @param text     - Text in which to look for values for the specified keywords
     * @param keywords - List containing all the keywords we want to find values for
     * @return A List containing sub-lists of pairs of keywords with single matched values
     * @throws IllegalArgumentException If the keywords list is empty
     */
    public List getAllObjects(String text, Map<String, Specification> keywords) throws IllegalArgumentException {
        return getAllObjects(text, keywords, new HashMap<>());

    }

    /**
     * Method that will iterate through a list of given keywords and will try to obtain a list containing
     * sub-lists that have all keywords and only one value for each of them (representing a single JSON object for each of the sub-lists)
     *
     * @param text        - Text in which to look for values for the specified keywords
     * @param keywords    - List containing all the keywords we want to find values for
     * @param clientRegEx - Optional parameter - If the client already has a predefined Regular Expression for a given key
     * @return A List containing sub-lists of pairs of keywords with single matched values
     * @throws IllegalArgumentException If the keywords list is empty
     */
    public List getAllObjects(String text, Map<String, Specification> keywords, Map<String, String> clientRegEx) throws IllegalArgumentException {
        if (keywords.isEmpty())
            throw new IllegalArgumentException("The list of keywords should not be empty");

        Option<String> textOpt = (text != null && !text.equals("")) ? Some.apply(text) : Option.apply(null);
        scala.collection.immutable.List result = Extractor.getAllObjects(textOpt, keywordsToScala(keywords), regexToScala(clientRegEx));
        List<scala.collection.immutable.Map<String, scala.collection.immutable.List<String>>> javaResult = JavaConverters.seqAsJavaList(result);

        //Convert from scala List[Map[String, List[String]] to java List<Map<String, List<String>>>
        List returnList = new ArrayList();
        for (scala.collection.immutable.Map subMap : javaResult) {
            returnList.add(scalaResultToJava(subMap));
        }
        return returnList;
    }

    /**
     * Method that encapsulates the entire process of finding values for the given keywords list and converting the MatchedPair type to a JSON Object
     * Method overload representing the users decision to not pass a String flag and a regex Map
     *
     * @param text     - Text in which to look for values for the specified keywords
     * @param keywords - List containing all the keywords we want to find values for
     * @return a List of Strings representing a JSON object for each MatchedPair type
     * @throws IllegalArgumentException If the keywords list is empty
     */
    public List getJSONObjects(String text, Map<String, Specification> keywords) throws IllegalArgumentException {
        return getJSONObjects(text, keywords, "empty", new HashMap<>());
    }

    /**
     * Method that encapsulates the entire process of finding values for the given keywords list and converting the MatchedPair type to a JSON Object
     * Method overload representing the users decision to not pass a String flag
     *
     * @param text        - Text in which to look for values for the specified keywords
     * @param keywords    - List containing all the keywords we want to find values for
     * @param clientRegEx - Optional parameter - If the client already has a predefined Regular Expression for a given key
     * @return a List of Strings representing a JSON object for each MatchedPair type
     * @throws IllegalArgumentException If the keywords list is empty
     */
    public List getJSONObjects(String text, Map<String, Specification> keywords, Map<String, String> clientRegEx) throws IllegalArgumentException {
        return getJSONObjects(text, keywords, "empty", clientRegEx);
    }

    /**
     * Method that encapsulates the entire process of finding values for the given keywords list and converting the MatchedPair type to a JSON Object
     * Method overload representing the users decision to not pass a regex Map
     *
     * @param text     - Text in which to look for values for the specified keywords
     * @param keywords - List containing all the keywords we want to find values for
     * @param flag     - Optional flag with information on how to return non-existing values
     * @return a List of Strings representing a JSON object for each MatchedPair type
     * @throws IllegalArgumentException If the keywords list is empty
     */
    public List getJSONObjects(String text, Map<String, Specification> keywords, String flag) throws IllegalArgumentException {
        return getJSONObjects(text, keywords, flag, new HashMap<>());
    }

    /**
     * Method that encapsulates the entire process of finding values for the given keywords list and converting the MatchedPair type to a JSON Object
     *
     * @param text        - Text in which to look for values for the specified keywords
     * @param keywords    - List containing all the keywords we want to find values for
     * @param flag        - Optional flag with information on how to return non-existing values
     * @param clientRegEx - Optional parameter - If the client already has a predefined Regular Expression for a given key
     * @return a List of Strings representing a JSON object for each MatchedPair type
     * @throws IllegalArgumentException If the keywords list is empty
     */
    public List getJSONObjects(String text, Map<String, Specification> keywords, String flag, Map<String, String> clientRegEx) throws IllegalArgumentException {
        if (keywords.isEmpty())
            throw new IllegalArgumentException("The list of keywords should not be empty");

        Option<String> textOpt = (text != null && !text.equals("")) ? Some.apply(text) : Option.apply(null);
        scala.collection.immutable.List result = Extractor.getJSONObjects(textOpt, keywordsToScala(keywords), flag, regexToScala(clientRegEx));
        return JavaConverters.seqAsJavaList(result);
    }

    /**
     * Method that encapsulates the process of making a single JSON object from all the information found in the text for the given keywords.
     * Method overload representing the users decision to not pass a String flag and a regex Map
     *
     * @param text     - Text in which to look for values for the specified keywords
     * @param keywords - List containing all the keywords we want to find values for
     * @return a Single JSON string containing all the information
     */
    public String getSingleJSON(String text, Map<String, Specification> keywords) throws IllegalArgumentException {
        return getSingleJSON(text, keywords, "empty", new HashMap<>());
    }

    /**
     * Method that encapsulates the process of making a single JSON object from all the information found in the text for the given keywords.
     * This method overload represents the user decision to not pass an additional flag specifying how to output the values in case there is none for a given keyword
     *
     * @param text        - Text in which to look for values for the specified keywords
     * @param keywords    - List containing all the keywords we want to find values for
     * @param clientRegEx - Optional parameter - If the client already has a predefined Regular Expression for a given key
     * @return a Single JSON string containing all the information
     */
    public String getSingleJSON(String text, Map<String, Specification> keywords, Map<String, String> clientRegEx) throws IllegalArgumentException {
        return getSingleJSON(text, keywords, "empty", clientRegEx);
    }

    /**
     * Method that encapsulates the process of making a single JSON object from all the information found in the text for the given keywords.
     * This method overload represents the user decision of not passing an additional regex Map
     *
     * @param text     - Text in which to look for values for the specified keywords
     * @param keywords - List containing all the keywords we want to find values for
     * @param flag     - Optional parameter - flag with information on how to return non-existing values
     * @return a Single JSON string containing all the information
     */
    public String getSingleJSON(String text, Map<String, Specification> keywords, String flag) throws IllegalArgumentException {
        return getSingleJSON(text, keywords, flag, new HashMap<>());
    }

    /**
     * Method that encapsulates the process of making a single JSON object from all the information found in the text for the given keywords.
     *
     * @param text        - Text in which to look for values for the specified keywords
     * @param keywords    - List containing all the keywords we want to find values for
     * @param flag        - Optional parameter - flag with information on how to return non-existing values
     * @param clientRegEx - Optional parameter - If the client already has a predefined Regular Expression for a given key
     * @return a Single JSON string containing all the information
     */
    public String getSingleJSON(String text, Map<String, Specification> keywords, String flag, Map<String, String> clientRegEx) throws IllegalArgumentException {
        if (keywords.isEmpty())
            throw new IllegalArgumentException("The list of keywords should not be empty");
        Option<String> textOpt = (text != null && !text.equals("")) ? Some.apply(text) : Option.apply(null);
        return Extractor.getSingleJSON(textOpt, keywordsToScala(keywords), flag, regexToScala(clientRegEx));
    }

    /**
     * Method that given a List of pairs of keywords and their respective values will create a string in JSON format
     * <p>
     * This method receives an optional flag with information on how to return non existing values,
     * this flag can be :
     * "empty" (default) - returns an empty string
     * "null" - returns the value null (in quotations, can be changed)
     * "remove" - removes that specific field altogether
     * <p>
     * Method overload that implements the users decision of not sending an optional String flag.
     *
     * @param listJSON - List of pairs of keywords and their respective values
     * @return a JSON string
     */
    public String makeJSONString(Map<String, List<String>> listJSON) {
        return makeJSONString(listJSON, "empty");
    }

    /**
     * Method that given a List of pairs of keywords and their respective values will create a string in JSON format
     * <p>
     * This method receives an optional flag with information on how to return non existing values,
     * this flag can be :
     * "empty" (default) - returns an empty string
     * "null" - returns the value null (in quotations, can be changed)
     * "remove" - removes that specific field altogether
     *
     * @param listJSON - List of pairs of keywords and their respective values
     * @param flag     - Optional flag with information on how to return non-existing values
     * @return a JSON string
     */
    public String makeJSONString(Map<String, List<String>> listJSON, String flag) {

        //convert input map to a scala mutable map
        scala.collection.mutable.Map mutableMap = JavaConverters.mapAsScalaMapConverter(listJSON).asScala();

        //Convert scala mutable map to scala immutable map by concatenating it with an empty immutable HashMap
        scala.collection.immutable.Map immutableMap = (scala.collection.immutable.Map) new scala.collection.immutable.HashMap<>().$plus$plus(mutableMap);

        return Extractor.makeJSONString(immutableMap, flag);
    }

    /**
     * Method that gets all keywords and respective values from know form and returns a JSON string
     *
     * @param text - Text in which to look for key-value pairs
     * @return - A JSON String containing all the information in the text passed by arguments
     */
    public String getJSONFromForm(String text) {
        Option<String> textOpt = (text != null && !text.equals("")) ? Some.apply(text) : Option.apply(null);
        return Extractor.getJSONFromForm(textOpt);
    }

    /**
     * Private method that receives a keywords java.util.Map and processes it to return a scala.collection.immutable.List
     *
     * @param keywords - The java.util.Map to be converted
     * @return a scala.collection.immutable.List converted from the input parameter
     */
    private scala.collection.immutable.Map keywordsToScala(Map<String, Specification> keywords) {

        //Convert javaMap to a scala mutable map
        scala.collection.mutable.Map mutableMap = JavaConverters.mapAsScalaMapConverter(keywords).asScala();

        //Convert scala mutable map to scala immutable map by concatenating it with an empty immutable HashMap
        return (scala.collection.immutable.Map) new scala.collection.immutable.HashMap<>().$plus$plus(mutableMap);
    }

    /**
     * Private method that receives a regex java.util.Map and processes it to return a scala.collection.immutable.Map
     *
     * @param clientRegEx - The java.util.Map to be converted
     * @return a scala.collection.immutable.Map converted from the input parameter
     */
    private scala.collection.immutable.Map regexToScala(Map<String, String> clientRegEx) {

        //Mimic a scala Map[String, Regex]
        HashMap<String, Regex> regexMap = new HashMap<>();

        //Populate regexMap
        for (Map.Entry<String, String> entry : clientRegEx.entrySet()) {
            Regex r = new Regex(entry.getValue(), null); //TODO probably not null
            regexMap.put(entry.getKey(), r);
        }

        //convert java.util.Map to scala.collection.mutable.Map, then to scala.collection.immutable.Map
        scala.collection.mutable.Map mutableMap = JavaConverters.mapAsScalaMapConverter(regexMap).asScala();
        return (scala.collection.immutable.Map) new scala.collection.immutable.HashMap<>().$plus$plus(mutableMap);
    }


    /**
     * Private method that converts a scala list and returns a Java-usable list
     *
     * @param result - the scala list to be converted
     * @return a java.util.List with all elements capable of being used in java
     */
    private Map<String, List<String>> scalaResultToJava(scala.collection.immutable.Map result) {
        HashMap<String, List<String>> returnMap = new HashMap<>();

        //convert scala map to java map
        Map<String, scala.collection.immutable.List<String>> preScalaMap = JavaConverters.mapAsJavaMap(result);

        for (Map.Entry<String, scala.collection.immutable.List<String>> entry : preScalaMap.entrySet()) {
            List javaList = JavaConverters.seqAsJavaList(entry.getValue()); //conver inner scala list to Java
            returnMap.put(entry.getKey(), javaList);
        }
        return returnMap;
    }

}
