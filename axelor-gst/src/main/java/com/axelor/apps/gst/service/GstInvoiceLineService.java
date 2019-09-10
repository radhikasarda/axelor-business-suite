package com.axelor.apps.gst.service;

import java.util.List;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.base.db.Address;

public interface GstInvoiceLineService {

  public InvoiceLine calculateInvoiceLine(
      InvoiceLine invoiceLine, Address invoiceAddress, Address companyAddress);

  public  Invoice setInvoiceLine(Invoice invoice,List productIdList);

}
