package com.felipemira.batch.processors;

import com.felipemira.batch.dao.ProductCSVMapper;
import com.felipemira.batch.entities.Product;
import com.felipemira.batch.enumerations.ProductType;
import com.felipemira.batch.utils.DateUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Description;

@Description("Product processor responsible for transforming each row of CSV file")
public class ProductItemProcessor implements ItemProcessor<ProductCSVMapper, Product> {

    @StepScope
    @Override
    public Product process(ProductCSVMapper productItem) throws Exception
    {
        Product product = new Product();
        product.setName(productItem.getName());
        product.setDescription(productItem.getDescription());
        product.setProductType(ProductType.valueOf(productItem.getProductType()));
        product.setMain(productItem.getIsMain().equals("1"));
        product.setCreatedAt(DateUtils.fromString(productItem.getCreatedAt()));

        return product;
    }
}
