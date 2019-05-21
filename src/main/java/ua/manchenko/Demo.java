package ua.manchenko;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.manchenko.exceptions.HTMLParserException;
import org.apache.commons.lang3.StringUtils;
import ua.manchenko.service.ButtonFinderService;
import ua.manchenko.utils.ParserUtils;

import java.io.File;
import java.util.List;

import static ua.manchenko.utils.ParserUtils.printTreeOfElement;

/**
 * @author Yaroslav_Manchenko created 5/21/2019
 */
public class Demo {
    private static final Logger LOGGER = LoggerFactory.getLogger(Demo.class);
    private static final String ORIGINAL_PAGE_HAVE_ATTRIBUTES = "Original button have %d attributes";

    public static void main(String[] args) {
        if (args.length < 2 || StringUtils.isBlank(args[0]) || StringUtils.isBlank(args[1])) {
            throw new HTMLParserException("Wrong arguments");
        }
        File originFilePath = new File(args[0]);
        File sampleFilePath = new File(args[1]);
        ParserUtils.checkFile(originFilePath);
        ParserUtils.checkFile(sampleFilePath);
        ButtonFinderService service = new ButtonFinderService();
        final List<String> listOfAttributes = service.getListOfAttributes(originFilePath);
        LOGGER.info(String.format(ORIGINAL_PAGE_HAVE_ATTRIBUTES, listOfAttributes.size()));
        final Element result = service.findByAttributes(sampleFilePath, listOfAttributes);
        printTreeOfElement(result);
    }

}
