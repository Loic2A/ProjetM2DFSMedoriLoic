package com.example.hystrix.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ProductServiceController {
    @Autowired
    RestTemplate restTemplate;

    //Afficher liste des produits
    @RequestMapping(value = "/ListeProduit", method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "fallbackMethod2")
    public String getProducts()
    {

        String response = restTemplate.exchange("http://localhost:9090/Produits",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();

        System.out.println("Response Body " + response);

        return "Products Details " + response;
    }

    //Get product by id
    @RequestMapping(value = "/ProduitParId/{id}", method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "fallbackMethod")
    public String afficherUnProduit(@PathVariable int id)
    {

        String response = restTemplate.exchange("http://localhost:9090/afficherUnProduit/{id}",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, id).getBody();

        System.out.println("Response Body " + response);

        return "Product Id -  " + id + " [ Product Details " + response+" ]";
    }

    //Calcul marge du produit
    @RequestMapping(value = "/calculMargeProduit/{id}", method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "fallbackMethod2")
    public String calculerMargeProduit()
    {
        String response = restTemplate.exchange("http://localhost:9090/calculerMargeProduit/{id}",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();

        System.out.println("Response Body " + response);

        return " Products Details " + response;
    }

    //Tri des produits
    @RequestMapping(value = "/ProductsSorted", method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "fallbackMethod2")
    public String getProductsSorted()
    {

        String response = restTemplate.exchange("http://localhost:9090/sortProduit",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();

        System.out.println("Response Body " + response);

        return "Products Details " + response;
    }

    public String  fallbackMethod(int id){

        return "Fallback response:: No product details available temporarily";
    }

    public String  fallbackMethod2(){

        return "Fallback response:: No products details available temporarily";
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
