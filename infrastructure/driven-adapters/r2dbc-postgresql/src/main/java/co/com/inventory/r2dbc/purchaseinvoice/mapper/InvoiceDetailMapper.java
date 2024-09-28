package co.com.inventory.r2dbc.purchaseinvoice.mapper;

import co.com.inventory.model.purchaseinvoice.InvoiceDetail;
import co.com.inventory.r2dbc.purchaseinvoice.data.InvoiceDetailData;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Objects;

@UtilityClass
public class InvoiceDetailMapper {

    public InvoiceDetail buildInvoiceDetail(InvoiceDetailData invoiceDetailData) {
        return InvoiceDetail.builder()
                .id(invoiceDetailData.getId())
                .urlPhoto(invoiceDetailData.getUrlPhoto())
                .purchaseDate(invoiceDetailData.getPurchaseDate().toString())
                .entryDate(invoiceDetailData.getEntryDate().toString())
                .total(invoiceDetailData.getTotal())
                .amountPaid(invoiceDetailData.getAmountPaid())
                .state(invoiceDetailData.getState())
                .supplierId(invoiceDetailData.getSupplierId())
                .build();
    }

    public InvoiceDetailData buildInvoiceDetailData(InvoiceDetail invoiceDetail) {
        var supplierPurchaseInvoiceData = InvoiceDetailData.builder()
                .urlPhoto(invoiceDetail.getUrlPhoto())
                .purchaseDate(LocalDateTime.parse(invoiceDetail.getPurchaseDate()))
                .entryDate(LocalDateTime.parse(invoiceDetail.getEntryDate()))
                .total(invoiceDetail.getTotal())
                .amountPaid(invoiceDetail.getAmountPaid())
                .state(invoiceDetail.getState())
                .supplierId(invoiceDetail.getSupplierId())
                .build();

        return Objects.nonNull(invoiceDetail.getId())
                ? supplierPurchaseInvoiceData.toBuilder().id(invoiceDetail.getId()).build()
                : supplierPurchaseInvoiceData;
    }
}
