package com.axelor.apps.gst.web;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.base.db.Address;
import com.axelor.apps.gst.service.GstInvoiceLineService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceLineControllerGst {

  @Inject GstInvoiceLineService invoiceLineService;

  public void calculateInvoiceLine(ActionRequest request, ActionResponse response) {
    InvoiceLine invoiceline = request.getContext().asType(InvoiceLine.class);
    Invoice invoice = request.getContext().getParent().asType(Invoice.class);
    Address companyAddress = null;
    Address invoiceAddress = invoice.getAddress();
    if (invoice.getCompany() == null) {
      companyAddress = null;
    } else {
      companyAddress = invoice.getCompany().getAddress();
    }
    invoiceline =
        invoiceLineService.calculateInvoiceLine(invoiceline, invoiceAddress, companyAddress);
    response.setValues(invoiceline);
  }
}
