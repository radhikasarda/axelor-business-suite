package com.axelor.apps.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.repo.InvoiceRepository;
import com.axelor.apps.account.service.app.AppAccountService;
import com.axelor.apps.account.service.invoice.InvoiceLineService;
import com.axelor.apps.account.service.invoice.factory.CancelFactory;
import com.axelor.apps.account.service.invoice.factory.ValidateFactory;
import com.axelor.apps.account.service.invoice.factory.VentilateFactory;
import com.axelor.apps.base.service.PartnerService;
import com.axelor.apps.base.service.alarm.AlarmEngineService;
import com.axelor.apps.businessproject.service.InvoiceServiceProjectImpl;
import com.axelor.exception.AxelorException;

public class GstInvoiceService extends InvoiceServiceProjectImpl {

	@Inject
	public GstInvoiceService(ValidateFactory validateFactory, VentilateFactory ventilateFactory,
			CancelFactory cancelFactory, AlarmEngineService<Invoice> alarmEngineService, InvoiceRepository invoiceRepo,
			AppAccountService appAccountService, PartnerService partnerService, InvoiceLineService invoiceLineService) {
		super(validateFactory, ventilateFactory, cancelFactory, alarmEngineService, invoiceRepo, appAccountService,
				partnerService, invoiceLineService);

	}

	@Override
	public Invoice compute(final Invoice invoice) throws AxelorException {

		Invoice gstinvoice=super.compute(invoice);
	
		BigDecimal invoiceigst = BigDecimal.ZERO;
		BigDecimal invoicesgst = BigDecimal.ZERO;
		BigDecimal invoicecgst = BigDecimal.ZERO;

		for (InvoiceLine invoiceItemsList : gstinvoice.getInvoiceLineList()) {

			invoiceigst = invoiceigst.add(invoiceItemsList.getIGST());
			invoicecgst = invoicecgst.add(invoiceItemsList.getCGST());
			invoicesgst = invoicesgst.add(invoiceItemsList.getSGST());

		}
		gstinvoice.setNetIGST(invoiceigst);
		gstinvoice.setNetSGST(invoicesgst);
		gstinvoice.setNetCGST(invoicecgst);

		return gstinvoice;
	}

}
