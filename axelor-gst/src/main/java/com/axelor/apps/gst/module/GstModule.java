package com.axelor.apps.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.apps.account.service.AccountManagementServiceAccountImpl;
import com.axelor.apps.account.service.invoice.print.InvoicePrintServiceImpl;
import com.axelor.apps.businessproject.service.InvoiceServiceProjectImpl;
import com.axelor.apps.gst.service.GstInvoiceLineService;
import com.axelor.apps.gst.service.GstInvoiceLineServiceImpl;
import com.axelor.apps.gst.service.GstInvoiceReportService;
import com.axelor.apps.gst.service.GstInvoiceService;
import com.axelor.apps.gst.service.GstTaxLineService;

public class GstModule extends AxelorModule {
  @Override
  protected void configure() {

    bind(AccountManagementServiceAccountImpl.class).to(GstTaxLineService.class);
    bind(InvoiceServiceProjectImpl.class).to(GstInvoiceService.class);
    bind(InvoicePrintServiceImpl.class).to(GstInvoiceReportService.class);
    bind(GstInvoiceLineService.class).to(GstInvoiceLineServiceImpl.class);
  }
}
