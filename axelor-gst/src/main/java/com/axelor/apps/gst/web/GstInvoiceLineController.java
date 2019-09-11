package com.axelor.apps.gst.web;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.base.db.Address;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.repo.PartnerRepository;
import com.axelor.apps.gst.service.GstInvoiceLineService;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;
import com.google.inject.Inject;
import java.util.List;

public class GstInvoiceLineController {

  @Inject GstInvoiceLineService gstInvoiceLineService;

  public void calculateInvoiceLine(ActionRequest request, ActionResponse response) {
    InvoiceLine invoiceline = request.getContext().asType(InvoiceLine.class);
    Invoice invoice = request.getContext().getParent().asType(Invoice.class);
    Address companyAddress = null;
    Address invoiceAddress = invoice.getAddress();

    companyAddress = invoice.getCompany().getAddress();
   
    invoiceline =
        gstInvoiceLineService.calculateInvoiceLine(invoiceline, invoiceAddress, companyAddress);
    response.setValues(invoiceline);
  }

  public void setInvoiceLineData(ActionRequest request, ActionResponse response)
      throws AxelorException {

    Context context = request.getContext();
    List<String> productIdList = (List<String>) context.get("_product_ids");
    Integer partner_id = (Integer) request.getContext().get("_partner_id");
    Invoice invoice = request.getContext().asType(Invoice.class);

    if (productIdList != null && partner_id != null) {

      Partner partner =
          Beans.get(PartnerRepository.class).all().filter("self.id = ?", partner_id).fetchOne();
      invoice.setPartner(partner);

      if (partner.getMainAddress() != null) {
        invoice.setAddress(partner.getMainAddress());
      }

      invoice.setCurrency(partner.getCurrency());
      invoice = gstInvoiceLineService.setInvoiceLine(invoice, productIdList);
      response.setValues(invoice);
    }
  }
}
