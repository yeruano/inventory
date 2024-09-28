package co.com.inventory.r2dbc.purchaseinvoice.mapper;

import co.com.inventory.model.purchaseinvoice.InvoiceItem;
import co.com.inventory.r2dbc.purchaseinvoice.data.InvoiceItemData;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class PurchaseInvoiceMapper {

    public InvoiceItemData buildPurchaseInvoiceDetailData(InvoiceItem invoiceItem) {
        var supplierPurchaseInvoiceDetailData = InvoiceItemData.builder()
                .invoiceId(invoiceItem.getInvoiceId())
                .productId(invoiceItem.getProductId())
                .amount(invoiceItem.getAmount())
                .price(invoiceItem.getPrice())
                .build();

        return Objects.nonNull(invoiceItem.getId())
                ? supplierPurchaseInvoiceDetailData.toBuilder().id(invoiceItem.getId()).build()
                : supplierPurchaseInvoiceDetailData;
    }

    public InvoiceItem buildInvoiceItem(InvoiceItemData invoiceItemData) {
        return InvoiceItem.builder()
                .id(invoiceItemData.getId())
                .invoiceId(invoiceItemData.getInvoiceId())
                .productId(invoiceItemData.getProductId())
                .amount(invoiceItemData.getAmount())
                .price(invoiceItemData.getPrice())
                .build();
    }
}
