package de.htwg.swqs.order.service;

import de.htwg.swqs.order.model.CustomerInfo;

public interface CustomerInfoService {

    CustomerInfo persistCustomerInfo(CustomerInfo customerInfo);

}