package com.axelor.apps.gst.web;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.exception.AxelorException;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;
import java.math.BigDecimal;

public class GstInvoiceLineController extends com.axelor.apps.account.web.InvoiceLineController {
  @Override
  public void computeAnalyticDistribution(ActionRequest request, ActionResponse response)
      throws AxelorException {

    super.computeAnalyticDistribution(request, response);
    Context context = request.getContext();

    InvoiceLine invoiceLine = context.asType(InvoiceLine.class);

    if (context.getParent().getContextClass() == InvoiceLine.class) {
      context = request.getContext().getParent();
    }

    Invoice invoice = this.getInvoice(context);

    if (invoiceLine.getTaxLine() != null) {
      if (invoiceLine.getTaxLine() == null
          || invoice.getCompany() == null
          || invoice.getCompany().getAddress() == null
          || invoice.getCompany().getAddress().getState() == null
          || invoice.getAddress() == null
          || invoice.getAddress().getState() == null) {
        invoiceLine.setIGST(null);
        invoiceLine.setSGST(null);
        invoiceLine.setCGST(null);
        response.setValues(invoiceLine);
      } else {

        if (!invoice
            .getCompany()
            .getAddress()
            .getState()
            .getName()
            .equals(invoice.getAddress().getState().getName())) {
          BigDecimal igst =
              (invoiceLine.getExTaxTotal().multiply(invoiceLine.getTaxLine().getValue()))
                  .divide(BigDecimal.valueOf(100));
          invoiceLine.setIGST(igst);
          invoiceLine.setSGST(null);
          invoiceLine.setCGST(null);
        } else {
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
        response.setValues(invoiceLine);
      }
    }
  }
}
