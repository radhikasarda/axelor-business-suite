package com.axelor.apps.gst.service;

import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.base.db.Address;
import java.math.BigDecimal;

public class GstInvoiceLineServiceImpl implements GstInvoiceLineService {

  @Override
  public InvoiceLine calculateInvoiceLine(
      InvoiceLine invoiceLine, Address invoiceAddress, Address companyAddress) {
    if (invoiceLine.getTaxLine() == null
        || invoiceAddress == null
        || companyAddress == null
        || invoiceAddress.getState() == null
        || companyAddress.getState() == null
        || companyAddress.getState().getName() == null) {
      invoiceLine.setIGST(null);
      invoiceLine.setSGST(null);
      invoiceLine.setCGST(null);
      return invoiceLine;

    } else if (invoiceLine.getTaxLine() != null) {

    	System.out.println("CA:::"+companyAddress.getState().getName());
    	System.out.println("IA:::"+invoiceAddress.getState().getName());
      if (!companyAddress.getState().getName().equals(invoiceAddress.getState().getName())) {
        BigDecimal igst =
            (invoiceLine.getExTaxTotal().multiply(invoiceLine.getTaxLine().getValue()))
                .divide(BigDecimal.valueOf(100));
        System.out.println("Inside Equal");
        invoiceLine.setIGST(igst);
        invoiceLine.setSGST(null);
        invoiceLine.setCGST(null);
      } else {
    	  System.out.println("Inside Not equal");
        BigDecimal sgst =
            (invoiceLine.getExTaxTotal().multiply(invoiceLine.getTaxLine().getValue()))
                .divide(BigDecimal.valueOf(200));
        BigDecimal cgst =
            (invoiceLine.getExTaxTotal().multiply(invoiceLine.getTaxLine().getValue()))
                .divide(BigDecimal.valueOf(200));
        invoiceLine.setSGST(sgst);
        invoiceLine.setCGST(cgst);
        invoiceLine.setIGST(null);
      }
    }
    return invoiceLine;
  }
}
