class OrderItem {
  final int id;
  final int productId;
  final String productName;
  final String productImage;
  final String size;
  final String color;
  final double price;
  final int quantity;
  final bool selected;

  OrderItem({
    required this.id,
    required this.productId,
    required this.productName,
    required this.productImage,
    required this.size,
    required this.color,
    required this.price,
    required this.quantity,
    required this.selected,
  });

  factory OrderItem.fromJson(Map<String, dynamic> json) {
    return OrderItem(
      id: json['id'],
      productId: json['productId'],
      productName: json['productName'],
      productImage: json['productImage'],
      size: json['size'],
      color: json['color'],
      price: json['price'],
      quantity: json['quantity'],
      selected: json['selected'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'productId': productId,
      'productName': productName,
      'productImage': productImage,
      'size': size,
      'color': color,
      'price': price,
      'quantity': quantity,
      'selected': selected,
    };
  }
}
