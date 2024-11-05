package tn.esprit.devops_project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.InvoiceDetail;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.repositories.InvoiceDetailRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class InvoiceDetailServiceImplTest {

    @InjectMocks
    private InvoiceDetailServiceImpl invoiceDetailService;

    @Mock
    private InvoiceDetailRepository invoiceDetailRepository;

    private Product product;
    private Invoice invoice;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Initialize Product and Invoice with necessary values
        product = new Product(1L, "Product A", 50.0f, 100, null, null); // ProductCategory and Stock can be null for simplicity
        invoice = new Invoice(1L, 10.0f, 200.0f, new Date(), new Date(), false, new HashSet<>(), null);
    }

    @Test
    public void testRetrieveAllInvoiceDetails() {
        // Given
        InvoiceDetail invoiceDetail1 = new InvoiceDetail(1L, 2, 100.0f, product, invoice);
        InvoiceDetail invoiceDetail2 = new InvoiceDetail(2L, 3, 200.0f, product, invoice);
        List<InvoiceDetail> mockInvoiceDetails = Arrays.asList(invoiceDetail1, invoiceDetail2);

        when(invoiceDetailRepository.findAll()).thenReturn(mockInvoiceDetails);

        // When
        List<InvoiceDetail> result = invoiceDetailService.retrieveAllInvoiceDetails();

        // Then
        assertEquals(2, result.size());
        assertEquals(100.0f, result.get(0).getPrice());
        assertEquals(3, result.get(1).getQuantity());
    }

    @Test
    public void testAddInvoiceDetail() {
        // Given
        InvoiceDetail newInvoiceDetail = new InvoiceDetail(null, 2, 100.0f, product, invoice);

        when(invoiceDetailRepository.save(newInvoiceDetail)).thenReturn(new InvoiceDetail(1L, 2, 100.0f, product, invoice));

        // When
        InvoiceDetail savedInvoiceDetail = invoiceDetailService.addInvoiceDetail(newInvoiceDetail);

        // Then
        assertEquals(1L, savedInvoiceDetail.getIdInvoiceDetail());
        assertEquals(100.0f, savedInvoiceDetail.getPrice());
        assertEquals(2, savedInvoiceDetail.getQuantity());
    }

//    @Test
//    public void testUpdateInvoiceDetail() {
//        // Given
//        InvoiceDetail existingInvoiceDetail = new InvoiceDetail(1L, 2, 100.0f, product, invoice);
//        InvoiceDetail updatedInvoiceDetail = new InvoiceDetail(1L, 3, 150.0f, product, invoice);
//
//        when(invoiceDetailRepository.findById(1L)).thenReturn(Optional.of(existingInvoiceDetail));
//        when(invoiceDetailRepository.save(updatedInvoiceDetail)).thenReturn(updatedInvoiceDetail);
//
//        // When
//        InvoiceDetail result = invoiceDetailService.updateInvoiceDetail(1L, updatedInvoiceDetail);
//
//        // Then
//        assertEquals(1L, result.getIdInvoiceDetail());
//        assertEquals(3, result.getQuantity());
//        assertEquals(150.0f, result.getPrice());
//    }

    @Test
    public void testDeleteInvoiceDetail() {
        // Given
        InvoiceDetail invoiceDetail = new InvoiceDetail(1L, 2, 100.0f, product, invoice);

        when(invoiceDetailRepository.findById(1L)).thenReturn(Optional.of(invoiceDetail));

        // When
        invoiceDetailService.deleteInvoiceDetail(1L);

        // Then
        verify(invoiceDetailRepository, times(1)).delete(invoiceDetail);
    }

    @Test
    public void testRetrieveInvoiceDetail() {
        // Given
        InvoiceDetail invoiceDetail = new InvoiceDetail(1L, 2, 100.0f, product, invoice);

        when(invoiceDetailRepository.findById(1L)).thenReturn(Optional.of(invoiceDetail));

        // When
        InvoiceDetail result = invoiceDetailService.retrieveInvoiceDetail(1L);

        // Then
        assertEquals(1L, result.getIdInvoiceDetail());
        assertEquals(100.0f, result.getPrice());
        assertEquals(2, result.getQuantity());
    }

    @Test
    public void testRetrieveInvoiceDetail_NotFound() {
        // Given
        when(invoiceDetailRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            invoiceDetailService.retrieveInvoiceDetail(1L);
        });
    }



    //Si le service lance des exceptions personnalisées (par exemple, lorsque la quantité dépasse le stock disponible ou en cas de facture archivée),
    @Test
    public void testUpdateInvoiceDetail_ThrowsExceptionWhenQuantityExceedsStock() {
        // Given
        Product productWithLowStock = new Product(1L, "Product A", 50.0f, 5, null, null); // Only 5 in stock
        InvoiceDetail invoiceDetail = new InvoiceDetail(1L, 10, 100.0f, productWithLowStock, invoice); // Trying to update with quantity 10

        when(invoiceDetailRepository.findById(1L)).thenReturn(Optional.of(invoiceDetail));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            invoiceDetailService.updateInvoiceDetail(1L, invoiceDetail);
        });
    }


    //Tester le calcul du montant total d'une facture détaillée (InvoiceDetail)
    @Test
    public void testCalculateTotalAmount() {
        // Given
        InvoiceDetail invoiceDetail = new InvoiceDetail(1L, 5, 50.0f, product, invoice); // 5 * 50 = 250

        // When
        float totalAmount = invoiceDetailService.calculateTotalAmount(invoiceDetail);

        // Then
        assertEquals(250.0f, totalAmount);
    }




    //Dans le cas où une mise à jour d'un détail de facture (par exemple, le prix ou la quantité) nécessite une   vérification  des stocks ou les limites de prix),
    @Test
    public void testUpdateInvoiceDetailWithLogic() {
        // Given
        Product productWithStock = new Product(1L, "Product B", 50.0f, 10, null, null); // Product with enough stock
        InvoiceDetail existingInvoiceDetail = new InvoiceDetail(1L, 2, 100.0f, productWithStock, invoice);
        InvoiceDetail updatedInvoiceDetail = new InvoiceDetail(1L, 3, 150.0f, productWithStock, invoice);

        when(invoiceDetailRepository.findById(1L)).thenReturn(Optional.of(existingInvoiceDetail));
        when(invoiceDetailRepository.save(any(InvoiceDetail.class))).thenReturn(updatedInvoiceDetail);

        // When
        InvoiceDetail result = invoiceDetailService.updateInvoiceDetail(1L, updatedInvoiceDetail);

        // Then
        assertEquals(1L, result.getIdInvoiceDetail());
        assertEquals(3, result.getQuantity());
        assertEquals(150.0f, result.getPrice());
    }




}
