package com.axelor.apps.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.apps.account.service.AccountManagementServiceAccountImpl;
import com.axelor.apps.account.service.invoice.print.InvoicePrintServiceImpl;
import com.axelor.apps.businessproject.service.InvoiceServiceProjectImpl;
import com.axelor.apps.gst.service.InvoiceLineGstService;
import com.axelor.apps.gst.service.InvoiceLineGstServiceImpl;
import com.axelor.apps.gst.service.InvoiceReportGstService;
import com.axelor.apps.gst.service.InvoiceServiceGst;
import com.axelor.apps.gst.service.TaxLineGstService;

public class GstModule extends AxelorModule {
  @Override
  protected void configure() {

    bind(AccountManagementServiceAccountImpl.class).to(TaxLineGstService.class);
    bind(InvoiceServiceProjectImpl.class).to(InvoiceServiceGst.class);
    bind(InvoicePrintServiceImpl.class).to(InvoiceReportGstService.class);
    bind(InvoiceLineGstService.class).to(InvoiceLineGstServiceImpl.class);
  }
}
