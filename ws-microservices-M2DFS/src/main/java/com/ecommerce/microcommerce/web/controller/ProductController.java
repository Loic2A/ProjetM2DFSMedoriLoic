package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProduitGratuitException;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;

    private static Map<Integer, Product> productList = new HashMap<Integer, Product>(){
        {

            put(1, new Product(1, "B", 12, 12));
            put(2, new Product(2, "A 2", 22, 15));
            put(3, new Product(3, "Y 3", 102, 90));
            put(4, new Product(4, "C 4", 54, 40));
            put(5, new Product(5, "E 5", 68, 30));
        }
    };

    //Récupérer la liste des produits en liste
    @GetMapping(value = "/ListeProduits")
    public Map afficherListeProduit(){
        return productList;
    }

    //Récupérer la liste des produits en base de donnée
    @RequestMapping(value = "/Produits", method = RequestMethod.GET)
    public MappingJacksonValue listeProduits() {
        Iterable<Product> produits = productDao.findAll();
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);
        produitsFiltres.setFilters(listDeNosFiltres);
        return produitsFiltres;
    }


    //Récupérer un produit par son Id
    @GetMapping(value = "/afficherUnProduit/{id}")
    public Product afficherUnProduit(@PathVariable int id) {
        Product product = productList.get(id);
        if(product == null){
            product = new Product(0, "NA", 0, 0);
        }
        return product;

    }



    //ajouter un produit
    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {
/*


 */
        return null;
    }

    // supprimer un produit
    @DeleteMapping(value = "/deleteProduct/{productId}")
    public ResponseEntity<Void> supprimerProduit(@PathVariable int productId) {
        System.out.println("Deleting product with ID " + productId);
        if (productList.get(productId) == null){
            return ResponseEntity.notFound().build();
        }
        productList.remove(productId);
        return ResponseEntity.ok().build();
    }


    @PutMapping(value = "/updateProduit/{id}")
    public void updateProduit(@PathVariable int id, @RequestBody Product product) {
        /*for (Product produit : productList) {
            if (produit.getId() == id) {
                produit.setId(product.getId());
                produit.setNom(product.getNom());
                produit.setPrix(product.getPrix());
                produit.setPrixAchat(product.getPrixAchat());
            }
        }*/
    }

    @GetMapping(value = "calculerMargeProduit/{id}")
    public String calculerMargeProduit(@PathVariable int id) {
        int prix = productList.get(id).getPrix();
        int prixAchat = productList.get(id).getPrixAchat();
        String nomProduit = productList.get(id).getNom();
        int marge = 0;
        if(prix > prixAchat) {
            marge = prix - prixAchat;

        }
        if(marge != 0){
            return "Marge du produit " + nomProduit + " : " +marge;
        }
        else{
            return null;
        }

    }

    //Ne trie que la BDD
    @GetMapping(value = "/sortProduit")
    public List<Product> sortProduit() {
        return productDao.findAllByOrderByNomAsc();
    }

    //Pour les tests
    @GetMapping(value = "test/produits/{prix}")
    public List<Product>  testeDeRequetes(@PathVariable int prix) {
        if(prix == 0){
            throw new ProduitGratuitException("Attention, produit gratuit");
        }
        else {
            return productDao.chercherUnProduitCher(502);
        }
    }



}
