package tn.esprit.devops_project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.services.ProductServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class testproduct {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private Stock stock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stock = new Stock();
        stock.setIdStock(1L);

        product = new Product();
        product.setIdProduct(1L);
        product.setTitle("Product Title");
        product.setPrice(100.0f);
        product.setQuantity(10);
        product.setCategory(ProductCategory.ELECTRONICS); // Remplacez par une catégorie valide
        product.setStock(stock);
    }

    @Test
    void testAddProduct() {
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(productRepository.save(product)).thenReturn(product);

        Product addedProduct = productService.addProduct(product, 1L);
        assertNotNull(addedProduct);
        assertEquals(product.getIdProduct(), addedProduct.getIdProduct());
        assertEquals(product.getTitle(), addedProduct.getTitle());
        assertEquals(product.getStock(), addedProduct.getStock());

        verify(stockRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testRetrieveProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product retrievedProduct = productService.retrieveProduct(1L);
        assertNotNull(retrievedProduct);
        assertEquals(product.getIdProduct(), retrievedProduct.getIdProduct());
        assertEquals(product.getTitle(), retrievedProduct.getTitle());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testRetreiveAllProduct() {
        Product product2 = new Product();
        product2.setIdProduct(2L);
        product2.setTitle("Another Product");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product, product2));

        List<Product> products = productService.retreiveAllProduct();
        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals(product.getTitle(), products.get(0).getTitle());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveProductByCategory() {
        when(productRepository.findByCategory(ProductCategory.ELECTRONICS)).thenReturn(Arrays.asList(product));

        List<Product> products = productService.retrieveProductByCategory(ProductCategory.ELECTRONICS);
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(product.getCategory(), products.get(0).getCategory());

        verify(productRepository, times(1)).findByCategory(ProductCategory.ELECTRONICS);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRetrieveProductStock() {
        when(productRepository.findByStockIdStock(1L)).thenReturn(Arrays.asList(product));

        List<Product> products = productService.retreiveProductStock(1L);
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(product.getStock().getIdStock(), products.get(0).getStock().getIdStock());

        verify(productRepository, times(1)).findByStockIdStock(1L);
    }

    @Test
    void testRetrieveProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NullPointerException.class, () -> productService.retrieveProduct(1L));
        assertEquals("Product not found", exception.getMessage());

        verify(productRepository, times(1)).findById(1L);
    }
}
