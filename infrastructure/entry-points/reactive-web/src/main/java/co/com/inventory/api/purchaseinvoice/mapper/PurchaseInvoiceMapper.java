package co.com.inventory.api.purchaseinvoice.mapper;

import co.com.inventory.api.product.mapper.ProductMapper;
import co.com.inventory.api.purchaseinvoice.dto.PurchaseInvoiceQueryDTO;
import co.com.inventory.api.supplier.mapper.SupplierMapper;
import co.com.inventory.api.purchaseinvoice.dto.InstallmentDTO;
import co.com.inventory.api.purchaseinvoice.dto.InvoiceDetailDTO;
import co.com.inventory.api.purchaseinvoice.dto.PurchaseInvoiceDTO;
import co.com.inventory.model.commons.InvoiceStatus;
import co.com.inventory.model.purchaseinvoice.Installment;
import co.com.inventory.model.purchaseinvoice.InvoiceDetail;
import co.com.inventory.model.purchaseinvoice.PurchaseInvoice;
import co.com.inventory.model.purchaseinvoice.PurchaseInvoiceQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PurchaseInvoiceMapper {

    public static PurchaseInvoiceDTO buildPurchaseInvoiceDTO(PurchaseInvoice purchaseInvoice) {
        return PurchaseInvoiceDTO.builder()
                .products(purchaseInvoice.getProducts().stream().map(ProductMapper::buildProductDTO).toList())
                .invoice(buildInvoiceDetailDTO(purchaseInvoice.getInvoice()))
                .supplier(SupplierMapper.buildSupplierDTO(purchaseInvoice.getSupplier()))
                .installments(purchaseInvoice.getInstallments().stream()
                        .map(PurchaseInvoiceMapper::buildInstallmentDTO).toList())
                .build();
    }

    public static InvoiceDetailDTO buildInvoiceDetailDTO(InvoiceDetail invoiceDetail) {
        return InvoiceDetailDTO.builder()
                .id(invoiceDetail.getId())
                .purchaseDate(invoiceDetail.getPurchaseDate())
                .entryDate(invoiceDetail.getEntryDate())
                .urlPhoto(invoiceDetail.getUrlPhoto())
                .total(invoiceDetail.getTotal())
                .amountPaid(invoiceDetail.getAmountPaid())
                .state(invoiceDetail.getState().name())
                .supplierId(invoiceDetail.getSupplierId())
                .build();
    }

    public static PurchaseInvoice buildPurchaseInvoice(PurchaseInvoiceDTO purchaseInvoiceDTO) {
        return PurchaseInvoice.builder()
                .products(Objects.nonNull(purchaseInvoiceDTO.getProducts())
                        ? purchaseInvoiceDTO.getProducts().stream().map(ProductMapper::buildProduct).toList()
                        : List.of())
                .invoice(buildInvoice(purchaseInvoiceDTO.getInvoice()))
                .supplier(SupplierMapper.buildSupplier(purchaseInvoiceDTO.getSupplier()))
                .installments(Objects.nonNull(purchaseInvoiceDTO.getInstallments())
                        ? purchaseInvoiceDTO.getInstallments().stream()
                        .map(PurchaseInvoiceMapper::buildInstallment).toList()
                        : List.of())
                .build();
    }

    public static InvoiceDetail buildInvoice(InvoiceDetailDTO invoiceDTO) {
        var invoiceDetail = InvoiceDetail.builder()
                .purchaseDate(invoiceDTO.getPurchaseDate())
                .entryDate(invoiceDTO.getEntryDate())
                .urlPhoto(invoiceDTO.getUrlPhoto())
                .total(invoiceDTO.getTotal())
                .build();

        if (Objects.nonNull(invoiceDTO.getAmountPaid())) {
            invoiceDetail.setAmountPaid(invoiceDTO.getAmountPaid());
        }

        if (Objects.nonNull(invoiceDTO.getState())) {
            invoiceDetail.setState(InvoiceStatus.valueOf(invoiceDTO.getState()));
        }

        if (Objects.nonNull(invoiceDTO.getId())) {
            invoiceDetail.setId(invoiceDTO.getId());
        }

        if (Objects.nonNull(invoiceDTO.getSupplierId())) {
            invoiceDetail.setSupplierId(invoiceDTO.getSupplierId());
        }

        return invoiceDetail;
    }

    public static Installment buildInstallment(InstallmentDTO installmentDTO) {
        var installment = Installment.builder()
                .amount(installmentDTO.getAmount())
                .paymentDate(installmentDTO.getPaymentDate())
                .urlPhoto(installmentDTO.getUrlPhoto())
                .build();

        if (Objects.nonNull(installmentDTO.getId())) {
            installment.setId(installmentDTO.getId());
        }

        if (Objects.nonNull(installmentDTO.getInvoiceId())) {
            installment.setInvoiceId(installmentDTO.getInvoiceId());
        }

        return installment;
    }

    public static InstallmentDTO buildInstallmentDTO(Installment installment) {
        return InstallmentDTO.builder()
                .id(installment.getId())
                .amount(installment.getAmount())
                .paymentDate(installment.getPaymentDate())
                .urlPhoto(installment.getUrlPhoto())
                .invoiceId(installment.getInvoiceId())
                .build();
    }

    public static PurchaseInvoiceQuery buildPurchaseInvoiceQuery(PurchaseInvoiceQueryDTO purchaseInvoiceQueryDTO) {
        return PurchaseInvoiceQuery.builder()
                .startDate(purchaseInvoiceQueryDTO.getStartDate())
                .endDate(purchaseInvoiceQueryDTO.getEndDate())
                .build();
    }

    public static List<PurchaseInvoiceDTO> buildPurchaseInvoiceDTOS(List<PurchaseInvoice> purchaseInvoices) {
        return purchaseInvoices.stream().map(PurchaseInvoiceMapper::buildPurchaseInvoiceDTO).toList();
    }
}
