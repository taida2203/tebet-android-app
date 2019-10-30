package com.tebet.mojual.data.models.enumeration


/**
 * Created by TaiDA
 */
enum class ContainerOrderStatus {
    FUTURE_SALE_REQUESTED, // customer create order in future
    FUTURE_SALE_MISSED, // customer create order in future but not submit quality checking on time
    // Initial price offerred
    INITIAL_PRICE_OFFERED, // container pass first verification by IOT device, and container is assigned initial price
    // Quality rejected
    QUALITY_REJECTED, // container NOT pass first verification by IOT device
    // Initial price rejected
    INITIAL_PRICE_REJECTED, // Customer not accept initial price and don’t want to sale
    // Sale requested
    SALE_REQUESTED, // Customer want to sale and submitted sale request
    // Checking
    CONTACTED_LOGISTIC_TO_PICK, // Logistic staff already contacted Logistic company to pick container
    PICKED_TO_FACTORY, // container is picked up to Factory (on the way to the factory
    ARRIVED_IN_FACTORY, // container is arrived in Factory
    // Sale rejected
    SALE_REJECTED, // Customer reject final price offering (don’t want to sale)
    WILL_BE_RETURNED, // container will be returned back to customer
    CONTACTED_LOGISTIC_TO_RETURN, // Logistic staff already contacted Logistic company to return
    PICKED_TO_RETURN, // container is picked by Logistic Staff to return back to customer
    // Returned
    RETURNED_TO_CUSTOMER, // container is returned to customer
    // Final price offerred
    FIRST_FINALIZED_PRICE_OFFERED, // System automatic calculate final result checking and generate first Final Price
    SECOND_FINALIZED_PRICE_OFFERED, // Admin offer second finalized price to customer
    // Sale confirmed
    SALE_CONFIRMED, // Customer confirm want to Sale with final price
    PAYMENT_IN_PROGRESS, // system processing payment
    PAYMENT_FAILURE, // payment is failure because any problem
    // Paid
    PAID_TO_CUSTOMER // payment successfully to customer
}
