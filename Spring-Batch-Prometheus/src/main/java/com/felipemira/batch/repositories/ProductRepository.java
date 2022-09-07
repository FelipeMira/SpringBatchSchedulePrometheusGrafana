package com.felipemira.batch.repositories;

import com.felipemira.batch.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> { }
