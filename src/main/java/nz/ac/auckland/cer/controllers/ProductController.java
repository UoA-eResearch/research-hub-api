package nz.ac.auckland.cer.controllers;

import nz.ac.auckland.cer.model.Product;
import nz.ac.auckland.cer.model.categories.ProductType;
import nz.ac.auckland.cer.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return productRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/products/{id}")
    public Product getProduct(@PathVariable Integer id) {
        Product product = productRepository.findOne(id);
        System.out.print(product);
//        String productTypeName = product.getProductType().getName().toLowerCase();

//        if(productTypeName.equals("service"))
//        {
//            product.getEligibleGroups();
//            product.getRequirements();
//            product.getCost();
//            product.getLifeCycleStages();
////            product.getFeatures();
////            product.getLimitations();
////            product.getConsiderations();
////            product.getSupport();
////            product.getReferences();
////            product.getContacts();
//        }
//        else if(productTypeName.equals("education"))
//        {
//            product.getStudyLevels();
//            product.getProgrammes();
//            product.getCost();
////            product.getContacts();
//        }

        return product;
    }
}
