package com.axelor.apps.gst.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.base.db.Address;
import java.util.List;

public interface InvoiceLineGstService {

  public InvoiceLine calculateInvoiceLine(
      InvoiceLine invoiceLine, Address invoiceAddress, Address companyAddress);

  public Invoice setInvoiceLine(Invoice invoice, List productIdList);
}
