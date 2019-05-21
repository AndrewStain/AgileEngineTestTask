package ua.manchenko.service;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static ua.manchenko.utils.ParserUtils.findElementsByQuery;

/**
 * @author Yaroslav_Manchenko created 5/21/2019
 */
public class ButtonFinderService {
    private static final String ORIGIN_BUTTON_QUERY = "a[id=\"make-everything-ok-button\"]";
    private static final String SEARCH_QUERY = "a[%s]";

    public List<String> getListOfAttributes(File file) {

        Optional<Elements> elementsOpt = findElementsByQuery(file, ORIGIN_BUTTON_QUERY);

        Optional<List<String>> elementsAttrsOpts = elementsOpt.map(buttons ->
                {
                    List<String> stringifiedAttrs = new ArrayList<>();

                    buttons.iterator().forEachRemaining(button ->
                            stringifiedAttrs.addAll(
                                    button.attributes().asList().stream()
                                            .map(attr -> attr.getKey() + " = " + attr.getValue())
                                            .collect(Collectors.toList())));

                    return stringifiedAttrs;
                }
        );
        return elementsAttrsOpts.orElse(Collections.emptyList());
    }

    public Element findByAttributes(File file, List<String> attr) {
        final Set<Element> buttons = new HashSet<>();
        attr.forEach(attribute -> {
            Optional<Elements> elementsOpt = findElementsByQuery(file, String.format(SEARCH_QUERY, attribute));
            elementsOpt.ifPresent(elements -> elements.iterator().forEachRemaining(buttons::add)
            );
        });
        return findElementMatchesMostCases(buttons, attr);
    }

    private Element findElementMatchesMostCases(Set<Element> elements, List<String> attributes) {
        final List<Element> listOfElement = new ArrayList<>(elements);
        Element result = listOfElement.get(0);
        int maxCount = findCountOfAttributeMatches(result, attributes);
        for (int i = 1; i < listOfElement.size(); i++) {
            final Element tmp = listOfElement.get(i);
            int tmpCount = findCountOfAttributeMatches(tmp, attributes);
            if (tmpCount > maxCount) {
                result = tmp;
            }
        }
        return result;
    }

    private int findCountOfAttributeMatches(Element element, List<String> attributes) {
        int count = 0;
        for (Attribute attribute : element.attributes().asList()) {
            String value = attribute.getKey() + " = " + attribute.getValue();
            if (attributes.contains(value)) {
                count++;
            }
        }
        return count;
    }
}
