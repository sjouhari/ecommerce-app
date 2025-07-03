class Inventory {
  final int id;
  final int productId;
  final String size;
  final String color;
  final double price;
  final int quantity;

  Inventory({
    required this.id,
    required this.productId,
    required this.size,
    required this.color,
    required this.price,
    required this.quantity,
  });

  factory Inventory.fromJson(Map<String, dynamic> json) {
    return Inventory(
      id: json['id'],
      productId: json['productId'],
      size: json['size'],
      color: json['color'],
      price: json['price'],
      quantity: json['quantity'],
    );
  }
}
