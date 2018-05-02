package de.htwg.swqs.order.payment;

import de.htwg.swqs.order.model.Cost;
import de.htwg.swqs.order.model.CustomerInfo;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

  private String validPostcodes[] = {"78462", "78464", "78465", "78467"};
  private String blacklist[] = {"Max/Mustermann"};
  private BigDecimal limit = new BigDecimal("1000.00");

  public List<PaymentMethod> getAcceptedMethods(CustomerInfo customerInfo, Cost cost) {

    System.out.println("Payment Method Service called");

    // check properties
    boolean inBlacklist = Arrays.asList(blacklist)
        .contains(customerInfo.getFirstname() + "/" + customerInfo.getSurname());

    boolean inConstance = Arrays.asList(validPostcodes).contains(customerInfo.getPostcode());

    boolean inLimit = cost.getAmount().compareTo(limit) > 0 ? false : true;

    // apply rules
    if (inBlacklist) {
      return Collections.emptyList();
    }

    if (inConstance) {
      if (inLimit) {
        return Arrays.asList(PaymentMethod.creditCard, PaymentMethod.invoice,
            PaymentMethod.prePayment);
      } else {
        return Arrays.asList(PaymentMethod.creditCard, PaymentMethod.prePayment);
      }
    } else {
      return Arrays.asList(PaymentMethod.prePayment);
    }
  }
}
