class OrderItem {
  final int id;
  final String productId;
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
      productId: json['product_id'],
      productName: json['product_name'],
      productImage: json['product_image'],
      size: json['size'],
      color: json['color'],
      price: json['price'],
      quantity: json['quantity'],
      selected: json['selected'],
    );
  }
}
