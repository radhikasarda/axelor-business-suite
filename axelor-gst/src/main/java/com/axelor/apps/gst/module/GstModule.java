package com.axelor.apps.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.apps.account.service.AccountManagementServiceAccountImpl;
import com.axelor.apps.businessproject.service.InvoiceServiceProjectImpl;
import com.axelor.apps.gst.service.GstInvoiceLineService;
import com.axelor.apps.gst.service.GstInvoiceService;

public class GstModule extends AxelorModule {
  @Override
  protected void configure() {

    bind(AccountManagementServiceAccountImpl.class).to(GstInvoiceLineService.class);
    bind(InvoiceServiceProjectImpl.class).to(GstInvoiceService.class);
  }
}
