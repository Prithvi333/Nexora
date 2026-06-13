package com.nexora.product.variant.controller;
import com.nexora.product.utility.constants.IUrls;
import com.nexora.product.variant.service.UserProductVariantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(IUrls.INTERNAL)
public class InternalProductVariantController {
    private static final Logger logger = LoggerFactory.getLogger(InternalProductVariantController.class);

    @Autowired
    private UserProductVariantService userProductVariantService;


    @GetMapping("/quantity/validate/{variantUid}/{quantity}")
    public void validateQuantity(@PathVariable("variantUid") String variantUid, @PathVariable("quantity") Integer quantity) {
        logger.info("Received request to validate quantity for variantUid: {} and quantity: {}", variantUid, quantity);
        userProductVariantService.validateQuantity(variantUid, quantity);
        logger.info("Quantity validation completed for variantUid: {} and quantity: {}", variantUid, quantity);
    }

}