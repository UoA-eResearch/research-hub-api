package nz.ac.auckland.cer.controllers;

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
public class ContentController extends AbstractController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ContentRepository contentRepository;

    public ContentController() {
        super();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/content")
    public ResponseEntity<String> getContentItems() {
        final List<Content> contentItems = contentRepository.findAll();
        String results = this.getFilteredResults(contentItems, Content.ENTITY_NAME, "researchPhases", "externalUrls");
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/content/{id}")
    public ResponseEntity<String> getContentItems(@PathVariable Integer id) {
        final Content content = contentRepository.findOne(id);
        String results = this.getFilteredResults(content, Content.ENTITY_NAME);
        return new ResponseEntity<>(results, HttpStatus.OK);
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
}
