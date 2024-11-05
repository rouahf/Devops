package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.devops_project.entities.InvoiceDetail;
import tn.esprit.devops_project.repositories.InvoiceDetailRepository;
import tn.esprit.devops_project.services.Iservices.IInvoiceDetailService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class InvoiceDetailServiceImpl implements IInvoiceDetailService {

    final InvoiceDetailRepository invoiceDetailRepository;

    @Override
    public List<InvoiceDetail> retrieveAllInvoiceDetails() {
        return invoiceDetailRepository.findAll();
    }

    @Override
    public InvoiceDetail addInvoiceDetail(InvoiceDetail invoiceDetail) {
        return invoiceDetailRepository.save(invoiceDetail);
    }

    @Override
    public InvoiceDetail updateInvoiceDetail(Long id, InvoiceDetail invoiceDetail) {
        InvoiceDetail existingDetail = invoiceDetailRepository.findById(id).orElseThrow(() -> new NullPointerException("InvoiceDetail not found"));

        // Check if the quantity exceeds the available stock
        if (invoiceDetail.getQuantity() > invoiceDetail.getProduct().getQuantity()) {
            throw new IllegalArgumentException("Quantity exceeds available stock");
        }

        existingDetail.setQuantity(invoiceDetail.getQuantity());
        existingDetail.setPrice(invoiceDetail.getPrice());
        existingDetail.setProduct(invoiceDetail.getProduct());
        existingDetail.setInvoice(invoiceDetail.getInvoice());

        return invoiceDetailRepository.save(existingDetail);
    }


    @Override
    public void deleteInvoiceDetail(Long idInvoiceDetail) {
        InvoiceDetail invoiceDetail = invoiceDetailRepository.findById(idInvoiceDetail)
                .orElseThrow(() -> new NullPointerException("InvoiceDetail not found"));

        invoiceDetailRepository.delete(invoiceDetail);
    }

    @Override
    public InvoiceDetail retrieveInvoiceDetail(Long idInvoiceDetail) {
        return invoiceDetailRepository.findById(idInvoiceDetail)
                .orElseThrow(() -> new NullPointerException("InvoiceDetail not found"));
    }

    public float calculateTotalAmount(InvoiceDetail invoiceDetail) {
        return invoiceDetail.getQuantity() * invoiceDetail.getPrice();
    }


}
