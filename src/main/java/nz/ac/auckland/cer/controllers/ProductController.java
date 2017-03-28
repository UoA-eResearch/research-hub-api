package nz.ac.auckland.cer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import nz.ac.auckland.cer.model.Product;
import nz.ac.auckland.cer.model.categories.ProductType;
import nz.ac.auckland.cer.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;


@RestController
public class ProductController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    private static SimpleFilterProvider getProductsFilter()
    {
        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter("Product", SimpleBeanPropertyFilter.serializeAllExcept("lifeCycleStages", "eligibleGroups", "serviceTypes", "programmes", "studyLevels", "cost", "requirements", "features", "limitations", "considerations", "support", "references", "contacts"));
        return filter;
    }

    private static SimpleFilterProvider getServiceFilter()
    {
        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter("Product", SimpleBeanPropertyFilter.serializeAllExcept("programmes", "studyLevels"));
        return filter;
    }

    private static SimpleFilterProvider getEducationFilter()
    {
        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter("Product", SimpleBeanPropertyFilter.serializeAllExcept("serviceTypes", "requirements", "features", "limitations", "considerations", "support", "references"));
        return filter;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/products")
    public ResponseEntity<String> getProduct() {
        final List<Product> products = productRepository.findAll();
        final SimpleFilterProvider filter = ProductController.getProductsFilter();
        String result = "";

        try
        {
            result = objectMapper.writer(filter).writeValueAsString(products);
        }
        catch (JsonProcessingException e) {

        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/products/{id}")
    public ResponseEntity<String> getProduct(@PathVariable Integer id) {
        final Product product = productRepository.findOne(id);
        String productTypeName = product.getProductType().getName().toLowerCase();
        String result = "";

        try
        {
            SimpleFilterProvider filter = new SimpleFilterProvider();

            if(productTypeName.equals("service"))
            {
                filter = ProductController.getServiceFilter();
            }
            else if(productTypeName.equals("education"))
            {
                filter = ProductController.getEducationFilter();
            }

            result = objectMapper.writer(filter).writeValueAsString(product);
        }
        catch (JsonProcessingException e) {
            int i = 0;
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
