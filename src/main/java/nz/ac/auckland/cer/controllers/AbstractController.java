package nz.ac.auckland.cer.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractController {

    @Autowired
    private ObjectMapper objectMapper;

    public AbstractController() {

    }

    public String getFilteredResults(Object rawResults, String entityName, String... excludeProperties)
    {
        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter(entityName, SimpleBeanPropertyFilter.serializeAllExcept(excludeProperties));

        String result = "";

        try
        {
            result = objectMapper.writer(filter).writeValueAsString(rawResults);
        }
        catch (JsonProcessingException e) {

        }

        return result;
    }
}
