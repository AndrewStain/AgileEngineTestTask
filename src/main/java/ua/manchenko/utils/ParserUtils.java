package ua.manchenko.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.manchenko.exceptions.HTMLParserException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ListIterator;
import java.util.Optional;

/**
 * @author Yaroslav_Manchenko created 5/21/2019
 */
public class ParserUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(ParserUtils.class);
    private static final String FILE_NOT_FOUND = "File %s not found";

    private ParserUtils() {

    }

    public static void checkFile(File file) {
        if (!file.exists()) {
            throw new HTMLParserException(String.format(FILE_NOT_FOUND, file.getPath()));
        }
    }

    public static Optional<Elements> findElementsByQuery(File htmlFile, String cssQuery) {
        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    StandardCharsets.UTF_8.name(),
                    htmlFile.getAbsolutePath());

            return Optional.of(doc.select(cssQuery));

        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            return Optional.empty();
        }
    }

    public static void printTreeOfElement(Element element) {
        StringBuilder stringBuilder = new StringBuilder("Path: ");
        Elements parents = element.parents();
        final ListIterator<Element> listIterator = parents.listIterator(parents.size());

        while (listIterator.hasPrevious()) {
            Element node = listIterator.previous();
            stringBuilder.append(node.tag()).append(">>");
        }
        stringBuilder.append(element.tag());
        LOGGER.info(stringBuilder.toString());
    }
}
