package com.axelor.apps.gst.service;

import com.axelor.apps.account.db.FiscalPosition;
import com.axelor.apps.account.db.TaxLine;
import com.axelor.apps.account.service.AccountManagementServiceAccountImpl;
import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.exceptions.IExceptionMessage;
import com.axelor.apps.base.service.tax.FiscalPositionService;
import com.axelor.apps.base.service.tax.TaxService;
import com.axelor.exception.AxelorException;
import com.axelor.exception.db.repo.TraceBackRepository;
import com.axelor.i18n.I18n;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.inject.Inject;

public class GstInvoiceLineService extends AccountManagementServiceAccountImpl {

  private TaxService taxService;

  @Inject
  public GstInvoiceLineService(FiscalPositionService fiscalPositionService, TaxService taxService) {
    super(fiscalPositionService, taxService);
    this.taxService = taxService;
  }

  @Override
  public TaxLine getTaxLine(
      LocalDate date,
      Product product,
      Company company,
      FiscalPosition fiscalPosition,
      boolean isPurchase)
      throws AxelorException {

    TaxLine taxLine =
        taxService.getTaxLine(
            this.getProductTax(product, company, fiscalPosition, isPurchase), date);
    BigDecimal gstRate = product.getGstRate().divide(new BigDecimal(100));
    if (taxLine != null)
      if (taxLine.getValue().compareTo(gstRate) == 0) {
        return taxLine;
      }

    throw new AxelorException(
        TraceBackRepository.CATEGORY_CONFIGURATION_ERROR,
        I18n.get(IExceptionMessage.ACCOUNT_MANAGEMENT_2),
        product.getCode());
  }
}
