package co.com.inventory.usecase.purchaseinvoice.util;

import co.com.inventory.model.commons.InvoiceStatus;
import co.com.inventory.model.exception.BusinessException;
import co.com.inventory.model.product.Product;
import co.com.inventory.model.purchaseinvoice.Installment;
import co.com.inventory.model.purchaseinvoice.InvoiceDetail;
import co.com.inventory.model.purchaseinvoice.PurchaseInvoice;
import co.com.inventory.model.supplier.Supplier;
import reactor.util.function.Tuple2;

import java.util.List;

import static co.com.inventory.model.exception.messages.BusinessExceptionMessage.THE_AMOUNT_OF_THE_INSTALLMENTS_IS_NOT_VALID;

public class PurchaseInvoiceUtil {

    private static final int ZERO = 0;

    public double sumOfInstallments(List<Installment> installments) {
        return installments.stream()
                .mapToDouble(Installment::getAmount)
                .sum();
    }

    public InvoiceStatus getInvoiceStatus(double totalSumOfInstallments, double totalInvoice) {
        return totalSumOfInstallments == totalInvoice ? InvoiceStatus.PAID : InvoiceStatus.UNPAID;
    }

    /**
     * This method validates the installments when an invoice is created or entered for the first time, it is validated
     * that the sum of installments is not negative or that it is not greater than the total amount of the invoice.
     * Finally, if the validations pass, an InvoiceDetail object is returned with the details of the invoice.
     * @param purchaseInvoice Represents the information of the invoice that is being attempted to be created.
     * @param supplier Represents the supplier information associated with the invoice.
     * @return An InvoiceDetail object, with the invoice details.
     * @throws BusinessException If the sum of the installments does not meet the validations.
     */
    public InvoiceDetail validateInstallmentsForNewInvoice(PurchaseInvoice purchaseInvoice, Supplier supplier) {
        double totalSumOfInstallments = sumOfInstallments(purchaseInvoice.getInstallments());
        InvoiceDetail invoiceDetail = purchaseInvoice.getInvoice();

        if (totalSumOfInstallments < ZERO || totalSumOfInstallments > invoiceDetail.getTotal()) {
            throw new BusinessException(THE_AMOUNT_OF_THE_INSTALLMENTS_IS_NOT_VALID);
        }

        InvoiceStatus invoiceStatus = getInvoiceStatus(totalSumOfInstallments, invoiceDetail.getTotal());

        return InvoiceDetail.builder()
                .urlPhoto(invoiceDetail.getUrlPhoto())
                .purchaseDate(invoiceDetail.getPurchaseDate())
                .entryDate(invoiceDetail.getEntryDate())
                .total(invoiceDetail.getTotal())
                .amountPaid(totalSumOfInstallments)
                .state(invoiceStatus)
                .supplierId(supplier.getId())
                .build();
    }

    /**
     * This method validates the installments that are trying to be added to an invoice that already exists, it
     * validates that the sum of installments is not negative or that the installments do not exceed the remaining
     * amount to be paid on the invoice. Finally, if the validations pass, an InvoiceDetail object is returned with
     * the details of the invoice.
     * @param purchaseInvoice Represents the information of the invoice that is being attempted to be modified.
     * @return An InvoiceDetail object, with the invoice details.
     * @throws BusinessException If the sum of the installments does not meet the validations.
     */
    public InvoiceDetail validateInstallmentsToSave(PurchaseInvoice purchaseInvoice) {
        double totalSumOfInstallments = sumOfInstallments(purchaseInvoice.getInstallments());
        InvoiceDetail invoiceDetail = purchaseInvoice.getInvoice();
        double remainingAmount = invoiceDetail.getTotal() - invoiceDetail.getAmountPaid();

        if (totalSumOfInstallments < ZERO || totalSumOfInstallments > remainingAmount) {
            throw new BusinessException(THE_AMOUNT_OF_THE_INSTALLMENTS_IS_NOT_VALID);
        }

        double newAmountPaid = invoiceDetail.getAmountPaid() + totalSumOfInstallments;
        InvoiceStatus invoiceState = getInvoiceStatus(newAmountPaid, invoiceDetail.getTotal());

        return InvoiceDetail.builder()
                .id(invoiceDetail.getId())
                .urlPhoto(invoiceDetail.getUrlPhoto())
                .purchaseDate(invoiceDetail.getPurchaseDate())
                .entryDate(invoiceDetail.getEntryDate())
                .total(invoiceDetail.getTotal())
                .amountPaid(newAmountPaid)
                .state(invoiceState)
                .supplierId(purchaseInvoice.getSupplier().getId())
                .build();
    }

    /**
     * This method validates the installments that are being attempted to be updated from an invoice that already
     * exists, it validates that the new installment does not exceed the total amount of the invoice. Finally, if the
     * validations pass, an InvoiceDetail object is returned with the details of the invoice.
     * @param tuple2 Represents the information of the invoice and installments to be modified.
     * @param installmentToUpdate Represents the new information of the invoice that is being attempted to be modified.
     * @return An InvoiceDetail object, with the invoice details.
     * @throws BusinessException If the sum of the installments does not meet the validations.
     */
    public InvoiceDetail validateInstallmentToUpdate(Tuple2<InvoiceDetail, Installment> tuple2,
                                                     Installment installmentToUpdate) {
        InvoiceDetail invoiceDetail = tuple2.getT1();
        Installment installment = tuple2.getT2();

        double provisionalAmountPaid = invoiceDetail.getAmountPaid() - installment.getAmount();
        double newAmountPaid = provisionalAmountPaid + installmentToUpdate.getAmount();

        if (newAmountPaid > invoiceDetail.getTotal()) {
            throw new BusinessException(THE_AMOUNT_OF_THE_INSTALLMENTS_IS_NOT_VALID);
        }

        invoiceDetail.setAmountPaid(newAmountPaid);
        invoiceDetail.setState(getInvoiceStatus(newAmountPaid, invoiceDetail.getTotal()));
        return invoiceDetail;
    }

    /**
     * This method validates the installments that are being attempted to be deleted from an invoice that already
     * exists, it validates that the sum of the installments does not exceed the amount paid on the invoice up to the
     * time of deletion. Finally, if the validations pass, an InvoiceDetail object is returned with the details of the invoice.
     * @param installments Represents the information of the installments to be deleted.
     * @param invoiceDetail Represents the new information of the invoice that is being attempted to be deleted.
     * @return An InvoiceDetail object, with the invoice details.
     * @throws BusinessException If the sum of the installments does not meet the validations.
     */
    public InvoiceDetail validateInstallmentsToDelete(List<Installment> installments, InvoiceDetail invoiceDetail) {
        double totalSumOfInstallments = sumOfInstallments(installments);

        if (totalSumOfInstallments > invoiceDetail.getAmountPaid()) {
            throw new BusinessException(THE_AMOUNT_OF_THE_INSTALLMENTS_IS_NOT_VALID);
        }

        return invoiceDetail;
    }

    public Product calculateAmountAndPurchasePrice(Product newProduct, Product savedProduct) {
        int newAmount = savedProduct.getAmount() + newProduct.getAmount();
        double weightedPurchasePriceTotal = savedProduct.getAmount() * savedProduct.getPurchasePrice() +
                newProduct.getAmount() * newProduct.getPurchasePrice();
        double newPurchasePrice = weightedPurchasePriceTotal / newAmount;

        return savedProduct.toBuilder()
                .amount(newAmount)
                .purchasePrice(newPurchasePrice)
                .createdAt(savedProduct.getCreatedAt())
                .build();
    }
}
