package com.mycompany.product;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.product.dao.ProductRepository;
import com.mycompany.product.entity.Product;
import com.mycompany.product.exception.BadRequestException;

@RestController
public class ProductService {
	@Autowired
	ProductRepository prodRepo;

	@RequestMapping("/product/{id}")
	Product getProduct(@PathVariable("id") int id) {
		Optional<Product> product = prodRepo.findById(id);
		
		if (!product.isPresent()) {
			throw new BadRequestException(BadRequestException.ID_NOT_FOUND, "No product for id " + id);
		}
		
		return product.get();
	}

	@RequestMapping(value = "/product", method = RequestMethod.POST)
	ResponseEntity<Product> insertProduct(@RequestBody Product product) {
		Product savedProduct = prodRepo.save(product);

		return new ResponseEntity<Product>(savedProduct, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/product/{id}", method = RequestMethod.PUT)
	ResponseEntity<Product> updateProduct(@PathVariable("id") int id, @RequestBody Product product)	{
		Product existingProduct = prodRepo.findById(id).get();
		
		existingProduct.setCatId(product.getCatId());
		existingProduct.setName(product.getName());
		
		Product savedProduct = prodRepo.save(existingProduct);
		
		return new ResponseEntity<Product>(savedProduct, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
	Product deleteProduct(@PathVariable("id") int id) {
		Optional<Product> existingProduct = prodRepo.findById(id);
		
		if (!existingProduct.isPresent()) {
			throw new BadRequestException(BadRequestException.ID_NOT_FOUND, "Product not found with Id " + id);
		}
		
		prodRepo.delete(existingProduct.get());
		return existingProduct.get();
	}

	@RequestMapping("/products")
	List<Product> getProductsForCategory(@RequestParam("id") int id) {
		return prodRepo.findByCatId(id);
	}	
}
