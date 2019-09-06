package com.axelor.apps.gst.service;

import com.axelor.apps.ReportFactory;
import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.exception.IExceptionMessage;

import com.axelor.apps.account.service.invoice.print.InvoicePrintServiceImpl;
import com.axelor.apps.gst.report.IReport;
import com.axelor.apps.report.engine.ReportSettings;
import com.axelor.exception.AxelorException;
import com.axelor.exception.db.repo.TraceBackRepository;
import com.axelor.i18n.I18n;

public class GstInvoiceReportService extends InvoicePrintServiceImpl{
	
	@Override
	  public ReportSettings prepareReportSettings(Invoice invoice) throws AxelorException {

	    String locale = ReportSettings.getPrintingLocale(invoice.getPartner());

	    String title = I18n.get("Invoice");
	    if (invoice.getInvoiceId() != null) {
	      title += " " + invoice.getInvoiceId();
	    }
	    ReportSettings reportSetting =
	        ReportFactory.createReport(IReport.GSTINVOICE, title + "- ${date}");

	    return reportSetting.addParam("InvoiceId", invoice.getId()).addParam("Locale", locale);
	
	  }

}
