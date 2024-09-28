package co.com.inventory.usecase.purchaseinvoice;

import co.com.inventory.model.product.Product;
import co.com.inventory.model.supplier.Supplier;
import co.com.inventory.model.purchaseinvoice.Installment;
import co.com.inventory.model.purchaseinvoice.InvoiceDetail;
import co.com.inventory.model.purchaseinvoice.PurchaseInvoice;
import reactor.util.function.Tuple3;

import java.util.List;

public class PurchaseInvoiceMapper {

    public PurchaseInvoice buildPurchaseInvoice(List<Product> products,
                                                Tuple3<Supplier, InvoiceDetail, List<Installment>> tuple3) {
        Supplier supplier = tuple3.getT1();
        InvoiceDetail invoiceDetail = tuple3.getT2();
        List<Installment> installments = tuple3.getT3();

        return PurchaseInvoice.builder()
                .products(products.stream().map(product -> product.toBuilder().suppliers(List.of(supplier)).build())
                        .toList())
                .invoice(invoiceDetail)
                .supplier(supplier)
                .installments(installments)
                .build();
    }

    public List<Installment> buildInstallments(PurchaseInvoice purchaseInvoice, InvoiceDetail invoiceDetail) {
        return purchaseInvoice.getInstallments()
                .stream()
                .map(installment -> installment.toBuilder().invoiceId(invoiceDetail.getId()).build())
                .toList();
    }
}
