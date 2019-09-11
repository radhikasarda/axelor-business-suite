package com.axelor.apps.gst.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.TaxLine;
import com.axelor.apps.account.db.repo.TaxLineRepository;
import com.axelor.apps.account.service.invoice.InvoiceLineService;
import com.axelor.apps.account.service.invoice.generator.line.InvoiceLineManagement;
import com.axelor.apps.base.db.Address;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.repo.ProductRepository;
import com.axelor.db.mapper.Mapper;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GstInvoiceLineServiceImpl implements GstInvoiceLineService {

  @Inject GstInvoiceLineService gstInvoiceLineService;
  @Inject InvoiceLineService invoiceLineService;
  @Inject GstInvoiceService gstInvoiceService;

  @Override
  public InvoiceLine calculateInvoiceLine(
      InvoiceLine invoiceLine, Address invoiceAddress, Address companyAddress) {

    if (invoiceLine.getTaxLine() == null
        || invoiceAddress == null
        || companyAddress == null
        || invoiceAddress.getState() == null
        || companyAddress.getState() == null
        ) {

      invoiceLine.setIGST(null);
      invoiceLine.setSGST(null);
      invoiceLine.setCGST(null);
      return invoiceLine;

    } else if (invoiceLine.getTaxLine() != null) {

      BigDecimal exTaxTotal =
          InvoiceLineManagement.computeAmount(invoiceLine.getQty(), invoiceLine.getPrice());
      BigDecimal inTaxTotal =
          exTaxTotal.add(exTaxTotal.multiply(invoiceLine.getTaxLine().getValue()));
      invoiceLine.setExTaxTotal(exTaxTotal);
      invoiceLine.setInTaxTotal(inTaxTotal);

      if (!companyAddress.getState().equals(invoiceAddress.getState())) {
        BigDecimal igst =
            (invoiceLine.getExTaxTotal().multiply(invoiceLine.getTaxLine().getValue()))
                .divide(BigDecimal.valueOf(100));
        invoiceLine.setIGST(igst);
        invoiceLine.setSGST(null);
        invoiceLine.setCGST(null);
      } else {

        BigDecimal sgst =
            (invoiceLine.getExTaxTotal().multiply(invoiceLine.getTaxLine().getValue()))
                .divide(BigDecimal.valueOf(2));
        BigDecimal cgst =
            (invoiceLine.getExTaxTotal().multiply(invoiceLine.getTaxLine().getValue()))
                .divide(BigDecimal.valueOf(2));

        invoiceLine.setSGST(sgst);
        invoiceLine.setCGST(cgst);
        invoiceLine.setIGST(null);
      }
    }
    return invoiceLine;
  }

  @Override
  public Invoice setInvoiceLine(Invoice invoice, List productIdList) {
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

        try {
          invoiceLine =
              Mapper.toBean(
                  InvoiceLine.class,
                  invoiceLineService.fillProductInformation(invoice, invoiceLine));
        } catch (AxelorException e) {

          e.printStackTrace();
        }

        invoiceLine.setQty(BigDecimal.ONE);
        invoiceLine =
            gstInvoiceLineService.calculateInvoiceLine(
                invoiceLine, invoice.getAddress(), invoice.getCompany().getAddress());

        invoiceLineList.add(invoiceLine);
      }
      invoice.setInvoiceLineList(invoiceLineList);
      try {
        invoice = gstInvoiceService.compute(invoice);
      } catch (AxelorException e) {

        e.printStackTrace();
      }
    }
    return invoice;
  }
}
