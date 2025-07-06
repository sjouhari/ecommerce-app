class OrderRequest {
  final int userId;
  final int deliveryAddressId;
  final List<int> orderItemsIds;
  final String paymentMethod;
  String? chequeNumber;
  String? bankName;

  OrderRequest({
    required this.userId,
    required this.deliveryAddressId,
    required this.orderItemsIds,
    required this.paymentMethod,
    this.chequeNumber,
    this.bankName,
  });

  Map<String, dynamic> toJson() => {
    'userId': userId,
    'deliveryAddressId': deliveryAddressId,
    'orderItemsIds': orderItemsIds,
    'paymentMethod': paymentMethod,
    'chequeNumber': chequeNumber,
    'bankName': bankName,
  };
}
