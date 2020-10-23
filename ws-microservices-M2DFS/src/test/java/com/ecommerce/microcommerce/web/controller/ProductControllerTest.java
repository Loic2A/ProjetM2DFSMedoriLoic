package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ProductControllerTest {
    @Mock
    ProductDao productDao;
    @Mock
    Map<Integer, Product> productList;
    @InjectMocks
    ProductController productController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAfficherListeProduit() throws Exception {
        Map result = productController.afficherListeProduit();
        Assert.assertEquals(new HashMap() {{
            put("String", "String");
        }}, result);
    }

    @Test
    public void testListeProduits() throws Exception {
        MappingJacksonValue result = productController.listeProduits();
        Assert.assertEquals(null, result);
    }

    @Test
    public void testAfficherUnProduit() throws Exception {
        Product result = productController.afficherUnProduit(0);
        Assert.assertEquals(new Product(0, "nom", 0, 0), result);
    }

    @Test
    public void testAjouterProduit() throws Exception {
        ResponseEntity<Void> result = productController.ajouterProduit(new Product(0, null, 0, 0));
        Assert.assertEquals(null, result);
    }

    @Test
    public void testSupprimerProduit() throws Exception {
        ResponseEntity<Void> result = productController.supprimerProduit(0);
        Assert.assertEquals(null, result);
    }

    @Test
    public void testUpdateProduit() throws Exception {
        productController.updateProduit(0, new Product(0, "nom", 0, 0));
    }

    @Test
    public void testCalculerMargeProduit() throws Exception {
        String result = productController.calculerMargeProduit(0);
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSortProduit() throws Exception {
        when(productDao.findAllByOrderByNomAsc()).thenReturn(Arrays.<Product>asList(new Product(0, "nom", 0, 0)));

        List<Product> result = productController.sortProduit();
        Assert.assertEquals(Arrays.<Product>asList(new Product(0, "nom", 0, 0)), result);
    }

    @Test
    public void testTesteDeRequetes() throws Exception {
        when(productDao.chercherUnProduitCher(anyInt())).thenReturn(Arrays.<Product>asList(new Product(0, "nom", 0, 0)));

        List<Product> result = productController.testeDeRequetes(0);
        Assert.assertEquals(Arrays.<Product>asList(new Product(0, "nom", 0, 0)), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme