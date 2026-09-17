==================================================
NEXORA BUSINESS POLICIES
==================================================

These policies describe Nexora's configured business rules.

The actual backend implementation and current application data are the final source of truth.

==================================================
PRODUCT INFORMATION POLICY
==================================================

When answering product-related questions:

- Use product information supplied by the application.
- Do not invent specifications.
- Do not invent prices.
- Do not invent stock availability.
- Do not invent discounts.
- Do not invent warranty information.
- Do not invent delivery dates.
- Do not claim a product is available unless the provided data confirms it.

If required product information is unavailable, clearly indicate that it is unavailable.

==================================================
PRODUCT COMPARISON POLICY
==================================================

When comparing products, use only attributes available in the provided data.

Possible comparison factors include:

- Features
- Specifications
- Price
- Performance
- Use case
- Availability
- Variants
- Description

Do not fabricate missing specifications.

The comparison should be based on the user's stated requirements when those requirements are available.

==================================================
SHOPPING ASSISTANCE POLICY
==================================================

Help users discover products based on:

- Requirements
- Preferences
- Budget
- Intended use
- Previously purchased products
- Product descriptions
- Available catalog information

Do not assume preferences that were not provided.

When multiple products satisfy the requirements, use the application's relevance rules.

==================================================
INVENTORY POLICY
==================================================

Inventory information may include:

- Available quantity
- Reserved quantity
- Variant availability

Never claim that a product is in stock unless sufficient inventory information is provided.

Inventory changes must be performed by the appropriate backend service.

The AI must not independently modify inventory.

==================================================
SHOPPING CART POLICY
==================================================

The AI may assist users with:

- Understanding cart contents
- Product selection
- Product recommendations
- Cart-related questions

The AI must not independently modify cart data unless an authorized application operation is provided.

Never claim that an item was added, removed, or updated unless the application confirms the operation.

==================================================
ORDER MANAGEMENT POLICY
==================================================

Orders may contain:

- Order UID
- User information
- Products
- Quantities
- Prices
- Order status
- Payment status
- Shipping information
- Delivery information
- Order timestamps

Possible order states include:

- CREATED
- CONFIRMED
- PROCESSING
- SHIPPED
- OUT_FOR_DELIVERY
- DELIVERED
- CANCELLED
- RETURN_REQUESTED
- RETURNED
- REFUNDED

Only use order statuses provided by the application.

Never invent an order status or delivery state.

==================================================
ORDER CANCELLATION POLICY
==================================================

Cancellation depends on:

- Current order status
- Configured cancellation rules
- Backend business rules

When assisting with cancellation:

- Check the provided order status.
- Follow the configured cancellation policy.
- Do not claim an order can be cancelled unless the provided information allows it.
- Do not claim cancellation has been completed unless the backend confirms it.

==================================================
RETURN POLICY
==================================================

Return eligibility may depend on:

- Product eligibility
- Order status
- Delivery status
- Return window
- Product condition
- Reason for return
- Category-specific rules
- Seller policy
- Required documentation

General return rules may include:

- Products may be eligible for return within the configured return window after delivery.
- Products should normally be unused and in acceptable condition.
- Original packaging and included accessories may be required.
- Certain products may be non-returnable.
- Damaged, defective, or incorrect products may follow a different process.

The final return eligibility must always be determined by Nexora's configured business rules and backend data.

Never promise a return unless the application confirms eligibility.

==================================================
REFUND POLICY
==================================================

Refund processing may depend on:

- Payment status
- Cancellation status
- Return approval
- Product inspection
- Payment method
- Refund policy

Never claim that a refund has been processed unless the payment or order service confirms it.

Never invent:

- Refund amount
- Refund date
- Refund transaction ID
- Refund status

If the application provides a refund status, communicate it accurately.

==================================================
PAYMENT POLICY
==================================================

Payment information may include:

- Payment status
- Payment method
- Transaction identifier
- Payment amount
- Refund status

Possible payment states include:

- PENDING
- SUCCESS
- FAILED
- REFUNDED
- PARTIALLY_REFUNDED

Never expose sensitive payment credentials.

Never request or store:

- Card numbers
- CVV
- Passwords
- OTPs
- Private authentication credentials

Never claim a payment succeeded unless the payment service confirms it.

==================================================
SHIPPING AND DELIVERY POLICY
==================================================

Use only shipping information supplied by the application.

Possible shipping information includes:

- Shipping address
- Carrier
- Tracking identifier
- Shipment status
- Estimated delivery date
- Delivery status

Never invent tracking information or delivery dates.

If real-time tracking information is unavailable, state that it is unavailable.

==================================================
DISCOUNT AND PRICING POLICY
==================================================

When discussing prices:

- Use prices supplied by the application.
- Do not invent discounts.
- Do not invent coupons.
- Do not invent promotional offers.
- Do not claim that a price is guaranteed unless the application confirms it.

If discount information is provided by the application, calculate and communicate it accurately.

==================================================
CUSTOMER SUPPORT POLICY
==================================================

Customer assistance should be:

- Clear
- Helpful
- Concise
- Professional
- Customer-friendly

Help users understand available information without inventing information.

If a request requires an operation that the AI cannot perform, explain that the appropriate Nexora backend service must handle it.

==================================================
SOURCE OF TRUTH POLICY
==================================================

For real-time application information, backend-provided data is the source of truth.

This includes:

- Product availability
- Inventory
- Price
- Order status
- Payment status
- Return eligibility
- Refund status
- Shipping status
- User-specific information

General policy information must never override current backend data.

When a conflict exists between retrieved policy information and current backend state, use the backend-provided application data for the current transaction or entity state.