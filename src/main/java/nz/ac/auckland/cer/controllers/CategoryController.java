package nz.ac.auckland.cer.controllers;

import nz.ac.auckland.cer.model.categories.LifeCycle;
import nz.ac.auckland.cer.model.categories.ProductType;
import nz.ac.auckland.cer.model.categories.Provider;
import nz.ac.auckland.cer.repository.LifeCycleRepository;
import nz.ac.auckland.cer.repository.ProductTypeRepository;
import nz.ac.auckland.cer.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;


@RestController
public class CategoryController {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private LifeCycleRepository lifeCycleStages;

    @RequestMapping(method = RequestMethod.GET, value = "/category/productType")
    public Collection<ProductType> getProductCategories() {
        return productTypeRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/category/provider")
    public Collection<Provider> getProviders() {
        return providerRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/category/lifecycle")
    public Collection<LifeCycle> getLifeCycleStage() {
        return lifeCycleStages.findAll();
    }
}
