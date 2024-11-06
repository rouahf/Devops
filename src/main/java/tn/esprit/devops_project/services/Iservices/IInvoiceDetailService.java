package tn.esprit.devops_project.services.Iservices;

import tn.esprit.devops_project.entities.InvoiceDetail;

import java.util.List;

public interface IInvoiceDetailService {

    List<InvoiceDetail> retrieveAllInvoiceDetails();

    InvoiceDetail addInvoiceDetail(InvoiceDetail invoiceDetail);

    InvoiceDetail updateInvoiceDetail(Long idInvoiceDetail, InvoiceDetail invoiceDetail);

    void deleteInvoiceDetail(Long idInvoiceDetail);

    InvoiceDetail retrieveInvoiceDetail(Long idInvoiceDetail);
    float calculateTotalAmount(InvoiceDetail invoiceDetail);


    // Add any other method specific to InvoiceDetail, if needed
}
