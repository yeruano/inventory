package co.com.inventory.r2dbc.purchaseinvoice.mapper;

import co.com.inventory.model.purchaseinvoice.Installment;
import co.com.inventory.r2dbc.purchaseinvoice.data.SupplierPaymentHistoryData;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class InstallmentMapper {

    public List<SupplierPaymentHistoryData> buildSupplierPaymentHistoryDataList(List<Installment> installments) {
        return installments.stream().map(InstallmentMapper::buildSupplierPaymentHistoryData).toList();
    }

    public SupplierPaymentHistoryData buildSupplierPaymentHistoryData(Installment installment) {
        var supplierPaymentHistoryData = SupplierPaymentHistoryData.builder()
                .invoiceId(installment.getInvoiceId())
                .installmentAmount(installment.getAmount())
                .paymentDate(LocalDateTime.parse(installment.getPaymentDate()))
                .urlPhoto(installment.getUrlPhoto())
                .build();

        return Objects.nonNull(installment.getId())
                ? supplierPaymentHistoryData.toBuilder().id(installment.getId()).build()
                : supplierPaymentHistoryData;
    }

    public Installment buildInstallment(SupplierPaymentHistoryData supplierPaymentHistoryData) {
        return Installment.builder()
                .id(supplierPaymentHistoryData.getId())
                .invoiceId(supplierPaymentHistoryData.getInvoiceId())
                .amount(supplierPaymentHistoryData.getInstallmentAmount())
                .paymentDate(supplierPaymentHistoryData.getPaymentDate().toString())
                .urlPhoto(supplierPaymentHistoryData.getUrlPhoto())
                .build();
    }
}
