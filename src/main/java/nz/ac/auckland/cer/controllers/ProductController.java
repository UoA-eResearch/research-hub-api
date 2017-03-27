package nz.ac.auckland.cer.controllers;

import nz.ac.auckland.cer.model.LifeCycleCategory;
import nz.ac.auckland.cer.model.Product;
import nz.ac.auckland.cer.model.ProductCategory;
import nz.ac.auckland.cer.model.ProviderCategory;
import nz.ac.auckland.cer.repository.LifeCycleCategoryRepository;
import nz.ac.auckland.cer.repository.ProductCategoryRepository;
import nz.ac.auckland.cer.repository.ProductRepository;
import nz.ac.auckland.cer.repository.ProviderCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.JpaQueryMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;


@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/products")
    public Collection<Product> getProduct() {

//        QCustomer customer = QCustomer.customer;
//        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
//        Customer bob = query.select(customer)
//                .from(customer)
//                .where(customer.firstName.eq("Bob"))
//                .fetchOne();

        return productRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/products/{id}")
    public Product getProduct(@PathVariable Integer id) {
        Product product = productRepository.findOne(id);
        product.getLifeCycleCategories();
        return product;
    }
}
