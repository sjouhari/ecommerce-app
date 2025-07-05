import 'package:flutter/foundation.dart';
import 'package:mobile_app/models/order_item.dart';

class CartModel with ChangeNotifier {
  final int id;
  final int userId;
  final List<OrderItem> orderItems;

  CartModel({required this.id, required this.userId, required this.orderItems});

  factory CartModel.fromJson(Map<String, dynamic> json) {
    return CartModel(
      id: json['id'],
      userId: json['userId'],
      orderItems: List<OrderItem>.from(
        json['orderItems'].map((item) => OrderItem.fromJson(item)),
      ),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'userId': userId,
      'orderItems': orderItems.map((item) => item.toJson()).toList(),
    };
  }
}
