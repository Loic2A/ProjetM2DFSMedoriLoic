package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.*;

import static org.junit.Assert.assertEquals;
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



    }

    @Test
    public void testListeProduits() throws Exception {
        MappingJacksonValue result = productController.listeProduits();
        assertEquals(null, result);
    }

    @Test
    public void testAfficherUnProduit() throws Exception {
        Product result = productController.afficherUnProduit(1);
        Product p = new Product(1, "B", 12, 12);
        Assertions.assertTrue(p.getId() == result.getId());
        Assertions.assertTrue(p.getNom().equals(p.getNom()));
        Assertions.assertTrue(p.getPrix() == result.getPrix());
        Assertions.assertTrue(p.getPrixAchat() == result.getPrixAchat());
    }

    @Test
    public void testAjouterProduit() throws Exception {
        ResponseEntity<Void> result = productController.ajouterProduit(new Product(0, null, 10, 0));
        Assertions.assertTrue(result.getStatusCodeValue() >= 200);
        Assertions.assertTrue( result.getStatusCodeValue() < 300);
    }

    @Test
    public void testSupprimerProduit() throws Exception {
        ResponseEntity<Void> result = productController.supprimerProduit(1);
        Assertions.assertTrue(result.getStatusCodeValue() >= 200);
        Assertions.assertTrue( result.getStatusCodeValue() < 300);
    }

    @Test
    public void testUpdateProduit() throws Exception {
        productController.updateProduit(1, new Product(1, "C", 14, 10));

    }

    @Test
    public void testCalculerMargeProduit() throws Exception {
        String result = productController.calculerMargeProduit(2);
        assertEquals("Marge du produit A : 7", result);
    }

    @Test
    public void testSortProduit() throws Exception {
        //when(productDao.findAllByOrderByNomAsc()).thenReturn(Arrays.<Product>result(new Product(1, "Z", 10, 10), new Product(2, "B", 10, 10)));

        List<Product> result = new ArrayList<>();
        Product r1=new Product(1,"Z", 10, 10);
        Product r2=new Product(2,"E", 10, 10);
        Product r3=new Product(3,"A", 10, 10);
        result.add(r1);
        result.add(r2);
        result.add(r3);

        List<Product> asList = productController.sortProduit();
        //Assert.assertEquals(Arrays.<Product>asList(new Product(0, "nom", 0, 0)), result);




        assertEquals(asList, result);

    }

    @Test
    public void testTesteDeRequetes() throws Exception {
        when(productDao.chercherUnProduitCher(anyInt())).thenReturn(Arrays.<Product>asList(new Product(0, "nom", 0, 0)));

        List<Product> result = productController.testeDeRequetes(0);
        assertEquals(Arrays.<Product>asList(new Product(0, "nom", 0, 0)), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme