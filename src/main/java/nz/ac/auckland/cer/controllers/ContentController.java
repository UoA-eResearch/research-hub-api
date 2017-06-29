package nz.ac.auckland.cer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import nz.ac.auckland.cer.model.Content;
import nz.ac.auckland.cer.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@RestController
public class ContentController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContentRepository contentRepository;

    private static SimpleFilterProvider getProductsFilter()
    {
        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter(Content.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept("researchPhases", "externalUrls"));
        return filter;
    }

    private static SimpleFilterProvider getDetailsFilter()
    {
        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter(Content.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept());
        return filter;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/contentItems")
    public ResponseEntity<String> getContentItems() {
        final List<Content> contentItems = contentRepository.findAll();
        final SimpleFilterProvider filter = ContentController.getProductsFilter();
        String result = "";

        try
        {
            result = objectMapper.writer(filter).writeValueAsString(contentItems);
        }
        catch (JsonProcessingException e) {

        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/query")
//    public ResponseEntity<String> getContentItems(@RequestParam(required = false) Integer productType,
//                                              @RequestParam(required = false) Integer provider,
//                                              @RequestParam(required = false) Integer[] lifeCycleStages,
//                                              @RequestParam(required = false) Integer[] eligibleGroups,
//                                              @RequestParam(required = false) Integer[] serviceTypes,
//                                              @RequestParam(required = false) Integer[] programmes,
//                                              @RequestParam(required = false) Integer[] studyLevels,
//                                              @RequestParam(required = false) String searchText) {
//
//
//        Session session = entityManager.unwrap(Session.class);
//        JPQLQuery query = new HibernateQuery(session);
//        QProduct qProduct = QProduct.product;
//
//        BooleanBuilder builder = new BooleanBuilder();
//
//        if(productType != null)
//        {
//            builder.and(qProduct.productType.id.eq(productType));
//        }
//
//        if(provider != null)
//        {
//            builder.and(qProduct.provider.id.eq(provider));
//        }
//
//        if(lifeCycleStages != null)
//        {
//            for(Integer id : lifeCycleStages)
//            {
//                builder.or(qProduct.lifeCycleStages.contains(new ResearchPhase(id)));
//            }
//        }
//
//        if(eligibleGroups != null)
//        {
//            for(Integer id : eligibleGroups)
//            {
//                builder.or(qProduct.eligibleGroups.contains(new Eligibility(id)));
//            }
//        }
//
//        if(lifeCycleStages != null)
//        {
//            for(Integer id : serviceTypes)
//            {
//                builder.or(qProduct.serviceTypes.contains(new ServiceType(id)));
//            }
//        }
//
//        if(programmes != null)
//        {
//            for(Integer id : programmes)
//            {
//                builder.or(qProduct.programmes.contains(new Programme(id)));
//            }
//        }
//
//        if(studyLevels != null)
//        {
//            for(Integer id : studyLevels)
//            {
//                builder.or(qProduct.studyLevels.contains(new StudyLevel(id)));
//            }
//        }
//
//        if(searchText != null)
//        {
//            String searchTextLower = searchText.toLowerCase().trim();
//
//            if(!searchTextLower.equals("")) {
//                builder.andAnyOf(qProduct.name.toLowerCase().contains(searchTextLower), qProduct.summary.toLowerCase().contains(searchTextLower));
//            }
//        }
//
//        List<Product> products = query.from(qProduct)
//                .where(builder)
//                .list(qProduct);
//
//        final SimpleFilterProvider filter = ProductController.getProductsFilter();
//        String result = "";
//
//        try
//        {
//            result = objectMapper.writer(filter).writeValueAsString(products);
//        }
//        catch (JsonProcessingException e) {
//
//        }
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }

    @RequestMapping(method = RequestMethod.GET, value = "/contentItems/{id}")
    public ResponseEntity<String> getContentItems(@PathVariable Integer id) {
        final Content product = contentRepository.findOne(id);
        String contentTypeName = product.getContentType().getName().toLowerCase();
        String result = "";

        try
        {
            SimpleFilterProvider filter = new SimpleFilterProvider();
            filter = ContentController.getDetailsFilter();
//
//            if(productTypeName.equals("service"))
//            {
//                filter = ProductController.getServiceFilter();
//            }
//            else if(productTypeName.equals("education"))
//            {
//                filter = ProductController.getEducationFilter();
//            }

            result = objectMapper.writer(filter).writeValueAsString(product);
        }
        catch (JsonProcessingException e) {
            int i = 0;
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
