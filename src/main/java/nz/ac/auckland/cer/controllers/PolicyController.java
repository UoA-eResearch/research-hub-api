package nz.ac.auckland.cer.controllers;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.hibernate.HibernateQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.Policy;
import nz.ac.auckland.cer.model.QPolicy;
import nz.ac.auckland.cer.repository.PolicyRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@RestController
@Api(tags = {"Person"}, description = "Operations on person")
public class PolicyController extends AbstractController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PolicyRepository policyRepository;

    public PolicyController() {
        super();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/policy")
    @ApiOperation(value = "search for policies")
    public ResponseEntity<String> getPolicy(@RequestParam Integer page,
                                            @RequestParam Integer size,
                                            @RequestParam(required = false) String searchText) {

        // Make sure pages greater than 0 and page sizes less than 50
        page = page < 0 ? 0 : page;
        size = size > 50 ? 50 : size;

        Session session = entityManager.unwrap(Session.class);
        JPQLQuery<Policy> queryFactory = new HibernateQuery<>(session);
        QPolicy qPolicy = QPolicy.policy;

        BooleanBuilder builder = new BooleanBuilder();

        if (searchText != null) {
            String searchTextLower = searchText.toLowerCase().trim();

            if (!searchTextLower.equals("")) {
                builder.andAnyOf(
                        qPolicy.name.toLowerCase().contains(searchTextLower),
                        qPolicy.description.toLowerCase().contains(searchTextLower)
                );
            }
        }

        JPQLQuery<Policy> policyQuery = queryFactory.from(qPolicy).where(builder);
        JPQLQuery<Policy> paginatedQuery = policyQuery.offset(page * size).limit(size);

        int totalElements = (int) policyQuery.fetchCount();
        int totalPages = (int) Math.ceil((float) totalElements / (float) size);
        int numberOfElements = (int) paginatedQuery.fetchCount();

        nz.ac.auckland.cer.model.Page<Policy> hubPage = new nz.ac.auckland.cer.model.Page<>();
        hubPage.content = paginatedQuery.fetch();
        hubPage.last = (page + 1) >= totalPages;
        hubPage.totalPages = totalPages;
        hubPage.totalElements = totalElements;
//        hubPage.sort;
        hubPage.first = page == 0;
        hubPage.numberOfElements = numberOfElements;
        hubPage.size = size;
        hubPage.number = page;

        String result = this.getFilteredResults(hubPage, Policy.ENTITY_NAME, "contentItems");

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
