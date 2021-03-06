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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.bytebuddy.implementation.bytecode.Throw;
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
            put(2, new Product(2, "A", 22, 15));
            put(3, new Product(3, "Y", 102, 90));
            put(4, new Product(4, "C", 54, 40));
            put(5, new Product(5, "E", 68, 30));
        }
    };


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "erreur auth"),
            @ApiResponse(code = 403, message = "erreur accès"),
            @ApiResponse(code = 404, message = "page pas trouvée")
    })

    //Récupérer la liste des produits en liste
    @ApiOperation(value = "Recuperation des produits", response = Product.class, tags = "afficherListeProduit")
    @GetMapping(value = "/ListeProduits")
    public Map afficherListeProduit(){

        return productList;
    }

    //Récupérer la liste des produits en base de donnée
    @ApiOperation(value = "Recuperation des produits en base de donnée", response = Product.class, tags = "listeProduits")
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
    @ApiOperation(value = "Recuperation des produits en fonction de leur Id", response = Product.class, tags = "afficherUnProduit")
    @GetMapping(value = "/afficherUnProduit/{id}")
    public Product afficherUnProduit(@PathVariable int id) {
        Product product = productList.get(id);
        if(product == null){
            product = new Product(0, "NA", 0, 0);
        }
        return product;

    }



    //ajouter un produit
    @ApiOperation(value = "Ajouter un produit", response = Product.class, tags = "ajouterProduit")
    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {

        if(product.getPrix() == 0){
            throw new ProduitGratuitException("Attention, produit gratuit");
        }

        Product productAdded =  productDao.save(product);

        if (productAdded == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // supprimer un produit
    @ApiOperation(value = "Supprimer un produit", response = Product.class, tags = "supprimerProduit")
    @DeleteMapping(value = "/deleteProduct/{productId}")
    public ResponseEntity<Void> supprimerProduit(@PathVariable int productId) {
        System.out.println("Deleting product with ID " + productId);
        if (productList.get(productId) == null){
            return ResponseEntity.notFound().build();
        }
        productList.remove(productId);
        return ResponseEntity.ok().build();
    }


    @ApiOperation(value = "Supprimer un produit", response = Product.class, tags = "supprimerProduit")
    @PutMapping(value = "/updateProduit/{id}")
    public void updateProduit(@PathVariable int id, @RequestBody Product product) {
        List<Product> productList2 = new ArrayList<>(productList.values());
        for (Product produit : productList2) {
            if (produit.getId() == id) {
                produit.setId(product.getId());
                produit.setNom(product.getNom());
                produit.setPrix(product.getPrix());
                produit.setPrixAchat(product.getPrixAchat());
            }
        }
    }

    @ApiOperation(value = "Calculer la marge d'un produit", response = Product.class, tags = "calculerMargeProduit")
    @GetMapping(value = "/calculerMargeProduit/{id}")
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
    @ApiOperation(value = "Trier les produits par ordre alphabetique dans la base", response = Product.class, tags = "sortProduit")
    @GetMapping(value = "/sortProduit")
    public List<Product> sortProduit() {
        return productDao.findAllByOrderByNomAsc();
    }

    //Pour les tests
    @ApiOperation(value = "Test", response = Product.class, tags = "testeDeRequetes")
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
