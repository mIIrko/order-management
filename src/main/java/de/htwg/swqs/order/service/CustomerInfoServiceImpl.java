package de.htwg.swqs.order.service;

import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.order.repository.CustomerInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerInfoServiceImpl implements CustomerInfoService {

    private CustomerInfoRepository customerInfoRepository;

    @Autowired
    public CustomerInfoServiceImpl(CustomerInfoRepository customerInfoRepository) {
        this.customerInfoRepository = customerInfoRepository;
    }

    public CustomerInfo persistCustomerInfo(CustomerInfo customerInfo) {
        return this.customerInfoRepository.saveAndFlush(customerInfo);
    }

}
