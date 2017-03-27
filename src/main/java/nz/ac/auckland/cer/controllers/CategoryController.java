package nz.ac.auckland.cer.controllers;

import nz.ac.auckland.cer.model.LifeCycleCategory;
import nz.ac.auckland.cer.model.ProductCategory;
import nz.ac.auckland.cer.model.ProviderCategory;
import nz.ac.auckland.cer.repository.LifeCycleCategoryRepository;
import nz.ac.auckland.cer.repository.ProductCategoryRepository;
import nz.ac.auckland.cer.repository.ProviderCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;


@RestController
public class CategoryController {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProviderCategoryRepository providerCategoryRepository;

    @Autowired
    private LifeCycleCategoryRepository lifeCycleStages;

    @RequestMapping(method = RequestMethod.GET, value = "/category/product")
    public Collection<ProductCategory> getProductCategories() {
        return productCategoryRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/category/provider")
    public Collection<ProviderCategory> getProviders() {
        return providerCategoryRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/category/lifecycle")
    public Collection<LifeCycleCategory> getLifeCycleStage() {
        return lifeCycleStages.findAll();
    }
}
