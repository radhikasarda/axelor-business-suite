package com.axelor.apps.gst.web;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.base.db.Partner;
import com.axelor.exception.AxelorException;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.util.List;

public class GstInvoiceProductController {

  public void createInvoice(ActionRequest request, ActionResponse response) throws AxelorException {

    @SuppressWarnings("unchecked")
    List<String> list = (List<String>) request.getContext().get("productid");
    Partner partner = (Partner) request.getContext().get("partner");
    Long partner_id = partner.getId();
    if (!list.isEmpty()) {
      response.setView(
          ActionView.define("Invoice form")
              .model(Invoice.class.getName())
              .add("form", "invoice-form")
              .context("_operationTypeSelect", 3)
              .context("_product_ids", list)
              .context("_partner_id", partner_id)
              .map());
    }
  }
}
