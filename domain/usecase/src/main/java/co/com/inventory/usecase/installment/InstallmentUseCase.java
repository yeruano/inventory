package co.com.inventory.usecase.installment;

import co.com.inventory.model.commons.InvoiceStatus;
import co.com.inventory.model.purchaseinvoice.Installment;
import co.com.inventory.model.purchaseinvoice.InvoiceDetail;
import co.com.inventory.model.purchaseinvoice.PurchaseInvoice;
import co.com.inventory.model.purchaseinvoice.gateways.InstallmentGateway;
import co.com.inventory.model.purchaseinvoice.gateways.InvoiceDetailGateway;
import co.com.inventory.model.supplier.Supplier;
import co.com.inventory.model.supplier.gateways.SupplierGateway;
import co.com.inventory.usecase.purchaseinvoice.util.PurchaseInvoiceUtil;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class InstallmentUseCase {

    private final InvoiceDetailGateway invoiceDetailGateway;
    private final SupplierGateway supplierGateway;
    private final InstallmentGateway installmentGateway;
    private final PurchaseInvoiceUtil purchaseInvoiceUtil;

    public Mono<PurchaseInvoice> saveInstallments(PurchaseInvoice purchaseInvoice) {
        Mono<InvoiceDetail> invoiceDetailMono = invoiceDetailGateway.findById(purchaseInvoice.getInvoice().getId());
        Mono<Supplier> supplierMono = supplierGateway.findById(purchaseInvoice.getSupplier().getId());
        return Mono.zip(invoiceDetailMono, supplierMono)
                .map(tuple2 -> purchaseInvoice.toBuilder().invoice(tuple2.getT1()).supplier(tuple2.getT2()).build())
                .map(purchaseInvoiceUtil::validateInstallmentsToSave)
                .flatMap(invoiceDetail -> saveInstallments(invoiceDetail, purchaseInvoice));
    }

    public Mono<PurchaseInvoice> updateInstallment(PurchaseInvoice purchaseInvoice) {
        Installment installment = purchaseInvoice.getInstallments().get(0); // First installment
        Mono<InvoiceDetail> invoiceDetailMono = invoiceDetailGateway.findById(purchaseInvoice.getInvoice().getId());
        Mono<Installment> installmentMono = installmentGateway.findById(installment.getId());
        return Mono.zip(invoiceDetailMono, installmentMono)
                .filter(tuple2 -> Objects.equals(tuple2.getT1().getId(), tuple2.getT2().getInvoiceId()))
                .map(tuple2 -> purchaseInvoiceUtil.validateInstallmentToUpdate(tuple2, installment))
                .flatMap(invoice -> updateAndSaveInstallmentAndInvoice(invoice, installment, purchaseInvoice));
    }

    public Mono<Void> deleteInstallments(PurchaseInvoice purchaseInvoice) {
        var installments = purchaseInvoice.getInstallments();
        return invoiceDetailGateway.findById(purchaseInvoice.getInvoice().getId())
                .map(invoiceDetail -> purchaseInvoiceUtil.validateInstallmentsToDelete(installments, invoiceDetail))
                .flatMap(invoiceDetail -> deleteInstallmentsAndUpdateInvoice(invoiceDetail, installments));
    }

    private Mono<PurchaseInvoice> saveInstallments(InvoiceDetail invoiceDetail, PurchaseInvoice purchaseInvoice) {
        var installmentsToSave = addInvoiceIdToInstallments(purchaseInvoice.getInstallments(), invoiceDetail);
        return installmentGateway.saveAll(installmentsToSave)
                .collectList()
                .flatMap(installments -> updateInvoice(installments, invoiceDetail, purchaseInvoice));
    }

    private List<Installment> addInvoiceIdToInstallments(List<Installment> installments, InvoiceDetail invoiceDetail) {
        return installments.stream()
                .map(installment -> installment.toBuilder().invoiceId(invoiceDetail.getId()).build())
                .toList();
    }

    private Mono<PurchaseInvoice> updateInvoice(List<Installment> installments, InvoiceDetail invoiceDetail,
                                                PurchaseInvoice purchaseInvoice) {
        return invoiceDetailGateway.save(invoiceDetail)
                .map(invoiceDetailUpdated -> purchaseInvoice.toBuilder()
                        .invoice(invoiceDetailUpdated)
                        .installments(installments)
                        .build());
    }

    private Mono<PurchaseInvoice> updateAndSaveInstallmentAndInvoice(InvoiceDetail invoiceDetail,
                                                                     Installment installment,
                                                                     PurchaseInvoice purchaseInvoice) {
        Mono<Installment> installmentMono = installmentGateway.saveAll(List.of(installment)).next();
        Mono<InvoiceDetail> invoiceDetailMono = invoiceDetailGateway.save(invoiceDetail);
        return Mono.zip(installmentMono, invoiceDetailMono)
                .map(tuple2 -> purchaseInvoice.toBuilder()
                        .installments(List.of(tuple2.getT1()))
                        .invoice(tuple2.getT2())
                        .build());
    }

    private Mono<Void> deleteInstallmentsAndUpdateInvoice(InvoiceDetail invoiceDetail, List<Installment> installments) {
        return Flux.fromIterable(installments)
                .flatMap(installment -> installmentGateway.findById(installment.getId()))
                .filter(installment -> Objects.equals(installment.getInvoiceId(), invoiceDetail.getId()))
                .collectList()
                .flatMap(validInstallments -> installmentGateway.deleteByIds(getInstallmentIds(validInstallments))
                                .thenReturn(validInstallments))
                .map(purchaseInvoiceUtil::sumOfInstallments)
                .flatMap(sumOfInstallments -> updateInvoiceDetail(sumOfInstallments, invoiceDetail));
    }

    private List<Long> getInstallmentIds(List<Installment> installments) {
        return installments.stream().map(Installment::getId).toList();
    }

    private Mono<Void> updateInvoiceDetail(double sumOfInstallments, InvoiceDetail invoiceDetail) {
        double amountPaid = invoiceDetail.getAmountPaid() - sumOfInstallments;
        InvoiceStatus invoiceState = purchaseInvoiceUtil.getInvoiceStatus(amountPaid, invoiceDetail.getTotal());
        invoiceDetail.setAmountPaid(amountPaid);
        invoiceDetail.setState(invoiceState);

        return invoiceDetailGateway.save(invoiceDetail).then();
    }
}
