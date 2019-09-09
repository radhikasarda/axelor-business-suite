package com.axelor.apps.gst.web;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.TaxLine;
import com.axelor.apps.account.db.repo.TaxLineRepository;
import com.axelor.apps.account.service.invoice.InvoiceLineService;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.repo.PartnerRepository;
import com.axelor.apps.base.db.repo.ProductRepository;
import com.axelor.db.mapper.Mapper;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GstInvoiceProductController {

  @Inject InvoiceLineService invoiceLineService;

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

  public void setInvoiceData(ActionRequest request, ActionResponse response)
      throws AxelorException {

    Context context = request.getContext();
    List<String> productIdList = (List<String>) context.get("_product_ids");
    Integer partner_id = (Integer) request.getContext().get("_partner_id");
    Partner partner =
        Beans.get(PartnerRepository.class).all().filter("self.id = ?", partner_id).fetchOne();

    Invoice invoice = request.getContext().asType(Invoice.class);
    invoice.setPartner(partner);
    List<InvoiceLine> invoiceLineList = new ArrayList<>();
    if (productIdList != null) {

      for (int i = 0; i < productIdList.size(); i++) {
        Product product =
            Beans.get(ProductRepository.class)
                .all()
                .filter("self.id = ?", productIdList.get(i))
                .fetchOne();
        InvoiceLine invoiceLine = new InvoiceLine();
        invoiceLine.setProduct(product);

        BigDecimal gstRate = product.getGstRate().divide(new BigDecimal(100));
        TaxLine taxLine =
            Beans.get(TaxLineRepository.class).all().filter("self.value = ?", gstRate).fetchOne();
        invoiceLine.setTaxLine(taxLine);
        invoiceLine =
            Mapper.toBean(
                InvoiceLine.class, invoiceLineService.fillProductInformation(invoice, invoiceLine));
        invoiceLine.setQty(BigDecimal.ONE);

        invoiceLineList.add(invoiceLine);
      }
      invoice.setInvoiceLineList(invoiceLineList);
      response.setValues(invoice);
    }
  }
}
